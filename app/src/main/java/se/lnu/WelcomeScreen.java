package se.lnu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

  public static void show(Stage stage) {

    Label title = new Label("Welcome to Kiosk");
    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

    Label subtitle = new Label("Tap below to start your order");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    Button startButton = new Button("Start Order");
    startButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 25;" +
                    "-fx-background-radius: 10;"
    );

    // Go to OrderType screen
    startButton.setOnAction(e -> {
      OrderTypeScreen.show(stage);
    });

    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.setBackground(ScreenStyle.createBackground());
    layout.getChildren().addAll(title, subtitle, startButton);

    Scene scene = new Scene(layout, 600, 400);

    stage.setTitle("Kiosk");
    stage.setScene(scene);
    stage.show();
  }
}
