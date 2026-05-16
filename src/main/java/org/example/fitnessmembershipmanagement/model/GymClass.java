package org.example.fitnessmembershipmanagement.model;

public class GymClass extends BaseClassSession {

    public GymClass() {}

    public GymClass(String classId, String trainerId, String trainerName, String classType,
                    String dateTime, int duration, int totalSlots,
                    int availableSlots, String status) {
        super(classId, trainerId, trainerName, classType, dateTime, duration, totalSlots, availableSlots, status);
    }

    @Override
    public String getSessionType() {
        return "Gym Class";
    }

    public String toFileString() {
        return getClassId() + "|" + getTrainerId() + "|" + getTrainerName() + "|" +
                getClassType() + "|" + getDateTime() + "|" + getDuration() + "|" +
                getTotalSlots() + "|" + getAvailableSlots() + "|" + getStatus();
    }

    public static GymClass fromFileString(String line) {
        String[] d = line.split("\\|", -1);

        return new GymClass(
                d.length > 0 ? d[0] : "",
                d.length > 1 ? d[1] : "",
                d.length > 2 ? d[2] : "",
                d.length > 3 ? d[3] : "",
                d.length > 4 ? d[4] : "",
                d.length > 5 && !d[5].isEmpty() ? Integer.parseInt(d[5]) : 0,
                d.length > 6 && !d[6].isEmpty() ? Integer.parseInt(d[6]) : 0,
                d.length > 7 && !d[7].isEmpty() ? Integer.parseInt(d[7]) : 0,
                d.length > 8 ? d[8] : "Scheduled"
        );
    }
}