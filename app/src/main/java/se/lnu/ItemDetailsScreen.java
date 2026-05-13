package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ItemDetailsScreen displays detailed information about a menu item
 * including image, name, description, and price.
 * Users can view the full details before deciding to customize and add to cart.
 */
public class ItemDetailsScreen {

    public static void show(Stage stage, MenuItem item) {
        // Top navigation bar
        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        Button homeButton = ScreenStyle.createHomeButton(stage);
        Button cartButton = createCartButton(stage);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, spacer, homeButton, cartButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16));

        // Item image
        ImageView itemImage = createItemImage(item);

        // Item details section
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        Label priceLabel = new Label(String.format("%.2f kr", item.getPrice()));
        priceLabel.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #FF9800;" +
                        "-fx-padding: 12 0;"
        );

        Label descriptionLabel = new Label(item.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(600);
        descriptionLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #555555;" +
                        "-fx-line-spacing: 1.4;"
        );

        // Add to cart / Customize button
        Button addButton = new Button("Customize & Add to Cart");
        addButton.setPrefWidth(300);
        addButton.setPrefHeight(56);
        addButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 28;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.25), 7, 0, 0, 2);"
        );
        addButton.setOnAction(e -> MealSelectionScreen.show(stage, item));

        // Add to cart without customization (optional)
        Button quickAddButton = new Button("Quick Add to Cart");
        quickAddButton.setPrefWidth(300);
        quickAddButton.setPrefHeight(48);
        quickAddButton.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: #E8E8E8;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-background-radius: 24;" +
                        "-fx-cursor: hand;"
        );
        quickAddButton.setOnAction(e -> {
            Cart.getInstance().addItem(item, 1);
            // Show confirmation message by returning to category
            CategoryScreen.show(stage);
        });

        // Content area with image and details side by side
        HBox contentBox = new HBox(40);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(30));

        // Left side - Image
        VBox imageBox = new VBox();
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setPrefWidth(350);
        imageBox.getChildren().add(itemImage);

        // Right side - Details
        VBox detailsBox = new VBox(20);
        detailsBox.setPrefWidth(550);
        detailsBox.getChildren().addAll(
                nameLabel,
                priceLabel,
                descriptionLabel
        );

        contentBox.getChildren().addAll(imageBox, detailsBox);

        // Bottom action buttons
        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 30, 40, 30));
        buttonBox.getChildren().addAll(addButton, quickAddButton);

        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(topBar);

        // Scrollable content area
        ScrollPane scrollPane = new ScrollPane();
        VBox scrollContent = new VBox();
        scrollContent.getChildren().addAll(contentBox, buttonBox);
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollPane.setContent(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setCenter(scrollPane);

        // Create and show scene
        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Item Details");
        WindowManager.enforceStandardSize(stage);
    }

    /**
     * Creates an ImageView for the item with a placeholder if image not found
     */
    private static ImageView createItemImage(MenuItem item) {
        return ImageLoader.createImageView(item.getImageFileName(), 320, 320);
    }

    /**
     * Creates the cart button with current item count
     */
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