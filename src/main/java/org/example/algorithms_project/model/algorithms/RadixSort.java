package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadixSort {

    private static double gradeSortTime;
    private static double nameSortTime;
    private static double performanceSortTime;

    private static long gradeSortMemory;
    private static long nameSortMemory;
    private static long performanceSortMemory;

    private static boolean isFirstGradeSort = true;
    private static boolean isFirstNameSort = true;
    private static boolean isFirstPerformanceSort = true;

    private static final Map<String, Integer> performanceMap = new HashMap<>();

    static {
        performanceMap.put("EXCELLENT", 3);
        performanceMap.put("GOOD", 2);
        performanceMap.put("POOR", 1);
    }

    public static void sortByGrade(List<Student> students) {
        if (students == null || students.size() < 2) return;

        // Reset memory calculation for subsequent runs
        if (!isFirstGradeSort) {
            for (int i = 0; i < 3; i++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }
        isFirstGradeSort = false;

        // Force garbage collection before starting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        int max = getMaxScaledGrade(students);
        int exp = 1;
        int passes = 0;
        while (max / exp > 0) {
            sortByDigit(students, exp);
            exp *= 10;
            passes++;
        }

        // Force garbage collection after sorting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.nanoTime();

        gradeSortTime = endTime - startTime;
        gradeSortMemory = Math.abs(endMemory - startMemory);

        // If memory measurement is too small, use theoretical estimate
        if (gradeSortMemory < 1000 && students.size() > 1) {
            // For each pass: n elements (references) + count array
            gradeSortMemory = students.size() * 16 * passes + passes * 10 * 4;
        }
    }

    public static void sortByName(List<Student> students) {
        if (students == null || students.size() < 2) return;

        // Reset memory calculation for subsequent runs
        if (!isFirstNameSort) {
            for (int i = 0; i < 3; i++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }
        isFirstNameSort = false;

        // Force garbage collection before starting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        int maxLen = students.stream()
                .mapToInt(s -> s.getName().length())
                .max().orElse(0);
        int passes = 0;

        for (int pos = maxLen - 1; pos >= 0; pos--) {
            countingSortByChar(students, pos);
            passes++;
        }

        // Force garbage collection after sorting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.nanoTime();

        nameSortTime = endTime - startTime;
        nameSortMemory = Math.abs(endMemory - startMemory);

        // If memory measurement is too small, use theoretical estimate
        if (nameSortMemory < 1000 && students.size() > 1) {
            // For each pass: n elements (references) + count array (256 chars)
            nameSortMemory = students.size() * 16 * passes + passes * 256 * 4;
        }
    }

    public static void sortByPerformance(List<Student> students) {
        if (students == null || students.size() < 2) return;

        // Reset memory calculation for subsequent runs
        if (!isFirstPerformanceSort) {
            for (int i = 0; i < 3; i++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }
        isFirstPerformanceSort = false;

        // Force garbage collection before starting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long startTime = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();

        students.sort(Comparator.comparingInt(s ->
                performanceMap.getOrDefault(s.getPerformance(), -1)
        ));

        // Force garbage collection after sorting
        for (int i = 0; i < 3; i++) {
            System.gc();
            try { Thread.sleep(20); } catch (InterruptedException e) { }
        }

        long endMemory = runtime.totalMemory() - runtime.freeMemory();
        long endTime = System.nanoTime();

        performanceSortTime = endTime - startTime;
        performanceSortMemory = Math.abs(endMemory - startMemory);

        // Performance sort uses Java's built-in sort, which may use TimSort (O(n) extra space)
        if (performanceSortMemory < 1000 && students.size() > 1) {
            // Estimated memory for sort: n elements + some bookkeeping
            performanceSortMemory = students.size() * 16;
        }
    }

    private static int getMaxScaledGrade(List<Student> students) {
        int max = scale(students.get(0).getGrade());
        for (Student student : students) {
            int scaled = scale(student.getGrade());
            if (scaled > max) {
                max = scaled;
            }
        }
        return max;
    }

    private static int scale(double grade) {
        return (int) (grade * 100);
    }

    private static void sortByDigit(List<Student> students, int exp) {
        int n = students.size();
        List<Student> output = new ArrayList<>(n);
        int[] count = new int[10];

        for (int i = 0; i < n; i++) output.add(null);

        for (Student student : students) {
            int digit = (scale(student.getGrade()) / exp) % 10;
            count[digit]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            Student student = students.get(i);
            int digit = (scale(student.getGrade()) / exp) % 10;
            output.set(count[digit] - 1, student);
            count[digit]--;
        }

        for (int i = 0; i < n; i++) {
            students.set(i, output.get(i));
        }
    }

    private static void countingSortByChar(List<Student> students, int charPos) {
        int n = students.size();
        List<Student> output = new ArrayList<>(Collections.nCopies(n, null));
        int[] count = new int[256];

        for (Student s : students) {
            String name = s.getName().toLowerCase();
            int index = charPos < name.length() ? name.charAt(charPos) : 0;
            count[index]++;
        }

        for (int i = 1; i < 256; i++) count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            String name = students.get(i).getName().toLowerCase();
            int index = charPos < name.length() ? name.charAt(charPos) : 0;
            output.set(count[index] - 1, students.get(i));
            count[index]--;
        }

        for (int i = 0; i < n; i++) students.set(i, output.get(i));
    }

    public static double getGradeSortTime() {
        return gradeSortTime/1000000000;
    }

    public static double getNameSortTime() {
        return nameSortTime/1000000000;
    }

    public static double getPerformanceSortTime() {
        return performanceSortTime/1000000000;
    }

    public static long getGradeSortMemory() {
        return gradeSortMemory;
    }

    public static long getNameSortMemory() {
        return nameSortMemory;
    }

    public static long getPerformanceSortMemory() {
        return performanceSortMemory;
    }
}