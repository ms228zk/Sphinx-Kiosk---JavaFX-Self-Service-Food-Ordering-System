package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeScreen {

  public static void show(Stage stage) {
    Label badge = new Label("FAST • FRESH • EASY");
    badge.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-color: rgba(255, 193, 7, 0.30);" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 22;"
    );

    Label title = new Label("Welcome to\nKiosk");
    title.setWrapText(true);
    title.setStyle(
            "-fx-font-size: 58px;" +
                    "-fx-font-weight: 900;" +
                    "-fx-text-fill: #111111;" +
                    "-fx-line-spacing: -8;"
    );

    Label subtitle = new Label("Order your favorites in seconds.");
    subtitle.setWrapText(true);
    subtitle.setMaxWidth(430);
    subtitle.setStyle(
            "-fx-font-size: 21px;" +
                    "-fx-text-fill: #5f5f5f;" +
                    "-fx-line-spacing: 4;"
    );

    Button startButton = createMainButton("Start Order");
    startButton.setOnAction(e -> OrderTypeScreen.show(stage));

    // Hidden admin access button
    Button adminButton = new Button("⚙");
    adminButton.setStyle(
            "-fx-background-color: transparent;" +
                    "-fx-text-fill: rgba(0,0,0,0.15);" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;"
    );

    adminButton.setOnAction(e -> {
      se.lnu.admin.AdminLoginScreen.show(stage);
    });

    VBox leftContent = new VBox(24, badge, title, subtitle, startButton, adminButton);
    leftContent.setAlignment(Pos.CENTER_LEFT);
    leftContent.setMaxWidth(500);

    StackPane heroVisual = createHeroVisual();

    HBox heroLayout = new HBox(72, leftContent, heroVisual);
    heroLayout.setAlignment(Pos.CENTER);
    heroLayout.setPadding(new Insets(56, 72, 56, 72));

    StackPane mainCard = new StackPane(heroLayout);
    mainCard.setMaxWidth(1080);
    mainCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.96);" +
                    "-fx-background-radius: 38;" +
                    "-fx-border-color: rgba(255,152,0,0.30);" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 38;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 30, 0, 0, 8);"
    );

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(42));
    root.setBackground(ScreenStyle.createBackground());
    root.setCenter(mainCard);

    Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);

    stage.setScene(scene);
    stage.setTitle("Kiosk");
    WindowManager.enforceStandardSize(stage);
  }

  private static StackPane createHeroVisual() {
    StackPane visual = new StackPane();
    visual.setMinWidth(360);
    visual.setMinHeight(390);
    visual.setMaxWidth(390);
    visual.setMaxHeight(420);

    Region glow = new Region();
    glow.setPrefSize(355, 355);
    glow.setStyle(
            "-fx-background-color: radial-gradient(center 50% 50%, radius 63%, rgba(255,179,0,0.50), rgba(255,109,0,0.18), transparent);" +
                    "-fx-background-radius: 200;"
    );

    VBox foodCard = new VBox(18);
    foodCard.setAlignment(Pos.CENTER);
    foodCard.setPadding(new Insets(34, 32, 34, 32));
    foodCard.setMaxWidth(320);
    foodCard.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #fff4cf, #ffffff);" +
                    "-fx-background-radius: 36;" +
                    "-fx-border-color: rgba(255,152,0,0.45);" +
                    "-fx-border-width: 1.6;" +
                    "-fx-border-radius: 36;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 28, 0, 0, 9);"
    );

    VBox burgerIcon = createBurgerIcon();

    Label foodTitle = new Label("Today’s Favorites");
    foodTitle.setStyle(
            "-fx-font-size: 25px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #111111;"
    );

    Label promo = new Label("Freshly made for every order");
    promo.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: linear-gradient(to right, #ff3d00, #ff8f00);" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-radius: 22;"
    );

    HBox miniTags = new HBox(10);
    miniTags.setAlignment(Pos.CENTER);
    miniTags.getChildren().addAll(
            createSmallTag("HOT"),
            createSmallTag("FRESH"),
            createSmallTag("NEW")
    );

    foodCard.getChildren().addAll(burgerIcon, foodTitle, promo, miniTags);

    visual.getChildren().addAll(glow, foodCard);

    return visual;
  }

  private static VBox createBurgerIcon() {
    ImageView imageView = new ImageView(
            new Image(WelcomeScreen.class.getResource("/icons/burger.png").toExternalForm())
    );

    imageView.setFitWidth(200);
    imageView.setFitHeight(200);
    imageView.setPreserveRatio(true);

    VBox box = new VBox(imageView);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(3));
    return box;
  }

  private static Label createSmallTag(String text) {
    Label tag = new Label(text);

    tag.setStyle(
            "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-color: rgba(255,193,7,0.26);" +
                    "-fx-padding: 6 12;" +
                    "-fx-background-radius: 16;"
    );

    return tag;
  }

  private static Button createMainButton(String text) {
    Button button = new Button(text);

    button.setPrefWidth(320);
    button.setPrefHeight(68);

    String normalStyle =
            "-fx-font-size: 23px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 24;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.44), 17, 0, 0, 5);";

    String hoverStyle =
            "-fx-font-size: 23px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 24;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.48), 19, 0, 0, 6);";

    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));

    return button;
  }
}