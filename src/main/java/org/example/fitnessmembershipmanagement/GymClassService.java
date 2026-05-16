package org.example.fitnessmembershipmanagement;

import org.example.fitnessmembershipmanagement.model.GymClass;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GymClassService {

    private static final String FILE_PATH = "data/classes.txt";

    public List<GymClass> getAllClasses() {
        List<GymClass> classes = new ArrayList<>();
        File file = new File(FILE_PATH);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    classes.add(GymClass.fromFileString(line));
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Class read error: " + e.getMessage());
        }

        return classes;
    }

    public void addClass(GymClass gymClass) {
        gymClass.setClassId(generateClassId());
        gymClass.setAvailableSlots(gymClass.getTotalSlots());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(gymClass.toFileString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Class add error: " + e.getMessage());
        }
    }

    public GymClass getClassById(String classId) {
        for (GymClass gymClass : getAllClasses()) {
            if (gymClass.getClassId().equalsIgnoreCase(classId)) {
                return gymClass;
            }
        }
        return null;
    }

    public void updateClass(GymClass updatedClass) {
        List<GymClass> classes = getAllClasses();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (GymClass gymClass : classes) {
                if (gymClass.getClassId().equalsIgnoreCase(updatedClass.getClassId())) {
                    writer.write(updatedClass.toFileString());
                } else {
                    writer.write(gymClass.toFileString());
                }
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Class update error: " + e.getMessage());
        }
    }

    public void deleteClass(String classId) {
        List<GymClass> classes = getAllClasses();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (GymClass gymClass : classes) {
                if (!gymClass.getClassId().equalsIgnoreCase(classId)) {
                    writer.write(gymClass.toFileString());
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Class delete error: " + e.getMessage());
        }
    }

    public List<GymClass> searchClasses(String keyword) {
        List<GymClass> result = new ArrayList<>();

        for (GymClass gymClass : getAllClasses()) {
            if (gymClass.getClassId().toLowerCase().contains(keyword.toLowerCase())
                    || gymClass.getTrainerName().toLowerCase().contains(keyword.toLowerCase())
                    || gymClass.getClassType().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(gymClass);
            }
        }

        return result;
    }

    private String generateClassId() {
        int max = 0;

        for (GymClass gymClass : getAllClasses()) {
            try {
                int number = Integer.parseInt(gymClass.getClassId().replace("C", ""));
                if (number > max) max = number;
            } catch (Exception ignored) {}
        }

        return String.format("C%03d", max + 1);
    }
}