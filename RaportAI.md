# Raport privind generarea de teste asistată de AI

Acest raport analizează eficiența utilizării unui asistent AI (în acest caz, ChatGPT - modelul GPT-4o) pentru generarea de teste software. Scopul este de a compara suita proprie de teste, construită riguros (pe baza partițiilor de echivalență și valorilor de frontieră), cu testele autogenerate pornind de la un prompt simplu, evidențiind diferențele și limitările fundamentale ale AI-ului în lipsa unui ghidaj detaliat.

## 1. Context și Metodologie

Pentru acest experiment, ne-am axat specific pe categoria **Analiza Valorilor de Frontieră (Boundary Value Analysis)**. Am solicitat AI-ului să genereze un set de teste care să verifice granițele unui interval existent `[10:00 - 11:00]`. Scopul a fost de a demonstra că **AI-ul tinde să ofere abordări de testare superficiale**, ratând extremele de finețe pe care un inginer QA uman le-ar identifica.

### 1.1. Prompt-ul utilizat

Am oferit un prompt concentrat pe tehnica BVA:

> "Generează teste unitare cu JUnit 5 folosind metoda Analiza Valorilor de Frontieră (Boundary Value Analysis) pentru o clasă Java ce programează intervale per zi, metoda `addAvailability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime)`. Presupunem că în listă există deja intervalul 10:00 - 11:00 într-o zi de MONDAY. Testează cu atenție marginile intervalelor pentru a vedea dacă se suprapun sau dacă pot fi adăugate adiacent."

### 1.2. Răspunsul AI-ului (Testele generate)

Codul generat de AI se află în fișierul `MainTesteAI.java`. Asistentul a creat teste logice și redundante care verifică doar atingerile "fixe":

```java
// Exemplu limitat de frontieră generat de AI (MainTesteAI.java)
@Test
void testBoundaryAdjacentBefore() {
    app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0));
    String result = app.addAvailability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
    assertEquals("Succes: Intervalul a fost adăugat.", result);
}
/* A verificat 10:00 - 11:00 (exact), 09:00 - 10:00 (înainte), 11:00 - 12:00 (după) și 10:00 - 10:00 (suprapunere nulă). */
```

## 2. Rularea testelor autogenerate și Interpretare

La rularea suitei generate de AI folosind plugin-ul de recunoaștere a acoperirii (Coverage) din IntelliJ IDEA / analiză prin pitest, deși coverage-ul pe linii a fost rezonabil, a arătat lacune de calitate masive în eficacitatea limitelor.

*-Aici se va insera captura de ecran cu rularea / coverage-ul testelor BVA generate de AI-*
`![Screeshot Coverage AI](link_catre_poza_coverage_ai)`

### 2.1. Comparație cu suita proprie BVA și diferențe majore

Față de suita noastră documentată (în `TestareFunctionala.md` la secțiunea BVA), diferența clară reiese din atenția la granularitatea datelor:
- **Limite (Frontiere) vitale ratate:** AI-ul a interpretat sintagma „valori de frontieră” doar raportat la oră fixă (cazurile `E = S_ex` adică `09:00-10:00` și `S = E_ex` adică `11:00-12:00`). 
- **Ce am testat noi (BVA real):** Noi am considerat unitatea de timp `LocalTime` ca având un pas de minim **1 minut**. Astfel, frontierele noastre critice includ acele depășiri milimetrice esențiale care prind the "Off-by-one errors" în cod (ex: Suprapunerea la `S_ex + 1 min` adică `09:00 - 10:01` sau `E_ex - 1 min` adică `10:59 - 12:00`). AI-ul a ignorat total granițele stricte de $+1$ minut și $-1$ minut (rezoluția maximă a valorii conform logicii folosite).

## 3. Concluzii

Testele de frontieră autogenerate aflate în `MainTesteAI.java` sunt inferioare unei testări umane meticuloase. Deși ele par corecte, ele lasă un unghi mort pe exact motivul pentru care facem Boundary Value Analysis: erorile de un unghi `> =` vs `>`.

Acest comportament demonstrează că utilizarea unui tool AI pentru designul testelor necesită **prompt engineering riguros**. Trebuie să menționăm granularitatea direct asistentului (ex: "ia în considerare incrementul de 1 minut"), altfel seturile de teste generate vor rămâne conceptual generaliste și nu vor constitui o barieră completă contra bug-urilor.

## 4. Referințe bibliografice

1. OpenAI, *ChatGPT - Model GPT-4o*, https://platform.openai.com/ (accesat Mai 2026).
2. Documentație oficială JUnit 5, *User Guide*, https://junit.org/junit5/docs/current/user-guide/ (accesat Mai 2026).
3. Documentație JetBrains IntelliJ IDEA, *Code Coverage*, https://www.jetbrains.com/help/idea/code-coverage.html (accesat Mai 2026).
