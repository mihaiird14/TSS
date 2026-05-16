package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestDecisionCoverage {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
    @Test
    public void testDC1_D1_True_DayNull() {
        String result = app.addAvailability(null, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    public void testDC2_D1_False_D2_True_InvalidInterval() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testDC3_D3_False_D6_False_EmptyListSuccess() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testDC4_D4_True_D5_True_D6_True_Conflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(13, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testDC5_D4_False_ThenD3_False_D6_False_Success() {
        app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
    @Test
    public void testDC_StartNull() {
        String result = app.addAvailability(DayOfWeek.MONDAY, null, LocalTime.of(11, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // Linia 20: endTime == null → true
    @Test
    public void testDC_EndNull() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), null);
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // Linia 29: startTime.isBefore(getEndTime()) = false
// startTime(12:00) nu e before endTime_existent(11:00)
    @Test
    public void testDC_StartNotBeforeEndExistent() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    // Linia 31: startTime.equals(startExistent) → true
    @Test
    public void testDC_EqualStart() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    // Linia 31: startTime.equals(startExistent) → false (și C7=false, C8=false)
    @Test
    public void testDC_NotEqualStart_NoConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
    @Test
    public void testDC_C7_True() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }
}
