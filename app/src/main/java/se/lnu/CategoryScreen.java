package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    HBox topBar = new HBox(backBtn);
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

    // Root layout
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setTop(topBar);
    root.setCenter(layout);

    Scene scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Select Category");
    stage.show();
  }
}