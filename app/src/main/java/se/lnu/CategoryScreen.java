package se.lnu;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import se.lnu.database.DatabaseHelper;

public class CategoryScreen {

  private static FlowPane itemsPane;
  private static Label titleLabel;
  private static final List<Button> categoryButtons = new ArrayList<>();

  public static void show(Stage stage) {

    Button backBtn = ScreenStyle.createBackButton();
    backBtn.setOnAction(e -> OrderTypeScreen.show(stage));

    Button homeButton = ScreenStyle.createHomeButton(stage);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backBtn, spacer, homeButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    titleLabel = new Label("Choose a Category");
    titleLabel.setStyle(
            "-fx-font-size: 34px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    HBox categoryBar = new HBox(12);
    categoryBar.setAlignment(Pos.CENTER);
    categoryBar.setPadding(new Insets(10));

    itemsPane = new FlowPane();
    itemsPane.setHgap(18);
    itemsPane.setVgap(18);
    itemsPane.setAlignment(Pos.CENTER);
    itemsPane.setPadding(new Insets(20));

    List<Category> categories = DatabaseHelper.getCategories();

    categoryButtons.clear();

    for (Category category : categories) {
      Button categoryButton = createCategoryButton(category.getName());
      categoryButton.setUserData(category.getId());

      categoryButton.setOnAction(e -> {
        App.setCategory(category.getId(), category.getName());
        titleLabel.setText(category.getName());
        updateCategoryButtonStyles(category.getId());
        showItems(stage, category.getId());
      });

      categoryButtons.add(categoryButton);
      categoryBar.getChildren().add(categoryButton);
    }

    if (!categories.isEmpty()) {
      Category categoryToShow = categories.get(0);

      if (App.selectedCategoryId != -1) {
        for (Category category : categories) {
          if (category.getId() == App.selectedCategoryId) {
            categoryToShow = category;
            break;
          }
        }
      }

      App.setCategory(categoryToShow.getId(), categoryToShow.getName());
      titleLabel.setText(categoryToShow.getName());
      updateCategoryButtonStyles(categoryToShow.getId());
      showItems(stage, categoryToShow.getId());
    }

    VBox menuContent = new VBox(18, titleLabel, categoryBar, itemsPane);
    menuContent.setAlignment(Pos.TOP_CENTER);
    menuContent.setPadding(new Insets(18, 14, 18, 14));

    ScrollPane menuScrollPane = new ScrollPane(menuContent);
    menuScrollPane.setFitToWidth(true);
    menuScrollPane.setPannable(true);
    menuScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    menuScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    menuScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    VBox orderPanel = createOrderPanel(stage);

    HBox mainLayout = new HBox(22, menuScrollPane, orderPanel);
    mainLayout.setAlignment(Pos.TOP_CENTER);
    HBox.setHgrow(menuScrollPane, Priority.ALWAYS);

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(mainLayout);

    Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
    stage.setScene(scene);
    stage.setTitle("Select Category");
    WindowManager.enforceStandardSize(stage);
  }

  private static VBox createOrderPanel(Stage stage) {
    VBox panel = new VBox(18);
    panel.setPadding(new Insets(24));
    panel.setPrefWidth(390);
    panel.setMaxWidth(390);
    panel.setMinHeight(560);

    panel.setStyle(
            "-fx-background-color: rgba(255,255,255,0.96);" +
                    "-fx-background-radius: 28;" +
                    "-fx-border-radius: 28;" +
                    "-fx-border-color: rgba(255,152,0,0.20);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 18, 0, 0, 5);"
    );

    Label heading = new Label("Current Order");
    heading.setStyle(
            "-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    VBox itemsBox = new VBox(12);
    itemsBox.setAlignment(Pos.TOP_CENTER);

    List<Cart.CartItem> cartItems = Cart.getInstance().getItems();

    if (cartItems.isEmpty()) {
      Label emptyTitle = new Label("No items yet");
      emptyTitle.setStyle(
              "-fx-font-size: 19px;" +
                      "-fx-font-weight: bold;" +
                      "-fx-text-fill: #333333;"
      );

      Label emptyText = new Label("Select a meal to start your order.");
      emptyText.setWrapText(true);
      emptyText.setAlignment(Pos.CENTER);
      emptyText.setStyle(
              "-fx-font-size: 14px;" +
                      "-fx-text-fill: #777777;"
      );

      VBox emptyBox = new VBox(8, emptyTitle, emptyText);
      emptyBox.setAlignment(Pos.CENTER);
      emptyBox.setPadding(new Insets(28, 20, 28, 20));
      emptyBox.setStyle(
              "-fx-background-color: #FFF8EC;" +
                      "-fx-background-radius: 20;"
      );

      itemsBox.getChildren().add(emptyBox);
    } else {
      for (Cart.CartItem cartItem : cartItems) {
        itemsBox.getChildren().add(createOrderRow(stage, cartItem));
      }
    }

    ScrollPane orderScroll = new ScrollPane(itemsBox);
    orderScroll.setFitToWidth(true);
    orderScroll.setPannable(true);
    orderScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    orderScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    orderScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
    VBox.setVgrow(orderScroll, Priority.ALWAYS);

    Label totalText = new Label("Total");
    totalText.setStyle(
            "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label totalAmount = new Label(String.format("%.2f kr", Cart.getInstance().getTotalPrice()));
    totalAmount.setStyle(
            "-fx-font-size: 22px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #FF9800;"
    );

    Region totalSpacer = new Region();
    HBox.setHgrow(totalSpacer, Priority.ALWAYS);

    HBox totalRow = new HBox(totalText, totalSpacer, totalAmount);
    totalRow.setAlignment(Pos.CENTER);

    Button paymentButton = new Button("Process Payment");
    paymentButton.setMaxWidth(Double.MAX_VALUE);
    paymentButton.setPrefHeight(54);
    paymentButton.setDisable(cartItems.isEmpty());
    paymentButton.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 16;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.25), 10, 0, 0, 3);"
    );
    paymentButton.setOnAction(e -> PaymentScreen.show(stage));

    Button clearButton = new Button("Clear Order");
    clearButton.setMaxWidth(Double.MAX_VALUE);
    clearButton.setPrefHeight(48);
    clearButton.setDisable(cartItems.isEmpty());
    clearButton.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: white;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-border-color: #E0E0E0;" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 14;" +
                    "-fx-background-radius: 14;" +
                    "-fx-cursor: hand;"
    );

    clearButton.setOnAction(e -> {
      Cart.getInstance().clear();
      App.selectedPaymentMethod = "";
      CategoryScreen.show(stage);
    });

    panel.getChildren().addAll(
            heading,
            orderScroll,
            totalRow,
            paymentButton,
            clearButton
    );

    return panel;
  }

  private static VBox createOrderRow(Stage stage, Cart.CartItem cartItem) {
    VBox row = new VBox(10);
    row.setMaxWidth(Double.MAX_VALUE);
    row.setPadding(new Insets(16));
    row.setStyle(
            "-fx-background-color: #FFFFFF;" +
                    "-fx-background-radius: 18;" +
                    "-fx-border-radius: 18;" +
                    "-fx-border-color: #EEEEEE;" +
                    "-fx-border-width: 1.2;"
    );

    Label itemName = new Label(cartItem.getMenuItem().getName());
    itemName.setWrapText(true);
    itemName.setMaxWidth(160);
    itemName.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label itemPrice = new Label(String.format("%.2f kr", cartItem.getSubtotal()));
    itemPrice.setMinWidth(80);
    itemPrice.setAlignment(Pos.CENTER_RIGHT);
    itemPrice.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #FF9800;"
    );

    Button removeButton = new Button("×");
    removeButton.setStyle(
            "-fx-background-color: rgba(255,80,80,0.12);" +
                    "-fx-text-fill: #E53935;" +
                    "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 50;" +
                    "-fx-min-width: 38;" +
                    "-fx-min-height: 38;" +
                    "-fx-cursor: hand;"
    );

    removeButton.setOnAction(e -> {
      Cart.getInstance().removeItem(cartItem);

      if (Cart.getInstance().getItems().isEmpty()) {
        App.selectedPaymentMethod = "";
      }

      CategoryScreen.show(stage);
    });

    Region topSpacer = new Region();
    HBox.setHgrow(topSpacer, Priority.ALWAYS);

    HBox topRow = new HBox(10, itemName, topSpacer, itemPrice, removeButton);
    topRow.setAlignment(Pos.CENTER_LEFT);

    VBox detailBox = new VBox(5);
    detailBox.setAlignment(Pos.CENTER_LEFT);

    if (!cartItem.getExtrasText().isBlank()) {
      detailBox.getChildren().add(createDetailLabel("+ " + cartItem.getExtrasText()));
    }

    if (!cartItem.getRemovedText().isBlank()) {
      detailBox.getChildren().add(createDetailLabel("- " + cartItem.getRemovedText()));
    }

    if (!cartItem.getComboChoicesText().isBlank()) {
      addComboChoiceLabels(detailBox, cartItem.getComboChoicesText());
    }

    Button minusButton = new Button("-");
    minusButton.setStyle(createSmallQuantityButtonStyle());

    Label quantity = new Label(String.valueOf(cartItem.getQuantity()));
    quantity.setMinWidth(26);
    quantity.setAlignment(Pos.CENTER);
    quantity.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #555555;" +
                    "-fx-background-color: #FFF3E0;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 4 9 4 9;"
    );

    Button plusButton = new Button("+");
    plusButton.setStyle(createSmallQuantityButtonStyle());

    minusButton.setOnAction(e -> {
      if (cartItem.getQuantity() > 1) {
        Cart.getInstance().decreaseQuantity(cartItem);
      } else {
        Cart.getInstance().removeItem(cartItem);
      }

      if (Cart.getInstance().getItems().isEmpty()) {
        App.selectedPaymentMethod = "";
      }

      CategoryScreen.show(stage);
    });

    plusButton.setOnAction(e -> {
      Cart.getInstance().increaseQuantity(cartItem);
      CategoryScreen.show(stage);
    });

    HBox quantityRow = new HBox(7, minusButton, quantity, plusButton);
    quantityRow.setAlignment(Pos.CENTER_LEFT);

    Button editButton = new Button("Edit");
    editButton.setStyle(
            "-fx-background-color: white;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-border-color: #E0E0E0;" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 12;" +
                    "-fx-background-radius: 12;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 7 16;" +
                    "-fx-cursor: hand;"
    );

    editButton.setOnAction(e -> {
      Cart.getInstance().removeItem(cartItem);

      if (Cart.getInstance().getItems().isEmpty()) {
        App.selectedPaymentMethod = "";
      }

      MealSelectionScreen.show(stage, cartItem.getMenuItem());
    });

    Region actionSpacer = new Region();
    HBox.setHgrow(actionSpacer, Priority.ALWAYS);

    HBox bottomRow = new HBox(10, quantityRow, actionSpacer, editButton);
    bottomRow.setAlignment(Pos.CENTER_LEFT);

    row.getChildren().add(topRow);

    if (!detailBox.getChildren().isEmpty()) {
      row.getChildren().add(detailBox);
    }

    row.getChildren().add(bottomRow);

    return row;
  }

  private static void addComboChoiceLabels(VBox detailBox, String comboText) {
    String[] lines = comboText.split("\\n");

    for (String line : lines) {
      String cleaned = cleanComboLine(line);

      if (!cleaned.isBlank()) {
        detailBox.getChildren().add(createDetailLabel(cleaned));
      }
    }
  }

  private static String cleanComboLine(String line) {
    String cleaned = line == null ? "" : line.trim();

    cleaned = cleaned.replace("Choose Kids Main:", "Main:");
    cleaned = cleaned.replace("Choose Kids Side:", "Side:");
    cleaned = cleaned.replace("Choose Family Main:", "Main:");
    cleaned = cleaned.replace("Choose Sharing Side:", "Side:");
    cleaned = cleaned.replace("Choose Burger:", "Burger:");
    cleaned = cleaned.replace("Choose Chicken Main:", "Main:");
    cleaned = cleaned.replace("Choose Snack Main:", "Snack:");
    cleaned = cleaned.replace("Choose Extra Snack:", "Extra snack:");
    cleaned = cleaned.replace("Choose Drink:", "Drink:");
    cleaned = cleaned.replace("Combo Size:", "Size:");

    if (cleaned.startsWith("Dice") && cleaned.contains("Gift:")) {
      int giftIndex = cleaned.indexOf("Gift:");
      cleaned = cleaned.substring(giftIndex).trim();
    }

    return cleaned;
  }

  private static Label createDetailLabel(String text) {
    Label label = new Label(text);
    label.setWrapText(true);
    label.setMaxWidth(255);
    label.setStyle(
            "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #666666;" +
                    "-fx-background-color: #FFF7EC;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 4 8 4 8;"
    );

    return label;
  }

  private static String createSmallQuantityButtonStyle() {
    return "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 50;" +
            "-fx-min-width: 32;" +
            "-fx-min-height: 32;" +
            "-fx-cursor: hand;";
  }

  private static void showItems(Stage stage, int categoryId) {
    itemsPane.getChildren().clear();

    List<MenuItem> items = DatabaseHelper.getItemsByCategory(categoryId);

    if (items.isEmpty()) {
      Label emptyLabel = new Label("No items available");
      emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #777777;");
      itemsPane.getChildren().add(emptyLabel);
      return;
    }

    for (MenuItem item : items) {
      VBox itemCard = createItemCard(stage, item);
      itemsPane.getChildren().add(itemCard);
    }

    FadeTransition fade = new FadeTransition(Duration.millis(250), itemsPane);
    fade.setFromValue(0);
    fade.setToValue(1);
    fade.play();
  }

  private static Button createCategoryButton(String name) {
    Button button = new Button(name);

    button.setStyle(createNormalCategoryButtonStyle());
    addHoverAnimation(button);

    return button;
  }

  private static void updateCategoryButtonStyles(int selectedCategoryId) {
    for (Button button : categoryButtons) {
      Object categoryId = button.getUserData();

      if (categoryId instanceof Integer && ((Integer) categoryId) == selectedCategoryId) {
        button.setStyle(createActiveCategoryButtonStyle());
      } else {
        button.setStyle(createNormalCategoryButtonStyle());
      }
    }
  }

  private static String createNormalCategoryButtonStyle() {
    return "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff8f00);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 14 24;" +
            "-fx-background-radius: 28;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 8, 0, 0, 2);";
  }

  private static String createActiveCategoryButtonStyle() {
    return "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 14 26;" +
            "-fx-background-radius: 28;" +
            "-fx-border-color: rgba(255,255,255,0.75);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 28;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.42), 14, 0, 0, 4);";
  }

  private static VBox createItemCard(Stage stage, MenuItem item) {
    ImageView itemImage = createSafeImageView(item.getImageFileName(), 170, 130);

    VBox imageBox = new VBox();
    imageBox.setStyle(
            "-fx-background-color: #f0f0f0;" +
                    "-fx-background-radius: 16;" +
                    "-fx-padding: 8;"
    );
    imageBox.setAlignment(Pos.CENTER);
    imageBox.setPrefHeight(140);
    imageBox.getChildren().add(itemImage);

    Label nameLabel = new Label(item.getName());
    nameLabel.setWrapText(true);
    nameLabel.setAlignment(Pos.CENTER);
    nameLabel.setStyle(
            "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label descriptionLabel = new Label(item.getDescription());
    descriptionLabel.setWrapText(true);
    descriptionLabel.setAlignment(Pos.CENTER);
    descriptionLabel.setMaxHeight(42);
    descriptionLabel.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-text-fill: #666666;"
    );

    Label priceLabel = new Label(String.format("%.2f kr", item.getPrice()));
    priceLabel.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #FF9800;"
    );

    Button detailsButton = new Button("Details");
    detailsButton.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #2196F3;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 8 14;" +
                    "-fx-background-radius: 12;" +
                    "-fx-cursor: hand;"
    );
    detailsButton.setOnAction(e -> ItemDetailsScreen.show(stage, item));

    Button selectButton = new Button("Select");
    selectButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 9 18;" +
                    "-fx-background-radius: 14;" +
                    "-fx-cursor: hand;"
    );
    selectButton.setOnAction(e -> MealSelectionScreen.show(stage, item));

    HBox buttonBox = new HBox(8, detailsButton, selectButton);
    buttonBox.setAlignment(Pos.CENTER);

    VBox card = new VBox(10, imageBox, nameLabel, descriptionLabel, priceLabel, buttonBox);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(18));
    card.setPrefWidth(230);
    card.setMinHeight(300);
    card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
                    "-fx-background-radius: 26;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 12, 0, 0, 4);"
    );

    addHoverAnimation(card);

    return card;
  }

  private static ImageView createSafeImageView(String imageFileName, double width, double height) {
    ImageView imageView = new ImageView();

    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);

    if (imageFileName == null || imageFileName.isBlank()) {
      System.out.println("No image filename found.");
      return imageView;
    }

    String cleanedFileName = imageFileName.trim();

    Image image = loadImage(cleanedFileName);

    if (image != null && !image.isError()) {
      imageView.setImage(image);
      System.out.println("Image loaded successfully: " + cleanedFileName);
    } else {
      System.out.println("Image could not be loaded: " + cleanedFileName);
    }

    return imageView;
  }

  private static Image loadImage(String imagePath) {
    try {
      if (imagePath == null || imagePath.isBlank()) {
        System.out.println("Image path is empty.");
        return null;
      }

      String cleanedPath = imagePath.trim();

      /*
       * Case 1:
       * Load from compiled resources.
       * This works for images that were already inside resources before running.
       */
      URL resourceUrl = CategoryScreen.class.getResource("/images/items/" + cleanedPath);

      if (resourceUrl != null) {
        System.out.println("Loaded image from compiled resources: " + resourceUrl);
        return new Image(resourceUrl.toExternalForm());
      }

      /*
       * Case 2:
       * If the image path already includes images/items/...
       */
      resourceUrl = CategoryScreen.class.getResource("/" + cleanedPath);

      if (resourceUrl != null) {
        System.out.println("Loaded image from given resource path: " + resourceUrl);
        return new Image(resourceUrl.toExternalForm());
      }

      /*
       * Case 3:
       * IMPORTANT:
       * Your admin screen may save into:
       * app/src/main/resources/images/items/
       */
      File appModuleFile = new File("app/src/main/resources/images/items/" + cleanedPath);

      if (appModuleFile.exists()) {
        System.out.println("Loaded image from app module folder: " + appModuleFile.getAbsolutePath());
        return new Image(appModuleFile.toURI().toString());
      }

      /*
       * Case 4:
       * If IntelliJ runs inside the app folder, this path will work.
       */
      File normalFile = new File("src/main/resources/images/items/" + cleanedPath);

      if (normalFile.exists()) {
        System.out.println("Loaded image from normal resources folder: " + normalFile.getAbsolutePath());
        return new Image(normalFile.toURI().toString());
      }

      /*
       * Case 5:
       * In some Gradle/Maven runs, copied resources may be inside build/resources/main.
       */
      File buildResourcesFile = new File("build/resources/main/images/items/" + cleanedPath);

      if (buildResourcesFile.exists()) {
        System.out.println("Loaded image from build resources folder: " + buildResourcesFile.getAbsolutePath());
        return new Image(buildResourcesFile.toURI().toString());
      }

      /*
       * Case 6:
       * If the admin somehow saved the full file path.
       */
      File fullPathFile = new File(cleanedPath);

      if (fullPathFile.exists()) {
        System.out.println("Loaded image from full file path: " + fullPathFile.getAbsolutePath());
        return new Image(fullPathFile.toURI().toString());
      }

      System.out.println("Image not found anywhere: " + cleanedPath);
      System.out.println("Checked:");
      System.out.println("- /images/items/" + cleanedPath);
      System.out.println("- app/src/main/resources/images/items/" + cleanedPath);
      System.out.println("- src/main/resources/images/items/" + cleanedPath);
      System.out.println("- build/resources/main/images/items/" + cleanedPath);

    } catch (Exception e) {
      System.out.println("Could not load image: " + imagePath);
      e.printStackTrace();
    }

    return null;
  }

  private static void addHoverAnimation(javafx.scene.Node node) {
    node.setOnMouseEntered(e -> {
      ScaleTransition scale = new ScaleTransition(Duration.millis(120), node);
      scale.setToX(1.04);
      scale.setToY(1.04);
      scale.play();
    });

    node.setOnMouseExited(e -> {
      ScaleTransition scale = new ScaleTransition(Duration.millis(120), node);
      scale.setToX(1.0);
      scale.setToY(1.0);
      scale.play();
    });
  }
}