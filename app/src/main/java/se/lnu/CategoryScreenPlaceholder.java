package se.lnu;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryScreenPlaceholder {

  public static void show(Stage stage) {

    Label label = new Label("Category screen will be added later");

    Button backBtn = new Button("Back");

    backBtn.setOnAction(e -> {
      OrderTypeScreen.show(stage);
    });

    VBox layout = new VBox(20);
    layout.getChildren().addAll(label, backBtn);

    Scene scene = new Scene(layout, 400, 300);
    stage.setScene(scene);
    stage.setTitle("Categories");
  }
}