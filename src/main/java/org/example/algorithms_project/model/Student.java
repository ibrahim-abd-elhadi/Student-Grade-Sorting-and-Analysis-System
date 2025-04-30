package org.example.algorithms_project.model;

import javafx.beans.property.*;

public class Student {
    private final StringProperty name;
    private final DoubleProperty grade;
    private final StringProperty performance;

    public Student(String name, double grade) {
        this.name = new SimpleStringProperty(name);
        this.grade = new SimpleDoubleProperty(grade);
        this.performance = new SimpleStringProperty(calculatePerformance(grade).toString());
    }

    public StringProperty nameProperty() { return name; }
    public DoubleProperty gradeProperty() { return grade; }
    public StringProperty performanceProperty() { return performance; }

    public String getName() { return name.get(); }
    public double getGrade() { return grade.get(); }
    public Performance getPerformance() { return Performance.valueOf(performance.get()); }


    public void setName(String name) { this.name.set(name); }

    public void setGrade(double grade) {
        this.grade.set(grade);
        this.performance.set(calculatePerformance(grade).toString());
    }

    private Performance calculatePerformance(double grade) {
        if (grade >= 85) return Performance.EXCELLENT;
        if (grade >= 65) return Performance.GOOD;
        return Performance.POOR;
    }

    public enum Performance { EXCELLENT, GOOD, POOR }
}