package se.lnu.admin;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.lnu.ScreenStyle;
import se.lnu.WindowManager;
import se.lnu.database.DatabaseHelper;

public class AdminOrdersScreen {

  private static VBox ordersBox;
  private static Label statusLabel;

  public static void show(Stage stage) {
    Button backButton = ScreenStyle.createBackButton();
    backButton.setOnAction(e -> AdminDashboardScreen.show(stage));

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button refreshButton = createSecondaryButton("Refresh");
    refreshButton.setOnAction(e -> loadPendingOrders());

    HBox topBar = new HBox(12, backButton, spacer, refreshButton);
    topBar.setAlignment(Pos.CENTER_LEFT);

    Label badge = new Label("ADMIN MODE");
    badge.setStyle(
            "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #c62828;" +
                    "-fx-background-color: rgba(255, 193, 7, 0.30);" +
                    "-fx-padding: 8 18;" +
                    "-fx-background-radius: 22;"
    );

    Label title = new Label("Incoming Orders");
    title.setStyle(
            "-fx-font-size: 42px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label subtitle = new Label("View customer orders and mark them as completed.");
    subtitle.setStyle(
            "-fx-font-size: 17px;" +
                    "-fx-text-fill: #666666;"
    );

    statusLabel = new Label("");
    statusLabel.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #6d4c41;"
    );

    ordersBox = new VBox(18);
    ordersBox.setAlignment(Pos.TOP_CENTER);
    ordersBox.setPadding(new Insets(10));

    ScrollPane scrollPane = new ScrollPane(ordersBox);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setPrefHeight(460);
    scrollPane.setStyle(
            "-fx-background: transparent;" +
                    "-fx-background-color: transparent;"
    );

    VBox card = new VBox(18, badge, title, subtitle, statusLabel, scrollPane);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(34, 48, 34, 48));
    card.setMaxWidth(760);
    card.setStyle(ScreenStyle.createCardStyle());

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setBackground(ScreenStyle.createBackground());
    root.setTop(topBar);
    root.setCenter(card);

    Scene scene = new Scene(
            root,
            WindowManager.WINDOW_WIDTH,
            WindowManager.WINDOW_HEIGHT
    );

    stage.setTitle("Admin - Incoming Orders");
    stage.setScene(scene);
    WindowManager.enforceStandardSize(stage);

    loadPendingOrders();
  }

  private static void loadPendingOrders() {
    ordersBox.getChildren().clear();

    List<AdminOrder> orders = DatabaseHelper.getPendingOrders();

    if (orders.isEmpty()) {
      statusLabel.setText("No pending orders right now.");

      Label emptyLabel = new Label("All customer orders are completed.");
      emptyLabel.setStyle(
              "-fx-font-size: 18px;" +
                      "-fx-text-fill: #777777;"
      );

      ordersBox.getChildren().add(emptyLabel);
      return;
    }

    statusLabel.setText(orders.size() + " pending order(s).");

    for (AdminOrder order : orders) {
      ordersBox.getChildren().add(createOrderCard(order));
    }
  }

  private static VBox createOrderCard(AdminOrder order) {
    Label orderNumberLabel = new Label("Order #" + order.getFormattedOrderNumber());
    orderNumberLabel.setStyle(
            "-fx-font-size: 26px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1f1f1f;"
    );

    Label dateLabel = new Label("Date: " + order.getDate());
    dateLabel.setStyle(
            "-fx-font-size: 15px;" +
                    "-fx-text-fill: #666666;"
    );

    Label status = new Label(order.getStatus());
    status.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: #ff9800;" +
                    "-fx-background-radius: 16;" +
                    "-fx-padding: 6 16;"
    );

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox header = new HBox(10, orderNumberLabel, spacer, status);
    header.setAlignment(Pos.CENTER_LEFT);

    Label itemsTitle = new Label("Items:");
    itemsTitle.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #333333;"
    );

    Label itemsLabel = new Label(order.getItems());
    itemsLabel.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-text-fill: #444444;" +
                    "-fx-line-spacing: 4px;"
    );

    Button completeButton = createPrimaryButton("Mark as Completed");
    completeButton.setOnAction(e -> completeOrder(order));

    VBox orderCard = new VBox(10, header, dateLabel, itemsTitle, itemsLabel, completeButton);
    orderCard.setAlignment(Pos.CENTER_LEFT);
    orderCard.setPadding(new Insets(22));
    orderCard.setMaxWidth(650);
    orderCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.98);" +
                    "-fx-background-radius: 22;" +
                    "-fx-border-color: rgba(255,152,0,0.35);" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 22;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 14, 0, 0, 4);"
    );

    return orderCard;
  }

  private static void completeOrder(AdminOrder order) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Complete Order");
    alert.setHeaderText("Mark order #" + order.getFormattedOrderNumber() + " as completed?");
    alert.setContentText("This will remove it from the pending orders list.");

    ButtonType cancelButton = new ButtonType("Cancel");
    ButtonType completeButton = new ButtonType("Complete");

    alert.getButtonTypes().setAll(cancelButton, completeButton);

    Optional<ButtonType> result = alert.showAndWait();

    if (result.isEmpty() || result.get() != completeButton) {
      return;
    }

    boolean completed = DatabaseHelper.markOrderCompleted(order.getOrderNumber());

    if (completed) {
      loadPendingOrders();
    } else {
      statusLabel.setText("Could not complete order #" + order.getFormattedOrderNumber() + ".");
    }
  }

  private static Button createPrimaryButton(String text) {
    Button button = new Button(text);

    button.setPrefWidth(220);
    button.setPrefHeight(44);

    String normalStyle =
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ffb300, #ff6d00);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 16;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,109,0,0.28), 10, 0, 0, 3);";

    String hoverStyle =
            "-fx-font-size: 15px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: linear-gradient(to bottom, #ff3d00, #c62828);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 16;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.34), 12, 0, 0, 4);";

    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));

    return button;
  }

  private static Button createSecondaryButton(String text) {
    Button button = new Button(text);

    button.setPrefWidth(120);
    button.setPrefHeight(42);

    String normalStyle =
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,255,255,0.92);" +
                    "-fx-text-fill: #333333;" +
                    "-fx-background-radius: 16;" +
                    "-fx-border-color: rgba(255,152,0,0.45);" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 16;" +
                    "-fx-cursor: hand;";

    String hoverStyle =
            "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-color: rgba(255,152,0,0.92);" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 16;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 1.2;" +
                    "-fx-border-radius: 16;" +
                    "-fx-cursor: hand;";

    button.setStyle(normalStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(normalStyle));

    return button;
  }
}