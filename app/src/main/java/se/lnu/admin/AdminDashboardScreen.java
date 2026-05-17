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

        Label subtitle =
          new Label("Admin mode activated");

        subtitle.setStyle(
          "-fx-font-size: 18px;" +
            "-fx-text-fill: #666;"
        );

        // BUTTONS

        Button manageItemsButton =
          new Button("Manage Menu Items");

        Button editItemsButton =
          new Button("Edit Menu Items");

        Button manageCategoriesButton =
          new Button("Manage Categories");

        Button logoutButton =
          new Button("Logout");

        // BUTTON STYLE

        String buttonStyle =
          "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #FF9800;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 14 32;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 260;" +
            "-fx-min-height: 55;";

        manageItemsButton.setStyle(buttonStyle);

        editItemsButton.setStyle(buttonStyle);

        manageCategoriesButton.setStyle(buttonStyle);

        logoutButton.setStyle(
          "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #ff9800;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 12 28;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;"
        );

        // BUTTON ACTIONS

        manageItemsButton.setOnAction(e -> {

            AdminMenuItemsScreen.show(stage);
        });

        editItemsButton.setOnAction(e -> {

            itemEditorScreen.show(stage);
        });

        manageCategoriesButton.setOnAction(e -> {

            CategoryAdminScreen.show(stage);
        });

        logoutButton.setOnAction(e -> {

            WelcomeScreen.show(stage);
        });

        // LAYOUT

        VBox centerContent = new VBox(
          22,
          title,
          subtitle,
          manageItemsButton,
          editItemsButton,
          manageCategoriesButton,
          logoutButton
        );

        centerContent.setAlignment(Pos.CENTER);

        centerContent.setPadding(
          new Insets(40)
        );

        BorderPane root = new BorderPane();

        root.setBackground(
          ScreenStyle.createBackground()
        );

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