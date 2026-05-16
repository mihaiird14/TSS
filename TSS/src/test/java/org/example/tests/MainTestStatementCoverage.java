package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestStatementCoverage {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
    // T1: listă needă cu conflict
    @Test
    public void testT1_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    // T2: listă goală, succes
    @Test
    public void testT2_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }
}
