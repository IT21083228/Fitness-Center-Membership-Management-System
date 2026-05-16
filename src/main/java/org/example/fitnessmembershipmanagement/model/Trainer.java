package org.example.fitnessmembershipmanagement.model;

public class Trainer extends Person {
    private String level;
    private int yearsOfExperience;

    public Trainer() {}

    public Trainer(String id, String name, String nic, String phone, String email,
                   String address, String gender, String joinDate, String status,
                   String level, int yearsOfExperience) {
        super(id, name, nic, phone, email, address, gender, joinDate, status);
        this.level = level;
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String getRole() {
        return "Trainer";
    }

    public String getTrainerId() { return getId(); }
    public void setTrainerId(String trainerId) { setId(trainerId); }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String toFileString() {
        return getId() + "|" + getName() + "|" + getNic() + "|" + getPhone() + "|" +
                getEmail() + "|" + getAddress() + "|" + getGender() + "|" +
                getJoinDate() + "|" + getStatus() + "|" + level + "|" + yearsOfExperience;
    }

    public static Trainer fromFileString(String line) {
        String[] d = line.split("\\|", -1);

        return new Trainer(
                d.length > 0 ? d[0] : "",
                d.length > 1 ? d[1] : "",
                d.length > 2 ? d[2] : "",
                d.length > 3 ? d[3] : "",
                d.length > 4 ? d[4] : "",
                d.length > 5 ? d[5] : "",
                d.length > 6 ? d[6] : "",
                d.length > 7 ? d[7] : "",
                d.length > 8 ? d[8] : "Available",
                d.length > 9 ? d[9] : "Junior",
                d.length > 10 && !d[10].isEmpty() ? Integer.parseInt(d[10]) : 0
        );
    }
}