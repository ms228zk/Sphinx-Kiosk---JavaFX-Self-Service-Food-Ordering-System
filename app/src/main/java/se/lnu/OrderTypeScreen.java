package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrderTypeScreen {

  public static void show(Stage stage) {

    Label title = new Label("How would you like to order?");
    title.setStyle(
            "-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #2c2c2c;"
    );

    Label subtitle = new Label("Please choose one option");
    subtitle.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-text-fill: #666666;"
    );

    Button eatInBtn = new Button("Eat-in");
    Button takeawayBtn = new Button("Takeaway");

    styleButton(eatInBtn);
    styleButton(takeawayBtn);

    eatInBtn.setOnAction(e -> {
      App.orderType = "Eat-in";
      CategoryScreenPlaceholder.show(stage);
    });

    takeawayBtn.setOnAction(e -> {
      App.orderType = "Takeaway";
      CategoryScreenPlaceholder.show(stage);
    });

    VBox layout = new VBox(20);
    layout.setAlignment(Pos.CENTER);
    layout.setPadding(new Insets(40));
    layout.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #fff7e6, #ffffff);"
    );

    layout.getChildren().addAll(title, subtitle, eatInBtn, takeawayBtn);

    Scene scene = new Scene(layout, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Choose Order Type");
  }

  private static void styleButton(Button button) {
    button.setPrefWidth(260);
    button.setPrefHeight(60);

    button.setStyle(
            "-fx-background-color: #ff9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20;" +
                    "-fx-cursor: hand;"
    );

    button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #e68900;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20;" +
                    "-fx-cursor: hand;"
    ));

    button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #ff9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 20px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 20;" +
                    "-fx-cursor: hand;"
    ));
  }
}