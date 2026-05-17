package org.example.fitnessmembershipmanagement.model;

public class ClassAssignment {
    private String assignmentId;
    private String classId;
    private String memberId;
    private String memberName;

    public ClassAssignment() {}

    public ClassAssignment(String assignmentId, String classId, String memberId, String memberName) {
        this.assignmentId = assignmentId;
        this.classId = classId;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public String getAssignmentId() { return assignmentId; }
    public void setAssignmentId(String assignmentId) { this.assignmentId = assignmentId; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String toFileString() {
        return assignmentId + "|" + classId + "|" + memberId + "|" + memberName;
    }

    public static ClassAssignment fromFileString(String line) {
        String[] d = line.split("\\|", -1);

        return new ClassAssignment(
                d.length > 0 ? d[0] : "",
                d.length > 1 ? d[1] : "",
                d.length > 2 ? d[2] : "",
                d.length > 3 ? d[3] : ""
        );
    }
}