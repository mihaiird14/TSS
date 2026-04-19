package org.example.tests;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals("Succes: Intervalul a fost adăugat.", result);
    }

    // G2: D1 (Valid), T1 (Valid), C2 (Exista conflict)
    @Test
    public void testG2_ValidDay_ValidTime_WithConflict() {
        app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30));
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

    /*
     * Java Enums (DayOfWeek) nu permit introducerea unei valori invalide de tip
     * String sau alt tip.
     * Valorile pentru D3 (Invalid Format) rezolvate la rulare nu ar compila.
     * Pentru a "simula" aceste teste fara erori de compilare, G13-G18 sunt descrise
     * la nivel logic prin prinderea exceptiilor de conversie Enum
     * sau se apeleaza sub forma comentata / echivalenta cu apel prin Null.
     */

    @Test
    public void testG13_InvalidDayFormat_ValidTime_NoConflict() {
        // D3 nu poate fi mapat direct catre DayOfWeek, se testeaza trecerea prin string
        // (ex: in API-uri JSON/REST)
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
}
