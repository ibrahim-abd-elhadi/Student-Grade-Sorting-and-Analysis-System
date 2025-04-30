package org.example.algorithms_project.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataValidator {
    public static List<String> validateStudentData(File csvFile) throws IOException {
        List<String> errors = new ArrayList<>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;

            if ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.equals("StudentName,Grade")) {
                    errors.add("Line 1: Header must be exactly 'StudentName,Grade'");
                }
            }

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length != 2) {
                    errors.add("Line " + lineNumber + ": Each row must contain exactly 2 values separated by comma");
                    continue;
                }

                String name = parts[0].trim();
                if (!isValidName(name)) {
                    errors.add("Line " + lineNumber + ": Invalid student name - must contain only letters and spaces");
                }

                try {
                    double grade = Double.parseDouble(parts[1].trim());
                    if (grade < 0 || grade > 100) {
                        errors.add("Line " + lineNumber + ": Grade must be between 0 and 100");
                    }
                } catch (NumberFormatException e) {
                    errors.add("Line " + lineNumber + ": Invalid grade format - must be a number");
                }
            }
        }

        if (errors.isEmpty() && lineNumber == 0) {
            errors.add("File is empty");
        }

        return errors;
    }

    private static boolean isValidName(String name) {
        return name.matches("^[\\p{L} .'-]+$");
    }
}