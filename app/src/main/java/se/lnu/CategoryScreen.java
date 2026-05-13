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
    Button cartButton = createCartButton(stage);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backBtn, spacer, homeButton, cartButton);
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
      Category firstCategory = categories.get(0);
      App.setCategory(firstCategory.getId(), firstCategory.getName());
      titleLabel.setText(firstCategory.getName());
      showItems(stage, firstCategory.getId());
    }

    VBox centerContent = new VBox(20, titleLabel, categoryBar, itemsPane);
    centerContent.setAlignment(Pos.TOP_CENTER);
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

    Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
    stage.setScene(scene);
    stage.setTitle("Select Category");
    WindowManager.enforceStandardSize(stage);
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
    Button button = new Button(getCategoryIcon(name) + " " + name);

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
    // Item image (small box above name)
    ImageView itemImage = ImageLoader.createImageView(item.getImageFileName(), 180, 140);
    VBox imageBox = new VBox();
    imageBox.setStyle(
            "-fx-background-color: #f0f0f0;" +
                    "-fx-background-radius: 16;" +
                    "-fx-padding: 8;"
    );
    imageBox.setAlignment(Pos.CENTER);
    imageBox.setPrefHeight(150);
    imageBox.getChildren().add(itemImage);

    Label nameLabel = new Label(item.getName());
    nameLabel.setStyle(
            "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label descriptionLabel = new Label(item.getDescription());
    descriptionLabel.setWrapText(true);
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

    // View Details button
    Button detailsButton = new Button("View Details");
    detailsButton.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-background-color: #2196F3;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 8 16;" +
                    "-fx-background-radius: 12;" +
                    "-fx-cursor: hand;"
    );
    detailsButton.setOnAction(e -> ItemDetailsScreen.show(stage, item));

    // Select button
    Button selectButton = new Button("Select");
    selectButton.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 24;" +
                    "-fx-background-radius: 16;" +
                    "-fx-cursor: hand;"
    );
    selectButton.setOnAction(e -> MealSelectionScreen.show(stage, item));

    // Button container
    HBox buttonBox = new HBox(8);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(detailsButton, selectButton);

    VBox card = new VBox(12, imageBox, nameLabel, descriptionLabel, priceLabel, buttonBox);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(20));
    card.setPrefWidth(250);
    card.setMinHeight(210);
    card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.95);" +
                    "-fx-background-radius: 28;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 12, 0, 0, 4);"
    );

    addHoverAnimation(card);

    return card;
  }

  private static String getCategoryIcon(String name) {
    return "";
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

  private static Button createCartButton(Stage stage) {
    Button cartButton = new Button("Cart (" + Cart.getInstance().getItemCount() + ")");
    cartButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );
    cartButton.setOnAction(e -> CartScreen.show(stage));
    return cartButton;
  }
}