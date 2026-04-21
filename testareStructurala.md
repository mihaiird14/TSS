# Testare Structurală

## Graful de flux de control (CFG)

<img width="719" height="782" alt="diagrama" src="https://github.com/user-attachments/assets/38b875ff-9587-4cb6-ba65-f1e972155f94" />

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
| N8 | `if (conflictGasit)` |
| N9 | `return` – „există conflict" |
| N10 | `return` – „succes" |

**Arce (ramuri):**
- N1 →(adevărat) N2, N1 →(fals) N3
- N3 →(adevărat) N4, N3 →(fals) N5
- N5 →(adevărat – mai există elemente) N6, N5 →(fals – lista epuizată) N8
- N6 →(adevărat) N7, N6 →(fals) N5 (next iteration)
- N7 →(adevărat – CONFLICT GASIT) N8, N7 →(fals) N5 (next iteration)
- N8 →(adevărat) N9, N8 →(fals) N10

---

## 1. Acoperire la nivel de instrucțiune (Statement Coverage)

Pentru a acoperi toate instrucțiunile, trebuie să ne asigurăm că ramurile controlate de condiții sunt atinse.
Instrucțiunile „critice" care depind de condiții sunt nodurile N2, N4, N7, N9, N10.

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Noduri parcurși | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:----------------|:------------------|
| T1 | `MONDAY` | `10:00` | `12:00` | `[MONDAY 09:00 - 11:00]` | N1, N3, N5, N6, N7, N8, N9 | `Conflict: Există deja...` |
| T2 | `MONDAY` | `10:00` | `11:00` | `[]` (Goală) | N1, N3, N5, N8, N10 | `Succes: Intervalul a...` |

---

## 2. Acoperire la nivel de decizie (Decision / Branch Coverage)

### Decizii identificate

| # | Decizie | Ramura adevărat | Ramura fals |
|:--|:--------|:----------------|:------------|
| D1 | `dayOfWeek == null` | → N2 (return eroare) | → N3 |
| D2 | `startTime == null \|\| endTime == null \|\| !isBefore` | → N4 (return eroare) | → N5 |
| D3 | `for` – mai există elemente | → N6 | → N8 |
| D4 | `t.dayOfWeek == dayOfWeek` | → N7 | → N5 (next iteration) |
| D5 | suprapunere (condiție compusă) | → N8 (CONFLICT GASIT) | → N5 (next iteration) |
| D6 | `conflictGasit` | → N9 | → N10 |

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Decizii acoperite | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:------------------|:------------------|
| DC1 | `null` | `10:00` | `11:00` | `[]` | D1→adevărat | `Eroare: Ziua...` |
| DC2 | `MONDAY` | `11:00` | `10:00` | `[]` | D1→fals, D2→adevărat | `Eroare: Ora...` |
| DC3 | `MONDAY` | `10:00` | `11:00` | `[]` | D1→fals, D2→fals, D3→fals, D6→fals | `Succes...` |
| DC4 | `MONDAY` | `10:00` | `12:00` | `[MONDAY 11:00 - 13:00]` | D3→adevărat, D4→adevărat, D5→adevărat, D6→adevărat | `Conflict...` |
| DC5 | `MONDAY` | `10:00` | `11:00` | `[TUESDAY 10:00 - 11:00]`| D3→adevărat, D4→fals, D3→fals, D6→fals | `Succes...` |

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
| CC1 | `null` | `10:00` | `11:00` | `[]` | C1=adevărat | `Eroare: Ziua...` |
| CC2 | `MONDAY` | `10:00` | `11:00` | `[]` | C1=fals, C2=fals, C3=fals, C4=fals | `Succes...` |
| CC3 | `MONDAY` | `null` | `11:00` | `[]` | C2=adevărat | `Eroare: Ora...` |
| CC4 | `MONDAY` | `10:00` | `null` | `[]` | C3=adevărat | `Eroare: Ora...` |
| CC5 | `MONDAY` | `11:00` | `10:00` | `[]` | C4=adevărat | `Eroare: Ora...` |
| CC6 | `MONDAY` | `10:00` | `11:00` | `[MONDAY 09:00 - 12:00]`| C5=adevărat, C6=adevărat, C7=adevărat, C10=adevărat | `Conflict...` |
| CC7 | `MONDAY` | `10:00` | `12:00` | `[MONDAY 11:00 - 13:00]`| C8=adevărat | `Conflict...` |
| CC8 | `MONDAY` | `10:00` | `11:00` | `[MONDAY 10:00 - 12:00]`| C9=adevărat | `Conflict...` |

---

## 4. Testarea circuitelor independente

### Formula complexității ciclomatice

Pentru a obține un **graf complet conectat**, se adaugă câte un arc de la fiecare nod terminal înapoi la nodul de start:

- **n** (noduri) = 10 &nbsp;*(N1 – N10)*
- **e** (arce) = 16
  - Arce interne (12): N1→N2, N1→N3, N3→N4, N3→N5, N5→N6, N5→N8, N6→N7, N6→N5, N7→N8, N7→N5, N8→N9, N8→N10
  - Arce adăugate (4): N2→N1, N4→N1, N9→N1, N10→N1
- **V(G) = e − n + 2 = 16 − 10 + 2 = 8**

> Există **8 circuite independente**, deci sunt necesare cel puțin **8 căi de test** pentru a acoperi toate ramurile.

### Setul de bază

| Cale | Noduri parcurși | Descriere |
|:-----|:----------------|:----------|
| P1 | N1 → N2 | `dayOfWeek == null` → return eroare |
| P2 | N1 → N3 → N4 | dayOfWeek valid, interval invalid → return eroare |
| P3 | N1 → N3 → N5 → N8 → N10 | Lista goală, fără conflict → succes |
| P4 | N1 → N3 → N5 → N6 → N5 → N8 → N10 | Un element cu zi diferită → succes |
| P5 | N1 → N3 → N5 → N6 → N7 → N5 → N8 → N10 | Zi egală, fără suprapunere → succes |
| P6 | N1 → N3 → N5 → N6 → N7 → N8 → N9 | Conflict detectat → return conflict |
| P7 | N1 → N3 → N5 → N6 → N5 → N6 → N7 → N5 → N8 → N10 | Două iterații: zi diferită, apoi zi egală fără suprapunere → succes |
| P8 | N1 → N3 → N5 → N6 → N7 → N8 → N10 | *(nefezabilă – dacă conflictGasit=true, N8 ia ramura adevărat)* |

### Date de test

| ID | `dayOfWeek` | `startTime` | `endTime` | Stare listă | Cale | Rezultat așteptat |
|:---|:------------|:------------|:----------|:------------|:-----|:------------------|
| CP1 | `null` | `10:00` | `11:00` | `[]` | P1 | `Eroare: Ziua...` |
| CP2 | `MONDAY` | `11:00` | `10:00` | `[]` | P2 | `Eroare: Ora...` |
| CP3 | `MONDAY` | `10:00` | `11:00` | `[]` | P3 | `Succes...` |
| CP4 | `MONDAY` | `10:00` | `11:00` | `[TUESDAY 10:00 - 11:00]` | P4 | `Succes...` |
| CP5 | `MONDAY` | `10:00` | `11:00` | `[MONDAY 14:00 - 16:00]` | P5 | `Succes...` |
| CP6 | `MONDAY` | `10:00` | `12:00` | `[MONDAY 11:00 - 13:00]` | P6 | `Conflict...` |
| CP7 | `MONDAY` | `10:00` | `11:00` | `[TUESDAY 10:00-11:00, MONDAY 14:00-16:00]` | P7 | `Succes...` |
