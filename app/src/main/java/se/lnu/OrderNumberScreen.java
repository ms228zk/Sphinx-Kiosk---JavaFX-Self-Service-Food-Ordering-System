package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class OrderNumberScreen {

    public void start(Stage stage, String orderNumber) {

        // Title
        Label title = new Label("Your Order Number");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: #222;");

        // box
        Rectangle box = new Rectangle(600, 320);
        box.setArcWidth(30);
        box.setArcHeight(30);
        box.setFill(Color.web("#FFEB3B"));
        box.setStroke(null);

        // Big centered number
        Label numberLabel = new Label(orderNumber);
        numberLabel.setStyle("-fx-font-size: 100px; -fx-font-weight: bold; -fx-text-fill: black;");

        StackPane numberBox = new StackPane(box, numberLabel);
        numberBox.setPadding(new Insets(20));

        // Bottom message
        Label waitMsg = new Label("Please wait while we prepare your order.");
        waitMsg.setStyle("-fx-font-size: 30px;  -fx-text-fill: #222;");

        VBox centerContent = new VBox(30, title, numberBox, waitMsg);
        centerContent.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 600, 450);
        stage.setScene(scene);
        stage.setTitle("Order Number");
        stage.show();
    }
}
