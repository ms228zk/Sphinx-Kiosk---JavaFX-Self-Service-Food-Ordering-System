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
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MealSelectionScreen {

  public static void show(Stage stage, MenuItem item) {
    App.selectedMenuItem = item;
    App.selectedQuantity = 1;

    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
    );
    backButton.setOnAction(e -> CategoryScreen.show(stage));

    Button cartButton = createCartButton(stage);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(backButton, spacer, cartButton);
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

    boolean isCombo = App.selectedCategoryName != null
            && App.selectedCategoryName.equalsIgnoreCase("Combos");

    ComboSelection comboSelection = null;
    VBox comboBox = null;

    if (isCombo) {
      comboSelection = createComboSelection(item);
      comboBox = comboSelection.comboBox;
    }

    List<ExtraOption> extraOptions = getExtrasForItem(item);
    List<CheckBox> extrasCheckBoxes = new ArrayList<>();

    for (ExtraOption option : extraOptions) {
      CheckBox checkBox = createExtraCheckBox(option.getName(), option.getPrice());
      extrasCheckBoxes.add(checkBox);
    }

    CheckBox[] extrasBoxes = extrasCheckBoxes.toArray(new CheckBox[0]);

    Runnable updateTotal = () -> {
      double extrasPrice = getSelectedExtrasPrice(extrasBoxes);
      double finalTotal = (item.getPrice() + extrasPrice) * App.selectedQuantity;
      itemTotalLabel.setText("Item total: " + String.format("%.2f kr", finalTotal));
    };

    updateTotal.run();

    for (CheckBox checkBox : extrasBoxes) {
      checkBox.setOnAction(e -> updateTotal.run());
    }

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

    Label extrasTitle = new Label("Optional Extras");
    extrasTitle.setStyle(
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label extrasSubtitle = new Label("Optional add-ons increase the item price");
    extrasSubtitle.setWrapText(true);
    extrasSubtitle.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: #6f6f6f;" +
                    "-fx-padding: 2 0 8 0;"
    );

    VBox extrasBox = new VBox(8);
    extrasBox.getChildren().addAll(extrasTitle, extrasSubtitle);
    extrasBox.getChildren().addAll(extrasCheckBoxes);
    extrasBox.setAlignment(Pos.CENTER_LEFT);
    extrasBox.setMaxWidth(560);
    extrasBox.setPadding(new Insets(24, 30, 24, 30));
    extrasBox.setStyle(createWhiteCardStyle());

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

      List<String> comboChoices = new ArrayList<>();

      if (finalComboSelection != null) {
        comboChoices.add("Main: " + getSelectedRadioText(finalComboSelection.mainGroup));
        comboChoices.add("Side: " + getSelectedRadioText(finalComboSelection.sideGroup));
        comboChoices.add("Drink: " + getSelectedRadioText(finalComboSelection.drinkGroup));

        if (!finalComboSelection.giftLabel.getText().isBlank()) {
          comboChoices.add(finalComboSelection.giftLabel.getText());
        }
      }

      Cart.getInstance().addItem(
              item,
              App.selectedQuantity,
              selectedExtras,
              extrasPrice,
              comboChoices
      );

      if (selectedExtras.isEmpty()) {
        statusLabel.setText("Added " + App.selectedQuantity + " x " + item.getName());
      } else {
        statusLabel.setText("Added " + App.selectedQuantity + " x " + item.getName() + " with extras");
      }

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

    VBox centerContent = new VBox(18);
    centerContent.getChildren().addAll(title, description, priceLabel);

    if (comboBox != null) {
      centerContent.getChildren().add(comboBox);
    }

    centerContent.getChildren().addAll(
            extrasBox,
            quantityBox,
            itemTotalLabel,
            confirmButton,
            statusLabel,
            viewCartButton
    );

    centerContent.setAlignment(Pos.CENTER);
    centerContent.setPadding(new Insets(20));

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

  private static ComboSelection createComboSelection(MenuItem item) {
    String itemName = item.getName().toLowerCase();

    ToggleGroup mainGroup = new ToggleGroup();
    ToggleGroup sideGroup = new ToggleGroup();
    ToggleGroup drinkGroup = new ToggleGroup();

    VBox comboBox = new VBox(18);
    comboBox.setAlignment(Pos.CENTER);
    comboBox.setMaxWidth(700);
    comboBox.setPadding(new Insets(24, 30, 24, 30));
    comboBox.setStyle(createWhiteCardStyle());

    Label comboTitle = new Label("Required Combo Choices");
    comboTitle.setStyle(
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label comboSubtitle = new Label("Choose one main, one side, and one drink. A drink is required for every combo.");
    comboSubtitle.setWrapText(true);
    comboSubtitle.setMaxWidth(600);
    comboSubtitle.setAlignment(Pos.CENTER);
    comboSubtitle.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: #6f6f6f;"
    );

    VBox mainSection;
    VBox sideSection;

    if (itemName.contains("family feast")) {
      mainSection = createRadioSection(
              "Choose Family Main",
              mainGroup,
              "2 BBQ Smash Burgers + 2 Crispy Chicken Burgers",
              "2 Halloumi Burgers + 2 Crispy Chicken Burgers",
              "Mixed Burger Selection"
      );

      sideSection = createRadioSection(
              "Choose Sharing Side",
              sideGroup,
              "Loaded Fries",
              "Mozzarella Sticks",
              "Spicy Chicken Bites",
              "No Side"
      );

    } else if (itemName.contains("kids combo")) {
      mainSection = createRadioSection(
              "Choose Kids Main",
              mainGroup,
              "Crispy Chicken Burger",
              "Halloumi Burger",
              "Spicy Chicken Bites"
      );

      sideSection = createRadioSection(
              "Choose Kids Side",
              sideGroup,
              "Mini Donuts",
              "Loaded Fries",
              "Mozzarella Sticks",
              "No Side"
      );

    } else if (itemName.contains("burger combo")) {
      mainSection = createRadioSection(
              "Choose Burger",
              mainGroup,
              "BBQ Smash Burger",
              "Crispy Chicken Burger",
              "Halloumi Burger"
      );

      sideSection = createRadioSection(
              "Choose Side",
              sideGroup,
              "Loaded Fries",
              "Mozzarella Sticks",
              "Spicy Chicken Bites",
              "No Side"
      );

    } else if (itemName.contains("chicken combo")) {
      mainSection = createRadioSection(
              "Choose Chicken Main",
              mainGroup,
              "Crispy Chicken Burger",
              "Spicy Chicken Bites",
              "Chicken Bites + Mozzarella Sticks"
      );

      sideSection = createRadioSection(
              "Choose Side",
              sideGroup,
              "Loaded Fries",
              "Mozzarella Sticks",
              "Spicy Chicken Bites",
              "No Side"
      );

    } else {
      mainSection = createRadioSection(
              "Choose Snack Main",
              mainGroup,
              "Loaded Fries",
              "Mozzarella Sticks",
              "Spicy Chicken Bites"
      );

      sideSection = createRadioSection(
              "Choose Extra Snack",
              sideGroup,
              "Mozzarella Sticks",
              "Mini Donuts",
              "Loaded Fries",
              "No Side"
      );
    }

    VBox drinkSection = createRadioSection(
            "Choose Drink",
            drinkGroup,
            "Iced Coffee",
            "Mango Smoothie",
            "Lemon Mint Cooler"
    );

    HBox rowOne = new HBox(18, mainSection, sideSection);
    rowOne.setAlignment(Pos.CENTER);

    VBox rowTwo = new VBox(18, drinkSection);
    rowTwo.setAlignment(Pos.CENTER);

    Label giftLabel = new Label("");
    VBox giftBox = null;

    if (itemName.contains("kids combo")) {
      giftBox = createKidsGiftBox(giftLabel);
    }

    comboBox.getChildren().addAll(comboTitle, comboSubtitle, rowOne, rowTwo);

    if (giftBox != null) {
      comboBox.getChildren().add(giftBox);
    }

    return new ComboSelection(comboBox, mainGroup, sideGroup, drinkGroup, giftLabel);
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

    Button rollButton = new Button("🎲 Roll Dice");
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
      giftLabel.setText("Dice " + diceNumber + " → " + gifts[diceNumber - 1]);
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

  private static VBox createRadioSection(String title, ToggleGroup group, String... options) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    VBox section = new VBox(8);
    section.setAlignment(Pos.CENTER_LEFT);
    section.setPadding(new Insets(16));
    section.setPrefWidth(300);
    section.setStyle(
            "-fx-background-color: rgba(255,248,238,0.95);" +
                    "-fx-background-radius: 20;"
    );

    section.getChildren().add(titleLabel);

    for (int i = 0; i < options.length; i++) {
      RadioButton radioButton = new RadioButton(options[i]);
      radioButton.setToggleGroup(group);
      radioButton.setUserData(options[i]);
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

  private static Button createCartButton(Stage stage) {
    Button cartButton = new Button("Cart (" + Cart.getInstance().getItemCount() + ")");
    cartButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
    );
    cartButton.setOnAction(e -> CartScreen.show(stage));
    return cartButton;
  }

  private static String createWhiteCardStyle() {
    return "-fx-background-color: rgba(255,255,255,0.96);" +
            "-fx-background-radius: 26;" +
            "-fx-border-color: rgba(255,255,255,0.75);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 26;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 18, 0, 0, 4);";
  }

  private static List<ExtraOption> getExtrasForItem(MenuItem item) {
    List<ExtraOption> extras = new ArrayList<>();
    String itemName = item.getName().toLowerCase();

    if (itemName.contains("bbq smash burger")) {
      extras.add(new ExtraOption("Extra Cheese", 10.00));
      extras.add(new ExtraOption("Extra Beef Patty", 20.00));
      extras.add(new ExtraOption("Extra BBQ Sauce", 7.00));
      extras.add(new ExtraOption("Extra Onion", 5.00));
      extras.add(new ExtraOption("Extra Pickles", 5.00));

    } else if (itemName.contains("crispy chicken burger")) {
      extras.add(new ExtraOption("Extra Cheese", 10.00));
      extras.add(new ExtraOption("Extra Chicken Patty", 20.00));
      extras.add(new ExtraOption("Extra Garlic Mayo", 7.00));
      extras.add(new ExtraOption("Extra Lettuce", 5.00));
      extras.add(new ExtraOption("Extra Jalapeños", 8.00));

    } else if (itemName.contains("halloumi burger")) {
      extras.add(new ExtraOption("Extra Halloumi", 18.00));
      extras.add(new ExtraOption("Extra Salad", 5.00));
      extras.add(new ExtraOption("Extra Garlic Sauce", 7.00));
      extras.add(new ExtraOption("Extra Tomato", 5.00));
      extras.add(new ExtraOption("Extra Onion", 5.00));

    } else if (itemName.contains("iced coffee")) {
      extras.add(new ExtraOption("Extra Shot", 10.00));
      extras.add(new ExtraOption("Oat Milk", 6.00));
      extras.add(new ExtraOption("Vanilla Syrup", 7.00));
      extras.add(new ExtraOption("Caramel Syrup", 7.00));
      extras.add(new ExtraOption("Whipped Cream", 8.00));

    } else if (itemName.contains("mango smoothie")) {
      extras.add(new ExtraOption("Extra Mango", 8.00));
      extras.add(new ExtraOption("Protein Boost", 12.00));
      extras.add(new ExtraOption("Coconut Milk", 6.00));
      extras.add(new ExtraOption("Chia Seeds", 6.00));
      extras.add(new ExtraOption("Whipped Cream", 8.00));

    } else if (itemName.contains("lemon mint cooler")) {
      extras.add(new ExtraOption("Extra Mint", 5.00));
      extras.add(new ExtraOption("Extra Lemon", 5.00));
      extras.add(new ExtraOption("Ice Cubes", 3.00));
      extras.add(new ExtraOption("Sugar Syrup", 5.00));
      extras.add(new ExtraOption("Sparkling Water", 6.00));

    } else if (itemName.contains("loaded fries")) {
      extras.add(new ExtraOption("Extra Cheese Sauce", 10.00));
      extras.add(new ExtraOption("Extra BBQ Sauce", 7.00));
      extras.add(new ExtraOption("Extra Jalapeños", 8.00));
      extras.add(new ExtraOption("Extra Chicken Bites", 15.00));
      extras.add(new ExtraOption("Extra Onion", 5.00));

    } else if (itemName.contains("mozzarella sticks")) {
      extras.add(new ExtraOption("Extra Marinara Dip", 7.00));
      extras.add(new ExtraOption("Extra Garlic Dip", 7.00));
      extras.add(new ExtraOption("Extra Cheese Dust", 6.00));
      extras.add(new ExtraOption("Extra Spicy Dip", 7.00));

    } else if (itemName.contains("spicy chicken bites")) {
      extras.add(new ExtraOption("Extra Spicy Sauce", 7.00));
      extras.add(new ExtraOption("Extra Garlic Mayo", 7.00));
      extras.add(new ExtraOption("Extra BBQ Sauce", 7.00));
      extras.add(new ExtraOption("Extra Jalapeños", 8.00));

    } else if (itemName.contains("chocolate brownie")) {
      extras.add(new ExtraOption("Extra Chocolate Sauce", 7.00));
      extras.add(new ExtraOption("Vanilla Ice Cream", 12.00));
      extras.add(new ExtraOption("Whipped Cream", 8.00));
      extras.add(new ExtraOption("Sprinkles", 5.00));

    } else if (itemName.contains("mini donuts")) {
      extras.add(new ExtraOption("Extra Glaze", 6.00));
      extras.add(new ExtraOption("Chocolate Dip", 7.00));
      extras.add(new ExtraOption("Caramel Dip", 7.00));
      extras.add(new ExtraOption("Sprinkles", 5.00));

    } else if (itemName.contains("ice cream sundae")) {
      extras.add(new ExtraOption("Extra Chocolate Sauce", 7.00));
      extras.add(new ExtraOption("Extra Caramel Sauce", 7.00));
      extras.add(new ExtraOption("Extra Toppings", 8.00));
      extras.add(new ExtraOption("Whipped Cream", 8.00));

    } else if (itemName.contains("family feast")) {
      extras.add(new ExtraOption("Extra Large Fries", 25.00));
      extras.add(new ExtraOption("Extra Drink", 20.00));
      extras.add(new ExtraOption("Extra Sauce Pack", 15.00));
      extras.add(new ExtraOption("Extra Chicken Bites", 25.00));

    } else if (itemName.contains("kids combo")) {
      extras.add(new ExtraOption("Extra Juice", 15.00));
      extras.add(new ExtraOption("Extra Small Fries", 15.00));
      extras.add(new ExtraOption("Extra Dip", 5.00));

    } else if (itemName.contains("burger combo")) {
      extras.add(new ExtraOption("Extra Cheese", 10.00));
      extras.add(new ExtraOption("Extra Patty", 20.00));
      extras.add(new ExtraOption("Extra Fries", 20.00));
      extras.add(new ExtraOption("Extra Sauce", 7.00));

    } else if (itemName.contains("chicken combo")) {
      extras.add(new ExtraOption("Extra Chicken", 20.00));
      extras.add(new ExtraOption("Extra Garlic Mayo", 7.00));
      extras.add(new ExtraOption("Extra Fries", 20.00));
      extras.add(new ExtraOption("Extra Spicy Sauce", 7.00));

    } else if (itemName.contains("snack box")) {
      extras.add(new ExtraOption("Extra Nuggets", 20.00));
      extras.add(new ExtraOption("Extra Mozzarella Sticks", 18.00));
      extras.add(new ExtraOption("Extra Dip", 7.00));
      extras.add(new ExtraOption("Extra Drink", 20.00));
    }

    return extras;
  }

  private static class ComboSelection {
    private final VBox comboBox;
    private final ToggleGroup mainGroup;
    private final ToggleGroup sideGroup;
    private final ToggleGroup drinkGroup;
    private final Label giftLabel;

    public ComboSelection(
            VBox comboBox,
            ToggleGroup mainGroup,
            ToggleGroup sideGroup,
            ToggleGroup drinkGroup,
            Label giftLabel
    ) {
      this.comboBox = comboBox;
      this.mainGroup = mainGroup;
      this.sideGroup = sideGroup;
      this.drinkGroup = drinkGroup;
      this.giftLabel = giftLabel;
    }
  }

  private static class ExtraOption {
    private final String name;
    private final double price;

    public ExtraOption(String name, double price) {
      this.name = name;
      this.price = price;
    }

    public String getName() {
      return name;
    }

    public double getPrice() {
      return price;
    }
  }
}