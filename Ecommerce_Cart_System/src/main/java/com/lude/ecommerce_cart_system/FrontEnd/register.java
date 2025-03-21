package com.lude.ecommerce_cart_system.FrontEnd;

import com.lude.ecommerce_cart_system.Middleware.Middleware;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;

public class register extends Application {
    // Define color constants for easy theme adjustments
    private static final Color PRIMARY_COLOR = Color.web("#6C63FF");
    private static final Color SECONDARY_COLOR = Color.web("#536DFE");
    private static final Color DARK_TEXT = Color.web("#333333");
    private static final Color LIGHT_TEXT = Color.web("#666666");
    private static final Color BACKGROUND_COLOR = Color.web("#FFFFFF");
    private static final Color INPUT_BG = Color.web("#F5F5F5");
    private static final String FONT_FAMILY = "System";

    // Track window size for responsiveness
    private double windowWidth;
    private double windowHeight;

    @Override
    public void start(Stage primaryStage) {
        // Get screen dimensions for responsiveness
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Calculate initial window size (80% of screen)
        windowWidth = Math.min(screenWidth * 0.8, 1200);
        windowHeight = Math.min(screenHeight * 0.8, 700);

        // Create responsive layout
        StackPane root = new StackPane();

        // Create main content
        HBox mainContent = new HBox();
        mainContent.setAlignment(Pos.CENTER);

        // Left panel (decorative side)
        VBox leftPanel = createLeftPanel();

        // Right panel (form)
        ScrollPane scrollPane = new ScrollPane();
        VBox rightPanel = createRightPanel();
        scrollPane.setContent(rightPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("edge-to-edge");
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Add panels to main content
        mainContent.getChildren().addAll(leftPanel, scrollPane);

        // Set responsive constraints
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        // Add main content to root
        root.getChildren().add(mainContent);

        // Create the scene
        Scene scene = new Scene(root, windowWidth, windowHeight);

        // Set up responsive behavior
        setupResponsiveLayout(mainContent, leftPanel, rightPanel);

        // Handle window resize for responsiveness
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            windowWidth = newVal.doubleValue();
            adjustForWindowSize(mainContent, leftPanel);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            windowHeight = newVal.doubleValue();
        });

        // Apply styles and animations
        applyGlobalStyles(root);

        // Stage setup
        primaryStage.setTitle("Account Registration");
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(550);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add animations
        animateComponents(mainContent, leftPanel, rightPanel);
    }

    private void setupResponsiveLayout(HBox mainContent, VBox leftPanel, VBox rightPanel) {
        // Set initial sizes
        leftPanel.setPrefWidth(windowWidth * 0.4);
        rightPanel.setPrefWidth(windowWidth * 0.6);

        // Make right panel fill remaining space
        HBox.setHgrow(rightPanel, Priority.ALWAYS);
    }

    private void adjustForWindowSize(HBox mainContent, VBox leftPanel) {
        // Hide left panel on small screens
        if (windowWidth < 800) {
            leftPanel.setVisible(false);
            leftPanel.setManaged(false);
        } else {
            leftPanel.setVisible(true);
            leftPanel.setManaged(true);
            leftPanel.setPrefWidth(windowWidth * 0.4);
        }
    }

    private VBox createLeftPanel() {
        VBox panel = new VBox();
        panel.setPrefWidth(350);

        // Create gradient background with animation
        Rectangle backgroundRect = new Rectangle();
        backgroundRect.widthProperty().bind(panel.widthProperty());
        backgroundRect.heightProperty().bind(panel.heightProperty());
        // Modern gradient with multiple color stops
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6C63FF")),
                new Stop(0.4, Color.web("#7B70FF")),
                new Stop(0.8, Color.web("#8E54E9")),
                new Stop(1, Color.web("#5E72EB")));
        backgroundRect.setFill(gradient);

        // Add subtle pattern overlay
        Rectangle overlayRect = new Rectangle();
        overlayRect.widthProperty().bind(panel.widthProperty());
        overlayRect.heightProperty().bind(panel.heightProperty());
        overlayRect.setFill(Color.rgb(255, 255, 255, 0.03));
        overlayRect.setOpacity(0.1);

        // Welcome text with animated entry
        Text brandText = new Text("Nep Shop");
        brandText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 40));
        brandText.setFill(Color.WHITE);

        Text welcomeText = new Text("Join Our Community");
        welcomeText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 28));
        welcomeText.setFill(Color.WHITE);

        Text subtitleText = new Text("Create an account to get started\nwith your shopping experience");
        subtitleText.setFont(Font.font(FONT_FAMILY, 16));
        subtitleText.setFill(Color.WHITE);
        subtitleText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Add decorative elements
        VBox decorativeIcons = new VBox(20);
        decorativeIcons.setAlignment(Pos.CENTER);
        decorativeIcons.setOpacity(0.8);

        // Here we'd normally add SVG icons, but for this example we'll create placeholder shapes
        HBox iconRow = new HBox(30);
        iconRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < 3; i++) {
            Rectangle iconPlaceholder = new Rectangle(40, 40, Color.WHITE);
            iconPlaceholder.setArcWidth(10);
            iconPlaceholder.setArcHeight(10);
            iconPlaceholder.setOpacity(0.2);
            iconRow.getChildren().add(iconPlaceholder);
        }

        decorativeIcons.getChildren().add(iconRow);

        // Stack everything with proper positioning
        StackPane leftStack = new StackPane();
        leftStack.getChildren().addAll(backgroundRect, overlayRect);

        VBox textContainer = new VBox(20);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setPadding(new Insets(0, 30, 0, 30));
        textContainer.getChildren().addAll(brandText, welcomeText, subtitleText, decorativeIcons);

        leftStack.getChildren().add(textContainer);
        panel.getChildren().add(leftStack);

        return panel;
    }

    private VBox createRightPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(40, 60, 40, 60));
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setBackground(new Background(new BackgroundFill(BACKGROUND_COLOR, null, null)));

        // Create form fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        DatePicker dobPicker = new DatePicker();
        CheckBox termsCheckBox = new CheckBox();

        // Add responsive padding
        panel.paddingProperty().bind(new javafx.beans.binding.ObjectBinding<Insets>() {
            {
                bind(panel.widthProperty());
            }

            @Override
            protected Insets computeValue() {
                double width = panel.getWidth();
                if (width < 500) {
                    return new Insets(40, 20, 40, 20);
                } else {
                    return new Insets(40, 60, 40, 60);
                }
            }
        });

        // Header with enhanced styling
        Text headerText = new Text("Create Account");
        headerText.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 32));
        headerText.setFill(DARK_TEXT);

        // Subheader
        Text subHeaderText = new Text("Please fill in your details to get started");
        subHeaderText.setFont(Font.font(FONT_FAMILY, 14));
        subHeaderText.setFill(LIGHT_TEXT);

        // Form container with more spacing
        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.TOP_LEFT);
        formContainer.setMaxWidth(Double.MAX_VALUE);

        // Name fields in one row
        HBox nameFields = new HBox(15);
        nameFields.setMaxWidth(Double.MAX_VALUE);

        VBox firstNameBox = createFormField("First Name", "Utsab", firstNameField);
        VBox lastNameBox = createFormField("Last Name", "Acharya", lastNameField);

        HBox.setHgrow(firstNameBox, Priority.ALWAYS);
        HBox.setHgrow(lastNameBox, Priority.ALWAYS);

        nameFields.getChildren().addAll(firstNameBox, lastNameBox);

        // Email field
        VBox emailBox = createFormField("Email Address", "utsab.acharya@example.com", emailField);

        // Password fields with strength indicator
        VBox passwordBox = createPasswordField("Password", "Enter your password", passwordField);
        VBox confirmPasswordBox = createPasswordField("Confirm Password", "Confirm your password", confirmPasswordField);

        // Date of birth
        VBox dobBox = createDatePickerField("Date of Birth", "MM/DD/YYYY", dobPicker);

        // Terms and conditions with better styling
        HBox termsBox = new HBox(10);
        termsBox.setAlignment(Pos.CENTER_LEFT);

        termsCheckBox.setStyle("-fx-padding: 5px;");

        VBox termsTextBox = new VBox(3);
        Label termsLabel = new Label("I agree to the Terms and Conditions");
        termsLabel.setTextFill(LIGHT_TEXT);

        Hyperlink termsLink = new Hyperlink("Read Terms and Conditions");
        termsLink.setTextFill(PRIMARY_COLOR);
        termsLink.setBorder(Border.EMPTY);
        termsLink.setPadding(new Insets(0));
        termsLink.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 13));

        termsTextBox.getChildren().addAll(termsLabel, termsLink);
        termsBox.getChildren().addAll(termsCheckBox, termsTextBox);

        // Register button with improved styling
        Button registerButton = createStyledButton("Create Account");

        // Set up the register button action
        setupRegisterButtonAction(registerButton, firstNameField, lastNameField, emailField,
                passwordField, confirmPasswordField, dobPicker, termsCheckBox);

        // Login link with better styling
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20, 0, 0, 0));

        Text loginText = new Text("Already have an account?");
        loginText.setFill(LIGHT_TEXT);
        loginText.setFont(Font.font(FONT_FAMILY, 14));

        Hyperlink loginLink = new Hyperlink("Sign In");
        loginLink.setTextFill(PRIMARY_COLOR);
        loginLink.setBorder(Border.EMPTY);
        loginLink.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 14));

        // Add action to login link
        loginLink.setOnAction(event -> {
            // Close current stage
            Stage currentStage = (Stage) loginLink.getScene().getWindow();
            currentStage.close();

            // Launch login screen
            Main loginPage = new Main();
            try {
                loginPage.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                // Consider adding user-friendly error message here
                System.err.println("Error launching login page: " + e.getMessage());
            }
        });

        loginBox.getChildren().addAll(loginText, loginLink);

        // Add all components to the form container
        formContainer.getChildren().addAll(
                nameFields,
                emailBox,
                passwordBox,
                confirmPasswordBox,
                dobBox,
                termsBox,
                registerButton
        );

        // Add all elements to the panel
        panel.getChildren().addAll(
                headerText,
                subHeaderText,
                formContainer,
                loginBox
        );

        return panel;
    }

    private VBox createFormField(String labelText, String promptText, TextField field) {
        VBox fieldBox = new VBox(8);
        fieldBox.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(labelText);
        label.setTextFill(DARK_TEXT);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 13));

        field.setPromptText(promptText);
        styleTextField(field);

        fieldBox.getChildren().addAll(label, field);
        return fieldBox;
    }

    private VBox createPasswordField(String labelText, String promptText, PasswordField field) {
        VBox fieldBox = new VBox(8);
        fieldBox.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(labelText);
        label.setTextFill(DARK_TEXT);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 13));

        field.setPromptText(promptText);
        styleTextField(field);

        // Add password strength indicator
        ProgressBar strengthIndicator = new ProgressBar(0);
        strengthIndicator.setPrefHeight(5);
        strengthIndicator.setMaxWidth(Double.MAX_VALUE);
        strengthIndicator.setStyle("-fx-accent: #e74c3c;");

        // Simple listener to simulate password strength
        field.textProperty().addListener((obs, old, newValue) -> {
            double strength = Math.min(newValue.length() / 12.0, 1.0);
            strengthIndicator.setProgress(strength);

            if (strength < 0.3) {
                strengthIndicator.setStyle("-fx-accent: #e74c3c;"); // Red for weak
            } else if (strength < 0.7) {
                strengthIndicator.setStyle("-fx-accent: #f39c12;"); // Orange for medium
            } else {
                strengthIndicator.setStyle("-fx-accent: #2ecc71;"); // Green for strong
            }
        });

        Label strengthLabel = new Label("Password Strength");
        strengthLabel.setFont(Font.font(FONT_FAMILY, 11));
        strengthLabel.setTextFill(LIGHT_TEXT);

        VBox strengthBox = new VBox(2);
        strengthBox.getChildren().addAll(strengthIndicator, strengthLabel);

        fieldBox.getChildren().addAll(label, field, strengthBox);
        return fieldBox;
    }

    private VBox createDatePickerField(String labelText, String promptText, DatePicker datePicker) {
        VBox fieldBox = new VBox(8);
        fieldBox.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(labelText);
        label.setTextFill(DARK_TEXT);
        label.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 13));

        datePicker.setPromptText(promptText);
        datePicker.setPrefHeight(40);
        datePicker.setMaxWidth(Double.MAX_VALUE);

        datePicker.setStyle(
                "-fx-background-color: " + toHexString(INPUT_BG) + ";" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8px;" +
                        "-fx-font-size: 14px;"
        );

        datePicker.setEffect(new DropShadow(5, 0, 2, Color.rgb(0, 0, 0, 0.1)));

        fieldBox.getChildren().addAll(label, datePicker);
        return fieldBox;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: linear-gradient(to right, " + toHexString(PRIMARY_COLOR) + ", " + toHexString(SECONDARY_COLOR) + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4,0,2,2 );" +
                        "-fx-cursor: hand;"
        );
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(40);

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to right, " + toHexString(SECONDARY_COLOR) + ", " + toHexString(PRIMARY_COLOR) + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.3) , 5,0,2,2 );" +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: linear-gradient(to right, " + toHexString(PRIMARY_COLOR) + ", " + toHexString(SECONDARY_COLOR) + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.25) , 4,0,2,2 );" +
                        "-fx-cursor: hand;"
        ));
        return button;
    }

    private void setupRegisterButtonAction(Button registerButton, TextField firstNameField,
                                           TextField lastNameField, TextField emailField,
                                           PasswordField passwordField, PasswordField confirmPasswordField,
                                           DatePicker dobPicker, CheckBox termsCheckBox) {

        registerButton.setOnAction(event -> {
            // Get form values
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String confirmPass = confirmPasswordField.getText();
            LocalDate dob = dobPicker.getValue();
            boolean acceptedTerms = termsCheckBox.isSelected();

            // Validate form
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirmPass.isEmpty() || dob == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }

            if (!password.equals(confirmPass)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
                return;
            }

            if (!acceptedTerms) {
                showAlert(Alert.AlertType.ERROR, "Error", "You must accept the terms and conditions.");
                return;
            }

            // Register user
            boolean success = Middleware.registerUser(firstName, lastName, email, password, dob);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful! You can now login.");

                // Close registration window and open login
                Stage currentStage = (Stage) registerButton.getScene().getWindow();
                currentStage.close();

                // Launch login screen
                Main loginPage = new Main();
                try {
                    loginPage.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Email may already be in use.");
            }
        });
    }

    // Helper method to display alerts
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void applyGlobalStyles(StackPane root) {
        root.setStyle("-fx-background-color: " + toHexString(BACKGROUND_COLOR) + ";");
    }

    private void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: " + toHexString(INPUT_BG) + ";" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 8px;" +
                        "-fx-font-size: 14px;"
        );
        field.setPrefHeight(40);
        field.setMaxWidth(Double.MAX_VALUE);
        field.setEffect(new DropShadow(5, 0, 2, Color.rgb(0, 0, 0, 0.1)));

        // Focus listener for better UX
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: " + toHexString(PRIMARY_COLOR) + ";" +
                                "-fx-border-width: 1px;" +
                                "-fx-background-radius: 8px;" +
                                "-fx-padding: 8px;" +
                                "-fx-font-size: 14px;"
                );
            } else {
                field.setStyle(
                        "-fx-background-color: " + toHexString(INPUT_BG) + ";" +
                                "-fx-background-radius: 8px;" +
                                "-fx-padding: 8px;" +
                                "-fx-font-size: 14px;"
                );
            }
        });
    }

    private void animateComponents(HBox mainContent, VBox leftPanel, VBox rightPanel) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), mainContent);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        TranslateTransition translateLeft = new TranslateTransition(Duration.millis(1000), leftPanel);
        translateLeft.setFromX(-50);
        translateLeft.setToX(0);
        translateLeft.play();

        TranslateTransition translateRight = new TranslateTransition(Duration.millis(1000), rightPanel);
        translateRight.setFromX(50);
        translateRight.setToX(0);
        translateRight.play();
    }

    // Utility function to convert Color to Hex String
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch(args);
    }
}