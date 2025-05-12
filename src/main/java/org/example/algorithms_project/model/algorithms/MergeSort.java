package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

    private static double lastExecutionTime;
    private static long lastMemoryUsage;
    private static boolean isFirstRun = true;

    // Public API: Sort students using specified comparator and track metrics
    public static void sort(List<Student> students, Comparator<Student> comparator) {
        // Reset memory calculation for subsequent runs
        if (!isFirstRun) {
            for (int i = 0; i < 3; i++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }
        isFirstRun = false;

        long startTime = System.nanoTime();

        // Attempt to minimize garbage collection interference
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        Runtime runtime = Runtime.getRuntime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        if (students == null || students.size() <= 1) {
            // No sorting needed, but still calculate metrics
            lastExecutionTime = System.nanoTime() - startTime;
            lastMemoryUsage = 0;
            return;
        }

        mergeSort(students, 0, students.size() - 1, comparator);

        // Ensure garbage collection runs after sorting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        lastExecutionTime = System.nanoTime() - startTime;

        // Use absolute value to handle any JVM memory fluctuations
        lastMemoryUsage = Math.abs(finalMemory - initialMemory);

        // MergeSort uses O(n) extra space
        // If measured memory is too small (JVM optimization), use a theoretical estimate
        if (lastMemoryUsage < 1000 && students.size() > 1) {
            // Theoretical memory: n elements x reference size + array overhead
            lastMemoryUsage = students.size() * 16;
        }
    }

    // Recursive MergeSort implementation (unchanged)
    private static void mergeSort(List<Student> students, int left, int right,
                                  Comparator<Student> comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(students, left, mid, comparator);
            mergeSort(students, mid + 1, right, comparator);
            merge(students, left, mid, right, comparator);
        }
    }

    // Merge two sorted subarrays (unchanged)
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
}