package org.example.algorithms_project.utils;

import org.example.algorithms_project.model.Student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.algorithms_project.model.Student.Performance.*;

public class CSVReader {

    public static List<Student> readStudents(File csvFile) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    String name = tokens[0].trim();
                    double grade = Double.parseDouble(tokens[1].trim());

                    Student.Performance performance;
                    if (grade > 85) {
                        performance = EXCELLENT;
                    } else if (grade > 70) {
                        performance = GOOD;
                    } else {
                        performance = POOR;
                    }

                    students.add(new Student(name, grade, performance));
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // You can show error in the UI instead
        }
        return students;
    }
}
