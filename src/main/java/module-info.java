module org.example.algorithms_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.algorithms_project to javafx.fxml;
    exports org.example.algorithms_project;
    exports org.example.algorithms_project.model;
    opens org.example.algorithms_project.model to javafx.fxml;
    exports org.example.algorithms_project.model.algorithms;
    opens org.example.algorithms_project.model.algorithms to javafx.fxml;
    exports org.example.algorithms_project.controller;
    opens org.example.algorithms_project.controller to javafx.fxml;
    exports org.example.algorithms_project.utils;
    opens org.example.algorithms_project.utils to javafx.fxml;
}