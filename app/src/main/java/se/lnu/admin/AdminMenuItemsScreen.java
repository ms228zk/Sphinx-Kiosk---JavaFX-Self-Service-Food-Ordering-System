package se.lnu.admin;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import se.lnu.Category;
import se.lnu.MenuItem;
import se.lnu.ScreenStyle;
import se.lnu.WindowManager;
import se.lnu.database.DatabaseHelper;

public class AdminMenuItemsScreen {

    private static ComboBox<Category> categoryComboBox;
    private static ListView<MenuItem> menuItemListView;
    private static Label statusLabel;

    public static void show(Stage stage) {
        Label title = new Label("Manage Menu Items");
        title.setStyle(
                "-fx-font-size: 36px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222;"
        );

        Label subtitle = new Label("Select a category and delete products that should no longer be sold");
        subtitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: #666;"
        );

        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> AdminDashboardScreen.show(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(12, backButton, spacer);
        topBar.setAlignment(Pos.CENTER_LEFT);

        categoryComboBox = new ComboBox<>();
        categoryComboBox.setPrefWidth(320);
        categoryComboBox.setStyle("-fx-font-size: 16px; -fx-padding: 8;");
        categoryComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category category) {
                return category == null ? "" : category.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        categoryComboBox.setOnAction(e -> loadMenuItems());

        menuItemListView = new ListView<>();
        menuItemListView.setPrefSize(620, 360);
        menuItemListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + String.format("%.2f kr", item.getPrice()));
                }
            }
        });

        Button deleteButton = new Button("Delete Selected Item");
        deleteButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #C62828;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 24;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );
        deleteButton.disableProperty().bind(
                menuItemListView.getSelectionModel().selectedItemProperty().isNull()
        );
        deleteButton.setOnAction(e -> deleteSelectedItem());

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #555;");

        VBox controls = new VBox(14, categoryComboBox, menuItemListView, deleteButton, statusLabel);
        controls.setAlignment(Pos.CENTER);

        VBox centerContent = new VBox(20, title, subtitle, controls);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(30));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(topBar);
        root.setCenter(centerContent);

        Scene scene = new Scene(
                root,
                WindowManager.WINDOW_WIDTH,
                WindowManager.WINDOW_HEIGHT
        );

        stage.setScene(scene);
        stage.setTitle("Manage Menu Items");
        WindowManager.enforceStandardSize(stage);

        loadCategories();
    }

    private static void loadCategories() {
        List<Category> categories = DatabaseHelper.getCategories();
        categoryComboBox.getItems().setAll(categories);

        if (categories.isEmpty()) {
            statusLabel.setText("No categories available.");
            return;
        }

        categoryComboBox.getSelectionModel().selectFirst();
        loadMenuItems();
    }

    private static void loadMenuItems() {
        Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

        if (selectedCategory == null) {
            menuItemListView.getItems().clear();
            return;
        }

        List<MenuItem> menuItems = DatabaseHelper.getItemsByCategory(selectedCategory.getId());
        menuItemListView.getItems().setAll(menuItems);

        if (menuItems.isEmpty()) {
            statusLabel.setText("No menu items in " + selectedCategory.getName() + ".");
        } else {
            statusLabel.setText(menuItems.size() + " item(s) in " + selectedCategory.getName() + ".");
        }
    }

    private static void deleteSelectedItem() {
        MenuItem selectedItem = menuItemListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Menu Item");
        alert.setHeaderText("Delete " + selectedItem.getName() + "?");
        alert.setContentText("This removes the product from the menu and deletes its linked options.");

        ButtonType cancelButton = new ButtonType("Cancel");
        ButtonType deleteButton = new ButtonType("Delete");
        alert.getButtonTypes().setAll(cancelButton, deleteButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != deleteButton) {
            return;
        }

        boolean deleted = DatabaseHelper.deleteMenuItem(selectedItem.getId());

        if (deleted) {
            statusLabel.setText(selectedItem.getName() + " was deleted.");
            loadMenuItems();
        } else {
            statusLabel.setText("Could not delete " + selectedItem.getName() + ".");
        }
    }
}
