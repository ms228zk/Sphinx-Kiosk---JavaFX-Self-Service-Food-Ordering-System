package se.lnu;

import org.jspecify.annotations.NonNull;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OrderNumberScreen {

    public void start(Stage stage, String orderNumber) {

        Label title = new Label("Thank you for your order!");
        title.setStyle(
                "-fx-font-size: 46px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 8, 0.2, 1, 2);"
        );

        Label subtitle = new Label("Please collect your order when your number is called.");
        subtitle.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-text-fill: #555555;"
        );

        VBox ticket = createOrderTicket(orderNumber);

        Label returnMessage = new Label("This screen will return to the welcome page shortly.");
        returnMessage.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #6d4c41;"
        );

        VBox centerContent = getVBox(title, subtitle, ticket, returnMessage);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(centerContent);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Order Number");
        WindowManager.enforceStandardSize(stage);

        PauseTransition wait = new PauseTransition(Duration.seconds(5));
        wait.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(700), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            fade.setOnFinished(event -> {
                Cart.getInstance().reset();
                WelcomeScreen.show(stage);
            });

            fade.play();
        });

        wait.play();
    }

    private static VBox createOrderTicket(String orderNumber) {
        Label ticketLabel = new Label("ORDER NUMBER");
        ticketLabel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #6d4c41;" +
                        "-fx-letter-spacing: 1px;"
        );

        Label numberLabel = new Label(orderNumber);
        numberLabel.setStyle(
                "-fx-font-size: 104px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f1f1f;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.30), 12, 0, 0, 3);"
        );

        Label ticketMsg = new Label("Your order is being prepared");
        ticketMsg.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #333333;"
        );

        Label orderTypeLabel = new Label(getOrderTypeText());
        orderTypeLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
                        "-fx-background-radius: 18;" +
                        "-fx-padding: 8 22;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.25), 8, 0, 0, 2);"
        );

        VBox ticket = new VBox(18, ticketLabel, numberLabel, ticketMsg, orderTypeLabel);
        ticket.setAlignment(Pos.CENTER);
        ticket.setPadding(new Insets(36, 70, 36, 70));
        ticket.setMaxWidth(620);
        ticket.setStyle(
                "-fx-background-color: rgba(255,255,255,0.96);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255,152,0,0.55);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 22, 0, 0, 6);"
        );

        return ticket;
    }

    private static String getOrderTypeText() {
        if (App.orderType == null || App.orderType.isBlank()) {
            return "Order confirmed";
        }

        return App.orderType + " order confirmed";
    }

    private static @NonNull VBox getVBox(
            Label title,
            Label subtitle,
            VBox ticket,
            Label returnMessage
    ) {
        VBox centerContent = new VBox(20, title, subtitle, ticket, returnMessage);
        centerContent.setAlignment(Pos.CENTER);
        return centerContent;
    }
}