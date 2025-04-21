package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;
import java.util.Comparator;
import java.util.List;

public class QuickSort {

    // Public API: Sort students using specified comparator
    public static void sort(List<Student> students, Comparator<Student> comparator) {
        if (students == null || students.size() <= 1) return;
        quickSort(students, 0, students.size() - 1, comparator);
    }

    // Recursive QuickSort implementation
    private static void quickSort(List<Student> students, int low, int high,
                                  Comparator<Student> comparator) {
        if (low < high) {
            int pivotIndex = partition(students, low, high, comparator);
            quickSort(students, low, pivotIndex - 1, comparator);
            quickSort(students, pivotIndex + 1, high, comparator);
        }
    }

    // Randomized partition scheme to avoid worst-case performance
    private static int partition(List<Student> students, int low, int high,
                                 Comparator<Student> comparator) {
        // Random pivot selection
        int randomPivotIndex = low + (int) (Math.random() * (high - low + 1));
        swap(students, randomPivotIndex, high);

        Student pivot = students.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(students.get(j), pivot) <= 0) {
                i++;
                swap(students, i, j);
            }
        }
        swap(students, i + 1, high);
        return i + 1;
    }

    // Swap helper method
    private static void swap(List<Student> students, int i, int j) {
        Student temp = students.get(i);
        students.set(i, students.get(j));
        students.set(j, temp);
    }
}