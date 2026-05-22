package se.lnu;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

    for (Category category : categories) {
      Button categoryButton = createCategoryButton(category.getName());

      categoryButton.setOnAction(e -> {
        App.setCategory(category.getId(), category.getName());
        titleLabel.setText(category.getName());
        showItems(stage, category.getId());
      });

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
    panel.setPrefWidth(330);
    panel.setMaxWidth(330);
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

  private static HBox createOrderRow(Stage stage, Cart.CartItem cartItem) {
    Label itemName = new Label(cartItem.getMenuItem().getName());
    itemName.setWrapText(true);
    itemName.setMaxWidth(160);
    itemName.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Button minusButton = new Button("-");
    minusButton.setStyle(createSmallQuantityButtonStyle());

    Label quantity = new Label(String.valueOf(cartItem.getQuantity()));
    quantity.setMinWidth(28);
    quantity.setAlignment(Pos.CENTER);
    quantity.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #555555;" +
                    "-fx-background-color: #FFF3E0;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 4 10 4 10;"
    );

    Button plusButton = new Button("+");
    plusButton.setStyle(createSmallQuantityButtonStyle());

    minusButton.setOnAction(e -> {
      if (cartItem.getQuantity() > 1) {
        Cart.getInstance().decreaseQuantity(cartItem);
      } else {
        Cart.getInstance().removeItem(cartItem);
      }

      CategoryScreen.show(stage);
    });

    plusButton.setOnAction(e -> {
      Cart.getInstance().increaseQuantity(cartItem);
      CategoryScreen.show(stage);
    });

    HBox quantityBox = new HBox(8, minusButton, quantity, plusButton);
    quantityBox.setAlignment(Pos.CENTER_LEFT);

    VBox leftBox = new VBox(10, itemName, quantityBox);
    leftBox.setAlignment(Pos.CENTER_LEFT);

    Label itemPrice = new Label(String.format("%.2f kr", cartItem.getSubtotal()));
    itemPrice.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #FF9800;"
    );

    Button removeButton = new Button("×");
    removeButton.setStyle(
            "-fx-background-color: rgba(255,80,80,0.12);" +
                    "-fx-text-fill: #E53935;" +
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 50;" +
                    "-fx-min-width: 42;" +
                    "-fx-min-height: 42;" +
                    "-fx-cursor: hand;"
    );

    removeButton.setOnAction(e -> {
      Cart.getInstance().removeItem(cartItem);
      CategoryScreen.show(stage);
    });

    VBox rightBox = new VBox(12, itemPrice, removeButton);
    rightBox.setAlignment(Pos.CENTER_RIGHT);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox row = new HBox(12, leftBox, spacer, rightBox);
    row.setAlignment(Pos.CENTER_LEFT);
    row.setPadding(new Insets(16, 18, 16, 18));
    row.setStyle(
            "-fx-background-color: #FFFFFF;" +
                    "-fx-background-radius: 18;" +
                    "-fx-border-radius: 18;" +
                    "-fx-border-color: #EEEEEE;" +
                    "-fx-border-width: 1.2;"
    );

    return row;
  }

  private static String createSmallQuantityButtonStyle() {
    return "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 50;" +
            "-fx-min-width: 34;" +
            "-fx-min-height: 34;" +
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

    button.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 14 24;" +
                    "-fx-background-radius: 28;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 8, 0, 0, 2);"
    );

    addHoverAnimation(button);

    return button;
  }

  private static VBox createItemCard(Stage stage, MenuItem item) {
    ImageView itemImage = ImageLoader.createImageView(item.getImageFileName(), 170, 130);

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