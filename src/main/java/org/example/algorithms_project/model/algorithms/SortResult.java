package org.example.algorithms_project.model.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.example.algorithms_project.model.Student;
import org.example.algorithms_project.utils.CSVReader;

public class SortResult {
    
    private static List<Student> quickSortedList;
    private static List<Student> mergeSortedList;
    private static List<Student> radixSortedList;
    
    private static long quickSortTime;
    private static long mergeSortTime;
    private static long radixSortTime;
    
    private static long quickMemoryTime;
    private static long mergeMemoryTime;
    private static long radixMemoryTime;
    
    public static void sortingProcess(File csvFile, Comparator<Student> comparator){   
        List<Student> original= CSVReader.readStudents(csvFile);
        List<Student> quickList = new ArrayList<>(original);
        List<Student> mergeList = new ArrayList<>(original);
        List<Student> radixList = new ArrayList<>(original);
   
        QuickSort.sort(quickList, comparator);    
       /* quickMemoryTime=QuickSort.getLastMemoryUsage();
        quickSortTime= QuickSort.getLastExecutionTime();  
        quickSortedList= new ArrayList<>(quickList);
        */
    }

    public static List<Student> getQuickSortedList() {
        return quickSortedList;
    }

    public void setQuickSortedList(List<Student> quickSortedList) {
        this.quickSortedList = quickSortedList;
    }
/*
    public List<Student> getMergeSortedList() {
        return mergeSortedList;
    }

    public void setMergeSortedList(List<Student> mergeSortedList) {
        this.mergeSortedList = mergeSortedList;
    }

    public List<Student> getRadixSortedList() {
        return radixSortedList;
    }

    public void setRadixSortedList(List<Student> radixSortedList) {
        this.radixSortedList = radixSortedList;
    }
*/
    public static long getQuickSortTime() {
        return quickSortTime;
    }

    public static void setQuickSortTime(long quickSortTime) {
        SortResult.quickSortTime = quickSortTime;
    }

    public static long getMergeSortTime() {
        return mergeSortTime;
    }

    public static void setMergeSortTime(long mergeSortTime) {
        SortResult.mergeSortTime = mergeSortTime;
    }

    public static long getRadixSortTime() {
        return radixSortTime;
    }

    public static void setRadixSortTime(long radixSortTime) {
        SortResult.radixSortTime = radixSortTime;
    }

    public static long getQuickMemoryTime() {
        return quickMemoryTime;
    }

    public static void setQuickMemoryTime(long quickMemoryTime) {
        SortResult.quickMemoryTime = quickMemoryTime;
    }

    public static long getMergeMemoryTime() {
        return mergeMemoryTime;
    }

    public static void setMergeMemoryTime(long mergeMemoryTime) {
        SortResult.mergeMemoryTime = mergeMemoryTime;
    }

    public static long getRadixMemoryTime() {
        return radixMemoryTime;
    }

  /*  public static void setRadixMemoryTime(long radixMemoryTime) {
        SortResult.radixMemoryTime = radixMemoryTime;
    }
*/
}