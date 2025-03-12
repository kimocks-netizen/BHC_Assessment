module com.example.bhc_assessment2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bhc_assessment2 to javafx.fxml;
    exports com.example.bhc_assessment2;
}