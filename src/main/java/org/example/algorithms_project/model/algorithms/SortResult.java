package org.example.algorithms_project.model.algorithms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.example.algorithms_project.model.Student;
import org.example.algorithms_project.utils.CSVReader;

public class SortResult {
    
    private static List<Student> quickSortedList; // sorted list on dashboard
    
    private static long quickSortTime;
    private static long mergeSortTime;
    private static long radixSortTime;
    
    private static long quickMemoryTime;
    private static long mergeMemoryTime;
    private static long radixMemoryTime;
    
    public static void sortingProcess(File csvFile, Comparator<Student> comparator) throws IOException {
        
        List<Student> original= CSVReader.readStudents(csvFile); 
        
        List<Student> quickList = new ArrayList<>(original);
        List<Student> mergeList = new ArrayList<>(original);
        List<Student> radixList = new ArrayList<>(original);
   
        QuickSort.sort(quickList, comparator);              //pass to quicksort
        quickMemoryTime=QuickSort.getLastMemoryUsage();
        quickSortTime= QuickSort.getLastExecutionTime();
        
        quickSortedList= new ArrayList<>(quickList);        // sorted list on dashboard
        
        if(comparator==SortingManager.GRADE_COMPARATOR){    //pass to Radixsort
            RadixSort.sortByGrade(radixList);
            radixSortTime=RadixSort.getGradeSortTime();
            radixMemoryTime=RadixSort.getGradeSortMemory();
        }
        else if(comparator==SortingManager.NAME_COMPARATOR){
            RadixSort.sortByName(radixList);
            radixSortTime=RadixSort.getNameSortTime();
            radixMemoryTime=RadixSort.getNameSortMemory();
        }
        else{
            RadixSort.sortByPerformance(radixList);
            radixSortTime=RadixSort.getPerformanceSortTime();
            radixMemoryTime=RadixSort.getPerformanceSortMemory();
        }
        
        MergeSort.sort(mergeList, comparator);                //pass to mergesort
        mergeSortTime=MergeSort.getLastExecutionTime();
        mergeMemoryTime=MergeSort.getLastMemoryUsage();
    }

    public List<Student> getQuickSortedList() {
        return quickSortedList;
    }
    
    public static long getQuickSortTime() {
        return quickSortTime;
    }

    public static long getQuickMemoryTime() {
        return quickMemoryTime;
    }

    public static long getMergeMemoryTime() {
        return mergeMemoryTime;
    }
    
    public static long getMergeSortTime() {
        return mergeSortTime;
    }

    public static long getRadixMemoryTime() {
        return radixMemoryTime;
    }
    
    public static long getRadixSortTime() {
        return radixSortTime;
    }
}
