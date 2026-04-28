package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**Just a demo/prototype class I(Battur)'ve made to see whether the Cart class worked or not. Feel free to delete */
public class CartScreen {

    public static void show(Stage stage) {
        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-background-color: #eeeeee;" +
                "-fx-text-fill: #333333;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 10;"
        );
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        Label title = new Label("Your Cart");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        VBox itemsBox = new VBox(10);
        itemsBox.setAlignment(Pos.CENTER);

        java.util.List<Cart.CartItem> items = Cart.getInstance().getItems();

        if (items.isEmpty()) {
            itemsBox.getChildren().add(new Label("Your cart is empty."));
        } else {
            double total = 0;
            for (Cart.CartItem cartItem : items) {
                double lineTotal = cartItem.menuItem.getPrice() * cartItem.quantity;
                total += lineTotal;
                Label row = new Label(
                        cartItem.quantity + " x " + cartItem.menuItem.getName() +
                        "  —  " + String.format("%.2f", lineTotal) + " kr"
                );
                row.setStyle("-fx-font-size: 16px;");
                itemsBox.getChildren().add(row);
            }
            Label totalLabel = new Label("Total: " + String.format("%.2f", total) + " kr");
            totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15 0 0 0;");
            itemsBox.getChildren().add(totalLabel);
        }

        VBox centerContent = new VBox(20, title, itemsBox);
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(backButton);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Cart");
        stage.show();
    }
}
