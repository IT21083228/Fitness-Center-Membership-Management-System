package org.example.fitnessmembershipmanagement.model;

public abstract class Person {
    private String id;
    private String name;
    private String nic;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String joinDate;
    private String status;

    public Person() {}

    public Person(String id, String name, String nic, String phone, String email,
                  String address, String gender, String joinDate, String status) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.joinDate = joinDate;
        this.status = status;
    }

    public abstract String getRole();

    public String getDisplayName() {
        return getRole() + " - " + name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}