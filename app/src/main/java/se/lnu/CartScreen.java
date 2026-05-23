package se.lnu;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartScreen {

    public static void show(Stage stage) {
        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        Button homeButton = ScreenStyle.createHomeButton(stage);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, spacer, homeButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Current Order");
        title.setStyle(
                "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        VBox itemsBox = new VBox(18);
        itemsBox.setAlignment(Pos.TOP_CENTER);
        itemsBox.setFillWidth(true);

        List<Cart.CartItem> items = Cart.getInstance().getItems();

        if (items.isEmpty()) {
            itemsBox.getChildren().add(createEmptyCart(stage));
        } else {
            for (Cart.CartItem cartItem : items) {
                itemsBox.getChildren().add(createCartItemCard(stage, cartItem));
            }

            itemsBox.getChildren().add(createTotalSection(stage));
        }

        VBox centerContent = new VBox(26, title, itemsBox);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setFillWidth(true);
        centerContent.setPadding(new Insets(10, 0, 30, 0));

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;"
        );

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(26));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(topBar);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Cart");
        WindowManager.enforceStandardSize(stage);
    }

    private static VBox createCartItemCard(Stage stage, Cart.CartItem cartItem) {
        Label nameLabel = new Label(cartItem.getMenuItem().getName());
        nameLabel.setWrapText(true);
        nameLabel.setMinHeight(Control.USE_PREF_SIZE);
        nameLabel.setStyle(
                "-fx-font-size: 23px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        Label priceLabel = new Label(String.format("%.2f kr", cartItem.getSubtotal()));
        priceLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ff9800;"
        );

        Button removeButton = new Button("×");
        removeButton.setStyle(
                "-fx-background-color: rgba(255, 80, 80, 0.15);" +
                        "-fx-text-fill: #d32f2f;" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 50;" +
                        "-fx-min-width: 52;" +
                        "-fx-min-height: 52;" +
                        "-fx-cursor: hand;"
        );

        removeButton.setOnAction(e -> {
            Cart.getInstance().removeItem(cartItem);
            CartScreen.show(stage);
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(14, nameLabel, headerSpacer, priceLabel, removeButton);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        VBox customizationList = new VBox(7);
        customizationList.setAlignment(Pos.CENTER_LEFT);
        customizationList.setFillWidth(true);

        addComboChoices(customizationList, cartItem.getComboChoices());
        addExtras(customizationList, cartItem.getExtras());
        addRemoved(customizationList, cartItem.getRemoved());

        if (customizationList.getChildren().isEmpty()) {
            customizationList.getChildren().add(createBulletLine("• Original item"));
        }

        Label unitPriceLabel = new Label(
                "Unit price: " + String.format("%.2f kr", cartItem.getUnitPriceWithExtras())
        );
        unitPriceLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #777777;"
        );

        Button minusButton = createQuantityButton("-");
        Button plusButton = createQuantityButton("+");

        minusButton.setDisable(cartItem.getQuantity() <= 1);

        minusButton.setOnAction(e -> {
            if (cartItem.getQuantity() > 1) {
                Cart.getInstance().decreaseQuantity(cartItem);
                CartScreen.show(stage);
            }
        });

        plusButton.setOnAction(e -> {
            Cart.getInstance().increaseQuantity(cartItem);
            CartScreen.show(stage);
        });

        Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
        quantityLabel.setAlignment(Pos.CENTER);
        quantityLabel.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;" +
                        "-fx-background-color: rgba(255, 248, 225, 0.95);" +
                        "-fx-background-radius: 16;" +
                        "-fx-min-width: 54;" +
                        "-fx-min-height: 50;" +
                        "-fx-alignment: center;"
        );

        Button editButton = createEditButton();
        editButton.setOnAction(e -> {
            Cart.getInstance().removeItem(cartItem);
            MealSelectionScreen.show(stage, cartItem.getMenuItem());
        });

        HBox quantityRow = new HBox(12, minusButton, quantityLabel, plusButton, editButton);
        quantityRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(13, headerRow, customizationList, unitPriceLabel, quantityRow);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setFillWidth(true);
        card.setPadding(new Insets(24, 28, 24, 28));
        card.setPrefWidth(820);
        card.setMaxWidth(820);
        card.setMinHeight(Control.USE_PREF_SIZE);
        card.setStyle(
                "-fx-background-color: rgba(255,255,255,0.96);" +
                        "-fx-background-radius: 28;" +
                        "-fx-border-color: rgba(0,0,0,0.06);" +
                        "-fx-border-radius: 28;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 0, 4);"
        );

        return card;
    }

    private static void addExtras(VBox list, List<String> extras) {
        for (String extra : extras) {
            if (extra == null || extra.isBlank()) {
                continue;
            }

            list.getChildren().add(createBulletLine("• " + extra.trim()));
        }
    }

    private static void addRemoved(VBox list, List<String> removed) {
        for (String removedItem : removed) {
            if (removedItem == null || removedItem.isBlank()) {
                continue;
            }

            list.getChildren().add(createBulletLine("• No " + removedItem.trim()));
        }
    }

    private static void addComboChoices(VBox list, List<String> comboChoices) {
        for (String choice : comboChoices) {
            if (choice == null || choice.isBlank()) {
                continue;
            }

            list.getChildren().add(createBulletLine("• " + choice.trim()));
        }
    }

    private static Label createBulletLine(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(760);
        label.setMinHeight(Control.USE_PREF_SIZE);
        label.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #444444;" +
                        "-fx-line-spacing: 3px;"
        );

        return label;
    }

    private static Button createQuantityButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 16;" +
                        "-fx-min-width: 54;" +
                        "-fx-min-height: 50;" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private static Button createEditButton() {
        Button button = new Button("Edit");
        button.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-text-fill: #ff8c00;" +
                        "-fx-border-color: rgba(255,140,0,0.55);" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 14;" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 10 22;" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private static VBox createTotalSection(Stage stage) {
        Label totalText = new Label("Total amount");
        totalText.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        Label totalAmount = new Label(
                String.format("%.2f kr", Cart.getInstance().getTotalPrice())
        );
        totalAmount.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #ff9800;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox totalRow = new HBox(20, totalText, spacer, totalAmount);
        totalRow.setAlignment(Pos.CENTER);
        totalRow.setMaxWidth(820);
        totalRow.setPadding(new Insets(12, 4, 12, 4));

        Button confirmButton = new Button("Confirm Order");
        confirmButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 14 38;" +
                        "-fx-background-radius: 16;" +
                        "-fx-cursor: hand;"
        );

        confirmButton.setOnAction(e -> PaymentScreen.show(stage));

        VBox totalSection = new VBox(18, totalRow, confirmButton);
        totalSection.setAlignment(Pos.CENTER);
        totalSection.setMaxWidth(820);
        totalSection.setPadding(new Insets(10, 0, 30, 0));

        return totalSection;
    }

    private static VBox createEmptyCart(Stage stage) {
        Label cartIcon = new Label("Cart");
        cartIcon.setStyle("-fx-font-size: 48px;");

        Label emptyHeading = new Label("Your cart is empty");
        emptyHeading.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2d2d2d;"
        );

        Label emptySubtext = new Label("Browse the menu and add something delicious!");
        emptySubtext.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #888888;"
        );

        Button browseButton = new Button("Browse Menu");
        browseButton.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 11 32;" +
                        "-fx-background-radius: 16;" +
                        "-fx-cursor: hand;"
        );

        browseButton.setOnAction(e -> CategoryScreen.show(stage));

        VBox emptyCard = new VBox(16, cartIcon, emptyHeading, emptySubtext, browseButton);
        emptyCard.setAlignment(Pos.CENTER);
        emptyCard.setPadding(new Insets(30, 52, 30, 52));
        emptyCard.setMaxWidth(520);
        emptyCard.setStyle(
                "-fx-background-color: rgba(255,255,255,0.92);" +
                        "-fx-background-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 16, 0, 0, 3);"
        );

        return emptyCard;
    }
}