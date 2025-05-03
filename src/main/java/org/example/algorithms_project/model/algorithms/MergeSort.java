package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

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
            long finalMemory = runtime.totalMemory() - runtime.freeMemory();
            lastExecutionTime = System.nanoTime() - startTime;
            lastMemoryUsage = finalMemory - initialMemory;
            return;
        }

        mergeSort(students, 0, students.size() - 1, comparator);

        // Calculate final metrics
        System.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        lastExecutionTime = System.nanoTime() - startTime;
        lastMemoryUsage = finalMemory - initialMemory;
    }

    // Recursive MergeSort implementation
    private static void mergeSort(List<Student> students, int left, int right,
                                  Comparator<Student> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(students, left, mid, comparator);
            mergeSort(students, mid + 1, right, comparator);
            merge(students, left, mid, right, comparator);
        }
    }

    // Merge two sorted subarrays
    private static void merge(List<Student> students, int left, int mid, int right,
                              Comparator<Student> comparator) {
        // Create temporary arrays
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Student[] leftArray = new Student[n1];
        Student[] rightArray = new Student[n2];

        // Copy data to temporary arrays
        for (int i = 0; i < n1; i++) {
            leftArray[i] = students.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = students.get(mid + 1 + j);
        }

        // Merge the temporary arrays
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare(leftArray[i], rightArray[j]) <= 0) {
                students.set(k, leftArray[i]);
                i++;
            } else {
                students.set(k, rightArray[j]);
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftArray if any
        while (i < n1) {
            students.set(k, leftArray[i]);
            i++;
            k++;
        }

        // Copy remaining elements of rightArray if any
        while (j < n2) {
            students.set(k, rightArray[j]);
            j++;
            k++;
        }
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
                lastExecutionTime, lastExecutionTime / 1_000_000.0);
    }

    public static String getFormattedMemoryUsage() {
        return String.format("%d bytes | %.2f KB | %.2f MB",
                lastMemoryUsage, lastMemoryUsage / 1024.0, lastMemoryUsage / (1024.0 * 1024.0));
    }
}