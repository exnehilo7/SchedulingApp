module hopp.schedulingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hopp.schedulingapp to javafx.fxml;
    opens general to javafx.base;
    exports hopp.schedulingapp;
}