package se.lnu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.database.DatabaseInitializer;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label title = new Label("JavaFX Kiosk");
        Label dbStatus;

        try {
            DatabaseInitializer.initialize();
            dbStatus = new Label("SQLite database initialized successfully.");
        } catch (Exception e) {
            dbStatus = new Label("Error: " + e.getMessage());
        }

        root.getChildren().addAll(title, dbStatus);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sphinx Kiosk");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}