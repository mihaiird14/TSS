# Testare unitară în Java 
## Echipă
    - Coman Ioan Alexandru (334)
    - Ionescu Alexandru Ioan (334)
    - Iordache Mihai (334)
    - Predescu Andrei (334)
| | |
|:--|:--|
| [Prezentare](./prezentare.pptx) | [Demo](https://youtu.be/zWRsbMo__1A) |
| [Testare funcțională](./TestareFunctionala.md) |
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

Se testează o funcție care verifică dacă o zi și un interval orar sunt disponibile pentru o programare nouă. Mai precis, funcția primește 3 parametri: zi (ziua săptămânii), oraInceput și oraSfarsit (intervalul orar dorit), și verifică dacă aceștia sunt valizi și dacă nu există conflicte cu programările deja existente. Funcția va produce un mesaj care indică fie că programarea a fost adăugată cu succes, fie că există o eroare sau un conflict cu un interval deja rezervat. Utilizatorul poate apela funcția cu orice combinație de zi și interval orar pentru a testa toate cazurile posibile.

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

<img width="381" height="341" alt="Untitled Diagram drawio" src="https://github.com/user-attachments/assets/62fa9565-f825-418d-981f-6347c0359340" />


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
- **e** (arce) = 15 → 11 interne + 4 adăugate (N2→N1, N4→N1, N9→N1, N10→N1)
- **V(G) = e − n + 2 = 15 − 10 + 2 = 7**

Rezultă **7 circuite independente** și cel puțin 7 căi de test necesare.

---

## 6. Raport Coverage

### 6.1 Rezultate finale

| Clasă | Class, % | Method, % | Line, % | Branch, % |
|:------|:---------|:----------|:--------|:----------|
| `Main` | 100% | 100% | 100% | 100% |
| `Availability` | 100% | 100% | 100% | 100% |

### 6.2 Captură ecran

<img width="1222" height="308" alt="image" src="https://github.com/user-attachments/assets/90432a48-2855-499d-9565-ca355bc98c4c" />

---

## 7. Raport mutanți (PIT Mutation Testing)

### 7.1 Demonstrarea unui mutant supraviețuitor și repararea lui

**Înainte – test slab (9/15 mutanți uciși):**

<!-- Adaugă captura Image 1 și Image 2 -->
<img width="550" height="86" alt="image" src="https://github.com/user-attachments/assets/3b4f7ab8-f471-43eb-8611-37583af38ac4" />


<img width="786" height="266" alt="image" src="https://github.com/user-attachments/assets/d2305137-7468-44fe-a71f-c0316685ba96" />3" />


| Metric | Valoare |
|:-------|:--------|
| Line Coverage | 100% |
| Mutation Coverage | 94% |
| Test Strength | 94% |


**După – test reparat:**

<!-- Adaugă captura Image 3 și Image 4 -->
<img width="500" height="250" alt="image" src="https://github.com/user-attachments/assets/a46d325a-da6d-4df1-ad6e-4c14dde1ee96" />

<img width="500" height="255" alt="image" src="https://github.com/user-attachments/assets/9a54e3aa-070f-42b4-882d-793e55d4edc5" />


| Metric | Valoare |
|:-------|:--------|
| Line Coverage | 100% |
| Mutation Coverage | 100%  |
| Test Strength | 100%  |
| Mutanți supraviețuitori | 0 |

## 8. Referințe bibliografice

[1] JUnit Team, JUnit 5 User Guide online, https://junit.org/junit5/docs/current/user-guide/, Data ultimei accesări: 18             martie 2025

[2] PIT Mutation Testing Team, PIT Mutation Testing online, https://pitest.org, Data ultimei accesări: 20 martie 2025

[3] JetBrains, Code Coverage in IntelliJ IDEA, https://www.jetbrains.com/help/idea/code-coverage.html, Data ultimei                 accesări: 22 martie 2025

[4] JaCoCo Team, JaCoCo Java Code Coverage Library, https://www.jacoco.org/jacoco/, Data ultimei accesări: 25 martie 2025

[5] Apache Software Foundation, Maven Project, https://maven.apache.org, Data ultimei accesări: 28 martie 2025

[6] OpenAI, ChatGPT, https://chatgpt.com/, Data generării: 10 aprilie 2025
