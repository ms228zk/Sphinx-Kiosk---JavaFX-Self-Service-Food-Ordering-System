package se.lnu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

import java.util.List;

public class CategoryScreen {

  public static void show(Stage stage) {

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

      // ⭐ USER STORY 3 ONLY — print category
      btn.setOnAction(e -> {
        System.out.println("Category clicked: " + c.getName() + " (ID: " + c.getId() + ")");
      });

      layout.getChildren().add(btn);
    }

    Scene scene = new Scene(layout, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Select Category");
    stage.show();
  }
}