package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.MenuItemRepository;

import java.util.List;

public class ItemListScreen {

  public static void show(Stage stage, String categoryName) {
    App.selectedCategory = categoryName;

    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 10;"
    );
    backButton.setOnAction(e -> CategoryScreenPlaceholder.show(stage));

    Label title = new Label(categoryName);
    title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

    Label subtitle = new Label("Select a meal to view details");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    VBox itemBox = new VBox(15);
    itemBox.setAlignment(Pos.CENTER);

    List<MenuItem> items = MenuItemRepository.findByCategoryName(categoryName);
    if (items.isEmpty()) {
      Label emptyLabel = new Label("No items available in this category.");
      emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");
      itemBox.getChildren().add(emptyLabel);
    } else {
      for (MenuItem item : items) {
        Button itemButton = createItemButton(item);
        itemButton.setOnAction(e -> MealSelectionScreen.show(stage, item));
        itemBox.getChildren().add(itemButton);
      }
    }

    VBox centerContent = new VBox(20, title, subtitle, itemBox);
    centerContent.setAlignment(Pos.CENTER);

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(25));
    root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff7e6, #ffffff);");
    root.setTop(backButton);
    root.setCenter(centerContent);

    Scene scene = new Scene(root, 700, 500);
    stage.setTitle("Menu Items");
    stage.setScene(scene);
    stage.show();
  }

  private static Button createItemButton(MenuItem item) {
    Button button = new Button(item.getName() + " - $" + String.format("%.2f", item.getPrice()));
    button.setPrefWidth(320);
    button.setPrefHeight(55);
    button.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #ffffff;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-border-color: #FF9800;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 16;" +
                    "-fx-border-radius: 16;"
    );
    return button;
  }
}
