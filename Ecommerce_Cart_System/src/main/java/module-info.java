module com.lude.ecommerce_cart_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    // Open specific packages to JavaFX for reflection (e.g., FXML)
    opens com.lude.ecommerce_cart_system.FrontEnd to javafx.fxml;

    // Export packages that need to be accessed by other modules
    exports com.lude.ecommerce_cart_system.FrontEnd;
}
