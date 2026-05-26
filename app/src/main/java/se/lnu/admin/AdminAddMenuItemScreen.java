package se.lnu.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import se.lnu.Category;
import se.lnu.ScreenStyle;
import se.lnu.WindowManager;
import se.lnu.database.DatabaseHelper;

import javax.imageio.ImageIO;

public class AdminAddMenuItemScreen {

  private static TextField nameField;
  private static TextArea descriptionField;
  private static TextField priceField;
  private static ComboBox<Category> categoryComboBox;
  private static Label statusLabel;
  private static File selectedImageFile;
  private static Label imageLabel;
  private static FlowPane itemsContainer; // this must be your UI container

  public static void show(Stage stage) {
    Button backButton = ScreenStyle.createBackButton();
    backButton.setOnAction(e -> AdminDashboardScreen.show(stage));

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backButton, spacer);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label badge = new Label("ADMIN MODE");
    badge.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-color: rgba(255, 193, 7, 0.30);" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 22;"
    );

    Label title = new Label("Add New Menu Item");
    title.setStyle(
            "-fx-font-size: 40px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label subtitle = new Label("Create a new product and assign it to a category.");
    subtitle.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-text-fill: #666666;"
    );

    nameField = new TextField();
    nameField.setPromptText("Item name");
    nameField.setMaxWidth(430);
    nameField.setStyle(createInputStyle());

    descriptionField = new TextArea();
    descriptionField.setPromptText("Description");
    descriptionField.setWrapText(true);
    descriptionField.setMaxWidth(430);
    descriptionField.setPrefHeight(90);
    descriptionField.setStyle(createInputStyle());

    priceField = new TextField();
    priceField.setPromptText("Price");
    priceField.setMaxWidth(430);
    priceField.setStyle(createInputStyle());

    categoryComboBox = new ComboBox<>();
    categoryComboBox.setPromptText("Select category");
    categoryComboBox.setMaxWidth(430);
    categoryComboBox.setPrefWidth(430);
    categoryComboBox.setStyle(createComboBoxStyle());

    loadCategories();
    Button imageButton = createSecondaryButton("Choose PNG Image");

    imageLabel = new Label("No image selected");
    imageLabel.setStyle("-fx-text-fill: #666666;");

    imageButton.setOnAction(e -> chooseImage());

    Button saveButton = createPrimaryButton("Save Menu Item");
    saveButton.setOnAction(e -> saveMenuItem());

    Button clearButton = createSecondaryButton("Clear Form");
    clearButton.setOnAction(e -> clearForm(true));

    statusLabel = new Label("");
    statusLabel.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #555555;"
    );

    VBox formCard = new VBox(
            16,
            badge,
            title,
            subtitle,
            nameField,
            descriptionField,
            priceField,
            categoryComboBox,
            imageButton,
            imageLabel,
            saveButton,
            clearButton,
            statusLabel
    );

    formCard.setAlignment(Pos.CENTER);
    formCard.setPadding(new Insets(36, 58, 36, 58));
    formCard.setMaxWidth(640);
    formCard.setStyle(ScreenStyle.createCardStyle());

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(formCard);

    Scene scene = new Scene(
            root,
            WindowManager.WINDOW_WIDTH,
            WindowManager.WINDOW_HEIGHT
    );

    stage.setTitle("Admin - Add Menu Item");
    stage.setScene(scene);
    WindowManager.enforceStandardSize(stage);
  }

  private static void loadCategories() {
    List<Category> categories = DatabaseHelper.getCategories();
    categoryComboBox.getItems().setAll(categories);

    if (!categories.isEmpty()) {
      categoryComboBox.getSelectionModel().selectFirst();
    }
  }

  private static void saveMenuItem() {
    String name = nameField.getText().trim();
    String description = descriptionField.getText().trim();
    String priceText = priceField.getText().trim();
    Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();

    if (name.isEmpty()) {
      showWarning("Missing name", "Please enter the item name.");
      return;
    }

    if (description.isEmpty()) {
      showWarning("Missing description", "Please enter the item description.");
      return;
    }

    if (priceText.isEmpty()) {
      showWarning("Missing price", "Please enter the item price.");
      return;
    }

    double price;

    try {
      price = Double.parseDouble(priceText);
    } catch (NumberFormatException e) {
      showWarning("Invalid price", "Please enter a valid number for the price.");
      return;
    }

    if (price <= 0) {
      showWarning("Invalid price", "Price must be greater than 0.");
      return;
    }

    if (selectedCategory == null) {
      showWarning("Missing category", "Please select a category.");
      return;
    }
    String imageFileName = null;

    if (selectedImageFile != null) {

      String snakeCaseName = toSnakeCase(name);
      imageFileName = snakeCaseName + ".png";

      Path destination = Paths.get(
              "app/src/main/resources/images/items",
              imageFileName
      ).toAbsolutePath();

      try {
        Files.createDirectories(destination.getParent());

        BufferedImage image = ImageIO.read(selectedImageFile);

        ImageIO.write(image, "png", destination.toFile());

      } catch (IOException e) {
        e.printStackTrace();
        showWarning("Image Error", "Could not save image file.");
        return;
      }
    }

    boolean saved = DatabaseHelper.addMenuItem(
            name,
            description,
            price,
            selectedCategory.getId()
    );

    if (saved) {
      clearForm(false);

      statusLabel.setText("Menu item added successfully.");
      statusLabel.setStyle(
              "-fx-font-size: 15px;" +
                      "-fx-font-weight: bold;" +
                      "-fx-text-fill: #2e7d32;"
      );
    } else {
      statusLabel.setText("Could not save menu item.");
      statusLabel.setStyle(
              "-fx-font-size: 15px;" +
                      "-fx-font-weight: bold;" +
                      "-fx-text-fill: #c62828;"
      );
    }
  }

  private static void clearForm(boolean clearStatus) {
    nameField.clear();
    descriptionField.clear();
    priceField.clear();

    if (!categoryComboBox.getItems().isEmpty()) {
      categoryComboBox.getSelectionModel().selectFirst();
    }

    if (clearStatus) {
      statusLabel.setText("");
    }

    selectedImageFile = null;
    imageLabel.setText("No image selected");
  }

  private static void showWarning(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private static String createInputStyle() {
    return "-fx-font-size: 15px;" +
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,152,0,0.45);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 12;";
  }

  private static String createComboBoxStyle() {
    return "-fx-font-size: 15px;" +
            "-fx-background-color: white;" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,152,0,0.45);" +
            "-fx-border-width: 1.2;" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 8;";
  }

  private static Button createPrimaryButton(String text) {
    Button button = new Button(text);

    button.setPrefWidth(260);
    button.setPrefHeight(52);

    String normalStyle =
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 18;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.34), 12, 0, 0, 4);";

    String hoverStyle =
            "-fx-font-size: 17px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 18;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.40), 14, 0, 0, 5);";

    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));

    return button;
  }

  private static Button createSecondaryButton(String text) {
    Button button = new Button(text);

    button.setPrefWidth(260);
    button.setPrefHeight(46);

    String normalStyle =
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,255,255,0.92);" +
                    "-fx-text-fill: #333333;" +
                    "-fx-background-radius: 16;" +
                    "-fx-border-color: rgba(255,152,0,0.40);" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 16;" +
                    "-fx-cursor: hand;";

    String hoverStyle =
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,152,0,0.92);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 16;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 16;" +
                    "-fx-cursor: hand;";

    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));

    return button;
  }
  private static void chooseImage() {
    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle("Select PNG Image");

    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Images", "*.png")
    );

    File file = fileChooser.showOpenDialog(null);

    if (file != null) {
      selectedImageFile = file;
      imageLabel.setText(file.getName());
    }
  }

  private static String toSnakeCase(String text) {
    return text
            .trim()
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "_");
  }
}

