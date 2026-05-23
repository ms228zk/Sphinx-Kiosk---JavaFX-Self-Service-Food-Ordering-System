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

    // Dashboard buttons
    Button addMenuItemButton = new Button("Add New Menu Item");
    Button manageItemsButton = new Button("Manage Menu Items");
    Button editItemsButton = new Button("Edit Menu Items");
    Button manageCategoriesButton = new Button("Manage Categories");
    Button viewOrdersButton = new Button("View Orders");
    Button logoutButton = new Button("Logout");

    String mainButtonStyle =
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 13 28;" +
                    "-fx-background-radius: 14;" +
                    "-fx-min-width: 300px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.28), 10, 0, 0, 3);";

    String mainButtonHoverStyle =
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 13 28;" +
                    "-fx-background-radius: 14;" +
                    "-fx-min-width: 300px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.35), 12, 0, 0, 4);";

    String logoutButtonStyle =
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,255,255,0.92);" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 12 28;" +
                    "-fx-background-radius: 14;" +
                    "-fx-border-color: rgba(255,152,0,0.45);" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 14;" +
                    "-fx-min-width: 220px;" +
                    "-fx-cursor: hand;";

    String logoutButtonHoverStyle =
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,152,0,0.92);" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 28;" +
                    "-fx-background-radius: 14;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 14;" +
                    "-fx-min-width: 220px;" +
                    "-fx-cursor: hand;";

    styleButton(addMenuItemButton, mainButtonStyle, mainButtonHoverStyle);
    styleButton(manageItemsButton, mainButtonStyle, mainButtonHoverStyle);
    styleButton(editItemsButton, mainButtonStyle, mainButtonHoverStyle);
    styleButton(manageCategoriesButton, mainButtonStyle, mainButtonHoverStyle);
    styleButton(viewOrdersButton, mainButtonStyle, mainButtonHoverStyle);
    styleButton(logoutButton, logoutButtonStyle, logoutButtonHoverStyle);

    // Button actions
    addMenuItemButton.setOnAction(e -> AdminAddMenuItemScreen.show(stage));

    manageItemsButton.setOnAction(e -> AdminMenuItemsScreen.show(stage));

    editItemsButton.setOnAction(e -> itemEditorScreen.show(stage));

    manageCategoriesButton.setOnAction(e -> CategoryAdminScreen.show(stage));

    viewOrdersButton.setOnAction(e -> AdminOrdersScreen.show(stage));

    logoutButton.setOnAction(e -> WelcomeScreen.show(stage));

    VBox card = new VBox(
            16,
            title,
            subtitle,
            addMenuItemButton,
            manageItemsButton,
            editItemsButton,
            manageCategoriesButton,
            viewOrdersButton,
            logoutButton
    );

    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(36, 60, 36, 60));
    card.setMaxWidth(620);
    card.setStyle(ScreenStyle.createCardStyle());

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(36));
    root.setBackground(ScreenStyle.createBackground());
    root.setCenter(card);

    Scene scene = new Scene(
            root,
            WindowManager.WINDOW_WIDTH,
            WindowManager.WINDOW_HEIGHT
    );

    stage.setScene(scene);
    stage.setTitle("Admin Dashboard");

    WindowManager.enforceStandardSize(stage);
  }

  private static void styleButton(Button button, String normalStyle, String hoverStyle) {
    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));
  }
}