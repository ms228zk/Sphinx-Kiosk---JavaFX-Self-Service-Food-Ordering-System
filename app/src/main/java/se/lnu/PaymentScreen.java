package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaymentScreen {

    public static void show(Stage stage) {

        // Title
        Label title = new Label("Choose Payment Method");
        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        // Subtitle
        Label subtitle = new Label("Select how you want to pay");
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: gray;"
        );

        // Payment options
        RadioButton cardButton = new RadioButton("Card");
        RadioButton cashButton = new RadioButton("Cash");

        cardButton.setStyle("-fx-font-size: 18px;");
        cashButton.setStyle("-fx-font-size: 18px;");

        ToggleGroup paymentGroup = new ToggleGroup();

        cardButton.setToggleGroup(paymentGroup);
        cashButton.setToggleGroup(paymentGroup);

        // Error label
        Label errorLabel = new Label("");
        errorLabel.setStyle(
                "-fx-text-fill: red;" +
                        "-fx-font-size: 14px;"
        );

        // Complete button
        Button completeButton = new Button("Complete Order");

        completeButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 10;"
        );

        completeButton.setOnAction(e -> {

            // Validation
            if (paymentGroup.getSelectedToggle() == null) {
                errorLabel.setText("Please select a payment method");
                return;
            }

            String paymentMethod =
                    ((RadioButton) paymentGroup.getSelectedToggle()).getText();

            // Create order
            Order order = new Order(
                    Cart.getInstance().getItems(),
                    paymentMethod
            );

            // Go to confirmation screen
            new OrderConfirmationScreen().start(stage, order);
        });

        // Back button
        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> CartScreen.show(stage));

        VBox centerContent = new VBox(20);

        centerContent.setAlignment(Pos.CENTER);

        centerContent.getChildren().addAll(
                title,
                subtitle,
                cardButton,
                cashButton,
                errorLabel,
                completeButton
        );

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25));
        root.setBackground(ScreenStyle.createBackground());

        root.setTop(backButton);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);

        stage.setTitle("Payment");
        stage.setScene(scene);
        WindowManager.enforceStandardSize(stage);
    }
}