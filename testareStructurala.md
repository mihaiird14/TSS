# Testare Structurală

## Graful de flux de control (CFG)

<img width="719" height="781" alt="grafTSS" src="https://github.com/user-attachments/assets/8d7208c3-4927-498b-b0f1-4f1efd332d6f" />

Nodurile grafului corespund blocurilor de instrucțiuni și deciziilor din metodă:

| Nod | Instrucțiuni |
|:----|:------------|
| N1 | `if (dayOfWeek == null)` |
| N2 | `return` eroare – „Ziua săptămânii este obligatorie" |
| N3 | `if (startTime == null \|\| endTime == null \|\| !startTime.isBefore(endTime))` |
| N4 | `return` eroare – „Ora de început trebuie să fie strict mai mică decât ora de sfârșit" |
| N5 | `for each t in trainerAvailabilities` – condiție iterație |
| N6 | `if (t.dayOfWeek == dayOfWeek)` |
| N7 | `if (startTime in interval_existent \|\| endTime in interval_existent \|\| startTime == start_interval)` |
| N8 | `conflictFound = true; exit (break)` |
| N9 | `if (conflictGasit)` |
| N10 | `return` – „există conflict" |
| N11 | `return` – „succes" |


---

## 1. Acoperire la nivel de instrucțiune (Statement Coverage)

Pentru a acoperi toate instrucțiunile, trebuie să ne asigurăm că ramurile controlate de condiții sunt atinse.
Instrucțiunile „critice" care depind de condiții sunt nodurile N2, N4, N8, N10, N11.

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Noduri parcurși | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:----------------|:------------------|
| T1 | | | | | N1, N3, N5, N6, N7, N8, N9, N10 | |
| T2 | | | | | N1, N3, N5, N9, N11 | |

---

## 2. Acoperire la nivel de decizie (Decision / Branch Coverage)
### Decizii identificate

| # | Decizie | Ramura adevărat | Ramura fals |
|:--|:--------|:----------------|:------------|
| D1 | `dayOfWeek == null` | → N2 (return eroare) | → N3 |
| D2 | `startTime == null \|\| endTime == null \|\| !isBefore` | → N4 (return eroare) | → N5 |
| D3 | `for` – mai există elemente | → N6 | → N9 |
| D4 | `t.dayOfWeek == dayOfWeek` | → N7 | → N5 (next iteration) |
| D5 | suprapunere (condiție compusă) | → N8 | → N5 (next iteration) |
| D6 | `conflictGasit` | → N10 | → N11 |

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Decizii acoperite | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:------------------|:------------------|
| DC1 | | | | | D1→adevărat | |
| DC2 | | | | | D1→fals, D2→adevărat | |
| DC3 | | | | | D1→fals, D2→fals, D3→fals, D6→fals | |
| DC4 | | | | | D3→adevărat, D4→adevărat, D5→adevărat, D6→adevărat | |
| DC5 | | | | | D3→adevărat, D4→fals, D3→fals, D6→fals | |

---

## 3. Acoperire la nivel de condiție (Condition Coverage)

### Condiții individuale identificate

| Decizie | Condiții individuale |
|:--------|:--------------------|
| D1: `dayOfWeek == null` | C1: `dayOfWeek == null` |
| D2: `startTime == null \|\| endTime == null \|\| !isBefore` | C2: `startTime == null` · C3: `endTime == null` · C4: `!startTime.isBefore(endTime)` |
| D3: condiție `for` | C5: mai există elemente în listă |
| D4: `t.dayOfWeek == dayOfWeek` | C6: zilele sunt egale |
| D5: suprapunere (3 sub-condiții în OR) | C7: `startTime in interval_existent` · C8: `endTime in interval_existent` · C9: `startTime == start_interval` |
| D6: `conflictGasit` | C10: `conflictGasit` |

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Condiții acoperite | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:------------------|:------------------|
| CC1 | | | | | C1=adevărat | |
| CC2 | | | | | C1=fals, C2=fals, C3=fals, C4=fals | |
| CC3 | | | | | C2=adevărat | |
| CC4 | | | | | C3=adevărat | |
| CC5 | | | | | C4=adevărat | |
| CC6 | | | | | C5=adevărat, C6=adevărat, C7=adevărat, C10=adevărat | |
| CC7 | | | | | C8=adevărat | |
| CC8 | | | | | C9=adevărat | |

---

## 4. Testarea circuitelor independente
### Formula complexității ciclomatice

Pentru a obține un **graf complet conectat**, se adaugă câte un arc de la fiecare nod terminal înapoi la nodul de start:

- **n** (noduri) = 11 &nbsp;*(N1 – N11)*
- **e** (arce) = 17
  - Arce interne (13): N1→N2, N1→N3, N3→N4, N3→N5, N5→N6, N5→N9, N6→N7, N6→N5, N7→N8, N7→N5, N8→N9, N9→N10, N9→N11
  - Arce adăugate (4): N2→N1, N4→N1, N10→N1, N11→N1
- **V(G) = e − n + 2 = 17 − 11 + 2 = 8**

> Există **8 circuite independente**, deci sunt necesare cel puțin **8 căi de test** pentru a acoperi toate ramurile.

### Setul de bază

| Cale | Noduri parcurși | Descriere |
|:-----|:----------------|:----------|
| P1 | N1 → N2 | `dayOfWeek == null` → return eroare |
| P2 | N1 → N3 → N4 | dayOfWeek valid, interval invalid → return eroare |
| P3 | N1 → N3 → N5 → N9 → N11 | Lista goală, fără conflict → succes |
| P4 | N1 → N3 → N5 → N6 → N5 → N9 → N11 | Un element cu zi diferită → succes |
| P5 | N1 → N3 → N5 → N6 → N7 → N5 → N9 → N11 | Zi egală, fără suprapunere → succes |
| P6 | N1 → N3 → N5 → N6 → N7 → N8 → N9 → N10 | Conflict detectat → return conflict |
| P7 | N1 → N3 → N5 → N6 → N5 → N6 → N7 → N5 → N9 → N11 | Două iterații: zi diferită, apoi zi egală fără suprapunere → succes |
| P8 | N1 → N3 → N5 → N6 → N7 → N8 → N9 → N11 | *(nefezabilă – dacă conflictFound=true, N9 ia ramura adevărat)* |

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Cale | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:-----|:------------------|
| CP1 | | | | | P1 | |
| CP2 | | | | | P2 | |
| CP3 | | | | | P3 | |
| CP4 | | | | | P4 | |
| CP5 | | | | | P5 | |
| CP6 | | | | | P6 | |
| CP7 | | | | | P7 | |
