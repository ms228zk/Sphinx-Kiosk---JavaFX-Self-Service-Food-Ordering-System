package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Title
        Label title = new Label("Welcome to Kiosk");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        // Subtitle
        Label subtitle = new Label("Tap below to start your order");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

        // Button
        Button startButton = new Button("Start Order");
        startButton.setStyle(
          "-fx-font-size: 20px;" +
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 10;"
        );

        // Button action (simple for now)
        startButton.setOnAction(e -> {

            VBox nextLayout = new VBox(20);
            nextLayout.setStyle("-fx-alignment: center;");

            Label nextLabel = new Label("Next Screen");
            nextLabel.setStyle("-fx-font-size: 24px;");

            nextLayout.getChildren().add(nextLabel);

            Scene nextScene = new Scene(nextLayout, 600, 400);

            primaryStage.setScene(nextScene);
        });

        // Layout
        VBox layout = new VBox(30);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #f5f5f5;");
        layout.getChildren().addAll(title, subtitle, startButton);

        // Scene
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setTitle("Kiosk");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}