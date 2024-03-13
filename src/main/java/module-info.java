module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.demo2 to javafx.fxml;
    // OPEN THE FOLDER CONTAINING OBJECT CLASSES TO 'javafx.base'
    // WHEN NOT LOADING TO GUI !!!!!!
    // --------------------------------------------------------------
    opens com.example.demo2.domain.entities to javafx.base;
    // --------------------------------------------------------------
    exports com.example.demo2;

}