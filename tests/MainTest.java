package org.example.tests;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.example.Availability;
import org.example.Main;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }

    // --- Partea 1: Partiţionare de echivalenţă (Equivalence Partitioning) ---
    // Total 18 teste

    // G1: D1 (Valid), T1 (Valid), C1 (Nu exista conflict)
    @Test
    public void testG1_ValidDay_ValidTime_NoConflict() {
        // Adăugăm un interval într-o altă zi pentru a parcurge ramura "if
        // (!t.getDayOfWeek().equals(...))"
        app.addAvailability(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));

        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    // G2: D1 (Valid), T1 (Valid), C2 (Exista conflict)
    @Test
    public void testG2_ValidDay_ValidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));

        // Modificăm testul să aibă exact aceeași oră de start pentru a atinge a 3-a
        // condiție din conflict logic
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    // G3: D1 (Valid), T2 (Invalid Time - Start >= End), C1 (Nu exista conflict)
    @Test
    public void testG3_ValidDay_InvalidTime_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // G4: D1 (Valid), T2 (Invalid Time), C2 (Conflict)
    @Test
    public void testG4_ValidDay_InvalidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // G5: D1 (Valid), T3 (Time null), C1 (No conflict)
    @Test
    public void testG5_ValidDay_NullTime_NoConflict() {
        String result = app.addAvailability(DayOfWeek.MONDAY, null, LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // G6: D1 (Valid), T3 (Time null), C2 (Conflict)
    @Test
    public void testG6_ValidDay_NullTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 30), null);
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    // G7: D2 (Null day), T1 (Valid Time), C1 (No conflict)
    @Test
    public void testG7_NullDay_ValidTime_NoConflict() {
        String result = app.addAvailability(null, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G8: D2 (Null day), T1 (Valid Time), C2 (Conflict)
    @Test
    public void testG8_NullDay_ValidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(null, LocalTime.of(9, 30), LocalTime.of(10, 30));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G9: D2 (Null day), T2 (Invalid Time), C1 (No conflict)
    @Test
    public void testG9_NullDay_InvalidTime_NoConflict() {
        String result = app.addAvailability(null, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G10: D2 (Null day), T2 (Invalid Time), C2 (Conflict)
    @Test
    public void testG10_NullDay_InvalidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(null, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G11: D2 (Null day), T3 (Null time), C1 (No conflict)
    @Test
    public void testG11_NullDay_NullTime_NoConflict() {
        String result = app.addAvailability(null, null, LocalTime.of(10, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    // G12: D2 (Null day), T3 (Null time), C2 (Conflict)
    @Test
    public void testG12_NullDay_NullTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(null, LocalTime.of(9, 30), null);
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    public void testG13_InvalidDayFormat_ValidTime_NoConflict() {
        // D3 nu poate fi mapat direct catre DayOfWeek, se testeaza trecerea prin string
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DayOfWeek d = DayOfWeek.valueOf("Mondayy"); // Invalid
            app.addAvailability(d, LocalTime.of(9, 0), LocalTime.of(10, 0));
        });
    }

    @Test
    public void testG14_InvalidDayFormat_ValidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("Mondayy"), LocalTime.of(9, 30), LocalTime.of(10, 30));
        });
    }

    @Test
    public void testG15_InvalidDayFormat_InvalidTime_NoConflict() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("123"), LocalTime.of(11, 0), LocalTime.of(10, 0));
        });
    }

    @Test
    public void testG16_InvalidDayFormat_InvalidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("123"), LocalTime.of(11, 0), LocalTime.of(10, 0));
        });
    }

    @Test
    public void testG17_InvalidDayFormat_NullTime_NoConflict() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("Abc"), null, LocalTime.of(10, 0));
        });
    }

    @Test
    public void testG18_InvalidDayFormat_NullTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("Abc"), LocalTime.of(9, 30), null);
        });
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

    // --- Partea 3: Partitionarea in categorii (Category-Partitioning) ---
    // Total 8 teste

    @Test
    public void testCP1_D2_DayNull() {
        String result = app.addAvailability(null, LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", result);
    }

    @Test
    public void testCP2_D3_DayInvalidFormat() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            app.addAvailability(DayOfWeek.valueOf("NotADay"), LocalTime.of(12, 0), LocalTime.of(13, 0));
        });
    }

    @Test
    public void testCP3_D1T2_ValidDay_TimeStartEqualsEnd() {
        String result = app.addAvailability(DayOfWeek.SUNDAY, LocalTime.of(14, 0), LocalTime.of(14, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCP4_D1T3_ValidDay_TimeStartGreaterThanEnd() {
        String result = app.addAvailability(DayOfWeek.SUNDAY, LocalTime.of(15, 0), LocalTime.of(14, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCP5_D1T4_ValidDay_TimeNull() {
        String result = app.addAvailability(DayOfWeek.SUNDAY, null, LocalTime.of(14, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }

    @Test
    public void testCP6_D1T1L1_ValidDayTime_NoOverlap() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    @Test
    public void testCP7_D1T1L2_ValidDayTime_OverlapAtStart() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    @Test
    public void testCP8_D1T1L3_ValidDayTime_OverlapAtEnd() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(13, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", result);
    }

    // --- Partea 4: Testarea circuitelor independente ---

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