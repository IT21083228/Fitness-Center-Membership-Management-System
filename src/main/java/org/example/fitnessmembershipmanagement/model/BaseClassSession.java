package org.example.fitnessmembershipmanagement.model;

public abstract class BaseClassSession {
    private String classId;
    private String trainerId;
    private String trainerName;
    private String classType;
    private String dateTime;
    private int duration;
    private int totalSlots;
    private int availableSlots;
    private String status;

    public BaseClassSession() {}

    public BaseClassSession(String classId, String trainerId, String trainerName, String classType,
                            String dateTime, int duration, int totalSlots,
                            int availableSlots, String status) {
        this.classId = classId;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.classType = classType;
        this.dateTime = dateTime;
        this.duration = duration;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.status = status;
    }

    public abstract String getSessionType();

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getTotalSlots() { return totalSlots; }
    public void setTotalSlots(int totalSlots) { this.totalSlots = totalSlots; }

    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}