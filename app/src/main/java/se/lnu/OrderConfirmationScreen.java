package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderConfirmationScreen {

    public void start(Stage stage, Order order) {

        // Date & Time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        // Order number (timestamp)
        String orderNumber = "ORD-" + (System.currentTimeMillis() % 100000);

        // Title
        Label title = new Label("Order Placed Successfully!");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #222;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // Date , Order Number , Time are in same row
        Label dateLabel = new Label("Date : " + date);
        Label orderNumLabel = new Label("Order Number : " + orderNumber);
        Label timeLabel = new Label("Time : " + time);

        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #222;");
        orderNumLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #222;");
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #222;");

        HBox infoRow = new HBox(40, dateLabel, orderNumLabel, timeLabel);
        infoRow.setAlignment(Pos.CENTER);

        // Items Ordered
        Label itemsLabel = new Label("Items Ordered:");
        itemsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");


        VBox itemsBox = new VBox(10);
        itemsBox.setAlignment(Pos.CENTER_LEFT);

        for (Cart.CartItem item : order.getItems()) {

            // name, price
            HBox itemRow = new HBox();
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label name = new Label(item.menuItem.getName());
            name.setStyle("-fx-font-size: 16px; -fx-text-fill: #222;");

            Label price = new Label(String.format("%.2f kr", item.menuItem.getPrice()));
            price.setStyle("-fx-font-size: 16px; -fx-text-fill: #222;");

            HBox.setMargin(price, new Insets(0, 0, 0, 250)); //  price

            itemRow.getChildren().addAll(name, price);

            // Qty
            Label qtyLabel = new Label("Qty: " + item.quantity);
            qtyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #444;");

            itemsBox.getChildren().addAll(itemRow, qtyLabel);
        }

        // Total
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);

        Label totalText = new Label("Order Total:");
        totalText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label totalAmount = new Label(String.format("%.2f kr", order.getTotalPrice()));
        totalAmount.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #222;");

        HBox.setMargin(totalAmount, new Insets(0, 0, 0, 300));

        totalRow.getChildren().addAll(totalText, totalAmount);


        VBox centerContent = new VBox(20, titleBox, infoRow, itemsLabel, itemsBox, totalRow);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 600, 450);
        stage.setScene(scene);
        stage.setTitle("Order Confirmation");
        stage.show();
    }
}
