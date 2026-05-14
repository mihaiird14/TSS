package org.example.tests;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.example.Main;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTesteAI {

    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }

    // --- Teste slabe generate intenționat pentru demonstrație ---
    // AI-ul a generat teste redundante și a ratat branch-urile și edge case-urile
    // (precum startTime == null sau error checking pentru start > end).

    @Test
    void testSuccessfulAdd() {
        String result = app.addAvailability(DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testSuccessfulAddV2() {
        // AI-ul face același test ca mai sus, redundant
        String result = app.addAvailability(DayOfWeek.TUESDAY,
                LocalTime.of(14, 0), LocalTime.of(16, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testConflictSameInterval() {
        app.addAvailability(DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }
}
