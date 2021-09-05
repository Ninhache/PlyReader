module com.example.viewer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.viewer to javafx.fxml;
    exports com.example.viewer;
    exports com.example.viewer.stages;
    opens com.example.viewer.stages to javafx.fxml;
}