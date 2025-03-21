package com.lude.ecommerce_cart_system.FrontEnd;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create an instance of the login class
        login loginPage = new login();
        try {
            // Start the login page
            loginPage.start(primaryStage); // Pass the primaryStage to the login
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur during login page initialization
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
