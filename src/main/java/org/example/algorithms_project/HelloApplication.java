package org.example.algorithms_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.example.algorithms_project.controller.StudentGradeViewer;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        StudentGradeViewer studentView = new StudentGradeViewer(primaryStage);
        primaryStage.setScene(studentView.createStudentScene());
        primaryStage.setTitle("Student Grade Viewer");;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}