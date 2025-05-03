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
            Comparator.comparing(Student::getGrade);

    public enum CriteriaType {
        NAME_ASCENDING("Name (A-Z)"),
        NAME_DESCENDING("Name (Z-A)"),
        GRADE_ASCENDING("Grade (Low to High)"),
        GRADE_DESCENDING("Grade (High to Low)"),
        PERFORMANCE_ASCENDING("Performance (Worst to Best)"),
        PERFORMANCE_DESCENDING("Performance (Best to Worst)");

        private final String displayName;

        CriteriaType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static Comparator<Student> getComparator(CriteriaType criteria) {
        switch (criteria) {
            case NAME_ASCENDING:
                return NAME_COMPARATOR;
            case NAME_DESCENDING:
                return NAME_COMPARATOR.reversed();
            case GRADE_ASCENDING:
                return GRADE_COMPARATOR;
            case GRADE_DESCENDING:
                return GRADE_COMPARATOR.reversed();
            case PERFORMANCE_ASCENDING:
                return PERFORMANCE_COMPARATOR.reversed();
            case PERFORMANCE_DESCENDING:
                return PERFORMANCE_COMPARATOR;
            default:
                return NAME_COMPARATOR;
        }
    }
}