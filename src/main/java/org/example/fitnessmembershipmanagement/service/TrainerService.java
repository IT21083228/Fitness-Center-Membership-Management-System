package org.example.fitnessmembershipmanagement.service;

import org.example.fitnessmembershipmanagement.model.Trainer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainerService {

    private static final String FILE_PATH = "data/trainers.txt";

    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();
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
                    trainers.add(Trainer.fromFileString(line));
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Trainer read error: " + e.getMessage());
        }

        return trainers;
    }

    public void addTrainer(Trainer trainer) {
        trainer.setTrainerId(generateTrainerId());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(trainer.toFileString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Trainer add error: " + e.getMessage());
        }
    }

    public Trainer getTrainerById(String id) {
        for (Trainer trainer : getAllTrainers()) {
            if (trainer.getTrainerId().equalsIgnoreCase(id)) {
                return trainer;
            }
        }
        return null;
    }

    public void updateTrainer(Trainer updatedTrainer) {
        List<Trainer> trainers = getAllTrainers();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (Trainer trainer : trainers) {
                if (trainer.getTrainerId().equalsIgnoreCase(updatedTrainer.getTrainerId())) {
                    writer.write(updatedTrainer.toFileString());
                } else {
                    writer.write(trainer.toFileString());
                }
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Trainer update error: " + e.getMessage());
        }
    }

    public void deleteTrainer(String id) {
        List<Trainer> trainers = getAllTrainers();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (Trainer trainer : trainers) {
                if (!trainer.getTrainerId().equalsIgnoreCase(id)) {
                    writer.write(trainer.toFileString());
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Trainer delete error: " + e.getMessage());
        }
    }

    public boolean isNicExists(String nic, String currentId) {
        for (Trainer trainer : getAllTrainers()) {
            if (trainer.getNic().equalsIgnoreCase(nic)
                    && !trainer.getTrainerId().equalsIgnoreCase(currentId == null ? "" : currentId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmailExists(String email, String currentId) {
        for (Trainer trainer : getAllTrainers()) {
            if (trainer.getEmail().equalsIgnoreCase(email)
                    && !trainer.getTrainerId().equalsIgnoreCase(currentId == null ? "" : currentId)) {
                return true;
            }
        }
        return false;
    }

    private String generateTrainerId() {
        int max = 0;

        for (Trainer trainer : getAllTrainers()) {
            try {
                int number = Integer.parseInt(trainer.getTrainerId().replace("T", ""));
                if (number > max) max = number;
            } catch (Exception ignored) {}
        }

        return String.format("T%03d", max + 1);
    }
}