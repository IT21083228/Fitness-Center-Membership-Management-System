package org.example.fitnessmembershipmanagement.service;

import org.example.fitnessmembershipmanagement.model.ClassAssignment;
import org.example.fitnessmembershipmanagement.model.GymClass;
import org.example.fitnessmembershipmanagement.model.Member;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassAssignmentService {

    private static final String FILE_PATH = "data/class_assignments.txt";

    private final GymClassService gymClassService;
    private final MemberService memberService;

    public ClassAssignmentService(GymClassService gymClassService, MemberService memberService) {
        this.gymClassService = gymClassService;
        this.memberService = memberService;
    }

    public List<ClassAssignment> getAllAssignments() {
        List<ClassAssignment> assignments = new ArrayList<>();
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
                    assignments.add(ClassAssignment.fromFileString(line));
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Assignment read error: " + e.getMessage());
        }

        return assignments;
    }

    public List<ClassAssignment> getAssignmentsByClassId(String classId) {
        List<ClassAssignment> result = new ArrayList<>();

        for (ClassAssignment assignment : getAllAssignments()) {
            if (assignment.getClassId().equalsIgnoreCase(classId)) {
                result.add(assignment);
            }
        }

        return result;
    }

    public boolean assignMember(String classId, String memberId) {
        GymClass gymClass = gymClassService.getClassById(classId);
        Member member = memberService.getMemberById(memberId);

        if (gymClass == null || member == null) return false;
        if (gymClass.getAvailableSlots() <= 0) return false;
        if (isAlreadyAssigned(classId, memberId)) return false;

        ClassAssignment assignment = new ClassAssignment(
                generateAssignmentId(),
                classId,
                memberId,
                member.getName()
        );

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(assignment.toFileString());
            writer.newLine();
            writer.close();

            gymClass.setAvailableSlots(gymClass.getAvailableSlots() - 1);
            gymClassService.updateClass(gymClass);

            return true;

        } catch (IOException e) {
            System.out.println("Assignment add error: " + e.getMessage());
            return false;
        }
    }

    public boolean isAlreadyAssigned(String classId, String memberId) {
        for (ClassAssignment assignment : getAllAssignments()) {
            if (assignment.getClassId().equalsIgnoreCase(classId)
                    && assignment.getMemberId().equalsIgnoreCase(memberId)) {
                return true;
            }
        }

        return false;
    }

    private String generateAssignmentId() {
        int max = 0;

        for (ClassAssignment assignment : getAllAssignments()) {
            try {
                int number = Integer.parseInt(assignment.getAssignmentId().replace("A", ""));
                if (number > max) max = number;
            } catch (Exception ignored) {}
        }

        return String.format("A%03d", max + 1);
    }
}