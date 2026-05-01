package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CartScreen {

    public static void show(Stage stage) {
        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #eeeeee;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-padding: 14 32;" +
                        "-fx-background-radius: 16;" +
                        "-fx-cursor: hand;"
        );
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        Label title = new Label("Your Cart");
        title.setStyle(
                "-fx-font-size: 46px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        VBox itemsBox = new VBox(20);
        itemsBox.setAlignment(Pos.CENTER);

        java.util.List<Cart.CartItem> items = Cart.getInstance().getItems();

        if (items.isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty.");
            emptyLabel.setStyle(
                    "-fx-font-size: 22px;" +
                            "-fx-text-fill: #777777;"
            );
            itemsBox.getChildren().add(emptyLabel);
        } else {
            for (Cart.CartItem cartItem : items) {
                Button minusButton = new Button("−");
                Button plusButton = new Button("+");

                String quantityButtonStyle =
                        "-fx-background-color: linear-gradient(to bottom, #ffae00, #ff8c00);" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 23px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-background-radius: 16;" +
                                "-fx-min-width: 54;" +
                                "-fx-min-height: 54;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.16), 7, 0, 0, 2);";

                minusButton.setStyle(quantityButtonStyle);
                plusButton.setStyle(quantityButtonStyle);

                minusButton.setOnAction(e -> {
                    Cart.getInstance().decreaseQuantity(cartItem);
                    CartScreen.show(stage);
                });

                plusButton.setOnAction(e -> {
                    Cart.getInstance().increaseQuantity(cartItem);
                    CartScreen.show(stage);
                });

                Label itemNameLabel = new Label(cartItem.getMenuItem().getName());
                itemNameLabel.setStyle(
                        "-fx-font-size: 24px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #1f1f1f;"
                );

                Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
                quantityLabel.setStyle(
                        "-fx-font-size: 28px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #1f1f1f;" +
                                "-fx-min-width: 42;" +
                                "-fx-alignment: center;"
                );
                quantityLabel.setAlignment(Pos.CENTER);

                Label priceLabel = new Label(String.format("%.2f kr", cartItem.getSubtotal()));
                priceLabel.setStyle(
                        "-fx-font-size: 24px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: #1f1f1f;"
                );

                HBox quantityControls = new HBox(16, minusButton, quantityLabel, plusButton);
                quantityControls.setAlignment(Pos.CENTER);

                HBox row = new HBox(45, itemNameLabel, quantityControls, priceLabel);
                row.setAlignment(Pos.CENTER);
                row.setPadding(new Insets(18, 30, 18, 30));
                row.setMinWidth(760);
                row.setMaxWidth(820);
                row.setStyle(
                        "-fx-background-color: rgba(255,255,255,0.94);" +
                                "-fx-background-radius: 28;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0, 0, 3);"
                );

                itemsBox.getChildren().add(row);
            }

            Label totalLabel = new Label(
                    "Total: " + String.format("%.2f", Cart.getInstance().getTotalPrice()) + " kr"
            );
            totalLabel.setStyle(
                    "-fx-font-size: 30px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #1f1f1f;" +
                            "-fx-background-color: rgba(255,149,0,0.18);" +
                            "-fx-background-radius: 28;" +
                            "-fx-padding: 18 65 18 65;"
            );

            itemsBox.getChildren().add(totalLabel);
        }

        VBox centerContent = new VBox(32, title, itemsBox);
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(26));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(backButton);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Cart");
        stage.show();
    }
}