package se.lnu.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import se.lnu.Category;
import se.lnu.ScreenStyle;
import se.lnu.WindowManager;
import se.lnu.database.DatabaseHelper;

public class CategoryAdminScreen {

    public static void show(Stage stage) {

        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> AdminDashboardScreen.show(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, spacer);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label title = new Label("Manage Categories");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #222;");

        Label existingLabel = new Label("Existing categories:");
        existingLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox categoryListBox = new VBox(8);
        categoryListBox.setPadding(new Insets(10));
        categoryListBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-background-color: white;");
        refreshCategoryList(categoryListBox);

        Label createLabel = new Label("Create new category");
        createLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox createLabelBox = new HBox(createLabel);
        createLabelBox.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter category name");
        nameField.setPrefWidth(300);

        Button saveBtn = new Button("Save");
        saveBtn.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 20;" +
                        "-fx-background-radius: 10;"
        );
        saveBtn.setPrefWidth(120);

        HBox addRow = new HBox(10, nameField, saveBtn);
        addRow.setAlignment(Pos.CENTER);

        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #1f1f1f;");
        statusLabel.setMaxWidth(300);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setWrapText(true);

        Label deleteLabel = new Label("Delete category");
        deleteLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        HBox deleteLabelBox = new HBox(deleteLabel);
        deleteLabelBox.setAlignment(Pos.CENTER);

        ComboBox<Category> deleteDropdown = new ComboBox<>();
        deleteDropdown.setPrefWidth(300);

        deleteDropdown.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        deleteDropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        refreshDeleteDropdown(deleteDropdown);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: #FF0000;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 20;" +
                        "-fx-background-radius: 10;"
        );
        deleteBtn.setPrefWidth(120);

        HBox deleteRow = new HBox(10, deleteDropdown, deleteBtn);
        deleteRow.setAlignment(Pos.CENTER);

        Runnable clearMessage = () -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(ev -> statusLabel.setText(""));
            pause.play();
        };

        // ADD CATEGORY
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();

            // Validate empty input
            if (name.isEmpty()) {
                statusLabel.setText("Category name cannot be empty.");
                clearMessage.run();
                return;
            }

            // Capitalize
            if (name.length() == 1) {
                name = name.toUpperCase();
            } else {
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            }


            String finalName = name;

            // Prevent duplicates
            boolean exists = DatabaseHelper.getCategories()
                    .stream()
                    .anyMatch(c -> c.getName().equalsIgnoreCase(finalName));

            if (exists) {
                statusLabel.setText("Category already exists!");
                clearMessage.run();
                return;
            }
            // Add category to database
            boolean success = DatabaseHelper.addCategory(name);

            if (success) {
                nameField.clear();
                refreshCategoryList(categoryListBox);
                refreshDeleteDropdown(deleteDropdown);
                statusLabel.setText("Category added successfully!");
            } else {
                statusLabel.setText("Error adding category.");
            }

            clearMessage.run();
        });

        // DELETE CATEGORY
        deleteBtn.setOnAction(e -> {
            Category selected = deleteDropdown.getValue();
            // Validate selection
            if (selected == null) {
                statusLabel.setText("Please select a category to delete.");
                clearMessage.run();
                return;
            }
            // Delete category from database
            boolean success = DatabaseHelper.deleteCategory(selected.getId());

            if (success) {
                refreshCategoryList(categoryListBox);
                refreshDeleteDropdown(deleteDropdown);
                statusLabel.setText("Category deleted successfully!");
            } else {
                statusLabel.setText("Error deleting category.");
            }

            clearMessage.run();
        });

        VBox content = new VBox(
                20,
                title,
                existingLabel,
                categoryListBox,
                createLabelBox,
                addRow,
                deleteLabelBox,
                deleteRow,
                statusLabel
        );

        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(content);
        root.setBackground(ScreenStyle.createBackground());

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Manage Categories");
        WindowManager.enforceStandardSize(stage);
    }
    // Refresh category list display
    private static void refreshCategoryList(VBox listBox) {
        listBox.getChildren().clear();

        for (Category c : DatabaseHelper.getCategories()) {
            Label label = new Label(c.getName());
            label.setStyle("-fx-font-size: 16px;");
            listBox.getChildren().add(label);
        }
    }

    private static void refreshDeleteDropdown(ComboBox<Category> dropdown) {
        dropdown.getItems().clear();
        dropdown.getItems().addAll(DatabaseHelper.getCategories());
    }
}
