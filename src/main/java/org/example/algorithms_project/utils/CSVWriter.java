package org.example.algorithms_project.utils;

import org.example.algorithms_project.model.Student;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVWriter {
    
        public static void writeStudentsToCSV(String filePath, List<Student> students) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Student student : students) {
                writer.append(student.getName())
                      .append(",")
                      .append(String.valueOf(student.getGrade()))
                      .append(",")
                      .append(String.valueOf(student.getPerformance()))
                      .append("\n");
            }
            System.out.println("file was written successfully to : " + filePath);
        }
        catch (IOException e) {
            System.out.println("something wrong happend at CSV writer " + e.getMessage());
        }
    }
}
