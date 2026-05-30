package se.lnu.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import se.lnu.MenuItem;
import se.lnu.database.DatabaseHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class itemEditorScreen {
  private static File selectedImageFile;
  private static Label imageLabel;

  public static void show(Stage stage) {

    VBox mainLayout = new VBox(25);

    mainLayout.setPadding(new Insets(30));

    mainLayout.setAlignment(Pos.TOP_CENTER);

    mainLayout.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #f8f4ef, #f2d2a2);
                """);

    // TOP BAR

    Button backButton = new Button("← Back");

    backButton.setStyle("""
                -fx-background-color: #2d2d2d;
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-padding: 12 22;
                -fx-background-radius: 15;
                """);

    backButton.setOnAction(e ->
      AdminDashboardScreen.show(stage)
    );

    HBox topBar = new HBox(backButton);

    topBar.setAlignment(Pos.CENTER_LEFT);

    topBar.setMaxWidth(1200);

    // TITLE

    Label title = new Label("Manage Menu Items");

    title.setStyle("""
                -fx-font-size: 42px;
                -fx-font-weight: bold;
                -fx-text-fill: #1f1f1f;
                """);

    VBox itemsBox = new VBox(18);

    itemsBox.setPadding(new Insets(10));

    List<MenuItem> items =
      DatabaseHelper.getAllMenuItems();

    for (MenuItem item : items) {

      HBox row = new HBox(20);

      row.setAlignment(Pos.CENTER_LEFT);

      row.setPadding(new Insets(18));

      row.setMaxWidth(1200);

      row.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.92);
                    -fx-background-radius: 22;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3);
                    """);

      // ITEM INFO

      VBox itemInfo = new VBox(6);

      itemInfo.setPrefWidth(320);

      Label itemName = new Label(item.getName());

      itemName.setStyle("""
                    -fx-font-size: 24px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #222;
                    """);

      Label descriptionLabel =
        new Label(item.getDescription());

      descriptionLabel.setWrapText(true);

      descriptionLabel.setStyle("""
                    -fx-font-size: 14px;
                    -fx-text-fill: #666;
                    """);

      itemInfo.getChildren().addAll(
        itemName,
        descriptionLabel
      );

      // PRICE

      Label priceLabel =
        new Label(item.getPrice() + " kr");

      priceLabel.setStyle("""
                    -fx-font-size: 20px;
                    -fx-font-weight: bold;
                    -fx-text-fill: #ff9800;
                    """);

      // STATUS

      boolean available =
        DatabaseHelper.isItemAvailable(item.getId());

      Label statusLabel = new Label();

      if (available) {

        statusLabel.setText("Available");

        statusLabel.setStyle("""
                        -fx-text-fill: #2e7d32;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                        """);

      } else {

        statusLabel.setText("Unavailable");

        statusLabel.setStyle("""
                        -fx-text-fill: #c62828;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                        """);
      }

      // EDIT BUTTON

      Button editButton = new Button("Edit");

      editButton.setStyle("""
                    -fx-background-color: #9c27b0;
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                    -fx-padding: 10 22;
                    -fx-background-radius: 12;
                    """);

      editButton.setOnAction(e -> {

        Stage popupStage = new Stage();

        VBox popupLayout = new VBox(18);

        popupLayout.setPadding(new Insets(30));

        popupLayout.setAlignment(Pos.CENTER);

        popupLayout.setStyle("""
                        -fx-background-color: linear-gradient(to bottom right, #ffffff, #f4f4f4);
                        """);

        Label popupTitle =
          new Label("Edit Item");

        popupTitle.setStyle("""
                        -fx-font-size: 30px;
                        -fx-font-weight: bold;
                        """);

        TextField nameField =
          new TextField(item.getName());

        nameField.setPromptText("Item name");

        nameField.setPrefWidth(420);

        nameField.setStyle("""
                        -fx-font-size: 16px;
                        -fx-padding: 12;
                        -fx-background-radius: 10;
                        """);

        TextArea descriptionField =
          new TextArea(item.getDescription());

        descriptionField.setPromptText("Description");

        descriptionField.setWrapText(true);

        descriptionField.setPrefWidth(420);

        descriptionField.setPrefHeight(120);

        descriptionField.setStyle("""
                        -fx-font-size: 15px;
                        -fx-background-radius: 10;
                        """);

        TextField priceField =
          new TextField(
            String.valueOf(item.getPrice())
          );

        priceField.setPromptText("Price");

        priceField.setPrefWidth(420);

        priceField.setStyle("""
                        -fx-font-size: 16px;
                        -fx-padding: 12;
                        -fx-background-radius: 10;
                        """);

        Button editImage = createSecondaryButton("Edit image");

        imageLabel = new Label("No image selected");
        imageLabel.setStyle("-fx-text-fill: #666666;");

        editImage.setOnAction(event -> chooseImage());

        Button saveButton =
          new Button("Save Changes");

        saveButton.setStyle("""
                        -fx-background-color: #4CAF50;
                        -fx-text-fill: white;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                        -fx-padding: 12 30;
                        -fx-background-radius: 14;
                        """);

        saveButton.setOnAction(event -> {

          try {

            String newName = nameField.getText().trim();
            String newDescription = descriptionField.getText().trim();
            double newPrice = Double.parseDouble(priceField.getText());

            // Handle image replacement
            if (selectedImageFile != null) {

              String oldImageName =
                      toSnakeCase(item.getName()) + ".png";

              Path imageDirectory = Paths.get(
                      "app/src/main/resources/images/items"
              ).toAbsolutePath();

              Path oldImagePath =
                      imageDirectory.resolve(oldImageName);

              try {

                // Delete previous image if it exists
                Files.deleteIfExists(oldImagePath);

                // Save new image with updated item name
                String newImageName =
                        toSnakeCase(newName) + ".png";

                Path newImagePath =
                        imageDirectory.resolve(newImageName);

                Files.createDirectories(
                        newImagePath.getParent()
                );

                BufferedImage image =
                        ImageIO.read(selectedImageFile);

                if (image == null) {
                  System.out.println("Invalid image file");
                  return;
                }

                ImageIO.write(
                        image,
                        "png",
                        newImagePath.toFile()
                );

              } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(
                        "Could not replace image."
                );
                return;
              }
            }

            DatabaseHelper.updateItemName(
                    item.getId(),
                    newName
            );

            DatabaseHelper.updateItemPrice(
                    item.getId(),
                    newPrice
            );

            DatabaseHelper.updateItemDescription(
                    item.getId(),
                    newDescription
            );

            // Update UI
            itemName.setText(newName);
            descriptionLabel.setText(newDescription);
            priceLabel.setText(newPrice + " kr");

            popupStage.close();

          } catch (NumberFormatException ex) {

            System.out.println("Invalid price");
          }
        });

        popupLayout.getChildren().addAll(
          popupTitle,
          nameField,
          descriptionField,
          priceField,
          editImage,
          imageLabel,
          saveButton
        );

        Scene popupScene =
          new Scene(popupLayout, 520, 520);

        popupStage.setScene(popupScene);

        popupStage.show();
      });

      // UNAVAILABLE BUTTON

      Button unavailableButton =
        new Button("Unavailable");

      unavailableButton.setStyle("""
                    -fx-background-color: #ff9800;
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                    -fx-padding: 10 22;
                    -fx-background-radius: 12;
                    """);

      unavailableButton.setOnAction(e -> {

        DatabaseHelper.updateItemAvailability(
          item.getId(),
          false
        );

        statusLabel.setText("Unavailable");

        statusLabel.setStyle("""
                        -fx-text-fill: #c62828;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                        """);
      });

      // AVAILABLE BUTTON

      Button availableButton =
        new Button("Available");

      availableButton.setStyle("""
                    -fx-background-color: #4CAF50;
                    -fx-text-fill: white;
                    -fx-font-size: 15px;
                    -fx-font-weight: bold;
                    -fx-padding: 10 22;
                    -fx-background-radius: 12;
                    """);

      availableButton.setOnAction(e -> {

        DatabaseHelper.updateItemAvailability(
          item.getId(),
          true
        );

        statusLabel.setText("Available");

        statusLabel.setStyle("""
                        -fx-text-fill: #2e7d32;
                        -fx-font-size: 16px;
                        -fx-font-weight: bold;
                        """);
      });

      Region spacer = new Region();

      HBox.setHgrow(
        spacer,
        Priority.ALWAYS
      );

      row.getChildren().addAll(
        itemInfo,
        priceLabel,
        spacer,
        statusLabel,
        editButton,
        unavailableButton,
        availableButton
      );

      itemsBox.getChildren().add(row);
    }

    ScrollPane scrollPane =
      new ScrollPane(itemsBox);

    scrollPane.setFitToWidth(true);

    scrollPane.setStyle("""
                -fx-background: transparent;
                -fx-background-color: transparent;
                """);

    mainLayout.getChildren().addAll(
      topBar,
      title,
      scrollPane
    );

    Scene scene =
      new Scene(mainLayout, 1450, 850);

    stage.setScene(scene);

    stage.setMaximized(true);
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

