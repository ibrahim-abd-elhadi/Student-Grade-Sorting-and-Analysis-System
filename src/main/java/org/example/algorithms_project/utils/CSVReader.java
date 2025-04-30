package org.example.algorithms_project.utils;

import org.example.algorithms_project.model.Student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Student> readStudents(File csvFile) throws IOException, IllegalArgumentException {
        List<String> validationErrors = DataValidator.validateStudentData(csvFile);
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", validationErrors));
        }

        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",");
                String name = tokens[0].trim();
                double grade = Double.parseDouble(tokens[1].trim());
                students.add(new Student(name, grade));
            }
        }

        return students;
    }
}