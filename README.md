# Documentație

| | |
|:--|:--|
| [Prezentare](#) | [Demo](#) |
| [Raport AI](./RaportAI.md) | [Testare funcțională](./TestareFunctionala.md) |
| [Testare structurală](./testareStructurala.md) | |

---

## 1. Configurație hardware și software

### 1.1 Configurație hardware

| Componentă | Detalii |
|:-----------|:--------|
| Procesor | AMD Ryzen 5 |
| Memorie RAM | 16 GB |
| Sistem de operare | Windows 11 |
| Mașină virtuală | Nu s-a utilizat |

### 1.2 Configurație software

| Tool / Tehnologie | Versiune |
|:------------------|:---------|
| Java (JDK) | 23 |
| IntelliJ IDEA | 2024.3.4.1 |
| JUnit | 5.x |
| Maven | 3.x |
| PIT Mutation Testing | 1.4.11 |
| ChatGPT (OpenAI) | GPT-4o |

---

## 2. Structura proiectului

```
src/
├── main/java/org/example/
│   ├── Main.java               # Clasa principală cu metoda addAvailability
│   └── Availability.java       # Modelul de date pentru un interval
└── test/java/org/example/tests/
    └── MainTest.java           # Suita completă de teste JUnit 5
```

**Metoda testată:**

```java
public String addAvailability(DayOfWeek dayOfWeek,
                               LocalTime startTime,
                               LocalTime endTime)
```

| Condiție | Mesaj returnat |
|:---------|:---------------|
| `dayOfWeek == null` | `"Eroare: Ziua săptămânii este obligatorie."` |
| `startTime` sau `endTime` null, sau `startTime >= endTime` | `"Eroare: Ora de început trebuie să fie strict mai mică decât ora de sfârșit."` |
| Suprapunere cu interval existent | `"Conflict: Există deja un interval setat în această perioadă."` |
| Parametri valizi, fără conflict | `"Succes: Intervalul a fost adăugat."` |

---

## 3. Strategia de testare

Testarea a fost organizată în patru etape complementare:

1. **Testare funcțională (black-box)** – pe baza specificației, fără acces la cod:
   - Partiționare de echivalență (18 clase globale)
   - Analiza valorilor de frontieră (6 teste)
   - Partiționarea în categorii (8 teste)

2. **Testare structurală (white-box)** – pe baza codului sursă și a grafului CFG:
   - Statement Coverage
   - Decision / Branch Coverage
   - Condition Coverage
   - Basis Path (Complexitate ciclomatică McCabe)

3. **Analiză de mutanți** – evaluarea calității suitei de teste prin PIT

4. **Generare automată AI** – compararea suitei proprii cu teste generate de ChatGPT GPT-4o

---

## 4. Testare funcțională

### 4.1 Partiționare de echivalență

Clasele de echivalență identificate pentru cei trei parametri și starea listei sunt detaliate în fișierul [`TestareFunctionala.md`](./TestareFunctionala.md).

**Sumar clase globale:** 18 combinații D × T × C, acoperind toate scenariile valide și invalide.

**Observație privind D3 (format invalid enum):** `DayOfWeek` fiind un enum Java, valorile invalide nu pot fi transmise direct la compilare. Testele G13–G18 simulează acest scenariu prin `DayOfWeek.valueOf("Mondayy")` care aruncă `IllegalArgumentException`, verificat cu `assertThrows`.

### 4.2 Analiza valorilor de frontieră

Interval de referință utilizat: `[10:00 – 11:00]`

| Test | Valoare testată | Rezultat așteptat |
|:-----|:----------------|:------------------|
| BV1 | `09:00 – 09:00` (S = E) | Eroare |
| BV2 | `09:00 – 09:01` (S = E − 1 min) | Succes |
| BV3 | `09:00 – 10:00` (E = S_ex) | Succes |
| BV4 | `11:00 – 12:00` (S = E_ex) | Succes |
| BV5 | `09:00 – 10:01` (E = S_ex + 1 min) | Conflict |
| BV6 | `10:59 – 12:00` (S = E_ex − 1 min) | Conflict |

### 4.3 Partiționarea în categorii

| Caz | Descriere |
|:----|:----------|
| D2 | `dayOfWeek` null |
| D3 | `dayOfWeek` format invalid |
| D1T2 | Zi validă, `S = E` |
| D1T3 | Zi validă, `S > E` |
| D1T4 | Zi validă, timp null |
| D1T1L1 | Zi și timp valide, fără suprapunere |
| D1T1L2 | Zi și timp valide, suprapunere la început |
| D1T1L3 | Zi și timp valide, suprapunere la sfârșit |

---

## 5. Testare structurală

Documentația completă se găsește în [`testareStructurala.md`](./testareStructurala.md).

### 5.1 Graful de flux de control (CFG)

<img width="719" height="782" alt="diagrama" src="https://github.com/user-attachments/assets/8e7fba52-327f-4121-9624-e872c72b393f" />


| Nod | Instrucțiune |
|:----|:-------------|
| N1 | `if (dayOfWeek == null)` |
| N2 | `return` eroare ziua |
| N3 | `if (startTime == null \|\| endTime == null \|\| !isBefore)` |
| N4 | `return` eroare interval |
| N5 | `for each t in trainerAvailabilities` |
| N6 | `if (t.dayOfWeek == dayOfWeek)` |
| N7 | `if (suprapunere)` – 3 sub-condiții în OR |
| N9 | `if (conflictGasit)` |
| N9 | `return` conflict |
| N10 | `return` succes |

### 5.2 Complexitate ciclomatică McCabe

- **n** (noduri) = 10
- **e** (arce) = 16 → 12 interne + 4 adăugate (N2→N1, N4→N1, N9→N1, N10→N1)
- **V(G) = e − n + 2 = 17 − 11 + 2 = 8**

Rezultă **8 circuite independente** și cel puțin 8 căi de test necesare.

---

## 6. Raport Coverage

### 6.1 Rezultate finale

| Clasă | Class, % | Method, % | Line, % | Branch, % |
|:------|:---------|:----------|:--------|:----------|
| `Main` | 100% | 100% | 100% | 100% |
| `Availability` | 100% | 100% | 100% | 100% |

### 6.2 Captură ecran

<!-- Adaugă aici captura de ecran cu rezultatele coverage din IntelliJ -->
![Coverage Report](./coverage_report.png)

### 6.3 Interpretare

Acoperirea de 100% pe linii și metode pentru ambele clase confirmă că suita de teste execută fiecare instrucțiune din cod cel puțin o dată. Atingerea acestui nivel a necesitat adăugarea testului `testAvailabilityGetters()` pentru metodele getter din `Availability`, și a testelor `testCoverage_EndTimeEqualsExistingEndTime()` și `testCoverage_SameDay_NoOverlapBefore()` pentru ramurile rămase neacoperite din logica de conflict.

---

## 7. Raport mutanți (PIT Mutation Testing)

### 7.1 Rularea PIT

```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

Raportul HTML generat se găsește în `target/pit-reports/`.

### 7.2 Rezultate

<!-- Adaugă aici captura de ecran cu raportul PIT -->
![PIT Report](./pit_report.png)

| Metric | Valoare |
|:-------|:--------|
| Mutanți generați | 18 |
| Mutanți uciși | 18 |
| Mutanți supraviețuitori | 0 |
| Mutation Score | 100% |

**Exemplificarea Cerinței pentru Teste adiționale și repare mutanți:**
Deși suita extinsă atinge din prima un scor de 100% și nu mai lasă nimic în viață, conform cerințelor ("analiză raport creat de generatorul de mutanți, teste suplimentare pentru a omorî 2 dintre mutanții neechivalenți rămași în viață pe exemple proprii"), am adăugat și simulat acest comportament pe un set redus la finalul clasei `MainTest.java`. 

Acolo există comentarii clare care ilustrează scenariul:
1. **Un test pentru un mutant** - testul inițial și slab capabil care "dă rateu" (de exemplu, omiterea cazului exact `startTime == t.getStartTime()`).
2. **Testul reparat** - care suprasolicită acest caz limită și ucide parametrul mutant `startTime.equals(...) -> false`. 

Se pot vizualiza în fișier noile scenarii adăugate pe final (secțiunea "Partea 5: Analiza si Omorârea Mutanților").

## 8. Referințe bibliografice

| Tool | Link |
|:-----|:-----|
| JUnit 5 | https://junit.org/junit5/docs/current/user-guide/ |
| PIT Mutation Testing | https://pitest.org |
| IntelliJ Coverage | https://www.jetbrains.com/help/idea/code-coverage.html |
| JaCoCo | https://www.jacoco.org/jacoco/ |
| ChatGPT (OpenAI) | https://platform.openai.com |
| Maven | https://maven.apache.org |
