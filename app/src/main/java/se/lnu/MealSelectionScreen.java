package se.lnu;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jspecify.annotations.NonNull;
import se.lnu.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MealSelectionScreen {

  public static void show(Stage stage, MenuItem item) {
    App.selectedMenuItem = item;
    App.selectedQuantity = 1;

    String[] comboSize = {"Regular"};
    double[] comboPrice = {0};

    Button backButton = ScreenStyle.createBackButton();
    backButton.setOnAction(e -> CategoryScreen.show(stage));

    Button homeButton = ScreenStyle.createHomeButton(stage);
    Button cartButton = ScreenStyle.createCartButton(stage);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backButton, spacer, homeButton, cartButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label(item.getName());
    title.setStyle(
      "-fx-font-size: 42px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;"
    );

    Label description = new Label(item.getDescription());
    description.setWrapText(true);
    description.setMaxWidth(520);
    description.setAlignment(Pos.CENTER);
    description.setStyle(
      "-fx-font-size: 17px;" +
        "-fx-text-fill: #555555;"
    );

    Label priceLabel = new Label("Unit price: " + String.format("%.2f kr", item.getPrice()));
    priceLabel.setStyle(
      "-fx-font-size: 19px;" +
        "-fx-text-fill: #1f1f1f;"
    );

    Label itemTotalLabel = new Label();
    itemTotalLabel.setStyle(
      "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;" +
        "-fx-background-color: rgba(255,152,0,0.12);" +
        "-fx-background-radius: 18;" +
        "-fx-padding: 10 24 10 24;"
    );

    Button minusButton = new Button("-");
    Button plusButton = new Button("+");

    String quantityButtonStyle =
      "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
        "-fx-text-fill: white;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 14;" +
        "-fx-cursor: hand;" +
        "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.25), 7, 0, 0, 2);";

    minusButton.setStyle(quantityButtonStyle);
    plusButton.setStyle(quantityButtonStyle);

    Label quantityLabel = new Label(String.valueOf(App.selectedQuantity));
    quantityLabel.setStyle(
      "-fx-font-size: 22px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;" +
        "-fx-min-width: 42;" +
        "-fx-alignment: center;"
    );
    quantityLabel.setAlignment(Pos.CENTER);

    boolean isCombo =
      (App.selectedCategoryName != null && App.selectedCategoryName.equalsIgnoreCase("Combos"))
        || item.getName().toLowerCase().contains("combo")
        || item.getName().toLowerCase().contains("feast")
        || item.getName().toLowerCase().contains("snack box");

    List<ExtraOption> extraOptions = DatabaseHelper.getExtrasByItem(item.getId());
    List<CheckBox> extrasCheckBoxes = new ArrayList<>();

    for (ExtraOption option : extraOptions) {
      CheckBox checkBox = createExtraCheckBox(option.getName(), option.getPrice());
      extrasCheckBoxes.add(checkBox);
    }

    CheckBox[] extrasBoxes = extrasCheckBoxes.toArray(new CheckBox[0]);

    Runnable updateTotal = () -> {
      double extrasPrice = getSelectedExtrasPrice(extrasBoxes);

      double finalTotal =
        (item.getPrice() + extrasPrice + comboPrice[0])
          * App.selectedQuantity;

      itemTotalLabel.setText("Item total: " + String.format("%.2f kr", finalTotal));
    };

    for (CheckBox checkBox : extrasBoxes) {
      checkBox.setOnAction(e -> updateTotal.run());
    }

    ComboSelection comboSelection = null;
    VBox comboBox = null;
    List<CheckBox> comboCustomizeBoxes = new ArrayList<>();
    VBox comboCustomizeBox = null;

    if (isCombo) {
      VBox comboSizeSelector = createComboSizeSelector(comboSize, comboPrice, updateTotal);

      comboSelection = createComboSelection(item, comboSizeSelector);

      comboBox = comboSelection.comboBox;
      comboCustomizeBox = createComboCustomizeBox(comboCustomizeBoxes);
    }

    List<RemovableIngredient> removableIngredients = item.getRemovableIngredients();
    List<CheckBox> removablesCheckBoxes = new ArrayList<>();

    for (RemovableIngredient r : removableIngredients) {
      CheckBox box = new CheckBox(r.name());
      box.setStyle(
        "-fx-font-size: 16px;" +
          "-fx-font-weight: 600;" +
          "-fx-text-fill: #202020;" +
          "-fx-padding: 4 0 4 0;" +
          "-fx-cursor: hand;"
      );
      removablesCheckBoxes.add(box);
    }

    CheckBox[] removablesBoxes = removablesCheckBoxes.toArray(new CheckBox[0]);

    minusButton.setOnAction(e -> {
      if (App.selectedQuantity > 1) {
        App.selectedQuantity--;
        quantityLabel.setText(String.valueOf(App.selectedQuantity));
        updateTotal.run();
      }
    });

    plusButton.setOnAction(e -> {
      App.selectedQuantity++;
      quantityLabel.setText(String.valueOf(App.selectedQuantity));
      updateTotal.run();
    });

    HBox quantityBox = new HBox(16, minusButton, quantityLabel, plusButton);
    quantityBox.setAlignment(Pos.CENTER);

    VBox extrasBox = createBox(extrasCheckBoxes, "Optional Extras", "Optional add-ons increase the item price");
    VBox removablesBox = createBox(removablesCheckBoxes, "Remove Ingredients", "Remove unwanted parts");

    HBox ingredientChanges;

    if (isCombo) {
      comboCustomizeBox.setPrefWidth(360);
      comboCustomizeBox.setMaxWidth(360);

      extrasBox.setPrefWidth(360);
      extrasBox.setMaxWidth(360);

      ingredientChanges = new HBox(30, comboCustomizeBox, extrasBox);
      ingredientChanges.setAlignment(Pos.TOP_CENTER);
    } else {
      ingredientChanges = new HBox(30, extrasBox, removablesBox);
      ingredientChanges.setAlignment(Pos.CENTER);
    }

    Label statusLabel = new Label("");
    statusLabel.setOpacity(0);
    statusLabel.setStyle(
      "-fx-font-size: 16px;" +
        "-fx-text-fill: white;" +
        "-fx-background-color: #4CAF50;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 15;" +
        "-fx-font-weight: bold;"
    );

    Button confirmButton = new Button("Add to Order");
    confirmButton.setStyle(
      "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
        "-fx-text-fill: white;" +
        "-fx-padding: 13 30;" +
        "-fx-background-radius: 14;" +
        "-fx-cursor: hand;" +
        "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.30), 8, 0, 0, 2);"
    );

    ComboSelection finalComboSelection = comboSelection;

    confirmButton.setOnAction(e -> {
      List<String> selectedExtras = getSelectedExtras(extrasBoxes);
      double extrasPrice = getSelectedExtrasPrice(extrasBoxes);

      double totalExtraPrice = extrasPrice + comboPrice[0];

      List<String> removedIngredients = isCombo
        ? new ArrayList<>()
        : getSelectedExtras(removablesBoxes);

      List<String> comboChoices = new ArrayList<>();

      if (finalComboSelection != null) {
        for (int i = 0; i < finalComboSelection.groupNames.size(); i++) {
          String groupName = finalComboSelection.groupNames.get(i);
          ToggleGroup group = finalComboSelection.toggleGroups.get(i);
          comboChoices.add(groupName + ": " + getSelectedRadioText(group));
        }

        comboChoices.add("Combo Size: " + comboSize[0] + " (+" + String.format("%.2f kr", comboPrice[0]) + ")");

        List<String> comboCustomizations = getSelectedExtras(
          comboCustomizeBoxes.toArray(new CheckBox[0])
        );

        if (!comboCustomizations.isEmpty()) {
          comboChoices.add("Customizations: " + String.join(", ", comboCustomizations));
        }

        if (!finalComboSelection.giftLabel.getText().isBlank()) {
          comboChoices.add(finalComboSelection.giftLabel.getText());
        }
      }

      Cart.getInstance().addItem(
        item,
        App.selectedQuantity,
        selectedExtras,
        totalExtraPrice,
        removedIngredients,
        comboChoices
      );

      statusLabel.setText("Added " + App.selectedQuantity + " x " + item.getName());

      FadeTransition fadeIn = new FadeTransition(Duration.millis(400), statusLabel);
      fadeIn.setFromValue(0);
      fadeIn.setToValue(1);

      FadeTransition fadeOut = new FadeTransition(Duration.seconds(2.5), statusLabel);
      fadeOut.setFromValue(1);
      fadeOut.setToValue(0);
      fadeOut.setDelay(Duration.seconds(2));

      fadeIn.play();
      fadeOut.play();

      cartButton.setText("Cart (" + Cart.getInstance().getItemCount() + ")");
    });

    Button viewCartButton = getViewCartButton(stage);

    VBox centerContent = new VBox(18);
    centerContent.getChildren().addAll(title, description, priceLabel);

    if (comboBox != null) {
      centerContent.getChildren().add(comboBox);
    }

    centerContent.getChildren().addAll(
      ingredientChanges,
      quantityBox,
      itemTotalLabel,
      confirmButton,
      statusLabel,
      viewCartButton
    );

    centerContent.setAlignment(Pos.CENTER);
    centerContent.setPadding(new Insets(20));

    updateTotal.run();

    ScrollPane scrollPane = new ScrollPane(centerContent);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(scrollPane);

    Scene scene = new Scene(root, 900, 650);
    stage.setScene(scene);
    stage.setTitle("Select Meal");
    stage.show();
  }

  private static ComboSelection createComboSelection(
    MenuItem item,
    VBox comboSizeSelector
  ) {
    List<ComboChoiceGroup> comboGroups = DatabaseHelper.getComboChoiceGroupsByItem(item.getId());

    VBox comboBox = new VBox(18);
    comboBox.setAlignment(Pos.CENTER);
    comboBox.setMaxWidth(850);
    comboBox.setPadding(new Insets(24, 30, 24, 30));
    comboBox.setStyle(createWhiteCardStyle());

    Label comboTitle = new Label("Required Combo Choices");
    comboTitle.setStyle(
      "-fx-font-size: 24px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;"
    );

    Label comboSubtitle = new Label("Choose your main, side, drink, and combo size.");
    comboSubtitle.setWrapText(true);
    comboSubtitle.setMaxWidth(650);
    comboSubtitle.setAlignment(Pos.CENTER);
    comboSubtitle.setStyle(
      "-fx-font-size: 15px;" +
        "-fx-text-fill: #6f6f6f;"
    );

    comboBox.getChildren().addAll(comboTitle, comboSubtitle);
    comboBox.getChildren().add(comboSizeSelector);

    List<ToggleGroup> toggleGroups = new ArrayList<>();
    List<String> groupNames = new ArrayList<>();
    List<VBox> sections = new ArrayList<>();

    for (ComboChoiceGroup group : comboGroups) {
      ToggleGroup toggleGroup = new ToggleGroup();
      VBox section = createRadioSectionFromDatabase(group, toggleGroup);

      toggleGroups.add(toggleGroup);
      groupNames.add(group.getGroupName());
      sections.add(section);
    }

    HBox row = new HBox(22, sections.get(0), sections.get(1), sections.get(2));
    row.setAlignment(Pos.TOP_CENTER);
    comboBox.getChildren().add(row);
    //if (sections.size() >= 2) {
    //  HBox rowOne = new HBox(22, sections.get(0), sections.get(1));
    //  rowOne.setAlignment(Pos.TOP_CENTER);
    //  comboBox.getChildren().add(rowOne);
    ///} else if (sections.size() == 1) {
    //  comboBox.getChildren().add(sections.get(0));
    //}
    //if (sections.size() >= 3) {

    //  VBox drinkColumn = new VBox(12, sections.get(2));
    //  drinkColumn.setAlignment(Pos.TOP_CENTER);

    //  comboBox.getChildren().add(drinkColumn);
    //}

    for (int i = 3; i < sections.size(); i++) {
      comboBox.getChildren().add(sections.get(i));
    }

    Label giftLabel = new Label("");

    if (item.getName().toLowerCase().contains("kids combo")) {
      VBox giftBox = createKidsGiftBox(giftLabel);
      comboBox.getChildren().add(giftBox);
    }

    return new ComboSelection(comboBox, toggleGroups, groupNames, giftLabel);
  }

  private static VBox createComboSizeSelector(
    String[] selectedSize,
    double[] selectedPrice,
    Runnable updateTotal
  ) {
    Label title = new Label("Combo Size");
    title.setStyle(createSmallTitleStyle());

    ToggleButton regular = new ToggleButton("Regular");
    ToggleButton medium = new ToggleButton("Medium +15");
    ToggleButton large = new ToggleButton("Large +25");

    String normalStyle = createSizeButtonNormalStyle();
    String selectedStyle = createSizeButtonSelectedStyle();

    regular.setStyle(selectedStyle);
    medium.setStyle(normalStyle);
    large.setStyle(normalStyle);

    selectedSize[0] = "Regular";
    selectedPrice[0] = 0;

    Runnable resetStyles = () -> {
      regular.setStyle(normalStyle);
      medium.setStyle(normalStyle);
      large.setStyle(normalStyle);
    };

    regular.setOnAction(e -> {
      resetStyles.run();
      regular.setStyle(selectedStyle);
      selectedSize[0] = "Regular";
      selectedPrice[0] = 0;
      updateTotal.run();
    });

    medium.setOnAction(e -> {
      resetStyles.run();
      medium.setStyle(selectedStyle);
      selectedSize[0] = "Medium";
      selectedPrice[0] = 15;
      updateTotal.run();
    });

    large.setOnAction(e -> {
      resetStyles.run();
      large.setStyle(selectedStyle);
      selectedSize[0] = "Large";
      selectedPrice[0] = 25;
      updateTotal.run();
    });

    HBox buttons = new HBox(8, regular, medium, large);
    buttons.setAlignment(Pos.CENTER);

    VBox wrapper = new VBox(8, title, buttons);
    wrapper.setAlignment(Pos.CENTER);
    wrapper.setPadding(new Insets(14));
    wrapper.setPrefWidth(760);
    wrapper.setMaxWidth(760);
    wrapper.setStyle(createSmallOptionCardStyle());

    return wrapper;
  }

  private static String createSmallTitleStyle() {
    return "-fx-font-size: 15px;" +
      "-fx-font-weight: bold;" +
      "-fx-text-fill: #1f1f1f;";
  }

  private static String createSizeButtonNormalStyle() {
    return "-fx-background-color: white;" +
      "-fx-border-color: #ff9800;" +
      "-fx-border-radius: 20;" +
      "-fx-background-radius: 20;" +
      "-fx-padding: 7 14;" +
      "-fx-font-size: 12px;" +
      "-fx-font-weight: bold;" +
      "-fx-text-fill: #333333;" +
      "-fx-cursor: hand;";
  }

  private static String createSizeButtonSelectedStyle() {
    return "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
      "-fx-text-fill: white;" +
      "-fx-border-color: #ff9800;" +
      "-fx-border-radius: 20;" +
      "-fx-background-radius: 20;" +
      "-fx-padding: 7 14;" +
      "-fx-font-size: 12px;" +
      "-fx-font-weight: bold;" +
      "-fx-cursor: hand;";
  }

  private static String createSmallOptionCardStyle() {
    return "-fx-background-color: rgba(255,248,238,0.95);" +
      "-fx-background-radius: 20;";
  }

  private static VBox createRadioSectionFromDatabase(ComboChoiceGroup group, ToggleGroup toggleGroup) {
    Label titleLabel = new Label(group.getGroupName());
    titleLabel.setStyle(
      "-fx-font-size: 17px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;"
    );

    VBox section = new VBox(8);
    section.setAlignment(Pos.CENTER_LEFT);
    section.setPadding(new Insets(16));
    section.setPrefWidth(360);
    section.setMinHeight(160);
    section.setStyle(
      "-fx-background-color: rgba(255,248,238,0.95);" +
        "-fx-background-radius: 20;"
    );

    section.getChildren().add(titleLabel);

    List<ComboChoiceOption> options = group.getOptions();

    for (int i = 0; i < options.size(); i++) {
      ComboChoiceOption option = options.get(i);

      RadioButton radioButton = new RadioButton(option.getOptionName());
      radioButton.setToggleGroup(toggleGroup);
      radioButton.setUserData(option.getOptionName());

      radioButton.setWrapText(true);
      radioButton.setMaxWidth(320);

      radioButton.setStyle(
        "-fx-font-size: 14px;" +
          "-fx-text-fill: #202020;" +
          "-fx-cursor: hand;"
      );

      if (i == 0) {
        radioButton.setSelected(true);
      }

      section.getChildren().add(radioButton);
    }

    return section;
  }

  private static VBox createBox(List<CheckBox> checkBoxes, String title, String subtitle) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle(
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label subtitleLabel = new Label(subtitle);
    subtitleLabel.setWrapText(true);
    subtitleLabel.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: #6f6f6f;" +
                    "-fx-padding: 2 0 8 0;"
    );

    VBox box = new VBox(8);
    box.getChildren().addAll(titleLabel, subtitleLabel);
    box.getChildren().addAll(checkBoxes);
    box.setAlignment(Pos.CENTER_LEFT);
    box.setMaxWidth(560);
    box.setPadding(new Insets(24, 30, 24, 30));
    box.setStyle(createWhiteCardStyle());

    return box;
  }

  private static @NonNull Button getViewCartButton(Stage stage) {
    Button viewCartButton = new Button("View Cart");
    viewCartButton.setStyle(
      "-fx-font-size: 18px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-color: #4CAF50;" +
        "-fx-text-fill: white;" +
        "-fx-padding: 13 28;" +
        "-fx-background-radius: 14;" +
        "-fx-cursor: hand;" +
        "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.25), 8, 0, 0, 2);"
    );
    viewCartButton.setOnAction(e -> CartScreen.show(stage));
    return viewCartButton;
  }

  private static VBox createComboCustomizeBox(List<CheckBox> comboCustomizeBoxes) {
    Label title = new Label("Customize Combo Items");
    title.setStyle(
      "-fx-font-size: 24px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;"
    );

    Label subtitle = new Label("Choose small changes for the selected burger or drink.");
    subtitle.setWrapText(true);
    subtitle.setStyle(
      "-fx-font-size: 15px;" +
        "-fx-text-fill: #6f6f6f;" +
        "-fx-padding: 2 0 8 0;"
    );

    String[] options = {
      "No Onion",
      "No Pickles",
      "No Lettuce",
      "No Sauce",
      "Less Ice",
      "No Sugar"
    };

    VBox box = new VBox(8);
    box.getChildren().addAll(title, subtitle);

    for (String option : options) {
      CheckBox checkBox = new CheckBox(option);
      checkBox.setStyle(
        "-fx-font-size: 16px;" +
          "-fx-font-weight: 600;" +
          "-fx-text-fill: #202020;" +
          "-fx-padding: 4 0 4 0;" +
          "-fx-cursor: hand;"
      );

      comboCustomizeBoxes.add(checkBox);
      box.getChildren().add(checkBox);
    }

    box.setAlignment(Pos.CENTER_LEFT);
    box.setMaxWidth(560);
    box.setPadding(new Insets(24, 30, 24, 30));
    box.setStyle(createWhiteCardStyle());

    return box;
  }

  private static VBox createKidsGiftBox(Label giftLabel) {
    Label title = new Label("Surprise Gift Game");
    title.setStyle(
      "-fx-font-size: 20px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;"
    );

    Label subtitle = new Label("Roll the dice to reveal the kids meal surprise gift.");
    subtitle.setWrapText(true);
    subtitle.setStyle(
      "-fx-font-size: 14px;" +
        "-fx-text-fill: #666666;"
    );

    Button rollButton = new Button("Roll Dice");
    rollButton.setStyle(
      "-fx-font-size: 16px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
        "-fx-text-fill: white;" +
        "-fx-padding: 10 24;" +
        "-fx-background-radius: 14;" +
        "-fx-cursor: hand;"
    );

    giftLabel.setStyle(
      "-fx-font-size: 15px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: #1f1f1f;" +
        "-fx-background-color: rgba(255,152,0,0.14);" +
        "-fx-background-radius: 14;" +
        "-fx-padding: 9 16 9 16;"
    );

    rollButton.setOnAction(e -> {
      String[] gifts = {
        "Gift: Mini Toy Car",
        "Gift: Sticker Pack",
        "Gift: Puzzle Card",
        "Gift: Surprise Keychain",
        "Gift: Coloring Sheet",
        "Gift: Mystery Toy"
      };

      int diceNumber = new Random().nextInt(6) + 1;
      giftLabel.setText("Dice " + diceNumber + " -> " + gifts[diceNumber - 1]);
    });

    VBox box = new VBox(10, title, subtitle, rollButton, giftLabel);
    box.setAlignment(Pos.CENTER);
    box.setMaxWidth(560);
    box.setPadding(new Insets(18));
    box.setStyle(
      "-fx-background-color: rgba(255,248,238,0.95);" +
        "-fx-background-radius: 22;"
    );

    return box;
  }

  private static String getSelectedRadioText(ToggleGroup group) {
    Toggle selectedToggle = group.getSelectedToggle();

    if (selectedToggle == null) {
      return "";
    }

    return selectedToggle.getUserData().toString();
  }

  private static CheckBox createExtraCheckBox(String name, double price) {
    CheckBox checkBox = new CheckBox(name + " (+" + String.format("%.2f kr", price) + ")");
    checkBox.setUserData(price);
    checkBox.setStyle(
      "-fx-font-size: 16px;" +
        "-fx-font-weight: 600;" +
        "-fx-text-fill: #202020;" +
        "-fx-padding: 4 0 4 0;" +
        "-fx-cursor: hand;"
    );
    return checkBox;
  }

  private static List<String> getSelectedExtras(CheckBox... checkBoxes) {
    List<String> selectedExtras = new ArrayList<>();

    for (CheckBox checkBox : checkBoxes) {
      if (checkBox.isSelected()) {
        selectedExtras.add(checkBox.getText());
      }
    }

    return selectedExtras;
  }

  private static double getSelectedExtrasPrice(CheckBox... checkBoxes) {
    double total = 0;

    for (CheckBox checkBox : checkBoxes) {
      if (checkBox.isSelected()) {
        total += (double) checkBox.getUserData();
      }
    }

    return total;
  }

  private static String createWhiteCardStyle() {
    return "-fx-background-color: rgba(255,255,255,0.96);" +
      "-fx-background-radius: 26;" +
      "-fx-border-color: rgba(255,255,255,0.75);" +
      "-fx-border-width: 1.2;" +
      "-fx-border-radius: 26;" +
      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0, 0, 4);";
  }

  private static class ComboSelection {
    private final VBox comboBox;
    private final List<ToggleGroup> toggleGroups;
    private final List<String> groupNames;
    private final Label giftLabel;

    public ComboSelection(
      VBox comboBox,
      List<ToggleGroup> toggleGroups,
      List<String> groupNames,
      Label giftLabel
    ) {
      this.comboBox = comboBox;
      this.toggleGroups = toggleGroups;
      this.groupNames = groupNames;
      this.giftLabel = giftLabel;
    }
  }
}