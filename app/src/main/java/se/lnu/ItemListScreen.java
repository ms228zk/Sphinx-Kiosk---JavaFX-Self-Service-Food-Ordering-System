package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

import java.util.List;

public class ItemListScreen {

  public static void show(Stage stage) {
    Label title = new Label(App.selectedCategoryName);
    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333333;");

    Label subtitle = new Label("Select an item to continue");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );
    backButton.setOnAction(e -> CategoryScreen.show(stage));

    Button homeButton = ScreenStyle.createHomeButton(stage);
    Button cartButton = createCartButton(stage);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox topBar = new HBox(12, backButton, spacer, homeButton, cartButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    VBox itemsBox = new VBox(15);
    itemsBox.setAlignment(Pos.CENTER);

    List<MenuItem> items = DatabaseHelper.getItemsByCategory(App.selectedCategoryId);
    if (items.isEmpty()) {
      itemsBox.getChildren().add(new Label("No items available"));
    } else {
      for (MenuItem item : items) {
        Button itemButton = new Button(item.getName() + " - " + String.format("%.2f", item.getPrice()) + " kr");
        itemButton.setPrefWidth(280);
        itemButton.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-background-color: #FF9800;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 10;"
        );
        itemButton.setOnAction(e -> MealSelectionScreen.show(stage, item));
        itemsBox.getChildren().add(itemButton);
      }
    }

    VBox centerContent = new VBox(20, title, subtitle, itemsBox);
    centerContent.setAlignment(Pos.CENTER);

    ScrollPane scrollPane = new ScrollPane(centerContent);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(scrollPane);

    Scene scene = new Scene(root, 600, 450);
    stage.setScene(scene);
    stage.setTitle("Items");
    stage.show();
  }

  private static Button createCartButton(Stage stage) {
    Button cartButton = new Button("Cart (" + Cart.getInstance().getItemCount() + ")");
    cartButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );
    cartButton.setOnAction(e -> CartScreen.show(stage));
    return cartButton;
  }
}
