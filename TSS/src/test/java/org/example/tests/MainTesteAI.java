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

    // --- Teste generate de AI (Focus: Boundary Value Analysis) ---
    // AI-ului i s-a cerut să genereze teste de frontieră pentru un interval
    // existent.
    // Deși verifică limitele "mari", ratează complet granițele de 1 minut.

    @Test
    void testBoundaryExactMatch() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    void testBoundaryAdjacentBefore() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testBoundaryAdjacentAfter() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testBoundaryZeroDuration() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }
}
