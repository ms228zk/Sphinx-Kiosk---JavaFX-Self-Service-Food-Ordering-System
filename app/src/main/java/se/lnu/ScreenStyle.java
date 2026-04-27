package se.lnu;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
}
