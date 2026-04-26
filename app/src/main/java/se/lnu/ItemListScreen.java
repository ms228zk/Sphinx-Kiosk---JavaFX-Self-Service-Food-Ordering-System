package se.lnu;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseHelper;

import java.util.List;

public class ItemListScreen {

    public static void show(Stage stage) {

        Label title = new Label(App.selectedCategoryName);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: #eeeeee;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-padding: 8 18;" +
                        "-fx-background-radius: 10;"
        );
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        // ⭐ Put ONLY the back button in a left-aligned HBox
        HBox backContainer = new HBox(backButton);
        backContainer.setAlignment(Pos.TOP_LEFT);
        backContainer.setPadding(new Insets(10, 0, 0, 10));

        VBox itemsBox = new VBox(10);
        itemsBox.setAlignment(Pos.CENTER);

        List<MenuItem> items = DatabaseHelper.getItemsByCategory(App.selectedCategoryId);

        for (MenuItem item : items) {
            Button itemBtn = new Button(item.getName() + " - " + item.getPrice() + " kr");
            itemBtn.setPrefWidth(260);
            itemBtn.setStyle(
                    "-fx-font-size: 16px;" +
                            "-fx-background-color: #FF9800;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 15;"
            );

            itemBtn.setOnAction(e -> {
                System.out.println("Selected item: " + item.getName());
            });

            itemsBox.getChildren().add(itemBtn);
        }

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);

        // ⭐ Add backContainer instead of backButton
        layout.getChildren().addAll(backContainer, title, itemsBox);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Items");
        stage.show();
    }
}
