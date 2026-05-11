package se.lnu;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class ScreenStyle {

  public static Background createBackground() {
    BackgroundFill fallbackFill = new BackgroundFill(Color.web("#f5f5f5"), CornerRadii.EMPTY, Insets.EMPTY);

    BackgroundImage image = new BackgroundImage(
            new javafx.scene.image.Image(ScreenStyle.class.getResource("/images/kiosk-background.png").toExternalForm()),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(100, 100, true, true, true, false)
    );

    return new Background(new BackgroundFill[]{fallbackFill}, new BackgroundImage[]{image});
  }

  public static Button createHomeButton(Stage stage) {
    Button homeButton = new Button("🏠");

    String normalStyle =
            "-fx-font-size: 20px;" +
                    "-fx-background-color: rgba(255,255,255,0.82);" +
                    "-fx-text-fill: #444444;" +
                    "-fx-min-width: 56px;" +
                    "-fx-min-height: 56px;" +
                    "-fx-background-radius: 28px;" +
                    "-fx-border-color: rgba(255,255,255,0.65);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 28px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0.2, 0, 3);";

    String hoverStyle =
            "-fx-font-size: 20px;" +
                    "-fx-background-color: rgba(255,255,255,0.96);" +
                    "-fx-text-fill: #222222;" +
                    "-fx-min-width: 56px;" +
                    "-fx-min-height: 56px;" +
                    "-fx-background-radius: 28px;" +
                    "-fx-border-color: rgba(255,255,255,0.85);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 28px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 14, 0.25, 0, 4);";

    homeButton.setStyle(normalStyle);
    homeButton.setOnMouseEntered(e -> homeButton.setStyle(hoverStyle));
    homeButton.setOnMouseExited(e -> homeButton.setStyle(normalStyle));

    homeButton.setOnAction(e -> {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Start Over");
      alert.setHeaderText("Clear current order?");
      alert.setContentText("Your cart will be cleared and you will return to the welcome screen.");

      ButtonType cancelButton = new ButtonType("Cancel");
      ButtonType confirmButton = new ButtonType("Start Over");

      alert.getButtonTypes().setAll(cancelButton, confirmButton);

      Optional<ButtonType> result = alert.showAndWait();

      if (result.isPresent() && result.get() == confirmButton) {
        App.resetOrder();
        WelcomeScreen.show(stage);
      }
    });

    return homeButton;
  }

  public static Button createBackButton() {
    Button backButton = new Button("←");

    String normalStyle =
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,255,255,0.82);" +
                    "-fx-text-fill: #333333;" +
                    "-fx-min-width: 56px;" +
                    "-fx-min-height: 56px;" +
                    "-fx-background-radius: 28px;" +
                    "-fx-border-color: rgba(255,255,255,0.65);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 28px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0.2, 0, 3);";

    String hoverStyle =
            "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,152,0,0.92);" +
                    "-fx-text-fill: white;" +
                    "-fx-min-width: 56px;" +
                    "-fx-min-height: 56px;" +
                    "-fx-background-radius: 28px;" +
                    "-fx-border-color: rgba(255,255,255,0.85);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 28px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 14, 0.25, 0, 4);";

    backButton.setStyle(normalStyle);
    backButton.setOnMouseEntered(e -> backButton.setStyle(hoverStyle));
    backButton.setOnMouseExited(e -> backButton.setStyle(normalStyle));

    return backButton;
  }
}