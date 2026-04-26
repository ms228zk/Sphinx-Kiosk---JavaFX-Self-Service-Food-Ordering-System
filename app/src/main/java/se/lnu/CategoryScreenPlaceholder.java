package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryScreenPlaceholder {

  public static void show(Stage stage) {
    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 10;"
    );
    backButton.setOnAction(e -> OrderTypeScreen.show(stage));

    Label title = new Label("Choose a category");
    title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

    Label subtitle = new Label("Select a category to browse meals");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    Button burgersButton = createCategoryButton("Burgers");
    burgersButton.setOnAction(e -> ItemListScreen.show(stage, "Burgers"));

    Button drinksButton = createCategoryButton("Drinks");
    drinksButton.setOnAction(e -> ItemListScreen.show(stage, "Drinks"));

    VBox centerContent = new VBox(20, title, subtitle, burgersButton, drinksButton);
    centerContent.setAlignment(Pos.CENTER);

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(25));
    root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff7e6, #ffffff);");
    root.setTop(backButton);
    root.setCenter(centerContent);

    Scene scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Categories");
    stage.show();
  }

  private static Button createCategoryButton(String text) {
    Button button = new Button(text);
    button.setPrefWidth(260);
    button.setPrefHeight(60);
    button.setStyle(
            "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 20;"
    );
    return button;
  }
}
