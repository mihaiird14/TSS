package org.example.tests;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMutanti {
    private Main app;

    @BeforeEach
    public void setUp() {
        app = new Main();
    }

    @Test
    public void testIP2_Path2_InvalidInterval() {
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(10, 0));
        assertEquals("Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit.", result);
    }
    /*
    @Test
    public void test() {
        app.addAvailability(FRIDAY, 10:00, 12:00);
        String result = app.addAvailability(FRIDAY, 9:00, 11:59);
        assertEquals("Conflict...", result);
    }

    */
    @Test
    public void testMutant2_Reparat_KillIsBeforeEndTime() {
        // Omoară linia 16: return eroare dayOfWeek
        String r0 = app.addAvailability(null, LocalTime.of(10, 0), LocalTime.of(12, 0));
        assertEquals("Eroare: Ziua săptămânii este obligatorie.", r0);

        app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(12, 0));

        // Omoară linia 28: startTime strict după startTime existent
        String r1 = app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(11, 0), LocalTime.of(13, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", r1);

        // Omoară linia 29: endTime strict înainte de endTime existent
        String r2 = app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals("Conflict: Există deja un interval setat în această perioadă.", r2);

        // Omoară linia 40: conflictGasit=false → ramura succes
        String r3 = app.addAvailability(DayOfWeek.FRIDAY, LocalTime.of(13, 0), LocalTime.of(14, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", r3);
    }
}
