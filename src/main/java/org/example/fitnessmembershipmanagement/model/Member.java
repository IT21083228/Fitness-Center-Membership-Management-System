package org.example.fitnessmembershipmanagement.model;

public class Member extends Person {
    private String packageType;

    public Member() {}

    public Member(String id, String name, String nic, String phone, String email,
                  String address, String gender, String joinDate, String status, String packageType) {
        super(id, name, nic, phone, email, address, gender, joinDate, status);
        this.packageType = packageType;
    }

    @Override
    public String getRole() {
        return "Member";
    }

    public String getMemberId() { return getId(); }
    public void setMemberId(String memberId) { setId(memberId); }

    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }

    public String toFileString() {
        return getId() + "|" + getName() + "|" + getNic() + "|" + getPhone() + "|" +
                getEmail() + "|" + getAddress() + "|" + getGender() + "|" +
                getJoinDate() + "|" + getStatus() + "|" + packageType;
    }

    public static Member fromFileString(String line) {
        String[] d = line.split("\\|", -1);

        return new Member(
                d.length > 0 ? d[0] : "",
                d.length > 1 ? d[1] : "",
                d.length > 2 ? d[2] : "",
                d.length > 3 ? d[3] : "",
                d.length > 4 ? d[4] : "",
                d.length > 5 ? d[5] : "",
                d.length > 6 ? d[6] : "",
                d.length > 7 ? d[7] : "",
                d.length > 8 ? d[8] : "Active",
                d.length > 9 ? d[9] : "Standard"
        );
    }
}