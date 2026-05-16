package org.example.tests;
import java.time.DayOfWeek;
import java.time.LocalTime;

import org.example.Main;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTesteAI {

    private Main app;


    // --- Teste generate de AI (Focus: Boundary Value Analysis) ---
    // AI-ului i s-a cerut să genereze teste de frontieră pentru un interval
    // existent.
    // Deși verifică limitele "mari", ratează complet granițele de 1 minut.

    @BeforeEach
    public void setUp() {
        app = new Main();
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    void testBoundaryExactMatch() {
        // TUESDAY în loc de MONDAY → nu intră în logica de conflict
        String result = app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testBoundaryAdjacentBefore() {
        String result = app.addAvailability(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testBoundaryAdjacentAfter() {
        String result = app.addAvailability(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    void testBoundaryZeroDuration() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }
    @Test
    void testBoundaryConflict() {
        // Intră pe C9 (startTime egal cu existentul)
        // dar NU testează C7 (startTime înăuntru) și C8 (endTime înăuntru)
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }
}