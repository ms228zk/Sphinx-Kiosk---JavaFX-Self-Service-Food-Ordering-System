package se.lnu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrderConfirmationScreen {

    public void start(Stage stage, Order order) {

        // Date & Time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        // Title
        Label title = new Label("Order Placed Successfully!");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #222;");
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // Date ,Time are in same row
        Label dateLabel = new Label("Date : " + date);
        Label timeLabel = new Label("Time : " + time);

        dateLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");
        timeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");

        HBox infoRow = new HBox(40, dateLabel, timeLabel);
        infoRow.setAlignment(Pos.CENTER);

        // Items Ordered
        Label itemsLabel = new Label("Items Ordered:");
        itemsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");


        VBox itemsBox = new VBox(10);
        itemsBox.setAlignment(Pos.CENTER_LEFT);

        for (Cart.CartItem item : order.getItems()) {

            // name, price
            HBox itemRow = new HBox();
            itemRow.setAlignment(Pos.CENTER_LEFT);
            itemRow.setSpacing(10);

            Label name = new Label(item.getMenuItem().getName());
            name.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");

            Label price = new Label(String.format("%.2f kr", item.getSubtotal()));
            price.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");


            HBox.setHgrow(name, javafx.scene.layout.Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);

            itemRow.getChildren().addAll(name, price);

            // Qty
            Label qtyLabel = new Label("Qty: " + item.getQuantity());
            qtyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #444;");

            itemsBox.getChildren().addAll(itemRow, qtyLabel);
        }

        // Total
        HBox totalRow = new HBox();
        totalRow.setAlignment(Pos.CENTER_LEFT);
        totalRow.setSpacing(10);

        Label totalText = new Label("Order Total:");
        totalText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label totalAmount = new Label(String.format("%.2f kr", order.getTotalPrice()));
        totalAmount.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label paymentLabel = new Label();

        if (order.getPaymentMethod().equalsIgnoreCase("Cash")) {
            paymentLabel.setText("Please pay at the counter.");
        } else {
            paymentLabel.setText("Please follow the payment terminal instructions.");
        }

        paymentLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        HBox.setHgrow(totalText, javafx.scene.layout.Priority.ALWAYS);
        totalText.setMaxWidth(Double.MAX_VALUE);

        totalRow.getChildren().addAll(totalText, totalAmount);

        VBox centerContent = new VBox(20, titleBox, infoRow, itemsLabel, itemsBox, paymentLabel, totalRow);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(scrollPane);

        Button finishBtn = new Button("Finish Order");
        finishBtn.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 40px; " +
                        "-fx-background-radius: 8px;"
        );

        finishBtn.setOnAction(e -> {
            OrderNumberScreen ons = new OrderNumberScreen();
            ons.start(stage, order.getOrderNumber());
        });

        Button homeButton = ScreenStyle.createHomeButton(stage);

        HBox bottomBox = new HBox(12, homeButton, finishBtn);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10, 20, 20, 20));

        root.setBottom(bottomBox);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Order Confirmation");
        WindowManager.enforceStandardSize(stage);


    }
}