package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MealSelectionScreen {

  public static void show(Stage stage, MenuItem item) {
    App.selectedMenuItem = item;
    App.selectedQuantity = 1;

    Button backButton = new Button("Back");
    backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );
    backButton.setOnAction(e -> ItemListScreen.show(stage));

    Button cartButton = createCartButton(stage);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox topBar = new HBox(backButton, spacer, cartButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label title = new Label(item.getName());
    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

    Label description = new Label(item.getDescription());
    description.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
    Label priceLabel = new Label("Unit price: " + String.format("%.2f kr", item.getPrice()));
    priceLabel.setStyle("-fx-font-size: 18px;");
    Label itemTotalLabel = new Label("Item total: " + String.format("%.2f kr", item.getPrice() * App.selectedQuantity));
    itemTotalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    Button minusButton = new Button("-");
    Button plusButton = new Button("+");
    minusButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 18;" +
                    "-fx-background-radius: 10;"
    );
    plusButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 18;" +
                    "-fx-background-radius: 10;"
    );
    Label quantityLabel = new Label(String.valueOf(App.selectedQuantity));
    quantityLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    Label statusLabel = new Label("");
    statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");

    minusButton.setOnAction(e -> {
      if (App.selectedQuantity > 1) {
        App.selectedQuantity--;
        quantityLabel.setText(String.valueOf(App.selectedQuantity));
        itemTotalLabel.setText("Item total: " + String.format("%.2f kr", item.getPrice() * App.selectedQuantity));
      }
    });

    plusButton.setOnAction(e -> {
      App.selectedQuantity++;
      quantityLabel.setText(String.valueOf(App.selectedQuantity));
      itemTotalLabel.setText("Item total: " + String.format("%.2f kr", item.getPrice() * App.selectedQuantity));
    });

    Button confirmButton = new Button("Add to Order");
    confirmButton.setStyle(
            "-fx-font-size: 18px;" +
                    "-fx-background-color: #FF9800;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 12 25;" +
                    "-fx-background-radius: 10;"
    );
    confirmButton.setOnAction(e -> {
        Cart.getInstance().addItem(item, App.selectedQuantity);
        statusLabel.setText(App.selectedQuantity + " x " + item.getName() + " added to order!");
        cartButton.setText("Cart (" + Cart.getInstance().getItemCount() + ")");
    });

    HBox quantityBox = new HBox(15, minusButton, quantityLabel, plusButton);
    quantityBox.setAlignment(Pos.CENTER);


/**
 * You can delete from this...
*/
    Button viewCartButton = new Button("View Cart");
    viewCartButton.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-background-color: #4CAF50;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 12 25;" +
            "-fx-background-radius: 10;"
    );
    viewCartButton.setOnAction(e -> CartScreen.show(stage));

    VBox centerContent = new VBox(18, title, description, priceLabel, quantityBox, itemTotalLabel, confirmButton, statusLabel, viewCartButton);
    centerContent.setAlignment(Pos.CENTER);
/**
 * to this as it is the view cart button which was a demo I(Battur) created to check whether the Cart class worked or not.
 */



    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(centerContent);

    Scene scene = new Scene(root, 600, 400);
    stage.setScene(scene);
    stage.setTitle("Select Meal");
    stage.show();
  }

  private static Button createCartButton(Stage stage) {
    Button cartButton = new Button("Cart (" + Cart.getInstance().getItemCount() + ")");
    cartButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;"
    );
    cartButton.setOnAction(e -> CartScreen.show(stage));
    return cartButton;
  }
}
