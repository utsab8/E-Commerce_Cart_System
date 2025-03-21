package com.lude.ecommerce_cart_system.FrontEnd;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends Application {

    // Product list
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private static final String PRODUCT_FILE = "products.dat";
    private FlowPane productContainer;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Nep Shop - Admin Dashboard");

        // Load products from file
        loadProducts();

        // Create a modern topbar with search
        BorderPane topBar = createTopBar(primaryStage);

        // Create a sidebar
        VBox sidebar = createSidebar();

        // Create a scroll pane for product cards
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f5f5;");

        // Create a container for product cards
        productContainer = new FlowPane();
        productContainer.setPadding(new Insets(20));
        productContainer.setHgap(20);
        productContainer.setVgap(20);
        productContainer.setStyle("-fx-background-color: #f5f5f5;");

        // Display product cards
        refreshProductDisplay();

        scrollPane.setContent(productContainer);

        // Create buttons with modern styling
        HBox buttonBox = createButtonBar();

        // Create layout with a modern theme
        BorderPane contentArea = new BorderPane();
        contentArea.setCenter(scrollPane);
        contentArea.setBottom(buttonBox);

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Make dashboard responsive
        makeResponsive(scene, sidebar, mainLayout);

        // Save products when application closes
        primaryStage.setOnCloseRequest(e -> saveProducts());
    }

    private BorderPane createTopBar(Stage primaryStage) {
        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        // Left section - Logo/Title
        Label titleLabel = new Label("Nep Shop");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.WHITE);

        // Center section - Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPrefWidth(400);

        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(350);
        searchField.setStyle("-fx-background-radius: 20px; -fx-padding: 8px 15px; -fx-font-size: 14px;");

        Button searchButton = new Button("🔍");
        searchButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 50%; -fx-min-width: 35px; -fx-min-height: 35px; -fx-cursor: hand;");
        searchButton.setOnAction(e -> filterProducts(searchField.getText()));

        searchField.setOnAction(e -> filterProducts(searchField.getText()));

        searchBox.getChildren().addAll(searchField, searchButton);

        // Right section - Logout only
        HBox rightBox = new HBox(15);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 5px 15px; -fx-background-radius: 5px; -fx-cursor: hand;");
        logoutButton.setOnAction(e -> {
            // Close the current window
            primaryStage.close();

            // Open login screen
            Main loginScreen = new Main();
            try {
                loginScreen.start(new Stage());
            } catch (Exception ex) {
                showAlert("Error", "Failed to open login screen: " + ex.getMessage());
            }
        });

        rightBox.getChildren().add(logoutButton);

        topBar.setLeft(titleLabel);
        topBar.setCenter(searchBox);
        topBar.setRight(rightBox);

        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20, 15, 20, 15));
        sidebar.setStyle("-fx-background-color: #34495e;");

        // Admin profile section
        VBox profileSection = new VBox(10);
        profileSection.setAlignment(Pos.CENTER);
        profileSection.setPadding(new Insets(0, 0, 15, 0));
        profileSection.setStyle("-fx-border-color: transparent transparent #2c3e50 transparent; -fx-border-width: 0 0 1 0;");

        Label circleLabel = new Label("👤");
        circleLabel.setStyle("-fx-background-color: #3498db; -fx-background-radius: 50%; -fx-min-width: 70px; -fx-min-height: 70px; -fx-alignment: center; -fx-font-size: 30px;");

        Label adminName = new Label("Admin User");
        adminName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        adminName.setTextFill(Color.WHITE);

        Label adminRole = new Label("Administrator");
        adminRole.setFont(Font.font("Arial", 12));
        adminRole.setTextFill(Color.LIGHTGRAY);

        profileSection.getChildren().addAll(circleLabel, adminName, adminRole);

        // Menu items - only Dashboard and Products
        VBox menuItems = new VBox(5);
        menuItems.setPadding(new Insets(10, 0, 0, 0));

        String[] menuLabels = {"📊 Dashboard", "📦 Products"};

        for (String label : menuLabels) {
            Button menuButton = new Button(label);
            menuButton.setAlignment(Pos.CENTER_LEFT);
            menuButton.setPrefWidth(Double.MAX_VALUE);
            menuButton.setFont(Font.font("Arial", 14));

            // Style for the current view (Products)
            if (label.equals("📦 Products")) {
                menuButton.setStyle(
                        "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10px; " +
                                "-fx-background-radius: 5px; -fx-cursor: hand;"
                );
            } else {
                menuButton.setStyle(
                        "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10px; " +
                                "-fx-background-radius: 5px; -fx-cursor: hand;"
                );

                // Add hover effect
                menuButton.setOnMouseEntered(e ->
                        menuButton.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px; -fx-cursor: hand;")
                );

                menuButton.setOnMouseExited(e ->
                        menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px; -fx-cursor: hand;")
                );
            }

            menuItems.getChildren().add(menuButton);
        }

        // Add all sections to sidebar
        sidebar.getChildren().addAll(profileSection, menuItems);

        return sidebar;
    }

    private void makeResponsive(Scene scene, VBox sidebar, BorderPane mainLayout) {
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();

            if (width < 900) {
                sidebar.setPrefWidth(60);
                sidebar.setMinWidth(60);
                sidebar.setMaxWidth(60);
                // Hide text labels in sidebar
                for (int i = 0; i < sidebar.getChildren().size(); i++) {
                    if (i == 0) { // Profile section
                        VBox profileSection = (VBox) sidebar.getChildren().get(i);
                        if (profileSection.getChildren().size() > 1) {
                            profileSection.getChildren().remove(1, profileSection.getChildren().size()); // Remove name and role
                        }
                    } else if (i == 1) { // Menu items
                        VBox menuItems = (VBox) sidebar.getChildren().get(i);
                        for (int j = 0; j < menuItems.getChildren().size(); j++) {
                            Button btn = (Button) menuItems.getChildren().get(j);
                            // Extract just the emoji part
                            String text = btn.getText();
                            if (text.contains(" ")) {
                                btn.setText(text.substring(0, 2)); // Just the emoji
                            }
                        }
                    }
                }
            } else {
                sidebar.setPrefWidth(220);
                sidebar.setMinWidth(220);
                sidebar.setMaxWidth(220);
                // Restore sidebar to original state - this would need proper initialization
                mainLayout.setLeft(createSidebar());
            }

            // Adjust product cards based on width
            if (width < 768) {
                productContainer.setPrefWidth(width - 100);
            } else {
                productContainer.setPrefWidth(width - 240);
            }
        });
    }

    private void addProductCallback(Product product) {
        // Generate a new ID for the product (simple approach: max existing ID + 1)
        int newId = 1;
        if (!products.isEmpty()) {
            newId = products.stream()
                    .mapToInt(Product::getId)
                    .max()
                    .orElse(0) + 1;
        }

        // Set the new ID
        product.setId(newId);

        // Add to products list
        products.add(product);

        // Save products to file
        saveProducts();

        // Refresh the display
        refreshProductDisplay();
    }

    private HBox createButtonBar() {
        Button addProductButton = new Button("➕ Add New Product");
        styleButton(addProductButton, "#27ae60");
        addProductButton.setOnAction(e -> {
            try {
                AddProductForm addForm = new AddProductForm();
                addForm.setOnProductAdded(this::addProductCallback);
                addForm.start(new Stage());
            } catch (Exception ex) {
                showAlert("Error", "Failed to open add product form: " + ex.getMessage());
            }
        });

        // Button container - no view options
        HBox buttonBox = new HBox(15);
        buttonBox.setPadding(new Insets(15));
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getChildren().add(addProductButton);
        buttonBox.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");

        return buttonBox;
    }

    private void styleButton(Button button, String backgroundColor) {
        button.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-radius: 5px;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: derive(" + backgroundColor + ", -20%);" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10px 20px;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 5px;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: " + backgroundColor + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 10px 20px;" +
                                "-fx-cursor: hand;" +
                                "-fx-background-radius: 5px;"
                )
        );
    }

    private void refreshProductDisplay() {
        productContainer.getChildren().clear();
        for (Product product : products) {
            productContainer.getChildren().add(createProductCard(product));
        }
    }

    private void filterProducts(String searchText) {
        productContainer.getChildren().clear();
        if (searchText == null || searchText.isEmpty()) {
            refreshProductDisplay();
            return;
        }

        String lowerSearchText = searchText.toLowerCase();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lowerSearchText) ||
                    product.getDescription().toLowerCase().contains(lowerSearchText)) {
                productContainer.getChildren().add(createProductCard(product));
            }
        }
    }

    private VBox createProductCard(Product product) {
        // Card container
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefWidth(250);
        card.setMinHeight(300);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;"
        );

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4, 0.3));
        card.setEffect(shadow);

        // Product image placeholder
        StackPane imagePane = new StackPane();
        imagePane.setMinHeight(150);
        imagePane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5px;");

        Label imageLabel = new Label("📷");
        imageLabel.setFont(Font.font("Arial", 48));
        imageLabel.setTextFill(Color.web("#cccccc"));
        imagePane.getChildren().add(imageLabel);

        // Product ID and name
        Label idLabel = new Label("#" + product.getId());
        idLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setWrapText(true);

        // Price with currency
        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        priceLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Stock indicator
        HBox stockBox = new HBox(5);
        Circle stockIndicator = new Circle(5);
        stockIndicator.setFill(product.getStock() > 5 ? Color.GREEN : (product.getStock() > 0 ? Color.ORANGE : Color.RED));
        Label stockLabel = new Label("In Stock: " + product.getStock());
        stockBox.getChildren().addAll(stockIndicator, stockLabel);
        stockBox.setAlignment(Pos.CENTER_LEFT);

        // Description
        Label descLabel = new Label(product.getDescription());
        descLabel.setWrapText(true);
        descLabel.setMaxHeight(60);
        descLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #e0e0e0;");

        // Action buttons
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);

        Button editButton = new Button("✏️ Edit");
        styleActionButton(editButton, "#f39c12");
        editButton.setOnAction(e -> {
            showAlert("Info", "Edit functionality not implemented yet");
        });

        Button deleteButton = new Button("🗑️ Delete");
        styleActionButton(deleteButton, "#e74c3c");
        deleteButton.setOnAction(e -> {
            if (showConfirmation("Delete Product", "Are you sure you want to delete " + product.getName() + "?")) {
                products.remove(product);
                saveProducts();
                refreshProductDisplay();
            }
        });

        actionButtons.getChildren().addAll(editButton, deleteButton);

        // Add all elements to card
        card.getChildren().addAll(
                imagePane,
                idLabel,
                nameLabel,
                priceLabel,
                stockBox,
                new Separator(),
                descLabel,
                new Separator(),
                actionButtons
        );

        return card;
    }

    private void styleActionButton(Button button, String color) {
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 5px 10px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-cursor: hand;" +
                        "-fx-min-width: 70px;"
        );

        button.setOnMouseEntered(e ->
                button.setStyle(
                        "-fx-background-color: derive(" + color + ", -10%);" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 5px 10px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;" +
                                "-fx-min-width: 70px;"
                )
        );

        button.setOnMouseExited(e ->
                button.setStyle(
                        "-fx-background-color: " + color + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-padding: 5px 10px;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-cursor: hand;" +
                                "-fx-min-width: 70px;"
                )
        );
    }

    private void loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCT_FILE))) {
            List<Product> loadedProducts = (List<Product>) ois.readObject();
            products.clear();
            products.addAll(loadedProducts);
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, add sample products
            products.addAll(
                    new Product(1, "Laptop", "High-performance gaming laptop with RTX 3080, 16GB RAM, and 1TB SSD storage", 1299.99, 10),
                    new Product(2, "Smartphone", "Latest model with 5G support, 6.7-inch display, and 128GB storage", 799.99, 15),
                    new Product(3, "Headphones", "Noise-cancelling wireless headphones with 40-hour battery life", 199.99, 20),
                    new Product(4, "Smart Watch", "Fitness tracking, heart rate monitoring, and GPS capabilities", 249.99, 8),
                    new Product(5, "Tablet", "10-inch display, 64GB storage, perfect for productivity and entertainment", 349.99, 12)
            );
            saveProducts(); // Save sample products
        } catch (IOException | ClassNotFoundException e) {
            showAlert("Error", "Failed to load products: " + e.getMessage());
        }
    }

    public void saveProducts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCT_FILE))) {
            List<Product> productList = new ArrayList<>(products);
            oos.writeObject(productList);
        } catch (IOException e) {
            showAlert("Error", "Failed to save products: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Product class for storage
    public static class Product implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;


        private int id;
        private String name;
        private String description;
        private double price;
        private int stock;

        public Product(int id, String name, String description, double price, int stock) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
        }

        public Product(String smartphoneXsPro, String electronics, double v, String s, int stock) {
        }

        public Product() {

        }

        public Product(int newProductId, String name, String category, double price, int stock, String description) {
        }

        // Getters and setters
        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }

        public void setId(int id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setDescription(String description) { this.description = description; }
        public void setPrice(double price) { this.price = price; }
        public void setStock(int stock) { this.stock = stock; }

        // Property methods for JavaFX
        public SimpleIntegerProperty idProperty() { return new SimpleIntegerProperty(id); }
        public SimpleStringProperty nameProperty() { return new SimpleStringProperty(name); }
        public SimpleStringProperty descriptionProperty() { return new SimpleStringProperty(description); }
        public SimpleDoubleProperty priceProperty() { return new SimpleDoubleProperty(price); }
        public SimpleIntegerProperty stockProperty() { return new SimpleIntegerProperty(stock); }

        public String getCategory() {
            return "";
        }
    }

    // Interface for product add callback
    public interface ProductAddedCallback {
        void onProductAdded(Product product);
    }

    // Class for the circle indicator in the product card
    public static class Circle extends javafx.scene.shape.Circle {
        public Circle(double radius) {
            super(radius);
        }
    }
}