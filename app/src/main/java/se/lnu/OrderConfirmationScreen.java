package se.lnu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderConfirmationScreen {

    public void start(Stage stage, Order order) {

        // Date & Time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String dateTime = now.format(formatter);

        // title + date
        Label title = new Label("Order Placed Successfully!");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label timeLabel = new Label("Date & Time: " + dateTime);
        timeLabel.setStyle("-fx-font-size: 18px;");

        VBox topCenter = new VBox(0, title, timeLabel);
        topCenter.setAlignment(Pos.CENTER);

        // items
        Label items = new Label("Items Ordered:\n" + order.getItemList());
        items.setStyle("-fx-font-size: 18px;");
        items.setAlignment(Pos.CENTER_LEFT);

        // total
        Label total = new Label("Total: $" + order.getTotalPrice());
        total.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        total.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(20, topCenter, items, total);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-padding: 40px;");

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}

