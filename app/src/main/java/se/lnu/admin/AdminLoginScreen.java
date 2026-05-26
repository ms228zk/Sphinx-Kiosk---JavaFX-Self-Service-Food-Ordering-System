package se.lnu.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import se.lnu.ScreenStyle;
import se.lnu.WelcomeScreen;
import se.lnu.WindowManager;

public class AdminLoginScreen {

    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static int failedLoginAttempts = 0;

    public static void show(Stage stage) {

        Label title = new Label("Admin Login");
        title.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        Label subtitle = new Label("Secure access for restaurant staff");
        subtitle.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #666666;"
        );

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(340);
        usernameField.setPrefHeight(48);
        usernameField.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 0 14;" +
                        "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #D8D8D8;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1.2;"
        );

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(340);
        passwordField.setPrefHeight(48);
        passwordField.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 0 14;" +
                        "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #D8D8D8;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1.2;"
        );

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Password");
        visiblePasswordField.setMaxWidth(340);
        visiblePasswordField.setPrefHeight(48);
        visiblePasswordField.setManaged(false);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-padding: 0 14;" +
                        "-fx-background-color: #FAFAFA;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #D8D8D8;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1.2;"
        );

        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());

        CheckBox showPasswordCheckBox = new CheckBox("Show password");
        showPasswordCheckBox.setMaxWidth(340);
        showPasswordCheckBox.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #555555;" +
                        "-fx-cursor: hand;"
        );

        showPasswordCheckBox.setOnAction(e -> {
            boolean showPassword = showPasswordCheckBox.isSelected();

            visiblePasswordField.setVisible(showPassword);
            visiblePasswordField.setManaged(showPassword);

            passwordField.setVisible(!showPassword);
            passwordField.setManaged(!showPassword);
        });

        Label errorLabel = new Label("");
        errorLabel.setStyle(
                "-fx-text-fill: #D32F2F;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        Label infoLabel = new Label("Password is protected using hashed authentication.");
        infoLabel.setStyle(
                "-fx-text-fill: #777777;" +
                        "-fx-font-size: 12px;"
        );

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(340);
        loginButton.setPrefHeight(48);
        loginButton.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: #FF9800;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        Button backButton = new Button("Back");
        backButton.setMaxWidth(340);
        backButton.setPrefHeight(42);
        backButton.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555555;" +
                        "-fx-border-color: #CCCCCC;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;"
        );

        loginButton.setOnAction(e -> {

            if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                errorLabel.setText("Too many failed attempts. Access locked.");
                loginButton.setDisable(true);
                usernameField.setDisable(true);
                passwordField.setDisable(true);
                visiblePasswordField.setDisable(true);
                showPasswordCheckBox.setDisable(true);
                return;
            }

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username == null || username.trim().isEmpty()
                    || password == null || password.isEmpty()) {

                errorLabel.setText("Please enter both username and password.");
                return;
            }

            boolean isValidLogin = AdminAuthService.isValidLogin(username, password);

            if (isValidLogin) {
                failedLoginAttempts = 0;
                usernameField.clear();
                passwordField.clear();

                AdminDashboardScreen.show(stage);

            } else {
                failedLoginAttempts++;

                int attemptsLeft = MAX_LOGIN_ATTEMPTS - failedLoginAttempts;

                passwordField.clear();

                if (attemptsLeft > 0) {
                    errorLabel.setText("Invalid login. Attempts left: " + attemptsLeft);
                } else {
                    errorLabel.setText("Too many failed attempts. Access locked.");

                    loginButton.setDisable(true);
                    usernameField.setDisable(true);
                    passwordField.setDisable(true);
                    visiblePasswordField.setDisable(true);
                    showPasswordCheckBox.setDisable(true);
                }
            }
        });

        passwordField.setOnAction(e -> loginButton.fire());
        visiblePasswordField.setOnAction(e -> loginButton.fire());

        backButton.setOnAction(e -> {
            failedLoginAttempts = 0;
            WelcomeScreen.show(stage);
        });

        VBox loginBox = new VBox(
                14,
                title,
                subtitle,
                usernameField,
                passwordField,
                visiblePasswordField,
                showPasswordCheckBox,
                loginButton,
                errorLabel,
                infoLabel,
                backButton
        );

        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(38));
        loginBox.setMaxWidth(470);

        loginBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.96);" +
                        "-fx-background-radius: 26;" +
                        "-fx-border-color: rgba(255, 152, 0, 0.35);" +
                        "-fx-border-width: 1.4;" +
                        "-fx-border-radius: 26;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 24, 0, 0, 8);"
        );

        BorderPane root = new BorderPane();
        root.setBackground(ScreenStyle.createBackground());
        root.setCenter(loginBox);
        root.setPadding(new Insets(40));

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