package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestPartitionare {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }

    // --- Partiţionare de echivalenţă (Equivalence Partitioning) ---
    // G1 (D1, T1, C1): Test valid complet
    @Test
    public void testG1_ValidDay_ValidTime_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    // G2 (D2, T1, C1): Clasă invalidă - Ziua este null
    @Test
    public void testG2_NullDay_ValidTime_NoConflict() {
        String result = app.addAvailability(null, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G3 (D1, T2, C1): Clasă invalidă - Timp ilogic (start >= end)
    @Test
    public void testG3_ValidDay_InvalidTime_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // G4 (D1, T3, C1): Clasă invalidă - Timp null
    @Test
    public void testG4_ValidDay_NullTime_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, null, LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }
    // G5 (D1, T1, C2): Clasă invalidă - Există conflict
    @Test
    public void testG5_ValidDay_ValidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30));
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
}