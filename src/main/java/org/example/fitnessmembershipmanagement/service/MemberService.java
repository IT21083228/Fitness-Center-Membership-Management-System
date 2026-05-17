package org.example.fitnessmembershipmanagement.service;

import org.example.fitnessmembershipmanagement.model.Member;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private static final String FILE_PATH = "data/members.txt";

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
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
                    members.add(Member.fromFileString(line));
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Member read error: " + e.getMessage());
        }

        return members;
    }

    public void addMember(Member member) {
        member.setMemberId(generateMemberId());

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true));
            writer.write(member.toFileString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Member add error: " + e.getMessage());
        }
    }

    public Member getMemberById(String id) {
        for (Member member : getAllMembers()) {
            if (member.getMemberId().equalsIgnoreCase(id)) {
                return member;
            }
        }
        return null;
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> result = new ArrayList<>();

        for (Member member : getAllMembers()) {
            if (member.getMemberId().toLowerCase().contains(keyword.toLowerCase()) ||
                    member.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    member.getNic().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(member);
            }
        }

        return result;
    }

    public void updateMember(Member updatedMember) {
        List<Member> members = getAllMembers();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (Member member : members) {
                if (member.getMemberId().equalsIgnoreCase(updatedMember.getMemberId())) {
                    writer.write(updatedMember.toFileString());
                } else {
                    writer.write(member.toFileString());
                }
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Member update error: " + e.getMessage());
        }
    }

    public void deleteMember(String id) {
        List<Member> members = getAllMembers();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));

            for (Member member : members) {
                if (!member.getMemberId().equalsIgnoreCase(id)) {
                    writer.write(member.toFileString());
                    writer.newLine();
                }
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Member delete error: " + e.getMessage());
        }
    }

    public boolean isNicExists(String nic, String currentId) {
        for (Member member : getAllMembers()) {
            if (member.getNic().equalsIgnoreCase(nic)
                    && !member.getMemberId().equalsIgnoreCase(currentId == null ? "" : currentId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmailExists(String email, String currentId) {
        for (Member member : getAllMembers()) {
            if (member.getEmail().equalsIgnoreCase(email)
                    && !member.getMemberId().equalsIgnoreCase(currentId == null ? "" : currentId)) {
                return true;
            }
        }
        return false;
    }

    private String generateMemberId() {
        int max = 0;

        for (Member member : getAllMembers()) {
            try {
                int number = Integer.parseInt(member.getMemberId().replace("M", ""));
                if (number > max) max = number;
            } catch (Exception ignored) {}
        }

        return String.format("M%03d", max + 1);
    }
}