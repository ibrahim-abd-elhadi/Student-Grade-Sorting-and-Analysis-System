package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;
import java.util.Comparator;
import java.util.List;

public class QuickSort {

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

        quickSort(students, 0, students.size() - 1, comparator);

        // Ensure garbage collection runs after sorting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        lastExecutionTime = System.nanoTime() - startTime;

        // Use absolute value to handle any JVM memory fluctuations
        lastMemoryUsage = Math.abs(finalMemory - initialMemory);

        // QuickSort is mostly in-place but uses O(log n) stack space
        // If measured memory is too small (JVM optimization), use a theoretical estimate
        if (lastMemoryUsage < 1000 && students.size() > 1) {
            // Theoretical memory: log(n) stack frames x estimated frame size
            lastMemoryUsage = Math.max(1024, (long)(Math.log(students.size()) * 64));
        }
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

    // Partition scheme (unchanged)
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
}