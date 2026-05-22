package se.lnu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

public class OrderConfirmationScreen {

    public void start(Stage stage, Order order) {

        // Date & Time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        // Title
        Label title = new Label("Review Your Order");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label subtitle = new Label("Please check your order before placing it.");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #555;");

        VBox titleBox = new VBox(8, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        // Date ,Time are in same row
        Label dateLabel = new Label("Date : " + date);
        Label timeLabel = new Label("Time : " + time);
        Label paymentMethodLabel = new Label("Payment : " + order.getPaymentMethod());

        dateLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");
        timeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");
        paymentMethodLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #222;");

        HBox infoRow = new HBox(40, dateLabel, timeLabel, paymentMethodLabel);
        infoRow.setAlignment(Pos.CENTER);

        // Message before final confirmation
        Label reviewMessage = new Label(
                "Your order has not been placed yet. Please review everything and press Place Order when you are sure."
        );
        reviewMessage.setWrapText(true);
        reviewMessage.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;" +
                        "-fx-background-color: rgba(76,175,80,0.12);" +
                        "-fx-background-radius: 14;" +
                        "-fx-padding: 14px 20px;"
        );

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

            HBox.setHgrow(name, Priority.ALWAYS);
            name.setMaxWidth(Double.MAX_VALUE);

            itemRow.getChildren().addAll(name, price);

            // Qty
            Label qtyLabel = new Label("Qty: " + item.getQuantity());
            qtyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #444;");

            itemsBox.getChildren().addAll(itemRow, qtyLabel);

            // Extras, removed ingredients, and combo choices if available
            if (!item.getExtrasText().isBlank()) {
                Label extrasLabel = new Label("Extras: " + item.getExtrasText());
                extrasLabel.setWrapText(true);
                extrasLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
                itemsBox.getChildren().add(extrasLabel);
            }

            if (!item.getRemovedText().isBlank()) {
                Label removedLabel = new Label("Removed: " + item.getRemovedText());
                removedLabel.setWrapText(true);
                removedLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
                itemsBox.getChildren().add(removedLabel);
            }

            if (!item.getComboChoicesText().isBlank()) {
                Label comboLabel = new Label(item.getComboChoicesText());
                comboLabel.setWrapText(true);
                comboLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
                itemsBox.getChildren().add(comboLabel);
            }
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
            paymentLabel.setText("Payment note: Please pay at the counter after placing your order.");
        } else {
            paymentLabel.setText("Payment note: Please follow the card terminal instructions after placing your order.");
        }

        paymentLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;"
        );

        HBox.setHgrow(totalText, Priority.ALWAYS);
        totalText.setMaxWidth(Double.MAX_VALUE);

        totalRow.getChildren().addAll(totalText, totalAmount);

        VBox centerContent = new VBox(
                20,
                titleBox,
                infoRow,
                reviewMessage,
                itemsLabel,
                itemsBox,
                paymentLabel,
                totalRow
        );

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

        Button editOrderBtn = new Button("Edit Order");
        editOrderBtn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #222; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 40px; " +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        editOrderBtn.setOnAction(e -> CategoryScreen.show(stage));

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px 40px; " +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"
        );

        placeOrderBtn.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Place Order");
            alert.setHeaderText("Are you sure you want to place this order?");
            alert.setContentText("After confirming, your order number will be generated.");

            ButtonType cancelButton = new ButtonType("Cancel");
            ButtonType confirmButton = new ButtonType("Yes, Place Order");

            alert.getButtonTypes().setAll(cancelButton, confirmButton);

            alert.showAndWait().ifPresent(result -> {
                if (result == confirmButton) {

                    // Generate next order number from database
                    int orderNumber = DatabaseHelper.getNextOrderNumber();
                    String today = java.time.LocalDate.now().toString();

                    // Save each item in the order to the database
                    for (Cart.CartItem item : order.getItems()) {
                        DatabaseHelper.saveOrder(
                                orderNumber,
                                item.getMenuItem().getName(),
                                item.getQuantity(),
                                today
                        );
                    }

                    Cart.getInstance().clear();

                    // Show the order number screen
                    OrderNumberScreen ons = new OrderNumberScreen();
                    ons.start(stage, String.format("%04d", orderNumber));
                }
            });
        });

        Button homeButton = ScreenStyle.createHomeButton(stage);

        HBox bottomBox = new HBox(12, homeButton, editOrderBtn, placeOrderBtn);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10, 20, 20, 20));

        root.setBottom(bottomBox);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Review Order");
        WindowManager.enforceStandardSize(stage);
    }
}