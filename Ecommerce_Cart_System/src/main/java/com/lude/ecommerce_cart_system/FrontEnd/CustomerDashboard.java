package com.lude.ecommerce_cart_system.FrontEnd;

import com.lude.ecommerce_cart_system.Middleware.Middleware;
import com.lude.ecommerce_cart_system.Middleware.Middleware;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomerDashboard extends Application {

    // Product list
    private final ObservableList<AdminDashboard.Product> products = FXCollections.observableArrayList();
    private static final String PRODUCT_FILE = "products.dat";
    private ScheduledExecutorService scheduler;

    // UI Components
    private FlowPane productContainer;
    private Stage primaryStage;
    private Label cartCountLabel;
    private ShoppingCart cart;
    private BorderPane mainRoot;
    private ScrollPane scrollPane;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Nep Shop - Customer Experience");

        // Initialize the shopping cart
        cart = new ShoppingCart();

        // Load products from file
        loadProducts();

        // Set up file watcher to refresh products periodically
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::loadProducts, 5, 5, TimeUnit.SECONDS);

        // Create main scene
        mainRoot = new BorderPane();
        mainRoot.setTop(createHeader());
        mainRoot.setLeft(createSidebar());
        mainRoot.setCenter(createProductsView());

        Scene mainScene = new Scene(mainRoot, 1100, 750);

        // Apply CSS
        String css =
                ".sidebar-button {"
                        + "    -fx-background-color: transparent;"
                        + "    -fx-text-fill: white;"
                        + "    -fx-padding: 15px 20px;"
                        + "    -fx-font-size: 14px;"
                        + "    -fx-font-weight: bold;"
                        + "    -fx-alignment: CENTER_LEFT;"
                        + "    -fx-min-width: 200px;"
                        + "}"
                        + ".sidebar-button:hover {"
                        + "    -fx-background-color: rgba(255, 255, 255, 0.2);"
                        + "    -fx-cursor: hand;"
                        + "}"
                        + ".product-card:hover {"
                        + "    -fx-effect: dropshadow(gaussian, #3498db, 10, 0.5, 0, 0);"
                        + "    -fx-cursor: hand;"
                        + "}";

        mainScene.getStylesheets().add("data:text/css," + css.replace(" ", "%20"));

        primaryStage.setScene(mainScene);
        primaryStage.show();


        // Clean up when closing
        primaryStage.setOnCloseRequest(e -> {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        });
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #2c3e50, #4a6491);");

        // Create store logo with SVG path
        SVGPath logoPath = new SVGPath();
        logoPath.setContent("M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2M12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12A8,8 0 0,0 12,4M12,10.5A1.5,1.5 0 0,1 13.5,12A1.5,1.5 0 0,1 12,13.5A1.5,1.5 0 0,1 10.5,12A1.5,1.5 0 0,1 12,10.5Z");
        logoPath.setFill(Color.WHITE);
        logoPath.setScaleX(1.3);
        logoPath.setScaleY(1.3);

        // Add glow effect to logo
        Glow glow = new Glow();
        glow.setLevel(0.3);
        logoPath.setEffect(glow);

        // Logo animation on hover
        logoPath.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), logoPath);
            scale.setToX(1.5);
            scale.setToY(1.5);
            scale.play();
        });

        logoPath.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), logoPath);
            scale.setToX(1.3);
            scale.setToY(1.3);
            scale.play();
        });

        // Store title with shadow effect
        Label titleLabel = new Label("Nep Shop");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);

        DropShadow textShadow = new DropShadow();
        textShadow.setRadius(2.0);
        textShadow.setOffsetX(1.0);
        textShadow.setOffsetY(1.0);
        textShadow.setColor(Color.color(0, 0, 0, 0.5));
        titleLabel.setEffect(textShadow);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Search field with styling and search icon
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-background-radius: 20px;" +
                        "-fx-padding: 5px 15px;"
        );

        // Search icon
        SVGPath searchIcon = new SVGPath();
        searchIcon.setContent("M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z");
        searchIcon.setFill(Color.WHITE);

        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(250);
        searchField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7);" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 5px 0px;"
        );

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });

        searchBox.getChildren().addAll(searchIcon, searchField);

        // Home button with icon
        Button homeButton = createIconButton("M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z", "Home");

        // Logout button with icon
        Button logoutButton = createIconButton("M17 7l-1.41 1.41L18.17 11H8v2h10.17l-2.58 2.58L17 17l5-5zM4 5h8V3H4c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h8v-2H4V5z", "Logout");
        logoutButton.setOnAction(e -> {
            Main loginScreen = new Main();
            try {
                loginScreen.start(new Stage());
            } catch (Exception ex) {
                loginScreen.showAlert("Error", "Failed to open login screen: " + ex.getMessage());
            }
            primaryStage.close();
        });

        // Cart button with counter badge
        HBox cartButtonBox = createCartButton();

        header.getChildren().addAll(logoPath, titleLabel, spacer, searchBox, homeButton, cartButtonBox, logoutButton);
        return header;
    }

    private Button createIconButton(String svgPath, String text) {
        Button button = new Button();

        SVGPath icon = new SVGPath();
        icon.setContent(svgPath);
        icon.setFill(Color.WHITE);

        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", 12));

        VBox buttonContent = new VBox(5);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.getChildren().addAll(icon, label);

        button.setGraphic(buttonContent);
        button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        );

        // Button hover effect
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();

            button.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                            "-fx-background-radius: 5px;" +
                            "-fx-padding: 10px;" +
                            "-fx-cursor: hand;"
            );
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();

            button.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-padding: 10px;" +
                            "-fx-cursor: hand;"
            );
        });

        // Button click animation
        button.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.play();
        });

        button.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

        if (text.equals("Home")) {
            button.setOnAction(e -> refreshProductDisplay());
        }

        return button;
    }

    private HBox createCartButton() {
        SVGPath cartIcon = new SVGPath();
        cartIcon.setContent("M7 18c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zM1 2v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.14 0-.25-.11-.25-.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.58-6.49c.08-.14.12-.31.12-.48 0-.55-.45-1-1-1H5.21l-.94-2H1zm16 16c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2z");
        cartIcon.setFill(Color.WHITE);

        Label cartLabel = new Label("Cart");
        cartLabel.setTextFill(Color.WHITE);
        cartLabel.setFont(Font.font("Arial", 12));

        VBox cartContent = new VBox(5);
        cartContent.setAlignment(Pos.CENTER);
        cartContent.getChildren().addAll(cartIcon, cartLabel);

        Button cartButton = new Button();
        cartButton.setGraphic(cartContent);
        cartButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-padding: 10px;" +
                        "-fx-cursor: hand;"
        );

        // Counter badge
        cartCountLabel = new Label("0");
        cartCountLabel.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-background-radius: 50%;" +
                        "-fx-min-width: 20px;" +
                        "-fx-min-height: 20px;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 2px 8px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-alignment: center;"
        );

        // Add hover effect
        cartButton.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), cartButton);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();

            cartButton.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                            "-fx-background-radius: 5px;" +
                            "-fx-padding: 10px;" +
                            "-fx-cursor: hand;"
            );
        });

        cartButton.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), cartButton);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();

            cartButton.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-padding: 10px;" +
                            "-fx-cursor: hand;"
            );
        });

        // Button click animation
        cartButton.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), cartButton);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.play();
        });

        cartButton.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), cartButton);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

        // Open cart view when clicked
        cartButton.setOnAction(e -> showCartView());

        StackPane cartBadgeStack = new StackPane();
        cartBadgeStack.getChildren().addAll(cartButton, cartCountLabel);
        StackPane.setAlignment(cartCountLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(cartCountLabel, new Insets(-5, -5, 0, 0));

        HBox cartButtonBox = new HBox(cartBadgeStack);
        return cartButtonBox;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(20, 0, 0, 0));
        sidebar.setSpacing(5);



        // Divider
        Region divider = new Region();
        divider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");
        divider.setPrefHeight(1);
        divider.setMaxWidth(180);
        VBox.setMargin(divider, new Insets(15, 20, 15, 20));

        // Customer support section
        Label supportTitle = new Label("CUSTOMER SUPPORT");
        supportTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        supportTitle.setTextFill(Color.rgb(255, 255, 255, 0.8));
        supportTitle.setPadding(new Insets(0, 0, 10, 20));

        Button contactBtn = createSidebarButton("Contact Us", false);
        contactBtn.setOnAction(e -> {
            showContactDialog();
            highlightSidebarButton(contactBtn);
        });

        Button faqBtn = createSidebarButton("FAQ", false);
        faqBtn.setOnAction(e -> {
            showFAQDialog();
            highlightSidebarButton(faqBtn);
        });

        sidebar.getChildren().addAll(

                divider,
                supportTitle,
                contactBtn,
                faqBtn
        );

        return sidebar;
    }

    private Button createSidebarButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.getStyleClass().add("sidebar-button");

        if (isActive) {
            button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white;");
        }

        return button;
    }

    private void highlightSidebarButton(Button activeButton) {
        // Reset all sidebar buttons
        for (Node node : ((VBox) mainRoot.getLeft()).getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setStyle("");
                btn.getStyleClass().add("sidebar-button");
            }
        }

        // Highlight the active button
        activeButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); -fx-text-fill: white;");
    }

    private ScrollPane createProductsView() {
        // Section title
        Label sectionTitle = new Label("Featured Products");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        sectionTitle.setPadding(new Insets(20, 0, 20, 0));

        // Create a flow pane for products
        productContainer = new FlowPane();
        productContainer.setHgap(20);
        productContainer.setVgap(20);
        productContainer.setPadding(new Insets(0, 20, 20, 20));
        productContainer.setPrefWidth(700);

        // Wrap in VBox with title
        VBox productsViewContent = new VBox();
        productsViewContent.setPadding(new Insets(0, 0, 0, 20));
        productsViewContent.getChildren().addAll(sectionTitle, productContainer);

        // Display products
        displayProducts();

        // Wrap in ScrollPane for scrolling
        scrollPane = new ScrollPane(productsViewContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");
        return scrollPane;
    }
    private void displayProducts() {
        productContainer.getChildren().clear();
        // Add each product to the container
        for (AdminDashboard.Product product : products) {
            VBox productCard = createProductCard(product);
            productCard.setPrefWidth(220);
            productCard.setPrefHeight(280);
            productContainer.getChildren().add(productCard);
        }
    }
    private VBox createProductCard(AdminDashboard.Product product) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.CENTER);
        card.setSpacing(10);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        );

        // Product image placeholder
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(180, 120);
        imagePlaceholder.setMaxSize(180, 120);
        imagePlaceholder.setMinSize(180, 120);

        // Random color for placeholder
        int r = 100 + (int)(Math.random() * 100);
        int g = 100 + (int)(Math.random() * 100);
        int b = 100 + (int)(Math.random() * 100);
        imagePlaceholder.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + ");" +
                "-fx-background-radius: 5px;");

        // Product info
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(180);

        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setStyle("-fx-text-fill: #7f8c8d;");

        Label priceLabel = new Label("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Add to cart button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;"
        );

        // Add hover effect to button
        addToCartBtn.setOnMouseEntered(e ->
                addToCartBtn.setStyle(
                        "-fx-background-color: #2980b9;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 8px 16px;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-weight: bold;"
                )
        );

        addToCartBtn.setOnMouseExited(e ->
                addToCartBtn.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 5px;" +
                                "-fx-padding: 8px 16px;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-weight: bold;"
                )
        );

        // Add to cart functionality
        addToCartBtn.setOnAction(e -> {
            cart.addItem(product);
            updateCartCounter();
            showNotification("Added to Cart", product.getName() + " has been added to your cart.");

            // Animate button
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), addToCartBtn);
            scale.setToX(0.9);
            scale.setToY(0.9);
            scale.setAutoReverse(true);
            scale.setCycleCount(2);
            scale.play();
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(imagePlaceholder, nameLabel, categoryLabel, spacer, priceLabel, addToCartBtn);

        // Card click to show product details
        card.setOnMouseClicked(e -> showProductDetails(product));

        return card;
    }

    private void updateCartCounter() {
        cartCountLabel.setText(String.valueOf(cart.getItemCount()));
    }

    private void showNotification(String title, String message) {
        VBox notification = new VBox(10);
        notification.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 15px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0.0, 0.0);"
        );
        notification.setMaxWidth(300);
        notification.setMaxHeight(100);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        notification.getChildren().addAll(titleLabel, messageLabel);

        // Position the notification
        StackPane notificationContainer = new StackPane(notification);
        notificationContainer.setPadding(new Insets(20));
        StackPane.setAlignment(notification, Pos.BOTTOM_RIGHT);

        mainRoot.getChildren().add(notificationContainer);

        // Animation
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), notification);
        slideIn.setFromY(100);
        slideIn.setToY(0);
        slideIn.play();

        // Remove after delay
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), notification);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(3));
        fadeOut.setOnFinished(e -> mainRoot.getChildren().remove(notificationContainer));
        fadeOut.play();
    }

    private void filterProducts(String searchTerm) {
        productContainer.getChildren().clear();

        if (searchTerm == null || searchTerm.isEmpty()) {
            displayProducts();
            return;
        }

        searchTerm = searchTerm.toLowerCase();
        for (AdminDashboard.Product product : products) {
            if (product.getName().toLowerCase().contains(searchTerm) ||
                    product.getCategory().toLowerCase().contains(searchTerm) ||
                    product.getDescription().toLowerCase().contains(searchTerm)) {

                VBox productCard = createProductCard(product);
                productCard.setPrefWidth(220);
                productCard.setPrefHeight(280);
                productContainer.getChildren().add(productCard);
            }
        }
    }

    private void filterProductsByCategory(String category) {
        productContainer.getChildren().clear();

        for (AdminDashboard.Product product : products) {
            if (product.getCategory().equals(category)) {
                VBox productCard = createProductCard(product);
                productCard.setPrefWidth(220);
                productCard.setPrefHeight(280);
                productContainer.getChildren().add(productCard);
            }
        }
    }

    private void refreshProductDisplay() {
        displayProducts();
    }

    private void showProductDetails(AdminDashboard.Product product) {
        // Create a dialog for product details
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Product Details");
        dialog.setHeaderText(product.getName());

        // Set dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefSize(600, 400);
        dialogPane.getButtonTypes().addAll(ButtonType.CLOSE);

        // Content layout
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));

        // Product image placeholder
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(200, 200);
        imagePlaceholder.setMaxSize(200, 200);
        imagePlaceholder.setMinSize(200, 200);

        // Random color for placeholder
        int r = 100 + (int)(Math.random() * 100);
        int g = 100 + (int)(Math.random() * 100);
        int b = 100 + (int)(Math.random() * 100);
        imagePlaceholder.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + ");" +
                "-fx-background-radius: 5px;");

        // Product info
        VBox productInfo = new VBox(15);

        Label categoryLabel = new Label("Category: " + product.getCategory());
        categoryLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");

        Label priceLabel = new Label("Price: $" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label stockLabel = new Label("Stock: " + product.getStock() + " items");
        stockLabel.setStyle("-fx-font-size: 14px;");

        Label descriptionTitle = new Label("Description:");
        descriptionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label descriptionLabel = new Label(product.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(350);

        // Add to cart button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        // Quantity selector
        HBox quantityBox = new HBox(10);
        quantityBox.setAlignment(Pos.CENTER_LEFT);

        Label quantityLabel = new Label("Quantity:");

        Spinner<Integer> quantitySpinner = new Spinner<>(1, product.getStock(), 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(100);

        quantityBox.getChildren().addAll(quantityLabel, quantitySpinner);

        // Add to cart functionality
        addToCartBtn.setOnAction(e -> {
            int quantity = quantitySpinner.getValue();
            for (int i = 0; i < quantity; i++) {
                cart.addItem(product);
            }
            updateCartCounter();
            showNotification("Added to Cart", quantity + " x " + product.getName() + " has been added to your cart.");
            dialog.close();
        });

        productInfo.getChildren().addAll(categoryLabel, priceLabel, stockLabel,
                descriptionTitle, descriptionLabel,
                new Separator(), quantityBox, addToCartBtn);

        content.setLeft(imagePlaceholder);
        content.setCenter(productInfo);
        BorderPane.setMargin(productInfo, new Insets(0, 0, 0, 20));

        dialogPane.setContent(content);

        // Show dialog
        dialog.showAndWait();
    }

    private void showCartView() {
        // Create a dialog for cart
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Shopping Cart");
        dialog.setHeaderText("Your Shopping Cart");

        // Set dialog pane
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefSize(700, 500);
        dialogPane.getButtonTypes().addAll(ButtonType.CLOSE, ButtonType.APPLY);
        Button checkoutButton = (Button) dialogPane.lookupButton(ButtonType.APPLY);
        checkoutButton.setText("Checkout");

        // Content layout
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));

        if (cart.getItems().isEmpty()) {
            VBox emptyCart = new VBox(20);
            emptyCart.setAlignment(Pos.CENTER);

            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

            Button continueShopping = new Button("Continue Shopping");
            continueShopping.setStyle(
                    "-fx-background-color: #3498db;" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-padding: 10px 20px;" +
                            "-fx-cursor: hand;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 14px;"
            );

            continueShopping.setOnAction(e -> dialog.close());

            emptyCart.getChildren().addAll(emptyLabel, continueShopping);
            content.setCenter(emptyCart);

            checkoutButton.setOnAction(e -> {
                int userId = getCurrentUserId(); // Ensure this returns the logged-in user's ID

                com.lude.ecommerce_cart_system.Middleware.Middleware Middleware = null;
                boolean success = Middleware.placeOrder(userId, cart.getItems());

                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Order Confirmation");
                    alert.setHeaderText("Order Completed");
                    alert.setContentText("Your order has been placed successfully and saved in the database.");
                    alert.showAndWait(); // Show confirmation before closing

                    cart.clearCart(); // Clear the cart after placing order
                    updateCartCounter(); // Update the cart counter in UI
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Order Failed");
                    alert.setHeaderText("Error!");
                    alert.setContentText("Failed to place the order. Please try again.");
                    alert.showAndWait();
                }
            });




            // Disable checkout button
            checkoutButton.setDisable(true);
        } else {
            // Cart items list
            VBox cartItemsList = new VBox(10);
            ScrollPane scrollPane = new ScrollPane(cartItemsList);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(350);
            scrollPane.setStyle("-fx-background-color: transparent;");

            for (ShoppingCart.CartItem item : cart.getItems()) {
                HBox cartItemRow = createCartItemRow(item);
                cartItemsList.getChildren().add(cartItemRow);
            }

            // Summary section
            VBox summaryBox = new VBox(10);
            summaryBox.setPadding(new Insets(20, 0, 0, 0));

            HBox subtotalRow = new HBox();
            subtotalRow.setAlignment(Pos.BASELINE_RIGHT);
            Label subtotalLabel = new Label("Subtotal: ");
            Label subtotalValue = new Label("$" + String.format("%.2f", cart.getSubtotal()));
            subtotalValue.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            subtotalRow.getChildren().addAll(subtotalLabel, subtotalValue);

            HBox taxRow = new HBox();
            taxRow.setAlignment(Pos.BASELINE_RIGHT);
            Label taxLabel = new Label("Tax (10%): ");
            Label taxValue = new Label("$" + String.format("%.2f", cart.getSubtotal() * 0.1));
            taxRow.getChildren().addAll(taxLabel, taxValue);

            Separator separator = new Separator();
            separator.setPadding(new Insets(10, 0, 10, 0));

            HBox totalRow = new HBox();
            totalRow.setAlignment(Pos.BASELINE_RIGHT);
            Label totalLabel = new Label("Total: ");
            Label totalValue = new Label("$" + String.format("%.2f", cart.getSubtotal() * 1.1));
            totalValue.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            totalRow.getChildren().addAll(totalLabel, totalValue);

            summaryBox.getChildren().addAll(subtotalRow, taxRow, separator, totalRow);

            content.setCenter(scrollPane);
            content.setBottom(summaryBox);
        }

        dialogPane.setContent(content);



        // Show dialog
        dialog.showAndWait();
    }

    private int getCurrentUserId() {
        // TODO: Replace this with actual user authentication logic
        return 1; // Change this to dynamically fetch the logged-in user ID
    }


    private HBox createCartItemRow(ShoppingCart.CartItem item) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        );

        // Product image placeholder
        Region imagePlaceholder = new Region();
        imagePlaceholder.setPrefSize(60, 60);
        imagePlaceholder.setMaxSize(60, 60);
        imagePlaceholder.setMinSize(60, 60);

        // Random color for placeholder
        int r = 100 + (int)(Math.random() * 100);
        int g = 100 + (int)(Math.random() * 100);
        int b = 100 + (int)(Math.random() * 100);
        imagePlaceholder.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + ");" +
                "-fx-background-radius: 5px;");

        // Product info
        VBox productInfo = new VBox(5);
        productInfo.setPrefWidth(300);

        Label nameLabel = new Label(item.getProduct().getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label categoryLabel = new Label(item.getProduct().getCategory());
        categoryLabel.setStyle("-fx-text-fill: #7f8c8d;");

        productInfo.getChildren().addAll(nameLabel, categoryLabel);

        // Quantity control
        HBox quantityControl = new HBox(5);
        quantityControl.setAlignment(Pos.CENTER);

        Button decreaseBtn = new Button("-");
        decreaseBtn.setStyle(
                "-fx-background-radius: 15px;" +
                        "-fx-min-width: 30px;" +
                        "-fx-min-height: 30px;" +
                        "-fx-max-width: 30px;" +
                        "-fx-max-height: 30px;" +
                        "-fx-cursor: hand;"
        );

        Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
        quantityLabel.setStyle("-fx-padding: 0 10px;");

        Button increaseBtn = new Button("+");
        increaseBtn.setStyle(
                "-fx-background-radius: 15px;" +
                        "-fx-min-width: 30px;" +
                        "-fx-min-height: 30px;" +
                        "-fx-max-width: 30px;" +
                        "-fx-max-height: 30px;" +
                        "-fx-cursor: hand;"
        );

        decreaseBtn.setOnAction(e -> {
            cart.decreaseQuantity(item.getProduct());
            updateCartCounter();
            quantityLabel.setText(String.valueOf(item.getQuantity()));
            if (item.getQuantity() <= 0) {
                ((VBox) row.getParent()).getChildren().remove(row);
                if (cart.getItems().isEmpty()) {
                    showCartView(); // Refresh cart view if empty
                }
            }
        });

        increaseBtn.setOnAction(e -> {
            cart.increaseQuantity(item.getProduct());
            updateCartCounter();
            quantityLabel.setText(String.valueOf(item.getQuantity()));
        });

        quantityControl.getChildren().addAll(decreaseBtn, quantityLabel, increaseBtn);

        // Price
        Label priceLabel = new Label("$" + String.format("%.2f", item.getProduct().getPrice() * item.getQuantity()));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Remove button
        Button removeBtn = new Button("✕");
        removeBtn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #e74c3c;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );

        removeBtn.setOnAction(e -> {
            cart.removeItem(item.getProduct());
            updateCartCounter();
            ((VBox) row.getParent()).getChildren().remove(row);
            if (cart.getItems().isEmpty()) {
                showCartView(); // Refresh cart view if empty
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(imagePlaceholder, productInfo, spacer, quantityControl, priceLabel, removeBtn);

        return row;
    }



    private boolean validateCheckoutForm(TextField nameField, TextField emailField,
                                         TextField phoneField, TextField addressField,
                                         TextField cityField, TextField stateField,
                                         TextField zipField, TextField cardNumberField,
                                         TextField cvvField) {

        StringBuilder errorMessage = new StringBuilder();

        // Check for empty fields
        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("- Full name is required\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("- Email is required\n");
        } else if (!emailField.getText().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            errorMessage.append("- Invalid email format\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("- Phone number is required\n");
        }

        if (addressField.getText().trim().isEmpty()) {
            errorMessage.append("- Address is required\n");
        }

        if (cityField.getText().trim().isEmpty()) {
            errorMessage.append("- City is required\n");
        }

        if (stateField.getText().trim().isEmpty()) {
            errorMessage.append("- State/Province is required\n");
        }

        if (zipField.getText().trim().isEmpty()) {
            errorMessage.append("- ZIP/Postal code is required\n");
        }

        if (cardNumberField.getText().trim().isEmpty()) {
            errorMessage.append("- Card number is required\n");
        } else if (!cardNumberField.getText().matches("\\d{13,19}")) {
            errorMessage.append("- Invalid card number\n");
        }

        if (cvvField.getText().trim().isEmpty()) {
            errorMessage.append("- CVV is required\n");
        } else if (!cvvField.getText().matches("\\d{3,4}")) {
            errorMessage.append("- CVV must be 3 or 4 digits\n");
        }

        // If there are errors, show an alert
        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void processOrder() {
        // Show order confirmation dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText("Thank You for Your Purchase!");
        alert.setContentText("Your order has been placed successfully. " +
                "You will receive a confirmation email shortly.");

        // Clear the cart after purchase
        cart.clearCart();
        updateCartCounter();

        alert.showAndWait();
    }

    private void showContactDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Contact Us");
        dialog.setHeaderText("We'd Love to Hear From You!");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefSize(500, 400);
        dialogPane.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        Button submitButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        submitButton.setText("Submit");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label nameLabel = new Label("Your Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email Address:");
        TextField emailField = new TextField();

        Label subjectLabel = new Label("Subject:");
        ComboBox<String> subjectComboBox = new ComboBox<>();
        subjectComboBox.getItems().addAll(
                "Product Inquiry",
                "Order Status",
                "Return/Refund",
                "Technical Support",
                "Feedback",
                "Other"
        );
        subjectComboBox.setValue("Product Inquiry");
        subjectComboBox.setPrefWidth(200);

        Label messageLabel = new Label("Message:");
        TextArea messageArea = new TextArea();
        messageArea.setPrefHeight(150);
        messageArea.setWrapText(true);

        content.getChildren().addAll(
                nameLabel, nameField,
                emailLabel, emailField,
                subjectLabel, subjectComboBox,
                messageLabel, messageArea
        );

        dialogPane.setContent(content);

        submitButton.setOnAction(e -> {
            if (validateContactForm(nameField, emailField, messageArea)) {
                showNotification("Message Sent", "Thank you for your message. We'll get back to you soon!");
                dialog.close();
            }
        });

        dialog.showAndWait();
    }

    private boolean validateContactForm(TextField nameField, TextField emailField, TextArea messageArea) {
        StringBuilder errorMessage = new StringBuilder();

        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("- Name is required\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("- Email is required\n");
        } else if (!emailField.getText().matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
            errorMessage.append("- Invalid email format\n");
        }

        if (messageArea.getText().trim().isEmpty()) {
            errorMessage.append("- Message is required\n");
        }

        if (errorMessage.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void showFAQDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Frequently Asked Questions");
        dialog.setHeaderText("Frequently Asked Questions");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setPrefSize(600, 500);
        dialogPane.getButtonTypes().addAll(ButtonType.CLOSE);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // FAQ Items
        TitledPane faq1 = createFAQItem(
                "How do I track my order?",
                "You can track your order by logging into your account and clicking on 'Order History'. " +
                        "Each order has a tracking number that you can use to follow your package's journey."
        );

        TitledPane faq2 = createFAQItem(
                "What is your return policy?",
                "We offer a 30-day return policy for most items. Products must be unused and in their " +
                        "original packaging. Please contact our customer service team to initiate the return process."
        );

        TitledPane faq3 = createFAQItem(
                "How can I change or cancel my order?",
                "Orders can be modified or canceled within 1 hour of placement. After this time, the order " +
                        "enters our fulfillment process and cannot be changed. Please contact us immediately if you need to make changes."
        );
        TitledPane faq4 = createFAQItem(
                "What payment methods do you accept?",
                "We accept all major credit cards (Visa, MasterCard, American Express, Discover), " +
                        "PayPal, and Apple Pay. We also offer financing options on select purchases."
        );

        TitledPane faq5 = createFAQItem(
                "How long will shipping take?",
                "Standard shipping typically takes 3-5 business days. Express shipping (1-2 business days) " +
                        "is available for an additional fee. International shipping times vary by destination."
        );

        TitledPane faq6 = createFAQItem(
                "Do you ship internationally?",
                "Yes, we ship to most countries worldwide. International shipping costs and delivery times " +
                        "vary by location. Please note that import duties and taxes may apply and are the responsibility of the customer."
        );

        content.getChildren().addAll(faq1, faq2, faq3, faq4, faq5, faq6);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white;");

        dialogPane.setContent(scrollPane);
        dialog.showAndWait();
    }

    private TitledPane createFAQItem(String question, String answer) {
        TitledPane faqPane = new TitledPane();
        faqPane.setText(question);

        Label answerLabel = new Label(answer);
        answerLabel.setWrapText(true);
        answerLabel.setStyle("-fx-padding: 10px;");

        faqPane.setContent(answerLabel);
        return faqPane;
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @SuppressWarnings("unchecked")
    private void loadProducts() {
        File file = new File(PRODUCT_FILE);

        if (!file.exists()) {
            System.out.println("No product file found. Using default products.");
            createDefaultProducts();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<AdminDashboard.Product> loadedProducts = (List<AdminDashboard.Product>) obj;
                products.clear();
                products.addAll(loadedProducts);

                // Update UI if it's already created
                if (productContainer != null) {
                    Platform.runLater(this::refreshProductDisplay);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading products: " + e.getMessage());
        }
    }

    private void createDefaultProducts() {
        products.clear();

        products.add(new AdminDashboard.Product("Smartphone XS Pro", "Electronics", 999.99,
                "Latest flagship smartphone with advanced camera system and AI capabilities.", 45));

        products.add(new AdminDashboard.Product("Wireless Headphones", "Electronics", 149.99,
                "Premium wireless headphones with noise cancellation and 30-hour battery life.", 78));

        products.add(new AdminDashboard.Product("Smart Watch", "Electronics", 249.99,
                "Fitness and health tracking smartwatch with heart rate monitor and GPS.", 32));

        products.add(new AdminDashboard.Product("Cotton T-Shirt", "Clothing", 24.99,
                "Soft, comfortable cotton t-shirt available in multiple colors.", 120));

        products.add(new AdminDashboard.Product("Slim Fit Jeans", "Clothing", 59.99,
                "Classic slim fit jeans with stretch fabric for comfort.", 85));

        products.add(new AdminDashboard.Product("Running Shoes", "Clothing", 89.99,
                "Lightweight running shoes with responsive cushioning.", 64));

        products.add(new AdminDashboard.Product("Air Fryer", "Home & Kitchen", 79.99,
                "Digital air fryer for healthier cooking with multiple presets.", 25));

        products.add(new AdminDashboard.Product("Coffee Maker", "Home & Kitchen", 69.99,
                "Programmable coffee maker with thermal carafe.", 42));

        products.add(new AdminDashboard.Product("Bedding Set", "Home & Kitchen", 129.99,
                "100% cotton bedding set including duvet cover and pillowcases.", 36));

        products.add(new AdminDashboard.Product("Bestselling Novel", "Books", 14.99,
                "Latest bestselling fiction novel from award-winning author.", 110));

        products.add(new AdminDashboard.Product("Cookbook", "Books", 29.99,
                "Illustrated cookbook with 100+ recipes for beginners.", 55));

        products.add(new AdminDashboard.Product("Board Game", "Toys & Games", 34.99,
                "Strategic board game for 2-6 players, ages 10 and up.", 28));

        products.add(new AdminDashboard.Product("STEM Building Kit", "Toys & Games", 49.99,
                "Educational building kit that teaches engineering concepts.", 37));
    }

    public static class ShoppingCart {
        private final ObservableList<CartItem> items = FXCollections.observableArrayList();

        public void addItem(AdminDashboard.Product product) {
            for (CartItem item : items) {
                if (item.getProduct().getName().equals(product.getName())) {
                    item.incrementQuantity();
                    return;
                }
            }

            // If the product is not already in the cart
            items.add(new CartItem(product, 1));
        }

        public void removeItem(AdminDashboard.Product product) {
            items.removeIf(item -> item.getProduct().getName().equals(product.getName()));
        }

        public void decreaseQuantity(AdminDashboard.Product product) {
            for (CartItem item : items) {
                if (item.getProduct().getName().equals(product.getName())) {
                    item.decrementQuantity();
                    if (item.getQuantity() <= 0) {
                        items.remove(item);
                    }
                    return;
                }
            }
        }

        public void increaseQuantity(AdminDashboard.Product product) {
            for (CartItem item : items) {
                if (item.getProduct().getName().equals(product.getName())) {
                    item.incrementQuantity();
                    return;
                }
            }
        }

        public ObservableList<CartItem> getItems() {
            return items;
        }

        public int getItemCount() {
            int count = 0;
            for (CartItem item : items) {
                count += item.getQuantity();
            }
            return count;
        }

        public int getTotalQuantity() {
            return getItemCount();
        }

        public double getSubtotal() {
            double total = 0;
            for (CartItem item : items) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
            return total;
        }

        public void clearCart() {
            items.clear();
        }

        public static class CartItem {
            private final AdminDashboard.Product product;
            private int quantity;

            public CartItem(AdminDashboard.Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
            }

            public AdminDashboard.Product getProduct() {
                return product;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public void incrementQuantity() {
                quantity++;
            }

            public void decrementQuantity() {
                if (quantity > 0) {
                    quantity--;
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}