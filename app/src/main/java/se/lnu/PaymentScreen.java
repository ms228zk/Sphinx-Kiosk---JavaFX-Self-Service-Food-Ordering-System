package se.lnu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PaymentScreen {

    private static final String ACCENT = "#FF9800";
    private static final String ACCENT_LIGHT = "#FFF7EC";
    private static final String GREEN = "#4CAF50";
    private static final String GREEN_DARK = "#3E9142";
    private static final String TEXT_DARK = "#171717";
    private static final String TEXT_MUTED = "#6B7280";
    private static final String BORDER = "#E5E7EB";

    public static void show(Stage stage) {

        RadioButton cashButton = new RadioButton("Cash");
        RadioButton cardButton = new RadioButton("Credit/Debit Card");

        ToggleGroup paymentGroup = new ToggleGroup();
        cashButton.setToggleGroup(paymentGroup);
        cardButton.setToggleGroup(paymentGroup);

        Label title = new Label("Select Payment Method");
        title.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Button closeButton = new Button("×");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #555555;" +
                        "-fx-font-size: 24px;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> CategoryScreen.show(stage));

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);

        HBox titleRow = new HBox(title, titleSpacer, closeButton);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label paymentText = new Label("Payment Status");
        paymentText.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        Label readyText = new Label("Ready");
        readyText.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + GREEN + ";"
        );

        Region amountSpacer = new Region();
        HBox.setHgrow(amountSpacer, Priority.ALWAYS);

        HBox statusBox = new HBox(paymentText, amountSpacer, readyText);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPadding(new Insets(22, 20, 22, 20));
        statusBox.setStyle(
                "-fx-background-color: #F9FAFB;" +
                        "-fx-background-radius: 14;"
        );

        HBox cashOption = createPaymentOption(createCashIcon(), "Cash");
        HBox cardOption = createPaymentOption(createCardIcon(), "Credit/Debit Card");

        cashOption.setOnMouseClicked(e -> {
            cashButton.setSelected(true);
            App.selectedPaymentMethod = "Cash";
            updateOptionStyles(cashOption, cardOption, "cash");
        });

        cardOption.setOnMouseClicked(e -> {
            cardButton.setSelected(true);
            App.selectedPaymentMethod = "Credit/Debit Card";
            updateOptionStyles(cashOption, cardOption, "card");
        });

        // Re-select the previous payment method if the user comes back from Edit Order.
        if (App.selectedPaymentMethod.equalsIgnoreCase("Cash")) {
            cashButton.setSelected(true);
            updateOptionStyles(cashOption, cardOption, "cash");
        } else if (App.selectedPaymentMethod.equalsIgnoreCase("Credit/Debit Card")) {
            cardButton.setSelected(true);
            updateOptionStyles(cashOption, cardOption, "card");
        }

        Label errorLabel = new Label("");
        errorLabel.setStyle(
                "-fx-text-fill: #D32F2F;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(130, 50);
        cancelButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + BORDER + ";" +
                        "-fx-border-width: 1.5;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-cursor: hand;"
        );
        cancelButton.setOnAction(e -> CategoryScreen.show(stage));

        Button completeButton = new Button("Continue");
        completeButton.setPrefSize(190, 50);
        setCompleteButtonStyle(completeButton, false);

        completeButton.setOnMouseEntered(e -> setCompleteButtonStyle(completeButton, true));
        completeButton.setOnMouseExited(e -> setCompleteButtonStyle(completeButton, false));

        completeButton.setOnAction(e -> {
            if (paymentGroup.getSelectedToggle() == null) {
                errorLabel.setText("Please select a payment method before continuing.");
                return;
            }

            String paymentMethod =
                    ((RadioButton) paymentGroup.getSelectedToggle()).getText();

            App.selectedPaymentMethod = paymentMethod;

            Order order = new Order(
                    "TEMP",
                    Cart.getInstance().getItems(),
                    paymentMethod
            );

            new OrderConfirmationScreen().start(stage, order);
        });

        Region buttonSpacer = new Region();
        HBox.setHgrow(buttonSpacer, Priority.ALWAYS);

        HBox buttonRow = new HBox(12, buttonSpacer, cancelButton, completeButton);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox paymentModal = new VBox(18);
        paymentModal.setMaxWidth(560);
        paymentModal.setPadding(new Insets(30));
        paymentModal.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: rgba(0,0,0,0.08);" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 30, 0, 0, 8);"
        );

        paymentModal.getChildren().addAll(
                titleRow,
                statusBox,
                cashOption,
                cardOption,
                errorLabel,
                buttonRow
        );

        Button backButton = ScreenStyle.createBackButton();
        backButton.setOnAction(e -> CategoryScreen.show(stage));

        Button homeButton = ScreenStyle.createHomeButton(stage);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, spacer, homeButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        StackPane centerPane = new StackPane(paymentModal);
        centerPane.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(ScreenStyle.createBackground());
        root.setTop(topBar);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, WindowManager.WINDOW_WIDTH, WindowManager.WINDOW_HEIGHT);

        stage.setTitle("Payment");
        stage.setScene(scene);
        WindowManager.enforceStandardSize(stage);
    }

    private static HBox createPaymentOption(StackPane icon, String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1F2937;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label check = new Label("");
        check.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + ACCENT + ";"
        );

        HBox option = new HBox(16, icon, label, spacer, check);
        option.setAlignment(Pos.CENTER_LEFT);
        option.setPadding(new Insets(18, 20, 18, 20));
        option.setPrefHeight(72);
        option.setStyle(getOptionStyle(false));
        return option;
    }

    private static void updateOptionStyles(
            HBox cashOption,
            HBox cardOption,
            String selected
    ) {
        cashOption.setStyle(getOptionStyle(selected.equals("cash")));
        cardOption.setStyle(getOptionStyle(selected.equals("card")));

        setCheckMark(cashOption, selected.equals("cash"));
        setCheckMark(cardOption, selected.equals("card"));
    }

    private static void setCheckMark(HBox option, boolean selected) {
        Label check = (Label) option.getChildren().get(option.getChildren().size() - 1);
        check.setText(selected ? "✓" : "");
    }

    private static String getOptionStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: " + ACCENT_LIGHT + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-radius: 12;" +
                    "-fx-border-color: " + ACCENT + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-cursor: hand;";
        }

        return "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: " + BORDER + ";" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;";
    }

    private static StackPane createCashIcon() {
        Rectangle note = new Rectangle(30, 20);
        note.setArcWidth(4);
        note.setArcHeight(4);
        note.setFill(Color.TRANSPARENT);
        note.setStroke(Color.web(TEXT_MUTED));
        note.setStrokeWidth(2);

        Circle coin = new Circle(3);
        coin.setFill(Color.web(TEXT_MUTED));

        StackPane icon = new StackPane(note, coin);
        icon.setPrefSize(30, 30);
        return icon;
    }

    private static StackPane createCardIcon() {
        Rectangle card = new Rectangle(31, 22);
        card.setArcWidth(4);
        card.setArcHeight(4);
        card.setFill(Color.TRANSPARENT);
        card.setStroke(Color.web(TEXT_MUTED));
        card.setStrokeWidth(2);

        Rectangle line = new Rectangle(26, 3);
        line.setFill(Color.web(TEXT_MUTED));
        StackPane.setAlignment(line, Pos.TOP_CENTER);
        StackPane.setMargin(line, new Insets(6, 0, 0, 0));

        StackPane icon = new StackPane(card, line);
        icon.setPrefSize(30, 30);
        return icon;
    }

    private static void setCompleteButtonStyle(Button button, boolean hover) {
        if (hover) {
            button.setStyle(
                    "-fx-background-color: " + GREEN_DARK + ";" +
                            "-fx-background-radius: 12;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: " + GREEN + ";" +
                            "-fx-background-radius: 12;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-cursor: hand;"
            );
        }
    }
}