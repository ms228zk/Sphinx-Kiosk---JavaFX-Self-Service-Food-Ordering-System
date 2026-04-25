package se.lnu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

  public static void show(Stage stage) {

    // Title
    Label title = new Label("Welcome to Kiosk");
    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

    // Subtitle
    Label subtitle = new Label("Tap below to start your order");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    // Button
    Button startButton = new Button("Start Order");
    startButton.setStyle(
      "-fx-font-size: 18px;" +
        "-fx-background-color: #4CAF50;" +
        "-fx-text-fill: white;" +
        "-fx-padding: 12 25;" +
        "-fx-background-radius: 10;"
    );

    // TEMP navigation
    startButton.setOnAction(e -> {

      Label nextText = new Label("Next Screen");
      nextText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

      VBox layout = new VBox(20);
      layout.setAlignment(Pos.CENTER);
      layout.setStyle("-fx-background-color: #f5f5f5;");
      layout.getChildren().add(nextText);

      Scene nextScene = new Scene(layout, 600, 400);

      stage.setScene(nextScene);
    });

    // Layout
    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.setStyle("-fx-background-color: #f5f5f5;");
    layout.getChildren().addAll(title, subtitle, startButton);

    Scene scene = new Scene(layout, 600, 400);

    stage.setTitle("Kiosk");
    stage.setScene(scene);
    stage.show();
  }
}