package org.example.tests;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.example.Main;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTesteAI {

    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }

    // --- Teste generate de ChatGPT ---

    @Test
    void testNullDayOfWeek() {
        String result = app.addAvailability(null,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    void testNullStartTime() {
        String result = app.addAvailability(DayOfWeek.MONDAY,
                null, LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    void testStartEqualsEnd() {
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(9, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    void testStartGreaterThanEnd() {
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(11, 0), LocalTime.of(9, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    void testSuccessfulAdd() {
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testConflictSameInterval() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    void testNoConflictDifferentDay() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.TUESDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testConflictOverlapStart() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    void testConflictOverlapEnd() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(11, 0), LocalTime.of(13, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    void testAdjacentIntervalsNoConflict() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testNoConflictBeforeExisting() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testNoConflictAfterExisting() {
        app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
}