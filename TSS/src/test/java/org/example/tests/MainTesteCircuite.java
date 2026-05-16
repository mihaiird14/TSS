package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTesteCircuite {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
    @Test
    public void testIP1_Path1_DayNull() {
        String result = app.addAvailability(null, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    public void testIP2_Path2_InvalidInterval() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testIP3_Path3_EmptyListSuccess() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testIP4_Path4_DifferentDayInList() {
        app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testIP5_Path5_SameDayNoOverlap() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testIP6_Path6_ConflictDetected() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(13, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testIP7_Path7_TwoIterationsDifferentThenSameNoOverlap() {
        app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
}
