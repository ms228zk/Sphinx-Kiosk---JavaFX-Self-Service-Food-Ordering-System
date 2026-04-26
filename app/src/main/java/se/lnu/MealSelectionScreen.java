package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MealSelectionScreen {

  public static void show(Stage stage, MenuItem item) {
    App.selectedMenuItem = item;
    App.selectedQuantity = 1;

    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 10;"
    );
    backButton.setOnAction(e -> ItemListScreen.show(stage, App.selectedCategory));

    Label title = new Label(item.getName());
    title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

    Label description = new Label(item.getDescription());
    description.setStyle("-fx-font-size: 16px; -fx-text-fill: #666666;");

    Label price = new Label("Price: $" + String.format("%.2f", item.getPrice()));
    price.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

    Label quantityLabel = new Label(String.valueOf(App.selectedQuantity));
    quantityLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

    Label totalLabel = new Label(buildTotalText(item.getPrice(), App.selectedQuantity));
    totalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #333333;");

    Button minusButton = createQuantityButton("-");
    Button plusButton = createQuantityButton("+");

    minusButton.setOnAction(e -> {
      if (App.selectedQuantity > 1) {
        App.selectedQuantity--;
        quantityLabel.setText(String.valueOf(App.selectedQuantity));
        totalLabel.setText(buildTotalText(item.getPrice(), App.selectedQuantity));
      }
    });

    plusButton.setOnAction(e -> {
      App.selectedQuantity++;
      quantityLabel.setText(String.valueOf(App.selectedQuantity));
      totalLabel.setText(buildTotalText(item.getPrice(), App.selectedQuantity));
    });

    HBox quantityControls = new HBox(15, minusButton, quantityLabel, plusButton);
    quantityControls.setAlignment(Pos.CENTER);

    Label statusLabel = new Label("Meal ready to be added to the order");
    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #2e7d32;");

    Button confirmButton = new Button("Confirm Selection");
    confirmButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 25;" +
                    "-fx-background-radius: 14;"
    );
    confirmButton.setOnAction(e -> statusLabel.setText(
            "Selected " + App.selectedQuantity + " x " + item.getName() + ". Ready for add-to-order."
    ));

    VBox centerContent = new VBox(18, title, description, price, quantityControls, totalLabel, confirmButton, statusLabel);
    centerContent.setAlignment(Pos.CENTER);

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(25));
    root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff7e6, #ffffff);");
    root.setTop(backButton);
    root.setCenter(centerContent);

    Scene scene = new Scene(root, 700, 500);
    stage.setTitle("Select Meal");
    stage.setScene(scene);
    stage.show();
  }

  private static Button createQuantityButton(String text) {
    Button button = new Button(text);
    button.setPrefWidth(55);
    button.setPrefHeight(55);
    button.setStyle(
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 14;"
    );
    return button;
  }

  private static String buildTotalText(double price, int quantity) {
    return "Selected amount: " + quantity + " | Total: $" + String.format("%.2f", price * quantity);
  }
}
