package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;
import java.util.Comparator;
import java.util.List;

public class QuickSort {

    private static double lastExecutionTime;
    private static long lastMemoryUsage;

    // Public API: Sort students using specified comparator and track metrics
    public static void sort(List<Student> students, Comparator<Student> comparator) {
        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();

        // Attempt to minimize garbage collection interference
        System.gc();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        if (students == null || students.size() <= 1) {
            // No sorting needed, but still calculate metrics
            System.gc();
            long finalMemory = runtime.totalMemory() - runtime.freeMemory();
            lastExecutionTime = System.nanoTime() - startTime;
            lastMemoryUsage = Math.max(0, finalMemory - initialMemory); // Prevent negative values
            return;
        }

        quickSort(students, 0, students.size() - 1, comparator);

        // Calculate final metrics
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        lastExecutionTime = System.nanoTime() - startTime;
        lastMemoryUsage = Math.max(0, finalMemory - initialMemory); // Ensure non-negative memory usage
    }

    // Recursive QuickSort implementation (unchanged)
    private static void quickSort(List<Student> students, int low, int high,
                                  Comparator<Student> comparator) {
        if (low < high) {
            int pivotIndex = partition(students, low, high, comparator);
            quickSort(students, low, pivotIndex - 1, comparator);
            quickSort(students, pivotIndex + 1, high, comparator);
        }
    }

    // Randomized partition scheme (unchanged)
    private static int partition(List<Student> students, int low, int high,
                                 Comparator<Student> comparator) {
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

    // Swap helper method (unchanged)
    private static void swap(List<Student> students, int i, int j) {
        Student temp = students.get(i);
        students.set(i, students.get(j));
        students.set(j, temp);
    }

    // Getter for execution time (nanoseconds)
    public static double getLastExecutionTime() {
        return lastExecutionTime/1000000000;
    }

    // Getter for memory usage (bytes)
    public static long getLastMemoryUsage() {
        return lastMemoryUsage;
    }

    public static String getFormattedExecutionTime() {
        return String.format("%d ns | %.3f ms",
                (long) lastExecutionTime, lastExecutionTime / 1_000_000.0);
    }

    public static String getFormattedMemoryUsage() {
        return String.format("%d bytes | %.2f KB | %.2f MB",
                lastMemoryUsage, lastMemoryUsage / 1024.0, lastMemoryUsage / (1024.0 * 1024.0));
    }
}