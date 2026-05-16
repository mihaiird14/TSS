package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTestCategorii {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }
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
}
