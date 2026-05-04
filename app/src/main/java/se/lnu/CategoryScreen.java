package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

import java.util.List;

public class CategoryScreen {

  public static void show(Stage stage) {

    // Back button
    Button backBtn = new Button("Back");
    backBtn.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );

    backBtn.setOnAction(e -> {
      OrderTypeScreen.show(stage);
    });

    Button cartButton = createCartButton(stage);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(backBtn, spacer, cartButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    // category layout
    VBox layout = new VBox(15);
    layout.setAlignment(Pos.CENTER);

    List<Category> categories = DatabaseHelper.getCategories();

    for (Category c : categories) {
      Button btn = new Button(c.getName());
      btn.setPrefWidth(200);
      btn.setStyle("-fx-font-size: 18px;" +
              "-fx-background-color: #FF9800;" +
              "-fx-text-fill: white;" +
              "-fx-background-radius: 15;"
      );

      btn.setOnAction(e -> {
        App.setCategory(c.getId(), c.getName());
        ItemListScreen.show(stage);
      });

      layout.getChildren().add(btn);
    }

    ScrollPane scrollPane = new ScrollPane(layout);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

    // Root layout
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setTop(topBar);
    root.setCenter(scrollPane);

    Scene scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Select Category");
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
