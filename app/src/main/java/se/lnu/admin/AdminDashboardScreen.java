package se.lnu.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import se.lnu.ScreenStyle;
import se.lnu.WelcomeScreen;
import se.lnu.WindowManager;

public class AdminDashboardScreen {

    public static void show(Stage stage) {

        Label title = new Label("Admin Dashboard");
        title.setStyle(
                "-fx-font-size: 40px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        Label subtitle = new Label("Admin mode activated");
        subtitle.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-text-fill: #666;"
        );

        Button manageItemsButton = new Button("Manage Menu Items");
        Button manageCategoriesButton = new Button("Manage Categories");
        Button logoutButton = new Button("Logout");

        String buttonStyle =
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #FF9800;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 24;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;";

        manageItemsButton.setStyle(buttonStyle);
        manageCategoriesButton.setStyle(buttonStyle);
        logoutButton.setStyle(buttonStyle);

        manageItemsButton.setOnAction(e -> {
            AdminMenuItemsScreen.show(stage);
        });

        manageCategoriesButton.setOnAction(e -> {
            CategoryAdminScreen.show(stage);
        });

        logoutButton.setOnAction(e -> {
            WelcomeScreen.show(stage);
        });

        VBox centerContent = new VBox(
                20,
                title,
                subtitle,
                manageItemsButton,
                manageCategoriesButton,
                logoutButton
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
        stage.setTitle("Admin Dashboard");

        WindowManager.enforceStandardSize(stage);
    }
}
