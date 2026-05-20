package se.lnu;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ScreenStyle {

  private static final String SYMBOL_FONT =
          "-fx-font-family: 'Segoe UI Symbol', 'Arial Unicode MS', 'Arial';";

  public static Background createBackground() {
    LinearGradient baseGradient = new LinearGradient(
            0, 0,
            1, 1,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#fff3df")),
            new Stop(0.35, Color.web("#fffaf2")),
            new Stop(0.70, Color.web("#ffd994")),
            new Stop(1.0, Color.web("#ffb347"))
    );

    RadialGradient topLeftGlow = new RadialGradient(
            0, 0,
            0.04, 0.08,
            0.46,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#ff6d00", 0.42)),
            new Stop(0.42, Color.web("#ff9800", 0.20)),
            new Stop(1.0, Color.web("#ffffff", 0.0))
    );

    RadialGradient topRightGlow = new RadialGradient(
            0, 0,
            0.95, 0.10,
            0.42,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#ffc107", 0.36)),
            new Stop(0.48, Color.web("#ff9800", 0.16)),
            new Stop(1.0, Color.web("#ffffff", 0.0))
    );

    RadialGradient bottomLeftGlow = new RadialGradient(
            0, 0,
            0.08, 0.95,
            0.48,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#c62828", 0.20)),
            new Stop(0.45, Color.web("#ff7043", 0.12)),
            new Stop(1.0, Color.web("#ffffff", 0.0))
    );

    RadialGradient bottomRightGlow = new RadialGradient(
            0, 0,
            0.95, 0.95,
            0.50,
            true,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#ff8f00", 0.26)),
            new Stop(0.45, Color.web("#ffcc80", 0.14)),
            new Stop(1.0, Color.web("#ffffff", 0.0))
    );

    return new Background(
            new BackgroundFill(baseGradient, CornerRadii.EMPTY, Insets.EMPTY),
            new BackgroundFill(topLeftGlow, CornerRadii.EMPTY, Insets.EMPTY),
            new BackgroundFill(topRightGlow, CornerRadii.EMPTY, Insets.EMPTY),
            new BackgroundFill(bottomLeftGlow, CornerRadii.EMPTY, Insets.EMPTY),
            new BackgroundFill(bottomRightGlow, CornerRadii.EMPTY, Insets.EMPTY)
    );
  }

  public static Button createHomeButton(Stage stage) {
    String svgContent;
    try (InputStream is = ScreenStyle.class.getResourceAsStream("/icons/home.svg")) {
      svgContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load SVG icon", e);
    }

    String pathData = extractPathData(svgContent);

    SVGPath homeIcon = new SVGPath();
    homeIcon.setContent(pathData);

    homeIcon.setFill(Color.TRANSPARENT);
    homeIcon.setStroke(Color.web("#222222"));
    homeIcon.setStrokeWidth(2);
    homeIcon.setScaleX(1.2);
    homeIcon.setScaleY(1.2);

    Button homeButton = new Button();
    homeButton.setGraphic(homeIcon);

    String normalStyle = createCircleIconButtonStyle(
            "rgba(255,255,255,0.94)",
            "#222222",
            "rgba(255,152,0,0.75)",
            "rgba(0,0,0,0.20)"
    );

    String hoverStyle = createCircleIconButtonStyle(
            "linear-gradient(to bottom, #ffb300, #ff6d00)",
            "white",
            "white",
            "rgba(0,0,0,0.30)"
    );

    homeButton.setStyle(normalStyle);
    homeButton.setOnMouseEntered(e -> {
      homeButton.setStyle(hoverStyle);
      homeIcon.setStroke(Color.WHITE);
      });
    homeButton.setOnMouseExited(e -> {
      homeButton.setStyle(normalStyle);
      homeIcon.setStroke(Color.web("#222222"));
    });

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
    String svgContent;
    try (InputStream is = ScreenStyle.class.getResourceAsStream("/icons/back.svg")) {
      svgContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load SVG icon", e);
    }

    String pathData = extractPathData(svgContent);

    SVGPath backIcon = new SVGPath();
    backIcon.setContent(pathData);

    backIcon.setFill(Color.TRANSPARENT);
    backIcon.setStroke(Color.WHITE);
    backIcon.setStrokeWidth(1);
    backIcon.setScaleX(1.5);
    backIcon.setScaleY(1.5);

    Button backButton = new Button();
    backButton.setGraphic(backIcon);

    String normalStyle = createCircleIconButtonStyle(
            "linear-gradient(to bottom, #ffb300, #ff6d00)",
            "white",
            "white",
            "rgba(0,0,0,0.24)"
    );

    String hoverStyle = createCircleIconButtonStyle(
            "linear-gradient(to bottom, #ff3d00, #c62828)",
            "white",
            "white",
            "rgba(0,0,0,0.34)"
    );

    backButton.setStyle(normalStyle);
    backButton.setOnMouseEntered(e -> backButton.setStyle(hoverStyle));
    backButton.setOnMouseExited(e -> backButton.setStyle(normalStyle));

    return backButton;
  }

  public static Button createCartButton(Stage stage) {
    String svgContent;
    try (InputStream is = ScreenStyle.class.getResourceAsStream("/icons/cart.svg")) {
      svgContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load SVG icon", e);
    }

    String pathData = extractPathData(svgContent);

    SVGPath cartIcon = new SVGPath();
    cartIcon.setContent(pathData);

    cartIcon.setFill(Color.TRANSPARENT);
    cartIcon.setStroke(Color.WHITE);
    cartIcon.setStrokeWidth(2);
    cartIcon.setScaleX(0.9);
    cartIcon.setScaleY(0.9);

    Button cartButton = new Button(
            " (" + Cart.getInstance().getItemCount() + ")",
            cartIcon);

    cartButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
    );

    cartButton.setContentDisplay(ContentDisplay.LEFT);
    cartButton.setGraphicTextGap(8);

    cartButton.setOnAction(e -> CartScreen.show(stage));
    return cartButton;
  }

  private static String createCircleIconButtonStyle(
          String backgroundColor,
          String textColor,
          String borderColor,
          String shadowColor
  ) {
    return SYMBOL_FONT +
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-min-width: 56px;" +
            "-fx-min-height: 56px;" +
            "-fx-background-radius: 28px;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: 1.8px;" +
            "-fx-border-radius: 28px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, " + shadowColor + ", 14, 0.25, 0, 4);";
  }

  public static String createPrimaryButtonStyle() {
    return "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 15 44;" +
            "-fx-background-radius: 18;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.40), 14, 0, 0, 4);";
  }

  public static String createPrimaryButtonHoverStyle() {
    return "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
            "-fx-text-fill: white;" +
            "-fx-padding: 15 44;" +
            "-fx-background-radius: 18;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.45), 16, 0, 0, 5);";
  }

  public static String createCardStyle() {
    return "-fx-background-color: rgba(255,255,255,0.96);" +
            "-fx-background-radius: 28;" +
            "-fx-border-color: rgba(255,152,0,0.28);" +
            "-fx-border-width: 1.3;" +
            "-fx-border-radius: 28;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 20, 0, 0, 5);";
  }

  public static String extractPathData(String svg) {
    int start = svg.indexOf("d=\"");
    if (start == -1) throw new IllegalArgumentException("No SVG path found");

    start += 3;
    int end = svg.indexOf("\"", start);

    return svg.substring(start, end);
  }
}