package com.lude.ecommerce_cart_system.FrontEnd;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.CycleMethod;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.beans.property.SimpleBooleanProperty;

public class login extends Application {
    // Properties for responsive design
    private final SimpleBooleanProperty isNarrow = new SimpleBooleanProperty(false);
    private final double NARROW_WIDTH = 750;

    @Override
    public void start(Stage primaryStage) {
        // Main container with stack pane for responsiveness
        StackPane mainContainer = new StackPane();

        // Create BorderPane for the main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Create left and right panels
        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        // Set the panels to the root
        root.setLeft(leftPanel);
        root.setCenter(rightPanel);

        // Add the root to the main container
        mainContainer.getChildren().add(root);

        // Create the scene
        Scene scene = new Scene(mainContainer, 1000, 650);

        // Set up responsiveness
        setupResponsiveness(scene, leftPanel, root);

        // Set up the stage
        primaryStage.setTitle(" Login Here");
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial animations
        animateElements(leftPanel, rightPanel);
    }

    private void setupResponsiveness(Scene scene, VBox leftPanel, BorderPane root) {
        // Listen for window size changes
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();

            if (width < NARROW_WIDTH && !isNarrow.get()) {
                isNarrow.set(true);
                root.setLeft(null);
            } else if (width >= NARROW_WIDTH && isNarrow.get()) {
                isNarrow.set(false);
                root.setLeft(leftPanel);
            }

            // Adjust padding based on width
            if (width < 600) {
                ((VBox)root.getCenter()).setPadding(new Insets(40, 30, 40, 30));
            } else {
                ((VBox)root.getCenter()).setPadding(new Insets(50, 60, 50, 60));
            }
        });
    }

    private VBox createLeftPanel() {
        VBox panel = new VBox();
        panel.setPrefWidth(400);

        // Create decorative shapes for a modern look
        StackPane decorativePane = new StackPane();
        decorativePane.setPrefHeight(650);

        // Background rectangle with gradient
        Rectangle backgroundRect = new Rectangle(400, 650);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4776E6")),
                new Stop(0.5, Color.web("#8E54E9")),
                new Stop(1, Color.web("#4776E6")));
        backgroundRect.setFill(gradient);

        // Add floating circles for a modern touch
        Circle circle1 = createDecorativeCircle(80, Color.web("#ffffff", 0.1), -30, 100);
        Circle circle2 = createDecorativeCircle(120, Color.web("#ffffff", 0.08), 250, 400);
        Circle circle3 = createDecorativeCircle(60, Color.web("#ffffff", 0.12), 300, 200);

        // Welcome text container
        VBox textContainer = new VBox(15);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setPadding(new Insets(0, 40, 0, 40));

        // Brand text
        Text brandText = new Text("Nep Shop");
        brandText.setFont(Font.font("System", FontWeight.BOLD, 42));
        brandText.setFill(Color.WHITE);

        // Welcome text with shadow effect
        Text welcomeText = new Text("Welcome Back");
        welcomeText.setFont(Font.font("System", FontWeight.BOLD, 32));
        welcomeText.setFill(Color.WHITE);
        welcomeText.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.3)));

        // Subtitle text
        Text subtitleText = new Text("Your shopping journey continues here");
        subtitleText.setFont(Font.font("System", 16));
        subtitleText.setFill(Color.WHITE);

        // Decorative line
        Rectangle decorLine = new Rectangle(80, 4);
        decorLine.setFill(Color.WHITE);
        decorLine.setArcWidth(4);
        decorLine.setArcHeight(4);

        // Add all elements to the text container
        textContainer.getChildren().addAll(
                brandText,
                decorLine,
                welcomeText,
                subtitleText
        );

        // Add a feature highlight
        VBox featureBox = new VBox(10);
        featureBox.setAlignment(Pos.CENTER);
        featureBox.setPadding(new Insets(30, 0, 0, 0));
        Text featureTitle = new Text("");
        featureTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featureTitle.setFill(Color.WHITE);
        Text featureDescription = new Text("Sign in to unlock exclusive discounts");
        featureDescription.setFont(Font.font("System", 14));
        featureDescription.setFill(Color.WHITE);
        featureBox.getChildren().addAll(featureTitle, featureDescription);

        // Add everything to the decorative pane
        decorativePane.getChildren().addAll(
                backgroundRect,
                circle1, circle2, circle3,
                textContainer,
                featureBox
        );

        // Position the text container
        StackPane.setAlignment(textContainer, Pos.CENTER);
        StackPane.setMargin(textContainer, new Insets(0, 0, 100, 0));

        // Position the feature box
        StackPane.setAlignment(featureBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(featureBox, new Insets(0, 0, 50, 0));

        panel.getChildren().add(decorativePane);

        return panel;
    }

    private Circle createDecorativeCircle(double radius, Color color, double translateX, double translateY) {
        Circle circle = new Circle(radius, color);
        circle.setTranslateX(translateX);
        circle.setTranslateY(translateY);
        return circle;
    }

    private VBox createRightPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(50, 60, 50, 60));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        // Header with enhanced styling
        Text headerText = new Text("Sign In");
        headerText.setFont(Font.font("System", FontWeight.BOLD, 38));
        headerText.setFill(Color.web("#333333"));

        // Subheader
        Text subHeaderText = new Text("Please enter your account details");
        subHeaderText.setFont(Font.font("System", 14));
        subHeaderText.setFill(Color.web("#666666"));

        // Spacer
        Region spacer = new Region();
        spacer.setPrefHeight(20);

        // Email field with icon
        HBox emailBox = new HBox(10);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        VBox emailLabelBox = new VBox(8);

        Label emailLabel = new Label("Email Address");
        emailLabel.setTextFill(Color.web("#444444"));
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        TextField emailField = new TextField();
        emailField.setPromptText("yourname@example.com");
        styleTextField(emailField);

        emailLabelBox.getChildren().addAll(emailLabel, emailField);
        emailBox.getChildren().add(emailLabelBox);
        HBox.setHgrow(emailLabelBox, Priority.ALWAYS);

        // Password field with icon
        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        VBox passwordLabelBox = new VBox(8);

        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.web("#444444"));
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        styleTextField(passwordField);

        passwordLabelBox.getChildren().addAll(passwordLabel, passwordField);
        passwordBox.getChildren().add(passwordLabelBox);
        HBox.setHgrow(passwordLabelBox, Priority.ALWAYS);

        // Remember me and forgot password
        HBox optionsBox = new HBox();
        optionsBox.setAlignment(Pos.CENTER_LEFT);
        optionsBox.setSpacing(10);

        CheckBox rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setTextFill(Color.web("#555555"));
        rememberMeCheckBox.setFont(Font.font("System", 14));
        styleCheckBox(rememberMeCheckBox);
        Region optionSpacer = new Region();
        HBox.setHgrow(optionSpacer, Priority.ALWAYS);

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.setTextFill(Color.web("#6C63FF"));
        forgotPasswordLink.setBorder(Border.EMPTY);
        forgotPasswordLink.setFont(Font.font("System", FontWeight.BOLD, 14));

        optionsBox.getChildren().addAll(rememberMeCheckBox, optionSpacer, forgotPasswordLink);

        // Login button
        Button loginButton = new Button("SIGN IN");
        styleLoginButton(loginButton);

        // Sign up link
        HBox signupBox = new HBox(5);
        signupBox.setAlignment(Pos.CENTER);
        signupBox.setPadding(new Insets(30, 0, 0, 0));

        Text signupText = new Text("Don't have an account?");
        signupText.setFill(Color.web("#666666"));
        signupText.setFont(Font.font("System", 14));

        Hyperlink signupLink = new Hyperlink("Sign Up");
        signupLink.setTextFill(Color.web("#6C63FF"));
        signupLink.setBorder(Border.EMPTY);
        signupLink.setFont(Font.font("System", FontWeight.BOLD, 14));

        signupLink.setOnAction(event -> {
            // Close current stage
            Stage currentStage = (Stage) signupLink.getScene().getWindow();
            currentStage.close();

            // Launch register screen
            register registerPage = new register();
            try {
                registerPage.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                // Consider adding user-friendly error message here
            }
        });

        signupBox.getChildren().addAll(signupText, signupLink);

        // Add all components to the panel
        panel.getChildren().addAll(
                headerText,
                subHeaderText,
                spacer,
                emailBox,
                passwordBox,
                optionsBox,
                loginButton,
                signupBox
        );
        VBox.setVgrow(spacer, Priority.ALWAYS);

        return panel;
    }

    private void styleTextField(TextField field) {
        field.setStyle("-fx-background-color: #f7f7f9; -fx-background-radius: 8px; -fx-padding: 12px 15px; -fx-font-size: 14px;");
        field.setEffect(new DropShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.1)));
        field.setPrefHeight(45);
        field.setMaxWidth(Double.MAX_VALUE);

        // Add focus property change listener
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                field.setStyle("-fx-background-color: white; -fx-border-color: #6C63FF; -fx-border-width: 2px; -fx-border-radius: 8px; -fx-padding: 12px 15px; -fx-font-size: 14px;");
            } else {
                field.setStyle("-fx-background-color: #f7f7f9; -fx-background-radius: 8px; -fx-padding: 12px 15px; -fx-font-size: 14px;");
            }
        });
    }

    private void styleCheckBox(CheckBox checkBox) {
        checkBox.setStyle("-fx-text-fill: #555555;");
    }

    private void styleLoginButton(Button button) {
        button.setStyle("-fx-background-color: linear-gradient(to right, #6C63FF, #536DFE); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                "-fx-padding: 14px 20px; -fx-background-radius: 30px; -fx-cursor: hand;");
        button.setEffect(new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.25)));
        button.setPrefHeight(50);
        button.setMaxWidth(Double.MAX_VALUE);

        // Add hover effect
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            button.setStyle("-fx-background-color: linear-gradient(to right, #5953FF, #4254F3); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                    "-fx-padding: 14px 20px; -fx-background-radius: 30px; -fx-cursor: hand;");
            button.setEffect(new DropShadow(10, 0, 6, Color.rgb(0, 0, 0, 0.4)));

            // Add scale effect
            button.setScaleX(1.03);
            button.setScaleY(1.03);
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            button.setStyle("-fx-background-color: linear-gradient(to right, #6C63FF, #536DFE); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                    "-fx-padding: 14px 20px; -fx-background-radius: 30px; -fx-cursor: hand;");
            button.setEffect(new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.25)));

            // Reset scale
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        // Add pressed effect
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            button.setStyle("-fx-background-color: linear-gradient(to right, #5048e5, #3d47d5); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                    "-fx-padding: 14px 20px; -fx-background-radius: 30px; -fx-cursor: hand;");
            button.setEffect(new DropShadow(6, 0, 3, Color.rgb(0, 0, 0, 0.3)));

            button.setScaleX(0.97);
            button.setScaleY(0.97);
        });

        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            button.setStyle("-fx-background-color: linear-gradient(to right, #6C63FF, #536DFE); " +
                    "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; " +
                    "-fx-padding: 14px 20px; -fx-background-radius: 30px; -fx-cursor: hand;");
            button.setEffect(new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.25)));

            // Reset scale
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }

    private void animateElements(VBox leftPanel, VBox rightPanel) {
        // Fade Transition for the Left Panel
        FadeTransition fadeTransitionLeft = new FadeTransition(Duration.millis(1000), leftPanel);
        fadeTransitionLeft.setFromValue(0.0);
        fadeTransitionLeft.setToValue(1.0);
        fadeTransitionLeft.play();

        // Translate Transition for the Right Panel
        TranslateTransition translateTransitionRight = new TranslateTransition(Duration.millis(1000), rightPanel);
        translateTransitionRight.setFromX(50);
        translateTransitionRight.setToX(0);
        translateTransitionRight.play();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
