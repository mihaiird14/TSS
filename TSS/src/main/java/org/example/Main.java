package org.example;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private List<Availability> trainerAvailabilities = new ArrayList<>();
   //3 parametrii
    public String addAvailability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {

        // Condiție simplă (fără else)
        if (dayOfWeek == null) {
            return "Eroare: Ziua săptămânii este obligatorie.";
        }

        // Condiție compusă
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) {
            return "Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.";
        }

        boolean conflictFound = false;

        // Instrucțiune repetitivă
        for (Availability t : trainerAvailabilities) {
            if (t.getDayOfWeek().equals(dayOfWeek)) {
                if ((startTime.isBefore(t.getEndTime()) && startTime.isAfter(t.getStartTime())) ||
                        (endTime.isAfter(t.getStartTime()) && endTime.isBefore(t.getEndTime())) ||
                        (startTime.equals(t.getStartTime()))) {

                    conflictFound = true;
                    break;
                }
            }
        }

        // Condițională cu ELSE
        if (conflictFound) {
            return "Conflict: Există deja un interval setat în această perioadă.";
        } else {
            Availability newAvailability = new Availability(dayOfWeek, startTime, endTime);
            trainerAvailabilities.add(newAvailability);
            return "Succes: Intervalul a fost adăugat.";
        }
    }
}