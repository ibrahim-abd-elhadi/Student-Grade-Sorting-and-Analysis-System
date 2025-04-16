package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;

import java.util.Comparator;

public class SortingManager {
    // Sort by name (A-Z)
    public static final Comparator<Student> NAME_COMPARATOR =
            Comparator.comparing(Student::getName);

    // Sort by grade (0-100)
    public static final Comparator<Student> GRADE_COMPARATOR =
            Comparator.comparingDouble(Student::getGrade);

    // Sort by performance (Best to Worst)
    public static final Comparator<Student> PERFORMANCE_COMPARATOR =
            Comparator.comparing(Student::getPerformance).reversed();
}
