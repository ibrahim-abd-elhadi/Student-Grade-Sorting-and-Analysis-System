package org.example.algorithms_project.model.algorithms;

import org.example.algorithms_project.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.*;

public class RadixSort {

    private static long gradeSortTime; 
    private static long nameSortTime;  
    private static long performanceSortTime;
    
    private static long gradeSortMemory; 
    private static long nameSortMemory;  
    private static long performanceSortMemory;

    private static final Map<String, Integer> performanceMap = new HashMap<>();

    static {
        performanceMap.put("EXCELLENT", 3);
        performanceMap.put("GOOD", 2);
        performanceMap.put("POOR", 1);
    }

    public static void sortByGrade(List<Student> students) {
        if (students == null || students.size() < 2) return;

        long startTime = System.nanoTime();
        long startMemory = getMemoryUsage();
        
        int max = getMaxScaledGrade(students);
        int exp = 1;
        while (max / exp > 0) {
            sortByDigit(students, exp);
            exp *= 10;
        }

        long endTime = System.nanoTime();
        long endMemory = getMemoryUsage();

        gradeSortTime = endTime - startTime;
        gradeSortMemory = endMemory - startMemory;
    }

    
    public static void sortByName(List<Student> students) {
        long startTime = System.nanoTime(); 
        long startMemory = getMemoryUsage();
        int maxLen = students.stream()
                             .mapToInt(s -> s.getName().length())
                             .max().orElse(0);

        for (int pos = maxLen - 1; pos >= 0; pos--) {
            countingSortByChar(students, pos);
        }

        long endTime = System.nanoTime();
        long endMemory = getMemoryUsage(); 

        nameSortTime = endTime - startTime; 
        nameSortMemory = endMemory - startMemory; 
    }
    
    public static void sortByPerformance(List<Student> students) {
        long startTime = System.nanoTime(); 
        long startMemory = getMemoryUsage();

        students.sort(Comparator.comparingInt(s ->
            performanceMap.getOrDefault(s.getPerformance(), -1)
        ));

        long endTime = System.nanoTime(); 
        long endMemory = getMemoryUsage(); 

        performanceSortTime = endTime - startTime;
        performanceSortMemory = endMemory - startMemory;
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

    private static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static long getGradeSortTime() {
        return gradeSortTime;
    }

    public static long getNameSortTime() {
        return nameSortTime;
    }

    public static long getPerformanceSortTime() {
        return performanceSortTime;
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
