package com.lude.ecommerce_cart_system.FrontEnd;

import com.lude.ecommerce_cart_system.BackEnd.BackEnd;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddProductForm extends Application {

    private TextField idField;
    private TextField nameField;

    private TextArea descriptionArea;
    private TextField priceField;
    private TextField stockField;
    private AdminDashboard.ProductAddedCallback callback;
    private Label formStatusLabel;

    // Match the color scheme from AdminDashboard
    private final String DARK_BLUE = "#2c3e50";
    private final String BLUE = "#3498db";
    private final String LIGHT_BLUE = "#e6f3fc";
    private final String GREEN = "#27ae60";

    @Override
    public void start(Stage stage) {
        stage.setTitle("Nep Shop - Add New Product");

        // Main container with gradient background matching AdminDashboard style
        BorderPane mainContainer = new BorderPane();
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");

        // Create top bar to match AdminDashboard
        BorderPane topBar = createTopBar();
        mainContainer.setTop(topBar);

        // Form container with card-like appearance
        VBox formCard = new VBox(20);
        formCard.setPadding(new Insets(25));
        formCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;"
        );

        // Add drop shadow to form card (matching product cards)
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4, 0.3));
        formCard.setEffect(shadow);

        // Form header with title
        HBox headerBox = new HBox(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Title with matching dashboard style
        Label titleLabel = new Label("Add New Product");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web(DARK_BLUE));

        // Status label for feedback
        formStatusLabel = new Label("");
        formStatusLabel.setFont(Font.font("Arial", 12));
        formStatusLabel.setTextFill(Color.web("#6c757d"));

        headerBox.getChildren().add(titleLabel);

        // Separator with matching style
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 15, 0));

        // Form fields container
        GridPane form = new GridPane();
        form.setHgap(20);
        form.setVgap(20);
        form.setPadding(new Insets(10, 0, 10, 0));

        // Product ID field
        Label idLabel = createFieldLabel("Product ID");
        idField = new TextField();
        idField.setPromptText("Enter unique product identifier");
        styleTextField(idField);

        // Product Name field
        Label nameLabel = createFieldLabel("Product Name");
        nameField = new TextField();
        nameField.setPromptText("Enter product name");
        styleTextField(nameField);


        // Price field with currency indicator
        Label priceLabel = createFieldLabel("Price");
        HBox priceBox = new HBox(0);
        Label currencyLabel = new Label("$");
        currencyLabel.setPadding(new Insets(8, 10, 8, 10));
        currencyLabel.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-border-color: #ced4da; " +
                "-fx-border-width: 1px 0px 1px 1px; -fx-border-radius: 4px 0 0 4px; -fx-text-fill: " + DARK_BLUE + ";");

        priceField = new TextField();
        priceField.setPromptText("0.00");
        styleTextField(priceField);
        priceField.setStyle(priceField.getStyle() + "-fx-border-radius: 0 4px 4px 0;");
        priceBox.getChildren().addAll(currencyLabel, priceField);

        // Stock field
        Label stockLabel = createFieldLabel("Stock Quantity");
        stockField = new TextField();
        stockField.setPromptText("Enter available quantity");
        styleTextField(stockField);

        // Description field
        Label descLabel = createFieldLabel("Description");
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter detailed product description");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);
        styleTextArea(descriptionArea);

        // Image upload placeholder (to match Dashboard)
        Label imageLabel = createFieldLabel("Product Image");
        StackPane imagePane = new StackPane();
        imagePane.setMinHeight(100);
        imagePane.setPrefWidth(200);
        imagePane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5px; -fx-cursor: hand;");

        Label imageIcon = new Label("📷");
        imageIcon.setFont(Font.font("Arial", 36));
        imageIcon.setTextFill(Color.web("#cccccc"));

        Label uploadLabel = new Label("Click to upload");
        uploadLabel.setTextFill(Color.web("#6c757d"));

        VBox uploadBox = new VBox(5);
        uploadBox.setAlignment(Pos.CENTER);
        uploadBox.getChildren().addAll(imageIcon, uploadLabel);

        imagePane.getChildren().add(uploadBox);

        // Add click event (placeholder)
        imagePane.setOnMouseClicked(e -> {
            // Image upload functionality would go here
            formStatusLabel.setText("Image upload not implemented yet");
            formStatusLabel.setTextFill(Color.web("#f39c12"));
        });

        // Arrange form fields
        form.add(idLabel, 0, 0);
        form.add(idField, 1, 0);
        form.add(nameLabel, 0, 1);
        form.add(nameField, 1, 1);

        form.add(priceLabel, 0, 3);
        form.add(priceBox, 1, 3);
        form.add(stockLabel, 0, 4);
        form.add(stockField, 1, 4);
        form.add(descLabel, 0, 5);
        form.add(descriptionArea, 1, 5);
        form.add(imageLabel, 0, 6);
        form.add(imagePane, 1, 6);

        // Set column constraints to make the form responsive
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(25);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(75);
        form.getColumnConstraints().addAll(column1, column2);

        // Action buttons with AdminDashboard styling
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button cancelButton = new Button("✕ Cancel");
        styleActionButton(cancelButton, "#e74c3c");

        Button saveButton = new Button("✓ Save Product");
        styleActionButton(saveButton, GREEN);

        saveButton.setOnAction(e -> saveProduct(stage));
        cancelButton.setOnAction(e -> stage.close());

        buttonBox.getChildren().addAll(cancelButton, saveButton);

        // Add form tooltips
        addFormTooltips();

        // Add all components to form card
        formCard.getChildren().addAll(headerBox, formStatusLabel, separator, form, buttonBox);

        // Add padding around the form card
        StackPane centerContent = new StackPane();
        centerContent.setPadding(new Insets(20));
        centerContent.getChildren().add(formCard);
        mainContainer.setCenter(centerContent);

        // Create scene with responsive width
        Scene scene = new Scene(mainContainer, 700, 750);
        stage.setMinWidth(600);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();

        // Animate form appearance
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), centerContent);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private BorderPane createTopBar() {
        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        // Title matching AdminDashboard
        Label titleLabel = new Label("Nep Shop");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.WHITE);

        // Form title
        Label formTitle = new Label(" > Add Product");
        formTitle.setFont(Font.font("Arial", 18));
        formTitle.setTextFill(Color.LIGHTGRAY);

        HBox titleBox = new HBox(5);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().addAll(titleLabel, formTitle);

        topBar.setLeft(titleBox);
        return topBar;
    }

    private Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        label.setTextFill(Color.web(DARK_BLUE));
        return label;
    }

    private void styleTextField(TextField field) {
        field.setPrefWidth(300);
        field.setPrefHeight(38);
        field.setFont(Font.font("Arial", 14));
        field.setStyle(
                "-fx-border-color: #ced4da;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 8px;" +
                        "-fx-background-color: #fcfdfe;"
        );

        // Add focus style with blue highlight to match AdminDashboard
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(field.getStyle() + "-fx-border-color: " + BLUE + "; -fx-border-width: 2px;");
            } else {
                field.setStyle(field.getStyle().replace("-fx-border-color: " + BLUE + "; -fx-border-width: 2px;", "-fx-border-color: #ced4da;"));
            }
        });
    }

    private void styleComboBox(ComboBox<String> comboBox) {
        comboBox.setPrefWidth(300);
        comboBox.setPrefHeight(38);
        comboBox.setStyle(
                "-fx-border-color: #ced4da;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 4px;" +
                        "-fx-background-color: #fcfdfe;" +
                        "-fx-font-family: 'Arial';" +
                        "-fx-font-size: 14px;"
        );

        // Add focus style to match other form elements
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBox.setStyle(comboBox.getStyle() + "-fx-border-color: " + BLUE + "; -fx-border-width: 2px;");
            } else {
                comboBox.setStyle(comboBox.getStyle().replace("-fx-border-color: " + BLUE + "; -fx-border-width: 2px;", "-fx-border-color: #ced4da;"));
            }
        });
    }

    private void styleTextArea(TextArea area) {
        area.setFont(Font.font("Arial", 14));
        area.setPrefWidth(300);
        area.setStyle(
                "-fx-border-color: #ced4da;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 8px;" +
                        "-fx-background-color: #fcfdfe;"
        );

        // Add focus style with blue highlight to match AdminDashboard
        area.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                area.setStyle(area.getStyle() + "-fx-border-color: " + BLUE + "; -fx-border-width: 2px;");
            } else {
                area.setStyle(area.getStyle().replace("-fx-border-color: " + BLUE + "; -fx-border-width: 2px;", "-fx-border-color: #ced4da;"));
            }
        });
    }

    private void styleActionButton(Button button, String color) {
        button.setPrefHeight(40);
        button.setPrefWidth(150);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-cursor: hand;"
        );

        // Hover effect - matching AdminDashboard
        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: derive(" + color + ", -10%);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: " + color + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );

        // Pressed effect
        button.setOnMousePressed(e ->
                button.setStyle(
                        "-fx-background-color: derive(" + color + ", -20%);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );

        button.setOnMouseReleased(e ->
                button.setStyle(
                        "-fx-background-color: " + color + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;"
                )
        );
    }

    private void saveProduct(Stage stage) {
        try {
            if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
                    descriptionArea.getText().isEmpty() || priceField.getText().isEmpty() ||
                    stockField.getText().isEmpty()) {
                showValidationError("All fields are required.");
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String description = descriptionArea.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            if (price <= 0) {
                showValidationError("Price must be greater than zero.");
                return;
            }

            if (stock < 0) {
                showValidationError("Stock cannot be negative.");
                return;
            }

            // Save to database
            boolean success = saveProductToDatabase(id, name, description, price, stock);

            if (success) {
                // Create new product
                AdminDashboard.Product newProduct = new AdminDashboard.Product(id, name, description, price, stock);

                // Show success message
                showSuccessMessage();

                // Call callback if exists
                if (callback != null) {
                    callback.onProductAdded(newProduct);
                }

                // Close form after a brief delay to show success message
                new Thread(() -> {
                    try {
                        Thread.sleep(800);
                        javafx.application.Platform.runLater(() -> stage.close());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                showValidationError("Failed to save product. Check if the ID already exists.");
            }
        } catch (NumberFormatException e) {
            showValidationError("Please enter valid numbers for ID, Price, and Stock.");
        }
    }

    private void showValidationError(String message) {
        formStatusLabel.setText(message);
        formStatusLabel.setTextFill(Color.web("#e74c3c"));

        // Shake animation for the status label
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50), formStatusLabel);
        shake.setFromX(0);
        shake.setByX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void showSuccessMessage() {
        formStatusLabel.setText("Product saved successfully!");
        formStatusLabel.setTextFill(Color.web("#27ae60"));

        // Fade in/out animation for success message
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), formStatusLabel);
        fadeIn.setFromValue(0.5);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(2);
        fadeIn.setAutoReverse(true);
        fadeIn.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white;");
        dialogPane.getStyleClass().add("modern-alert");

        alert.showAndWait();
    }

    public void setOnProductAdded(AdminDashboard.ProductAddedCallback callback) {
        this.callback = callback;
    }

    // Helper method to create a styled tooltip
    private Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setStyle(
                "-fx-background-color: " + DARK_BLUE + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 5px 10px;" +
                        "-fx-background-radius: 3px;"
        );
        return tooltip;
    }

    // Add tooltips to form fields
    private void addFormTooltips() {
        Tooltip.install(idField, createTooltip("Enter a unique numeric identifier for this product"));
        Tooltip.install(nameField, createTooltip("Enter the product name as it will appear in catalogs"));
        Tooltip.install(priceField, createTooltip("Enter the retail price in dollars"));
        Tooltip.install(stockField, createTooltip("Enter the quantity currently available"));
        Tooltip.install(descriptionArea, createTooltip("Enter a detailed description of the product"));
    }

    // Method to preload form with example data (for development purposes)
    public void preloadWithExampleData() {
        idField.setText("1001");
        nameField.setText("Professional Laptop");
        priceField.setText("999.99");
        stockField.setText("50");
        descriptionArea.setText("High-performance laptop featuring 16GB RAM, 512GB SSD, and dedicated graphics card. Perfect for professionals and creative users.");
    }

    // Method to reset the form
    public void resetForm() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        stockField.clear();
        descriptionArea.clear();
        formStatusLabel.setText("");
    }

    // Method to enable accessibility features
    private void setupAccessibility() {
        idField.setAccessibleText("Product ID field");
        nameField.setAccessibleText("Product name field");
        priceField.setAccessibleText("Product price field");
        stockField.setAccessibleText("Product stock quantity field");
        descriptionArea.setAccessibleText("Product description field");
    }

    private boolean saveProductToDatabase(int id, String name, String description, double price, int stock) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = BackEnd.getConnection();

            // First check if product ID already exists
            String checkSql = "SELECT COUNT(*) FROM products WHERE id = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Product with this ID already exists
                System.out.println("Product with ID " + id + " already exists.");

                // Option 1: Update the existing product instead of inserting
                String updateSql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, name);
                pstmt.setString(2, description);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, stock);
                pstmt.setInt(5, id);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;

                // Option 2: Alternatively, you could return false here to indicate failure
                // return false;
            } else {
                // Product ID doesn't exist, proceed with insert
                String insertSql = "INSERT INTO products (id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";
                if (pstmt != null) pstmt.close();
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, description);
                pstmt.setDouble(4, price);
                pstmt.setInt(5, stock);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error saving product: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            BackEnd.closeResources(conn, pstmt, rs);
        }
    }

    // Inner class to define the Product structure if not already defined elsewhere
    public static class AdminDashboard {
        public static class Product extends com.lude.ecommerce_cart_system.FrontEnd.AdminDashboard.Product {
            private int id;
            private String name;
            private String description;
            private double price;
            private int stock;

            public Product(int id, String name, String description, double price, int stock) {
                super();
                this.id = id;
                this.name = name;
                this.description = description;
                this.price = price;
                this.stock = stock;
            }

            // Getters
            public int getId() { return id; }
            public String getName() { return name; }
            public String getDescription() { return description; }
            public double getPrice() { return price; }
            public int getStock() { return stock; }
        }

        // Interface for callback
        public interface ProductAddedCallback {
            void onProductAdded(Product product);
        }
    }
}