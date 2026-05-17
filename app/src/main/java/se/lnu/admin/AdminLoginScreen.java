package se.lnu.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import se.lnu.ScreenStyle;
import se.lnu.WelcomeScreen;
import se.lnu.WindowManager;

public class AdminLoginScreen {

    // Simple hardcoded admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";

    public static void show(Stage stage) {

        Label title = new Label("Admin Login");
        title.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        Label subtitle = new Label("Restricted access only");
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #666;"
        );

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);

        Label errorLabel = new Label("");
        errorLabel.setStyle(
                "-fx-text-fill: red;" +
                        "-fx-font-size: 14px;"
        );

        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #FF9800;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10 25;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        loginButton.setOnAction(e -> {

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals(ADMIN_USERNAME)
                    && password.equals(ADMIN_PASSWORD)) {

                AdminDashboardScreen.show(stage);

            } else {
                errorLabel.setText("Invalid username or password");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> WelcomeScreen.show(stage));

        VBox centerContent = new VBox(
                18,
                title,
                subtitle,
                usernameField,
                passwordField,
                loginButton,
                errorLabel,
                backButton
        );

        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(centerContent);

        Scene scene = new Scene(
                root,
                WindowManager.WINDOW_WIDTH,
                WindowManager.WINDOW_HEIGHT
        );

        stage.setScene(scene);
        stage.setTitle("Admin Login");

        WindowManager.enforceStandardSize(stage);
    }
}