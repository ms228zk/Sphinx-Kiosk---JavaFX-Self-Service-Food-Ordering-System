package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    Label icon = new Label(iconText);
    icon.setStyle(
            "-fx-font-size: 26px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #ff6d00;" +
                    "-fx-background-color: white;" +
                    "-fx-min-width: 82px;" +
                    "-fx-min-height: 82px;" +
                    "-fx-alignment: center;" +
                    "-fx-background-radius: 41px;" +
                    "-fx-border-color: rgba(255,152,0,0.58);" +
                    "-fx-border-width: 1.7;" +
                    "-fx-border-radius: 41px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 13, 0, 0, 3);"
    );

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
}