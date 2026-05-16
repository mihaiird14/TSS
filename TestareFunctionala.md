# Testare Funcțională

### 1. Partiţionare de echivalenţă (Equivalence Partitioning)
#### Domeniul de intrări (Individual):
* **Ziua săptămânii (DayOfWeek - D):**
  * **D1** = {MONDAY, TUESDAY, ..., SUNDAY} (Valid)
  * **D2** = {null} (Eroare - obiect lipsă)

* **Intervalul orar (startTime, endTime - T):**
  * **T1** = {(S, E) | S < E, S, E != null} (Valid)
  * **T2** = {(S, E) | S >= E, S, E != null} (Eroare logică)
  * **T3** = {(S, E) | S = null sau E = null} (Eroare - date lipsă)

* **Starea conflictelor (Conflict - C):**
  * **C1** = {nu există suprapunere în listă} (Succes)
  * **C2** = {există cel puțin o suprapunere în listă} (Eroare conflict)


### Clase de echivalență globale:

Pentru a evita mascarea erorilor (Fault Masking), avem un test perfect valid, iar pentru restul testelor izolăm câte o singură clasă invalidă, păstrând restul intrărilor valide.

| ID | Combinație | Rezultat Aşteptat (Expected Output) |
| :--- | :--- | :--- |
| **G1** | (D1, T1, C1) | Succes: Intervalul a fost adăugat. |
| **G2** | (D2, T1, C1) | Eroare: Ziua săptămânii este obligatorie. |
| **G3** | (D1, T2, C1) | Eroare: Ora de început trebuie să fie strict mai mică... |
| **G4** | (D1, T3, C1) | Eroare: Ora de început trebuie să fie strict mai mică... |
| **G5** | (D1, T1, C2) | Conflict: Există deja un interval setat în această perioadă. |
---

### 2. Analiza valorilor de frontieră (Boundary Value Analysis)

Pentru exemplul nostru, odată ce au fost identificate clasele, valorile de frontieră sunt ușor de identificat:

* valorile pentru relația de timp: $S=E$, $S=E-1\text{min}$, $S=E+1\text{min}$
* relația cu un interval existent $[S_{ex}, E_{ex}]$: adiacent la început ($E=S_{ex}$), adiacent la sfârșit ($S=E_{ex}$), suprapunere de 1 minut ($E=S_{ex}+1\text{min}$ sau $S=E_{ex}-1\text{min}$)

Deci se vor testa următoarele valori:

a) Frontiera dimensiunii intervalului (Start < End)

  -> Aici testăm granița dintre un interval valid și unul invalid logic:
  
  * Test 1 (Eroare): Cel mai mic interval invalid ($Start = End$) $\rightarrow$ 09:00 - 09:00
  * Test 2 (Succes): Cel mai mic interval valid ($Start = End - 1 \text{ min}$) $\rightarrow$ 09:00 - 09:012. 

b) Frontiera suprapunerii (Regula: Fără conflict cu [10:00 - 11:00])

-> Aici testăm granița exactă unde intervalele se ating sau se suprapun cu o singură unitate (1 minut):
  * Test 3 (Succes): Atinge începutul, dar e valid (End = $Start_{existent}$) $\rightarrow$ 09:00 - 10:00
  * Test 4 (Succes): Atinge sfârșitul, dar e valid (Start = $End_{existent}$) $\rightarrow$ 11:00 - 12:00
  * Test 5 (Conflict): Suprapunere de 1 minut la început (End = $Start_{existent} + 1 \text{ min}$) $\rightarrow$ 09:00 - 10:01
  * Test 6 (Conflict): Suprapunere de 1 minut la sfârșit (Start = $End_{existent} - 1 \text{ min}$) $\rightarrow$ 10:59 - 12:00

În total sunt 6 date de test la frontieră (presupunând un interval existent `[10:00 - 11:00]`).


---

### 3. Partiţionarea în categorii (Category-Partitioning)

1. *Descompune specificația în unități:* avem o singură unitate (metoda `addAvailability`).
2. *Identifică parametrii și condițiile de mediu:*
   * Parametri: `D` (dayOfWeek), `S` (startTime), `E` (endTime).
   * Condiții de mediu: `L` (lista de disponibilități existentă).
4. *Găsește categorii:*
   * `D`: dacă este validă, null sau are format invalid
   * `T` (timp): relația dintre `S` și `E` (dacă este validă, invalidă sau cu date lipsă)
   * `L` (mediu): dacă există suprapunere cu noul interval sau nu
5. *Partiționează fiecare categorie în alternative:*
   * `D`: valid, null, invalid
   * `T`: S < E, S = E, S > E, null
   * `L`: fără suprapunere, suprapunere la început, suprapunere la sfârșit
6. *Scrie specificația de testare*
   * *D*
      1) { valid } `[ok]`
      2) { null } `[error]`
      3) { invalid } `[error]`
   * *T*
      1) { S < E } `[ok]`
      2) { S = E } `[error]`
      3) { S > E } `[error]`
      4) { null } `[error]`
   * *L (Condiție de mediu)*
      1) { nicio suprapunere } `[if ok]`
      2) { suprapunere la început } `[if ok]`
      3) { suprapunere la sfârșit } `[if ok]`

7. *Creează cazuri de testare*
   D2
   
   D3
   
   D1T2
   
   D1T3
   
   D1T4
   
   D1T1L1
   
   D1T1L2
   
   D1T1L3
