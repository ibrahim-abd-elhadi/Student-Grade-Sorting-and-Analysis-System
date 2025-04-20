package org.example.algorithms_project.model;

public class Student {

    public enum Performance { EXCELLENT, GOOD, POOR }
    private String name;
    private double grade;
    private Performance performance;

    public Student(String name, double grade, Performance performance) {
        this.name = name;
        this.grade = grade;
        this.performance = performance;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

    public Performance getPerformance() {
        return performance;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }


}
