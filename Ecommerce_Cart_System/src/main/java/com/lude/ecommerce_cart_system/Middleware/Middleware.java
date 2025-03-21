// Middleware.java
package com.lude.ecommerce_cart_system.Middleware;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List; // Added missing import

import com.lude.ecommerce_cart_system.FrontEnd.AdminDashboard;
import com.lude.ecommerce_cart_system.BackEnd.BackEnd;
import com.lude.ecommerce_cart_system.FrontEnd.CustomerDashboard;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Middleware {

    // User registration method
    public static boolean saveProduct(String name, String category, double price, int stock, String description) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = BackEnd.getConnection();
            if (conn == null) {
                System.out.println("❌ Database connection failed.");
                return false;
            }

            String sql = "INSERT INTO products (name, category, price, stock, description) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);
            pstmt.setString(5, description);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Product saved successfully!");
                return true;
            } else {
                System.out.println("❌ Failed to insert product.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("⚠️ SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            BackEnd.closeResources(conn, pstmt, null);
        }
    }

    public static boolean registerUser(String firstName, String lastName, String email,
                                       String password, LocalDate dob) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = BackEnd.getConnection();

            // Check if email already exists
            if (isEmailExists(conn, email)) {
                return false;
            }

            // Insert new user
            String sql = "INSERT INTO users (first_name, last_name, email, password, date_of_birth) " +
                    "VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, hashPassword(password)); // Hash password
            pstmt.setDate(5, java.sql.Date.valueOf(dob));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        } finally {
            BackEnd.closeResources(conn, pstmt, null);
        }
    }

    private static boolean isEmailExists(Connection conn, String email) {
        return false;
    }

    // ✅ Fixed testConnection() method
    public static void testConnection() {
        Connection conn = null;
        try {
            conn = BackEnd.getConnection();
            if (conn != null) {
                System.out.println("✅ Database connected successfully!");
            } else {
                System.out.println("❌ Failed to connect to database!");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error connecting to database: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Close the connection
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Consolidated login method with admin credentials check
    public static String login(String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = BackEnd.getConnection();

            String sql = "SELECT user_id, user_role FROM users WHERE email = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, hashPassword(password));

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_role");
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return null;
        } finally {
            BackEnd.closeResources(conn, pstmt, rs);
        }
    }

    // Method to place an order
    public static boolean placeOrder(int userId, List<CustomerDashboard.ShoppingCart.CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            System.out.println("⚠️ Cart is empty, nothing to order.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = BackEnd.getConnection();
            String sql = "INSERT INTO orders (user_id, product_name, quantity, total_price) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            for (CustomerDashboard.ShoppingCart.CartItem item : cartItems) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, item.getProduct().getName());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setDouble(4, item.getProduct().getPrice() * item.getQuantity());
                pstmt.addBatch();
            }

            int[] rowsAffected = pstmt.executeBatch();

            if (rowsAffected.length > 0) {
                System.out.println("✅ Order placed successfully for User ID: " + userId);
                return true;
            } else {
                System.out.println("❌ No rows affected, order failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("⚠️ SQL Error: " + e.getMessage());
            return false;
        } finally {
            BackEnd.closeResources(conn, pstmt, null);
        }
    }

    // Simple password hashing (for demonstration - use a proper hashing library in production)
    private static String hashPassword(String password) {
        return password; // ⚠️ Insecure! Use BCrypt or Argon2 in production.
    }

    // ✅ Main method now calls testConnection()
    public static void main(String[] args) {
        System.out.println("🔹 Middleware running...");
        testConnection(); // Call the test method when the program starts
    }

    public static void redirectToDashboard(String role) {
    }
}