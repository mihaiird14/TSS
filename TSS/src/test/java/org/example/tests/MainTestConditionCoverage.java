package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestConditionCoverage {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
    @Test
    public void testCC1_C1_True_DayNull() {
        String result = app.addAvailability(null, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    public void testCC2_C1False_C2False_C3False_C4False_Success() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testCC3_C2_True_StartNull() {
        String result = app.addAvailability(DayOfWeek.MONDAY, null, LocalTime.of(11, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCC4_C3_True_EndNull() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), null);
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCC5_C4_True_NotBefore() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCC6_C5True_C6True_C7True_C10True_Conflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testG5b_ConflictEndTimeOverlap() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testG5c_ConflictSameStartTime() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }
    @Test
    public void testG5d_NoConflictDifferentDay() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
    @Test
    public void testCC9_C6_False_DifferentDay() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    // CC10: C7=false, C8=false, C9=false, C10=false (interval adiacent, fără suprapunere)
    @Test
    public void testCC10_C7_C8_C9_False_NoConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testCC_EndTimeBeforeExistingStart() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
}
