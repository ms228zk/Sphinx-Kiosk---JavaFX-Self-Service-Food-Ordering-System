package se.lnu;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
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

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backButton, spacer, homeButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label(item.getName());
    title.setStyle(
            "-fx-font-size: 36px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label description = new Label(item.getDescription());
    description.setWrapText(true);
    description.setMaxWidth(520);
    description.setAlignment(Pos.CENTER);
    description.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: #555555;"
    );

    Label priceLabel = new Label("Unit price: " + String.format("%.2f kr", item.getPrice()));
    priceLabel.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label currentTotalLabel = new Label();
    currentTotalLabel.setStyle(
            "-fx-font-size: 19px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #FF8C00;"
    );

    Button minusButton = new Button("-");
    Button plusButton = new Button("+");

    String quantityButtonStyle =
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                    "-fx-text-fill: white;" +
                    "-fx-min-width: 48;" +
                    "-fx-min-height: 44;" +
                    "-fx-background-radius: 14;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.25), 7, 0, 0, 2);";

    minusButton.setStyle(quantityButtonStyle);
    plusButton.setStyle(quantityButtonStyle);

    Label quantityLabel = new Label(String.valueOf(App.selectedQuantity));
    quantityLabel.setStyle(
            "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;" +
                    "-fx-min-width: 38;" +
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

      currentTotalLabel.setText("Current total: " + String.format("%.2f kr", finalTotal));
    };

    for (CheckBox checkBox : extrasBoxes) {
      checkBox.setOnAction(e -> updateTotal.run());
    }

    ComboSelection comboSelection = null;
    VBox comboBox = null;
    List<CheckBox> comboCustomizeBoxes = new ArrayList<>();

    if (isCombo) {
      VBox comboSizeSelector = createComboSizeSelector(comboSize, comboPrice, updateTotal);
      comboSelection = createComboSelection(item, comboSizeSelector);
      comboBox = comboSelection.comboBox;
    }

    List<RemovableIngredient> removableIngredients =
            DatabaseHelper.getRemovableIngredientsByItem(item.getId());

    List<CheckBox> removablesCheckBoxes = new ArrayList<>();

    for (RemovableIngredient r : removableIngredients) {
      CheckBox box = new CheckBox(r.name());
      box.setStyle(
              "-fx-font-size: 15px;" +
                      "-fx-font-weight: 600;" +
                      "-fx-text-fill: #202020;" +
                      "-fx-padding: 3 0;" +
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

    HBox quantityBox = new HBox(14, minusButton, quantityLabel, plusButton);
    quantityBox.setAlignment(Pos.CENTER);

    VBox extrasBox = createBox(
            extrasCheckBoxes,
            "Optional Extras",
            "Optional add-ons increase the item price",
            false
    );

    VBox removablesBox = createBox(
            removablesCheckBoxes,
            "Remove Ingredients",
            "Remove unwanted parts",
            isCombo
    );

    extrasBox.setPrefWidth(360);
    extrasBox.setMaxWidth(360);

    removablesBox.setPrefWidth(isCombo ? 520 : 360);
    removablesBox.setMaxWidth(isCombo ? 520 : 360);

    HBox ingredientChanges = new HBox(20, extrasBox, removablesBox);
    ingredientChanges.setAlignment(Pos.TOP_CENTER);

    Label statusLabel = new Label("");
    statusLabel.setOpacity(0);
    statusLabel.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-padding: 9 18;" +
                    "-fx-background-radius: 14;" +
                    "-fx-font-weight: bold;"
    );

    Button confirmButton = new Button("Add to Order");
    confirmButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 30;" +
                    "-fx-background-radius: 15;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.30), 8, 0, 0, 2);"
    );

    ComboSelection finalComboSelection = comboSelection;

    confirmButton.setOnAction(e -> {
      List<String> selectedExtras = getSelectedExtras(extrasBoxes);
      double extrasPrice = getSelectedExtrasPrice(extrasBoxes);

      double totalExtraPrice = extrasPrice + comboPrice[0];

      List<String> removedIngredients = getSelectedExtras(removablesBoxes);

      List<String> comboChoices = new ArrayList<>();

      if (finalComboSelection != null) {
        for (int i = 0; i < finalComboSelection.groupNames.size(); i++) {
          String groupName = finalComboSelection.groupNames.get(i);
          ToggleGroup group = finalComboSelection.toggleGroups.get(i);
          comboChoices.add(groupName + ": " + getSelectedRadioText(group));
        }

        comboChoices.add(
                "Combo Size: " +
                        comboSize[0] +
                        " (+" +
                        String.format("%.2f kr", comboPrice[0]) +
                        ")"
        );

        List<String> comboCustomizations = getSelectedExtras(
                comboCustomizeBoxes.toArray(new CheckBox[0])
        );

        if (!comboCustomizations.isEmpty()) {
          comboChoices.add("Customizations: " + String.join(", ", comboCustomizations));
        }

        if (item.getName().toLowerCase().contains("kids combo")) {
          if (!finalComboSelection.giftLabel.getText().isBlank()) {
            comboChoices.add(finalComboSelection.giftLabel.getText());
          } else {
            comboChoices.add("Surprise gift included");
          }
        } else if (!finalComboSelection.giftLabel.getText().isBlank()) {
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
      confirmButton.setDisable(true);
      confirmButton.setText("Added");

      FadeTransition fadeIn = new FadeTransition(Duration.millis(250), statusLabel);
      fadeIn.setFromValue(0);
      fadeIn.setToValue(1);
      fadeIn.play();

      PauseTransition pause = new PauseTransition(Duration.seconds(1.1));
      pause.setOnFinished(event -> CategoryScreen.show(stage));
      pause.play();
    });

    VBox centerContent = new VBox(14);
    centerContent.getChildren().addAll(title, description, priceLabel);

    if (comboBox != null) {
      centerContent.getChildren().add(comboBox);
    }

    centerContent.getChildren().add(ingredientChanges);

    centerContent.setAlignment(Pos.CENTER);
    centerContent.setPadding(new Insets(16, 20, 125, 20));

    updateTotal.run();

    ScrollPane scrollPane = new ScrollPane(centerContent);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    HBox bottomActionBar = createBottomActionBar(
            item.getName(),
            currentTotalLabel,
            quantityBox,
            confirmButton
    );

    VBox bottomArea = new VBox(8, bottomActionBar, statusLabel);
    bottomArea.setAlignment(Pos.CENTER);
    bottomArea.setPadding(new Insets(6, 0, 0, 0));

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(scrollPane);
    root.setBottom(bottomArea);

    Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
    stage.setScene(scene);
    stage.setTitle("Select Meal");
    WindowManager.enforceStandardSize(stage);
  }

  private static HBox createBottomActionBar(
          String itemName,
          Label currentTotalLabel,
          HBox quantityBox,
          Button confirmButton
  ) {
    Label bottomItemName = new Label(itemName);
    bottomItemName.setWrapText(true);
    bottomItemName.setMaxWidth(260);
    bottomItemName.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    VBox summaryBox = new VBox(3, bottomItemName, currentTotalLabel);
    summaryBox.setAlignment(Pos.CENTER_LEFT);

    Region spacerOne = new Region();
    Region spacerTwo = new Region();

    HBox.setHgrow(spacerOne, Priority.ALWAYS);
    HBox.setHgrow(spacerTwo, Priority.ALWAYS);

    HBox bar = new HBox(24, summaryBox, spacerOne, quantityBox, spacerTwo, confirmButton);
    bar.setAlignment(Pos.CENTER);
    bar.setPadding(new Insets(10, 22, 10, 22));
    bar.setMaxWidth(900);
    bar.setStyle(
            "-fx-background-color: rgba(255,255,255,0.97);" +
                    "-fx-background-radius: 24;" +
                    "-fx-border-color: rgba(255,152,0,0.28);" +
                    "-fx-border-width: 1.4;" +
                    "-fx-border-radius: 24;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 16, 0, 0, 4);"
    );

    return bar;
  }

  private static ComboSelection createComboSelection(
          MenuItem item,
          VBox comboSizeSelector
  ) {
    List<ComboChoiceGroup> comboGroups =
            DatabaseHelper.getComboChoiceGroupsByItem(item.getId());

    VBox comboBox = new VBox(14);
    comboBox.setAlignment(Pos.CENTER);
    comboBox.setMaxWidth(820);
    comboBox.setPadding(new Insets(18, 22, 18, 22));
    comboBox.setStyle(createWhiteCardStyle());

    Label comboTitle = new Label("Required Combo Choices");
    comboTitle.setStyle(
            "-fx-font-size: 22px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label comboSubtitle = new Label("Choose your main, side, drink, and combo size.");
    comboSubtitle.setWrapText(true);
    comboSubtitle.setMaxWidth(620);
    comboSubtitle.setAlignment(Pos.CENTER);
    comboSubtitle.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-text-fill: #6f6f6f;"
    );

    comboBox.getChildren().addAll(comboTitle, comboSubtitle, comboSizeSelector);

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

    if (sections.size() >= 3) {
      HBox row = new HBox(16, sections.get(0), sections.get(1), sections.get(2));
      row.setAlignment(Pos.TOP_CENTER);
      comboBox.getChildren().add(row);
    } else if (sections.size() == 2) {
      HBox row = new HBox(16, sections.get(0), sections.get(1));
      row.setAlignment(Pos.TOP_CENTER);
      comboBox.getChildren().add(row);
    } else if (sections.size() == 1) {
      comboBox.getChildren().add(sections.get(0));
    }

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
    wrapper.setPadding(new Insets(12));
    wrapper.setPrefWidth(700);
    wrapper.setMaxWidth(700);
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

  private static VBox createRadioSectionFromDatabase(
          ComboChoiceGroup group,
          ToggleGroup toggleGroup
  ) {
    Label titleLabel = new Label(group.getGroupName());
    titleLabel.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    VBox section = new VBox(7);
    section.setAlignment(Pos.CENTER_LEFT);
    section.setPadding(new Insets(14));
    section.setPrefWidth(290);
    section.setMinHeight(140);
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
      radioButton.setMaxWidth(260);

      radioButton.setStyle(
              "-fx-font-size: 13px;" +
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

  private static VBox createBox(
          List<CheckBox> checkBoxes,
          String title,
          String subtitle,
          boolean compactScrollable
  ) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle(
            "-fx-font-size: 22px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label subtitleLabel = new Label(subtitle);
    subtitleLabel.setWrapText(true);
    subtitleLabel.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-text-fill: #6f6f6f;" +
                    "-fx-padding: 2 0 6 0;"
    );

    VBox box = new VBox(7);
    box.getChildren().addAll(titleLabel, subtitleLabel);

    if (checkBoxes.size() < 10) {
      VBox list = new VBox(6);
      list.getChildren().addAll(checkBoxes);
      box.getChildren().add(list);
    } else {
      HBox columns = new HBox(20);

      VBox col1 = new VBox(5);
      VBox col2 = new VBox(5);

      int half = (int) Math.ceil(checkBoxes.size() / 2.0);

      for (int i = 0; i < checkBoxes.size(); i++) {
        if (i < half) {
          col1.getChildren().add(checkBoxes.get(i));
        } else {
          col2.getChildren().add(checkBoxes.get(i));
        }
      }

      columns.getChildren().addAll(col1, col2);

      if (compactScrollable) {
        ScrollPane innerScroll = new ScrollPane(columns);
        innerScroll.setFitToWidth(true);
        innerScroll.setPannable(true);
        innerScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        innerScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        innerScroll.setMaxHeight(230);
        innerScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        box.getChildren().add(innerScroll);
      } else {
        box.getChildren().add(columns);
      }
    }

    box.setAlignment(Pos.CENTER_LEFT);
    box.setMaxWidth(560);
    box.setPadding(new Insets(20, 24, 20, 24));
    box.setStyle(createWhiteCardStyle());

    return box;
  }

  private static VBox createKidsGiftBox(Label giftLabel) {
    Label title = new Label("Surprise Gift Game");
    title.setStyle(
            "-fx-font-size: 19px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label subtitle = new Label("Roll the dice to reveal the kids meal surprise gift.");
    subtitle.setWrapText(true);
    subtitle.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-text-fill: #666666;"
    );

    Button rollButton = new Button("Roll Dice");
    rollButton.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 9 22;" +
                    "-fx-background-radius: 14;" +
                    "-fx-cursor: hand;"
    );

    giftLabel.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;" +
                    "-fx-background-color: rgba(255,152,0,0.14);" +
                    "-fx-background-radius: 14;" +
                    "-fx-padding: 8 14;"
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

    VBox box = new VBox(9, title, subtitle, rollButton, giftLabel);
    box.setAlignment(Pos.CENTER);
    box.setMaxWidth(520);
    box.setPadding(new Insets(16));
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
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: 600;" +
                    "-fx-text-fill: #202020;" +
                    "-fx-padding: 3 0;" +
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
            "-fx-background-radius: 24;" +
            "-fx-border-color: rgba(255,255,255,0.75);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 16, 0, 0, 4);";
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