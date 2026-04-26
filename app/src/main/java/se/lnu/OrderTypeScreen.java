package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OrderTypeScreen {

  // Displays the Order Type screen
  public static void show(Stage stage) {

    // Back button placed at the top
    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 10;"
    );

    // Navigate back to Welcome screen
    backButton.setOnAction(e -> WelcomeScreen.show(stage));

    // Title label
    Label title = new Label("How would you like to order?");
    title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

    // Subtitle label
    Label subtitle = new Label("Please choose one option");
    subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    // Buttons for order options
    Button eatInButton = createMainButton("Eat-in");
    Button takeawayButton = createMainButton("Takeaway");

    // Action for Eat-in
    eatInButton.setOnAction(e -> {
      App.orderType = "Eat-in"; // store selected type
      CategoryScreen.show(stage); // navigate forward
    });

    // Action for Takeaway
    takeawayButton.setOnAction(e -> {
      App.orderType = "Takeaway"; // store selected type
      CategoryScreen.show(stage); // navigate forward
    });

    // Center layout for content
    VBox centerContent = new VBox(25);
    centerContent.setAlignment(Pos.CENTER);
    centerContent.getChildren().addAll(title, subtitle, eatInButton, takeawayButton);

    // Root layout
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(25));
    root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff7e6, #ffffff);");

    // Position elements
    root.setTop(backButton);
    root.setCenter(centerContent);

    // Scene setup
    Scene scene = new Scene(root, 600, 400);

    stage.setTitle("Choose Order Type");
    stage.setScene(scene);
    stage.show();
  }

  // Helper method to create styled buttons
  private static Button createMainButton(String text) {
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