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
        Label title = new Label("THANK YOU FOR YOUR ORDER");
        title.setStyle(
                "-fx-font-size: 55px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 10, 0.3, 2, 2);"
        );

        // BOX
        VBox ticket = new VBox();
        ticket.setAlignment(Pos.CENTER);
        ticket.setSpacing(0);
        ticket.setStyle(
                "-fx-background-color: #FFEB3B;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 3px;"
        );
        ticket.setMaxWidth(600);

        // Top bar
        Label topBar = new Label("****** ORDER NUMBER ******");
        topBar.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: black;");
        topBar.setPadding(new Insets(20, 0, 20, 0));
        topBar.setMaxWidth(Double.MAX_VALUE);
        topBar.setAlignment(Pos.CENTER);

        // Separator line
        Rectangle line1 = new Rectangle(600, 3, Color.BLACK);

        // centered number
        Label numberLabel = new Label(orderNumber);
        numberLabel.setStyle("-fx-font-size: 100px; -fx-font-weight: bold; -fx-text-fill: black;");
        numberLabel.setPadding(new Insets(40, 0, 40, 0));
        numberLabel.setMaxWidth(Double.MAX_VALUE);
        numberLabel.setAlignment(Pos.CENTER);

        // Separator line
        Rectangle line2 = new Rectangle(600, 3, Color.BLACK);

        // Bottom message
        Label ticketMsg = new Label("Your order is being prepared ");
        ticketMsg.setStyle("-fx-font-size: 30px; -fx-text-fill: black;");
        ticketMsg.setPadding(new Insets(20, 0, 20, 0));
        ticketMsg.setMaxWidth(Double.MAX_VALUE);
        ticketMsg.setAlignment(Pos.CENTER);

        ticket.getChildren().addAll(topBar, line1, numberLabel, line2, ticketMsg);

        VBox centerContent = new VBox(30, title, ticket);
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
