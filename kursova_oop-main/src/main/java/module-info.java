module com.example.exampleoop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jlayer;
    requires com.google.gson;
    requires log4j;

    opens com.example.exampleoop to javafx.fxml;
    exports com.example.exampleoop;
    exports com.example.exampleoop.utils;
    opens com.example.exampleoop.utils to javafx.fxml;
    exports com.example.exampleoop.zombieDialogs;
    opens com.example.exampleoop.zombieDialogs to javafx.fxml;
    exports com.example.exampleoop.services;
    opens com.example.exampleoop.services to javafx.fxml;
    opens com.example.exampleoop.gameEntities.microObjects to com.google.gson;
    opens com.example.exampleoop.gameEntities.macroObjects to com.google.gson;
    exports com.example.exampleoop.gameEntities;
    opens com.example.exampleoop.gameEntities to com.google.gson, javafx.fxml;
}