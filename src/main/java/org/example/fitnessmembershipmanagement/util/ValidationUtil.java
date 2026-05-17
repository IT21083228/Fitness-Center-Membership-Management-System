package org.example.fitnessmembershipmanagement.util;

import java.time.LocalDate;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^0[0-9]{9}$");
    }

    public static boolean isValidNIC(String nic) {
        return nic != null && (
                nic.matches("^[0-9]{9}[vVxX]$") ||
                        nic.matches("^[0-9]{12}$")
        );
    }

    public static boolean isNotFutureDate(String date) {
        if (date == null || date.isEmpty()) return false;
        return !LocalDate.parse(date).isAfter(LocalDate.now());
    }
}