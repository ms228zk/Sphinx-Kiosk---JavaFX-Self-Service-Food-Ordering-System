package se.lnu;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ScreenStyle {

  public static Background createBackground() {
    BackgroundFill fallbackFill = new BackgroundFill(Color.web("#f5f5f5"), CornerRadii.EMPTY, Insets.EMPTY);
    BackgroundImage image = new BackgroundImage(
            new javafx.scene.image.Image(ScreenStyle.class.getResource("/images/kiosk-background.png").toExternalForm()),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(
                    100, 100, true, true, true, false
            )
    );

    return new Background(new BackgroundFill[]{fallbackFill}, new BackgroundImage[]{image});
  }

  public static Button createHomeButton(Stage stage) {
    Button homeButton = new Button("Home");
    homeButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #eeeeee;" +
                    "-fx-text-fill: #333333;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
    );
    homeButton.setOnAction(e -> {
      App.resetOrder();
      WelcomeScreen.show(stage);
    });
    return homeButton;
  }
}
