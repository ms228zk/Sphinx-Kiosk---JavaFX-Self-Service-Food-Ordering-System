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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

public class OrderConfirmationScreen {

    private static final String ORANGE = "#FF9800";
    private static final String GREEN = "#4CAF50";
    private static final String TEXT_DARK = "#1f1f1f";
    private static final String TEXT_MUTED = "#666666";
    private static final String DIVIDER = "rgba(0,0,0,0.08)";

    public void start(Stage stage, Order order) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        Label title = new Label("Review Your Order");
        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Label subtitle = new Label("Check your items before placing the order.");
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        VBox titleBox = new VBox(6, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        Label infoLine = new Label(
                "Date: " + date +
                        "   •   Time: " + time +
                        "   •   Payment: " + order.getPaymentMethod()
        );
        infoLine.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Label reviewMessage = new Label("Review carefully before placing your order.");
        reviewMessage.setWrapText(true);
        reviewMessage.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #5f4b00;" +
                        "-fx-background-color: rgba(255,193,7,0.20);" +
                        "-fx-background-radius: 16;" +
                        "-fx-padding: 12 18;"
        );

        Label summaryTitle = new Label("Order Summary");
        summaryTitle.setStyle(
                "-fx-font-size: 23px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        VBox orderSummaryCard = new VBox(0);
        orderSummaryCard.setMaxWidth(900);
        orderSummaryCard.setPadding(new Insets(10, 26, 10, 26));
        orderSummaryCard.setStyle(
                "-fx-background-color: rgba(255,255,255,0.90);" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: rgba(255,152,0,0.18);" +
                        "-fx-border-width: 1.2;" +
                        "-fx-border-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 15, 0, 0, 4);"
        );

        for (int i = 0; i < order.getItems().size(); i++) {
            Cart.CartItem item = order.getItems().get(i);
            orderSummaryCard.getChildren().add(createOrderItemRow(item));

            if (i < order.getItems().size() - 1) {
                orderSummaryCard.getChildren().add(createDivider());
            }
        }

        VBox totalSection = createTotalSection(order);

        VBox centerContent = new VBox(
                16,
                titleBox,
                infoLine,
                reviewMessage,
                summaryTitle,
                orderSummaryCard,
                totalSection
        );

        centerContent.setPadding(new Insets(18, 24, 120, 24));
        centerContent.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(centerContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button homeButton = ScreenStyle.createHomeButton(stage);
        Button editOrderBtn = createEditButton(stage);
        Button placeOrderBtn = createPlaceOrderButton(stage, order);

        HBox bottomBar = new HBox(14, homeButton, editOrderBtn, placeOrderBtn);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10, 18, 10, 18));
        bottomBar.setMaxWidth(680);
        bottomBar.setStyle(
                "-fx-background-color: rgba(255,255,255,0.96);" +
                        "-fx-background-radius: 24;" +
                        "-fx-border-color: rgba(255,152,0,0.24);" +
                        "-fx-border-width: 1.3;" +
                        "-fx-border-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 14, 0, 0, 4);"
        );

        VBox bottomArea = new VBox(bottomBar);
        bottomArea.setAlignment(Pos.CENTER);
        bottomArea.setPadding(new Insets(6, 0, 0, 0));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(scrollPane);
        root.setBottom(bottomArea);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);

        stage.setScene(scene);
        stage.setTitle("Review Order");
        WindowManager.enforceStandardSize(stage);
    }

    private VBox createOrderItemRow(Cart.CartItem item) {
        VBox row = new VBox(8);
        row.setPadding(new Insets(18, 8, 18, 8));
        row.setAlignment(Pos.CENTER_LEFT);

        Label itemName = new Label(item.getMenuItem().getName());
        itemName.setWrapText(true);
        itemName.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        // Small thumbnail next to the name
        ImageView itemThumb = CategoryScreen.createSafeImageView(item.getMenuItem().getImageFileName(), 40, 40);

        Label itemPrice = new Label(String.format("%.2f kr", item.getSubtotal()));
        itemPrice.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + ORANGE + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(12, itemThumb, itemName, spacer, itemPrice);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label quantityLabel = new Label("Qty: " + item.getQuantity());
        quantityLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        VBox detailBox = new VBox(5);
        detailBox.setAlignment(Pos.CENTER_LEFT);

        if (!item.getExtrasText().isBlank()) {
            detailBox.getChildren().add(createDetailLine("+ " + item.getExtrasText()));
        }

        if (!item.getRemovedText().isBlank()) {
            detailBox.getChildren().add(createDetailLine("- " + item.getRemovedText()));
        }

        if (!item.getComboChoicesText().isBlank()) {
            addComboChoiceLines(detailBox, item.getComboChoicesText());
        }

        row.getChildren().addAll(topRow, quantityLabel);

        if (!detailBox.getChildren().isEmpty()) {
            row.getChildren().add(detailBox);
        }

        return row;
    }

    private void addComboChoiceLines(VBox detailBox, String comboText) {
        String[] lines = comboText.split("\\n");

        for (String line : lines) {
            String cleaned = cleanComboLine(line);

            if (!cleaned.isBlank()) {
                detailBox.getChildren().add(createDetailLine(cleaned));
            }
        }
    }

    private String cleanComboLine(String line) {
        String cleaned = line == null ? "" : line.trim();

        cleaned = cleaned.replace("Choose Kids Main:", "Main:");
        cleaned = cleaned.replace("Choose Kids Side:", "Side:");
        cleaned = cleaned.replace("Choose Family Main:", "Main:");
        cleaned = cleaned.replace("Choose Sharing Side:", "Side:");
        cleaned = cleaned.replace("Choose Burger:", "Burger:");
        cleaned = cleaned.replace("Choose Chicken Main:", "Main:");
        cleaned = cleaned.replace("Choose Snack Main:", "Snack:");
        cleaned = cleaned.replace("Choose Extra Snack:", "Extra snack:");
        cleaned = cleaned.replace("Choose Drink:", "Drink:");
        cleaned = cleaned.replace("Combo Size:", "Size:");

        if (cleaned.startsWith("Dice") && cleaned.contains("Gift:")) {
            int giftIndex = cleaned.indexOf("Gift:");
            cleaned = cleaned.substring(giftIndex).trim();
        }

        return cleaned;
    }

    private Label createDetailLine(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(800);
        label.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        return label;
    }

    private Region createDivider() {
        Region divider = new Region();
        divider.setMinHeight(1);
        divider.setMaxHeight(1);
        divider.setStyle("-fx-background-color: " + DIVIDER + ";");
        return divider;
    }

    private VBox createTotalSection(Order order) {
        Label totalText = new Label("Total");
        totalText.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Label totalAmount = new Label(String.format("%.2f kr", order.getTotalPrice()));
        totalAmount.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + ORANGE + ";"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox totalRow = new HBox(12, totalText, spacer, totalAmount);
        totalRow.setAlignment(Pos.CENTER_LEFT);

        Label paymentNote = new Label();

        if (order.getPaymentMethod().equalsIgnoreCase("Cash")) {
            paymentNote.setText("Payment note: Please pay at the counter after placing your order.");
        } else {
            paymentNote.setText("Payment note: Please follow the card terminal instructions after placing your order.");
        }

        paymentNote.setWrapText(true);
        paymentNote.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        VBox section = new VBox(8, totalRow, paymentNote);
        section.setMaxWidth(900);
        section.setPadding(new Insets(14, 26, 14, 26));
        section.setStyle(
                "-fx-background-color: rgba(255,255,255,0.55);" +
                        "-fx-background-radius: 20;"
        );

        return section;
    }

    private Button createEditButton(Stage stage) {
        Button editOrderBtn = new Button("Edit Order");
        editOrderBtn.setPrefWidth(160);
        editOrderBtn.setPrefHeight(50);
        editOrderBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + TEXT_DARK + ";" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-width: 1.4;" +
                        "-fx-border-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        editOrderBtn.setOnAction(e -> CategoryScreen.show(stage));

        return editOrderBtn;
    }

    private Button createPlaceOrderButton(Stage stage, Order order) {
        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setPrefWidth(180);
        placeOrderBtn.setPrefHeight(50);
        placeOrderBtn.setStyle(
                "-fx-background-color: " + GREEN + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(76,175,80,0.26), 9, 0, 0, 3);"
        );

        placeOrderBtn.setOnAction(e -> showPlaceOrderConfirmation(stage, order));

        return placeOrderBtn;
    }

    private void showPlaceOrderConfirmation(Stage stage, Order order) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Place Order");
        alert.setHeaderText("Are you sure you want to place this order?");
        alert.setContentText("After confirming, your order number will be generated.");

        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType confirmButton = new ButtonType("Yes, Place Order");

        alert.getButtonTypes().setAll(cancelButton, confirmButton);

        alert.showAndWait().ifPresent(result -> {
            if (result == confirmButton) {
                placeOrder(stage, order);
            }
        });
    }

    private void placeOrder(Stage stage, Order order) {
        int orderNumber = DatabaseHelper.getNextOrderNumber();
        String today = java.time.LocalDate.now().toString();

        for (Cart.CartItem item : order.getItems()) {
            String customizations = buildCustomizationText(item);

            DatabaseHelper.saveOrder(
                    orderNumber,
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    today,
                    customizations
            );
        }

        Cart.getInstance().clear();

        OrderNumberScreen ons = new OrderNumberScreen();
        ons.start(stage, String.format("%04d", orderNumber));
    }

    private String buildCustomizationText(Cart.CartItem item) {
        StringBuilder text = new StringBuilder();

        if (!item.getExtrasText().isBlank()) {
            text.append("Extras: ")
                    .append(item.getExtrasText())
                    .append("\n");
        }

        if (!item.getRemovedText().isBlank()) {
            text.append("Removed: ")
                    .append(item.getRemovedText())
                    .append("\n");
        }

        if (!item.getComboChoicesText().isBlank()) {
            text.append(item.getComboChoicesText())
                    .append("\n");
        }

        return text.toString().trim();
    }
}