package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestFrontiera {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
    // --- Partea 2: Analiza valorilor de frontieră (Boundary Value Analysis) ---
    // Total 6 teste - interval existent: 10:00 - 11:00

    @Test
    public void testBV1_MinimumInvalidInterval_StartEqualsEnd() {
        String result = app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(9, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testBV2_MinimumValidInterval_StartEqualsEndMinus1Min() {
        String result = app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(9, 1));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testBV3_Valid_TouchesStart_EndEqualsStartExistent() {
        app.addAvailability(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testBV4_Valid_TouchesEnd_StartEqualsEndExistent() {
        app.addAvailability(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(12, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testBV5_Conflict_Overlap1MinStart_EndEqualsStartExistentPlus1Min() {
        app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(10, 1));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testBV6_Conflict_Overlap1MinEnd_StartEqualsEndExistentMinus1Min() {
        app.addAvailability(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.SATURDAY, LocalTime.of(10, 59), LocalTime.of(12, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }
    @Test
    public void testBV0_NullDay() {
        String result = app.addAvailability(null, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }
}
