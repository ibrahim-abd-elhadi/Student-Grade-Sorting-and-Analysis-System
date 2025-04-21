package org.example.algorithms_project.model;
 
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;
 
 public class GradeStatistics {
     
     public static int getTotalNumber(List<Student> students) {
         if (students.isEmpty()) 
             return 0;
         else
             return students.size();
     }
     
     public static double getMean(List<Student> students) {
         if (students.isEmpty()) 
             return 0;
         double total = 0;
         for(Student student : students) {
             total += student.getGrade();
         }
         return total/students.size();
     }
     
     
     public static double getMedian(List<Student> students) {
         if (students.isEmpty()) 
             return 0;
         List<Double> grades = new ArrayList<>();
         for (Student student : students) {
             grades.add(student.getGrade());
         }
         
         Collections.sort(grades);
         
         int size = grades.size();
         //odd --> 1 item in center 
         if (size % 2 == 1) {
             return grades.get(size / 2);}
         //even--> 2 items in center
         else {
             double mid1 = grades.get(size / 2 - 1);
             double mid2 = grades.get(size / 2);
             return (mid1 + mid2) / 2.0;
         }
     }
     
     public static double getMaxGrade(List<Student> students) {
         if (students.isEmpty()) 
             return 0;
         double max = Double.MIN_VALUE;
         for (Student student : students) {
             if (student.getGrade() > max) {
                 max = student.getGrade();
             }
         }
         return max;
     }
      
     public static double getMinGrade(List<Student> students) {
         if (students.isEmpty()) 
             return 0;
         double min = Double.MAX_VALUE;
         for (Student student : students) {
             if (student.getGrade() < min) {
                 min = student.getGrade();
             }
         }
         return min;
     }
     
     public static double getStandaredDeviation(List<Student> students) {
         if (students.isEmpty()) return 0;
         double mean = getMean(students);
         double sum = 0;
         for (Student student : students) {
             double diff = student.getGrade() - mean;
             sum += diff*diff;
         }
         return Math.sqrt(sum/(students.size()-1));
     }
         
         
     public static double getVariance(List<Student> students){
             double std=getStandaredDeviation(students);
             return std*std;
     }
 }