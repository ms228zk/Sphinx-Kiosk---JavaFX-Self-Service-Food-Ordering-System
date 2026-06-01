package se.lnu;

import java.util.List;

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, spacer, homeButton);
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

        // Ingredients section
        VBox ingredientsSection = createIngredientsSection(item);

        // Select button
        Button selectButton = new Button("Select");
        selectButton.setPrefWidth(140);
        selectButton.setPrefHeight(56);
        selectButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 28;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,140,0,0.25), 7, 0, 0, 2);"
        );
        selectButton.setOnAction(e -> MealSelectionScreen.show(stage, item));

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
                descriptionLabel,
                ingredientsSection
        );

        contentBox.getChildren().addAll(imageBox, detailsBox);

        // Bottom action button
        HBox buttonBox = new HBox(16);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 30, 40, 30));
        buttonBox.getChildren().add(selectButton);

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
     * Creates a styled section displaying the item's ingredients as chips
     */
    private static VBox createIngredientsSection(MenuItem item) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(16, 0, 0, 0));

        // Divider line
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setStyle("-fx-background-color: #E0E0E0;");

        // Section header
        Label headerLabel = new Label("Ingredients");
        headerLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        List<RemovableIngredient> ingredients = item.getRemovableIngredients();

        if (ingredients == null || ingredients.isEmpty()) {
            // Fetch from database if not already loaded
            ingredients = se.lnu.database.DatabaseHelper.getRemovableIngredientsByItem(item.getId());
        }

        section.getChildren().addAll(divider, headerLabel);

        if (ingredients.isEmpty()) {
            Label noIngredients = new Label("No ingredients listed for this item.");
            noIngredients.setStyle(
                    "-fx-font-size: 14px;" +
                            "-fx-text-fill: #999999;" +
                            "-fx-font-style: italic;"
            );
            section.getChildren().add(noIngredients);
        } else {
            FlowPane chipsPane = new FlowPane();
            chipsPane.setHgap(10);
            chipsPane.setVgap(10);
            chipsPane.setPadding(new Insets(4, 0, 0, 0));

            for (RemovableIngredient ingredient : ingredients) {
                Label chip = new Label(ingredient.name());
                chip.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #E65100;" +
                                "-fx-background-color: #FFF3E0;" +
                                "-fx-background-radius: 16;" +
                                "-fx-padding: 8 16;" +
                                "-fx-border-color: #FFCC80;" +
                                "-fx-border-radius: 16;" +
                                "-fx-border-width: 1.2;"
                );
                chipsPane.getChildren().add(chip);
            }

            section.getChildren().add(chipsPane);
        }

        return section;
    }

    /**
     * Creates an ImageView for the item with a placeholder if image not found
     */
    private static ImageView createItemImage(MenuItem item) {
        return ImageLoader.createImageView(item.getImageFileName(), 320, 320);
    }
}