package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public class OrderTypeScreen {

  public static void show(Stage stage) {
    Button backButton = ScreenStyle.createBackButton();
    backButton.setOnAction(e -> WelcomeScreen.show(stage));

    Button homeButton = ScreenStyle.createHomeButton(stage);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(12, backButton, spacer, homeButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label badge = new Label("CHOOSE YOUR ORDER TYPE");
    badge.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-color: rgba(255, 193, 7, 0.28);" +
                    "-fx-padding: 7 17;" +
                    "-fx-background-radius: 20;"
    );

    Label title = new Label("How would you like to order?");
    title.setStyle(
            "-fx-font-size: 38px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Button eatInCard = createOrderTypeCard(
            "IN",
            "Eat-in",
            "Enjoy here"
    );

    Button takeawayCard = createOrderTypeCard(
            "GO",
            "Takeaway",
            "Take it to go"
    );

    eatInCard.setOnAction(e -> {
      App.orderType = "Eat-in";
      CategoryScreen.show(stage);
    });

    takeawayCard.setOnAction(e -> {
      App.orderType = "Takeaway";
      CategoryScreen.show(stage);
    });

    HBox optionCards = new HBox(34, eatInCard, takeawayCard);
    optionCards.setAlignment(Pos.CENTER);

    VBox card = new VBox(
            34,
            badge,
            title,
            optionCards
    );

    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(42, 60, 42, 60));
    card.setMaxWidth(860);
    card.setStyle(ScreenStyle.createCardStyle());

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(card);

    Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);

    stage.setTitle("Choose Order Type");
    stage.setScene(scene);
    WindowManager.enforceStandardSize(stage);
  }

  private static Button createOrderTypeCard(
          String iconText,
          String titleText,
          String descriptionText
  ) {
    Label accentBar = new Label("");
    accentBar.setMinHeight(10);
    accentBar.setMaxWidth(Double.MAX_VALUE);
    accentBar.setStyle(
            "-fx-background-color: linear-gradient(to right, #ffb300, #ff6d00);" +
                    "-fx-background-radius: 28 28 0 0;"
    );

    Node icon = createIcon(iconText);

    Label title = new Label(titleText);
    title.setStyle(
            "-fx-font-size: 34px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label description = new Label(descriptionText);
    description.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-text-fill: #666666;"
    );

    VBox content = new VBox(24, accentBar, icon, title, description);
    content.setAlignment(Pos.CENTER);
    content.setMouseTransparent(true);

    Button cardButton = new Button();
    cardButton.setGraphic(content);
    cardButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    cardButton.setPrefWidth(285);
    cardButton.setPrefHeight(270);

    String normalStyle =
            "-fx-background-color: linear-gradient(to bottom right, #ffffff, #fff5df);" +
                    "-fx-background-radius: 30;" +
                    "-fx-border-color: rgba(255,152,0,0.36);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 30;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 18, 0, 0, 5);";

    String hoverStyle =
            "-fx-background-color: linear-gradient(to bottom right, #fff1cf, #ffffff);" +
                    "-fx-background-radius: 30;" +
                    "-fx-border-color: rgba(255,109,0,0.78);" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 30;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 0;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.34), 24, 0, 0, 8);";

    cardButton.setStyle(normalStyle);
    cardButton.setOnMouseEntered(e -> cardButton.setStyle(hoverStyle));
    cardButton.setOnMouseExited(e -> cardButton.setStyle(normalStyle));

    return cardButton;
  }

  private static Node createIcon(String iconText) {
    SVGPath icon = new SVGPath();

    if ("IN".equals(iconText)) {
      icon.setContent("M73.937,414.184c-12.552,12.551-12.552,32.905,0,45.457c12.544,12.545,32.897,12.545,45.441,0L241.91,337.112 " +
              "L199.019,289.1L73.937,414.184z " +

              "M499.383,118.316c-6.538-6.538-17.134-6.538-23.672,0l-79.764,79.756c-1.802,1.802-4.734,1.802-6.538,0l-11.248-11.249 " +
              "c-0.869-0.869-1.352-2.045-1.352-3.269c0-1.223,0.483-2.399,1.352-3.268l79.169-79.161c3.293-3.293,5.145-7.77,5.145-12.431 " +
              "c0-4.661-1.852-9.138-5.145-12.431c-3.317-3.317-7.786-5.169-12.439-5.169c-4.653,0-9.122,1.852-12.415,5.145L353.3,155.423 " +
              "c-0.869,0.871-2.045,1.354-3.269,1.354c-1.225,0-2.399-0.483-3.27-1.354l-11.247-11.247c-1.804-1.804-1.804-4.734,0-6.538 " +
              "l79.756-79.764c6.538-6.538,6.538-17.132,0-23.67c-3.156-3.165-7.407-4.927-11.844-4.927c-4.436,0-8.687,1.762-11.818,4.902 " +
              "l-86.455,86.456c-21.612,21.605-27.316,52.915-17.544,79.871l-13.933,13.934l45.454,45.457l13.935-13.935 " +
              "c26.96,9.776,58.271,4.073,79.88-17.536l86.439-86.439C505.921,135.448,505.921,124.852,499.383,118.316z " +

              "M78.364,54.098c-7.52-7.519-17.712-11.739-28.34-11.739c-10.627,0-20.821,4.22-28.34,11.739 " +
              "c-14.259,14.26-15.7,36.883-3.382,52.841l106.197,137.459c8.421,10.911,21.135,17.665,34.887,18.542 " +
              "c1.022,0.066,2.036,0.097,3.059,0.097c12.673,0,24.887-5.024,33.905-14.049l174.253,195.057 " +
              "c6.337,7.085,15.305,11.257,24.806,11.523c0.323,0.008,0.645,0.016,0.966,0.016c9.155,0,17.955-3.632,24.452-10.129 " +
              "c13.503-13.503,13.503-35.386,0-48.887L78.364,54.098z");
      icon.setScaleX(0.08);
      icon.setScaleY(0.08);
    } else {
      icon.setContent("M19.91,19.85a2,2,0,0,1-.52,1.51,2,2,0,0,1-1.47.64H6.08a2,2,0,0,1-1.47-.64,2,2,0,0,1-.52-1.51l.84-11A2,2,0,0,1,6.93,7H17.07a2,2,0,0,1,2,1.85ZM12,2A4,4,0,0,0,8,6h2a2,2,0,0,1,4,0h2A4,4,0,0,0,12,2Z");
      icon.setScaleX(1.8);
      icon.setScaleY(1.8);
    }

    icon.setFill(Color.ORANGE);

    StackPane circle = new StackPane(icon);

    circle.setPrefSize(82, 82);
    circle.setMinSize(82, 82);
    circle.setMaxSize(82, 82);

    circle.setStyle(
            "-fx-background-color: white;" +
                    "-fx-background-radius: 41;" +
                    "-fx-border-color: rgba(255,152,0,0.58);" +
                    "-fx-border-width: 1.7;" +
                    "-fx-border-radius: 41;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 13, 0, 0, 3);"
    );

    return circle;
  }
}