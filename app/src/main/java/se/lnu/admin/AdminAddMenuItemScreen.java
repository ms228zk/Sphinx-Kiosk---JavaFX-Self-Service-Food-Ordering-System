package se.lnu.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import se.lnu.Category;
import se.lnu.ExtraOption;
import se.lnu.RemovableIngredient;
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

  private static FlowPane extrasFlowPane;
  private static FlowPane removableIngredientsFlowPane;

  private static TextField customExtraNameField;
  private static TextField customExtraPriceField;
  private static VBox customExtrasListBox;

  private static TextField customRemovableField;
  private static VBox customRemovablesListBox;

  private static final List<CheckBox> extraCheckBoxes = new ArrayList<>();
  private static final List<CheckBox> removableIngredientCheckBoxes = new ArrayList<>();

  private static final List<DatabaseHelper.CustomExtraInput> customExtras = new ArrayList<>();
  private static final List<String> customRemovableIngredients = new ArrayList<>();

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

    Label subtitle = new Label("Create a new product and choose only relevant customization options.");
    subtitle.setWrapText(true);
    subtitle.setMaxWidth(600);
    subtitle.setAlignment(Pos.CENTER);
    subtitle.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-text-fill: #666666;"
    );

    nameField = new TextField();
    nameField.setPromptText("Item name");
    nameField.setMaxWidth(500);
    nameField.setStyle(createInputStyle());

    descriptionField = new TextArea();
    descriptionField.setPromptText("Description");
    descriptionField.setWrapText(true);
    descriptionField.setMaxWidth(500);
    descriptionField.setPrefHeight(90);
    descriptionField.setStyle(createInputStyle());

    priceField = new TextField();
    priceField.setPromptText("Price");
    priceField.setMaxWidth(500);
    priceField.setStyle(createInputStyle());

    categoryComboBox = new ComboBox<>();
    categoryComboBox.setPromptText("Select category");
    categoryComboBox.setMaxWidth(500);
    categoryComboBox.setPrefWidth(500);
    categoryComboBox.setStyle(createComboBoxStyle());

    loadCategories();

    categoryComboBox.setOnAction(e -> {
      loadExtraOptionsForSelectedCategory();
      loadRemovableIngredientsForSelectedCategory();
    });

    Button imageButton = createSecondaryButton("Choose PNG Image");

    imageLabel = new Label("No image selected");
    imageLabel.setStyle("-fx-text-fill: #666666;");

    imageButton.setOnAction(e -> chooseImage());

    VBox extrasSection = createExtrasSection();
    VBox removableSection = createRemovableIngredientsSection();

    Button saveButton = createPrimaryButton("Save Menu Item");
    saveButton.setOnAction(e -> saveMenuItem());

    Button clearButton = createSecondaryButton("Clear Form");
    clearButton.setOnAction(e -> clearForm(true));

    statusLabel = new Label("");
    statusLabel.setWrapText(true);
    statusLabel.setMaxWidth(560);
    statusLabel.setAlignment(Pos.CENTER);
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
            extrasSection,
            removableSection,
            saveButton,
            clearButton,
            statusLabel
    );

    formCard.setAlignment(Pos.CENTER);
    formCard.setPadding(new Insets(36, 58, 36, 58));
    formCard.setMaxWidth(820);
    formCard.setStyle(ScreenStyle.createCardStyle());

    ScrollPane scrollPane = new ScrollPane(formCard);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
    scrollPane.setPadding(new Insets(10));

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(scrollPane);

    Scene scene = new Scene(
            root,
            WindowManager.WINDOW_WIDTH,
            WindowManager.WINDOW_HEIGHT
    );

    stage.setTitle("Admin - Add Menu Item");
    stage.setScene(scene);
    WindowManager.enforceStandardSize(stage);
  }

  private static VBox createExtrasSection() {
    Label title = new Label("Suggested Extras / Add-ons");
    title.setStyle(createSectionTitleStyle());

    Label subtitle = new Label("Only relevant options are shown based on the selected category.");
    subtitle.setWrapText(true);
    subtitle.setStyle(createSectionSubtitleStyle());

    extrasFlowPane = new FlowPane();
    extrasFlowPane.setHgap(10);
    extrasFlowPane.setVgap(10);
    extrasFlowPane.setAlignment(Pos.CENTER_LEFT);
    extrasFlowPane.setMaxWidth(650);

    loadExtraOptionsForSelectedCategory();

    Label customTitle = new Label("Add New Extra");
    customTitle.setStyle(createMiniTitleStyle());

    customExtraNameField = new TextField();
    customExtraNameField.setPromptText("Extra name");
    customExtraNameField.setPrefWidth(250);
    customExtraNameField.setStyle(createInputStyle());

    customExtraPriceField = new TextField();
    customExtraPriceField.setPromptText("Price");
    customExtraPriceField.setPrefWidth(120);
    customExtraPriceField.setStyle(createInputStyle());

    Button addExtraButton = createSmallButton("Add Extra");
    addExtraButton.setOnAction(e -> addCustomExtra());

    Button clearExtraButton = createClearButton("Clear Extra");
    clearExtraButton.setOnAction(e -> {
      customExtraNameField.clear();
      customExtraPriceField.clear();
    });

    HBox customExtraInputRow = new HBox(
            10,
            customExtraNameField,
            customExtraPriceField,
            addExtraButton,
            clearExtraButton
    );
    customExtraInputRow.setAlignment(Pos.CENTER_LEFT);

    customExtrasListBox = new VBox(6);
    customExtrasListBox.setAlignment(Pos.CENTER_LEFT);

    VBox section = new VBox(
            12,
            title,
            subtitle,
            extrasFlowPane,
            customTitle,
            customExtraInputRow,
            customExtrasListBox
    );

    section.setAlignment(Pos.CENTER_LEFT);
    section.setMaxWidth(700);
    section.setPadding(new Insets(20));
    section.setStyle(createSmallCardStyle());

    return section;
  }

  private static VBox createRemovableIngredientsSection() {
    Label title = new Label("Removable Ingredients");
    title.setStyle(createSectionTitleStyle());

    Label subtitle = new Label("Only common relevant ingredients are shown, but staff can add new ones.");
    subtitle.setWrapText(true);
    subtitle.setStyle(createSectionSubtitleStyle());

    removableIngredientsFlowPane = new FlowPane();
    removableIngredientsFlowPane.setHgap(10);
    removableIngredientsFlowPane.setVgap(10);
    removableIngredientsFlowPane.setAlignment(Pos.CENTER_LEFT);
    removableIngredientsFlowPane.setMaxWidth(650);

    loadRemovableIngredientsForSelectedCategory();

    Label customTitle = new Label("Add New Removable Ingredient");
    customTitle.setStyle(createMiniTitleStyle());

    customRemovableField = new TextField();
    customRemovableField.setPromptText("Ingredient name");
    customRemovableField.setPrefWidth(380);
    customRemovableField.setStyle(createInputStyle());

    Button addIngredientButton = createSmallButton("Add Ingredient");
    addIngredientButton.setOnAction(e -> addCustomRemovableIngredient());

    Button clearIngredientButton = createClearButton("Clear Ingredient");
    clearIngredientButton.setOnAction(e -> customRemovableField.clear());

    HBox customIngredientInputRow = new HBox(
            10,
            customRemovableField,
            addIngredientButton,
            clearIngredientButton
    );
    customIngredientInputRow.setAlignment(Pos.CENTER_LEFT);

    customRemovablesListBox = new VBox(6);
    customRemovablesListBox.setAlignment(Pos.CENTER_LEFT);

    VBox section = new VBox(
            12,
            title,
            subtitle,
            removableIngredientsFlowPane,
            customTitle,
            customIngredientInputRow,
            customRemovablesListBox
    );

    section.setAlignment(Pos.CENTER_LEFT);
    section.setMaxWidth(700);
    section.setPadding(new Insets(20));
    section.setStyle(createSmallCardStyle());

    return section;
  }

  private static void loadExtraOptionsForSelectedCategory() {
    if (extrasFlowPane == null) {
      return;
    }

    extraCheckBoxes.clear();
    extrasFlowPane.getChildren().clear();

    Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
    String categoryName = selectedCategory == null ? "" : selectedCategory.getName();

    List<ExtraOption> extras = DatabaseHelper.getAllExtraOptions();

    for (ExtraOption extra : extras) {
      if (!isExtraRelevantForCategory(extra.getName(), categoryName)) {
        continue;
      }

      CheckBox checkBox = new CheckBox(
              extra.getName() + " (+" + String.format("%.2f kr", extra.getPrice()) + ")"
      );

      checkBox.setUserData(extra.getId());
      checkBox.setStyle(createCheckBoxStyle());

      extraCheckBoxes.add(checkBox);
      extrasFlowPane.getChildren().add(checkBox);
    }

    if (extraCheckBoxes.isEmpty()) {
      Label emptyLabel = new Label("No suggested extras for this category. Add a custom extra below.");
      emptyLabel.setStyle("-fx-text-fill: #777777; -fx-font-style: italic;");
      extrasFlowPane.getChildren().add(emptyLabel);
    }
  }

  private static void loadRemovableIngredientsForSelectedCategory() {
    if (removableIngredientsFlowPane == null) {
      return;
    }

    removableIngredientCheckBoxes.clear();
    removableIngredientsFlowPane.getChildren().clear();

    Category selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
    String categoryName = selectedCategory == null ? "" : selectedCategory.getName();

    List<RemovableIngredient> ingredients = DatabaseHelper.getAllRemovableIngredients();

    for (RemovableIngredient ingredient : ingredients) {
      if (!isIngredientRelevantForCategory(ingredient.name(), categoryName)) {
        continue;
      }

      CheckBox checkBox = new CheckBox(ingredient.name());

      checkBox.setUserData(ingredient.ingredientId());
      checkBox.setStyle(createCheckBoxStyle());

      removableIngredientCheckBoxes.add(checkBox);
      removableIngredientsFlowPane.getChildren().add(checkBox);
    }

    if (removableIngredientCheckBoxes.isEmpty()) {
      Label emptyLabel = new Label("No suggested ingredients for this category. Add a custom ingredient below.");
      emptyLabel.setStyle("-fx-text-fill: #777777; -fx-font-style: italic;");
      removableIngredientsFlowPane.getChildren().add(emptyLabel);
    }
  }

  private static boolean isExtraRelevantForCategory(String extraName, String categoryName) {
    String extra = extraName.toLowerCase();
    String category = categoryName.toLowerCase();

    if (category.contains("burger")) {
      return containsAny(extra,
              "cheese", "patty", "bbq", "onion", "pickle", "lettuce",
              "jalapeno", "jalapenos", "halloumi", "tomato", "mayo", "sauce"
      );
    }

    if (category.contains("drink")) {
      return containsAny(extra,
              "shot", "milk", "syrup", "cream", "mango", "protein",
              "coconut", "chia", "mint", "lemon", "ice", "sparkling", "water"
      );
    }

    if (category.contains("side")) {
      return containsAny(extra,
              "dip", "sauce", "cheese", "jalapeno", "jalapenos",
              "chicken", "marinara", "garlic", "spicy"
      );
    }

    if (category.contains("dessert")) {
      return containsAny(extra,
              "chocolate", "vanilla", "cream", "sprinkle", "sprinkles",
              "glaze", "caramel", "ice cream", "topping", "toppings"
      );
    }

    if (category.contains("combo")) {
      return containsAny(extra,
              "fries", "drink", "sauce", "dip", "cheese", "patty",
              "chicken", "nuggets", "mozzarella", "juice"
      );
    }

    return true;
  }

  private static boolean isIngredientRelevantForCategory(String ingredientName, String categoryName) {
    String ingredient = ingredientName.toLowerCase();
    String category = categoryName.toLowerCase();

    if (category.contains("burger")) {
      return containsAny(ingredient,
              "pickle", "onion", "bbq", "mayo", "cheese", "bun",
              "lettuce", "tomato", "jalape", "sauce"
      );
    }

    if (category.contains("drink")) {
      return containsAny(ingredient,
              "sugar", "milk", "cream", "ice", "mint", "soda", "water"
      );
    }

    if (category.contains("side")) {
      return containsAny(ingredient,
              "jalape", "cheese", "mayo", "ketchup", "meat",
              "dip", "breadcrumb", "breading", "sauce"
      );
    }

    if (category.contains("dessert")) {
      return containsAny(ingredient,
              "nuts", "chocolate", "ice cream", "sugar", "caramel",
              "cream", "cherry", "syrup"
      );
    }

    if (category.contains("combo")) {
      return containsAny(ingredient,
              "pickle", "onion", "mayo", "cheese", "lettuce",
              "tomato", "ice", "sugar", "sauce"
      );
    }

    return true;
  }

  private static boolean containsAny(String text, String... keywords) {
    for (String keyword : keywords) {
      if (text.contains(keyword)) {
        return true;
      }
    }

    return false;
  }

  private static void addCustomExtra() {
    String extraName = customExtraNameField.getText().trim();
    String priceText = customExtraPriceField.getText().trim();

    if (extraName.isEmpty()) {
      showWarning("Missing extra name", "Please enter the extra/add-on name.");
      return;
    }

    if (priceText.isEmpty()) {
      showWarning("Missing extra price", "Please enter the extra/add-on price.");
      return;
    }

    double extraPrice;

    try {
      extraPrice = Double.parseDouble(priceText);
    } catch (NumberFormatException e) {
      showWarning("Invalid extra price", "Please enter a valid number for the extra price.");
      return;
    }

    if (extraPrice < 0) {
      showWarning("Invalid extra price", "Extra price cannot be negative.");
      return;
    }

    DatabaseHelper.CustomExtraInput customExtra =
            new DatabaseHelper.CustomExtraInput(extraName, extraPrice);

    customExtras.add(customExtra);

    Label addedLabel = new Label(extraName + " (+" + String.format("%.2f kr", extraPrice) + ")");
    addedLabel.setStyle(createAddedOptionStyle());

    Button removeButton = createRemoveButton("Remove");

    HBox addedRow = new HBox(10, addedLabel, removeButton);
    addedRow.setAlignment(Pos.CENTER_LEFT);

    removeButton.setOnAction(event -> {
      customExtras.remove(customExtra);
      customExtrasListBox.getChildren().remove(addedRow);
    });

    customExtrasListBox.getChildren().add(addedRow);

    customExtraNameField.clear();
    customExtraPriceField.clear();
  }

  private static void addCustomRemovableIngredient() {
    String ingredientName = customRemovableField.getText().trim();

    if (ingredientName.isEmpty()) {
      showWarning("Missing ingredient", "Please enter the removable ingredient name.");
      return;
    }

    customRemovableIngredients.add(ingredientName);

    Label addedLabel = new Label(ingredientName);
    addedLabel.setStyle(createAddedOptionStyle());

    Button removeButton = createRemoveButton("Remove");

    HBox addedRow = new HBox(10, addedLabel, removeButton);
    addedRow.setAlignment(Pos.CENTER_LEFT);

    removeButton.setOnAction(event -> {
      customRemovableIngredients.remove(ingredientName);
      customRemovablesListBox.getChildren().remove(addedRow);
    });

    customRemovablesListBox.getChildren().add(addedRow);

    customRemovableField.clear();
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

    if (selectedImageFile != null) {
      String snakeCaseName = toSnakeCase(name);
      String imageFileName = snakeCaseName + ".png";

      Path destination = Paths.get(
              "app/src/main/resources/images/items",
              imageFileName
      ).toAbsolutePath();

      try {
        Files.createDirectories(destination.getParent());

        BufferedImage image = ImageIO.read(selectedImageFile);

        if (image == null) {
          showWarning("Image Error", "The selected file could not be read as an image.");
          return;
        }

        ImageIO.write(image, "png", destination.toFile());

      } catch (IOException e) {
        e.printStackTrace();
        showWarning("Image Error", "Could not save image file.");
        return;
      }
    }

    List<Integer> selectedExtraIds = getSelectedIds(extraCheckBoxes);
    List<Integer> selectedRemovableIngredientIds = getSelectedIds(removableIngredientCheckBoxes);

    boolean saved = DatabaseHelper.addMenuItemWithExistingAndCustomOptions(
            name,
            description,
            price,
            selectedCategory.getId(),
            selectedExtraIds,
            selectedRemovableIngredientIds,
            customExtras,
            customRemovableIngredients
    );

    if (saved) {
      clearForm(false);

      statusLabel.setText("Menu item added successfully with selected and custom options.");
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

  private static List<Integer> getSelectedIds(List<CheckBox> checkBoxes) {
    List<Integer> selectedIds = new ArrayList<>();

    for (CheckBox checkBox : checkBoxes) {
      if (checkBox.isSelected() && checkBox.getUserData() instanceof Integer) {
        selectedIds.add((Integer) checkBox.getUserData());
      }
    }

    return selectedIds;
  }

  private static void clearForm(boolean clearStatus) {
    nameField.clear();
    descriptionField.clear();
    priceField.clear();

    if (!categoryComboBox.getItems().isEmpty()) {
      categoryComboBox.getSelectionModel().selectFirst();
    }

    for (CheckBox checkBox : extraCheckBoxes) {
      checkBox.setSelected(false);
    }

    for (CheckBox checkBox : removableIngredientCheckBoxes) {
      checkBox.setSelected(false);
    }

    customExtras.clear();
    customRemovableIngredients.clear();

    if (customExtrasListBox != null) {
      customExtrasListBox.getChildren().clear();
    }

    if (customRemovablesListBox != null) {
      customRemovablesListBox.getChildren().clear();
    }

    if (customExtraNameField != null) {
      customExtraNameField.clear();
    }

    if (customExtraPriceField != null) {
      customExtraPriceField.clear();
    }

    if (customRemovableField != null) {
      customRemovableField.clear();
    }

    if (clearStatus) {
      statusLabel.setText("");
    }

    selectedImageFile = null;
    imageLabel.setText("No image selected");

    loadExtraOptionsForSelectedCategory();
    loadRemovableIngredientsForSelectedCategory();
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

  private static String createSectionTitleStyle() {
    return "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1f1f1f;";
  }

  private static String createSectionSubtitleStyle() {
    return "-fx-font-size: 14px;" +
            "-fx-text-fill: #666666;";
  }

  private static String createMiniTitleStyle() {
    return "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #333333;" +
            "-fx-padding: 8 0 0 0;";
  }

  private static String createCheckBoxStyle() {
    return "-fx-font-size: 14px;" +
            "-fx-font-weight: 600;" +
            "-fx-text-fill: #222222;" +
            "-fx-background-color: rgba(255,255,255,0.82);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,152,0,0.25);" +
            "-fx-border-radius: 14;" +
            "-fx-padding: 7 10;" +
            "-fx-cursor: hand;";
  }

  private static String createAddedOptionStyle() {
    return "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #2e7d32;" +
            "-fx-background-color: rgba(76,175,80,0.12);" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 7 12;";
  }

  private static String createSmallCardStyle() {
    return "-fx-background-color: rgba(255,248,238,0.95);" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: rgba(255,152,0,0.25);" +
            "-fx-border-radius: 22;" +
            "-fx-border-width: 1;";
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

  private static Button createSmallButton(String text) {
    Button button = new Button(text);

    button.setPrefHeight(42);
    button.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #ff9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 14;" +
                    "-fx-padding: 8 16;" +
                    "-fx-cursor: hand;"
    );

    return button;
  }

  private static Button createClearButton(String text) {
    Button button = new Button(text);

    button.setPrefHeight(42);
    button.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,255,255,0.92);" +
                    "-fx-text-fill: #555555;" +
                    "-fx-background-radius: 14;" +
                    "-fx-border-color: rgba(120,120,120,0.35);" +
                    "-fx-border-radius: 14;" +
                    "-fx-padding: 8 14;" +
                    "-fx-cursor: hand;"
    );

    return button;
  }

  private static Button createRemoveButton(String text) {
    Button button = new Button(text);

    button.setStyle(
            "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(198,40,40,0.12);" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: rgba(198,40,40,0.35);" +
                    "-fx-border-radius: 12;" +
                    "-fx-padding: 5 10;" +
                    "-fx-cursor: hand;"
    );

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