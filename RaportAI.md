# Raport: Folosirea unui Tool AI în Testarea Software

## 1. Introducere și Tool Utilizat
Pentru acest raport, am folosit **ChatGPT** (model GPT-4o) ca instrument de asistență bazat pe Inteligență Artificială pentru a genera automat suita de teste pentru funcționalitatea ddAvailability. 

## 2. Prompt și Răspuns

**Prompt-ul utilizat (exemplu documentat):**
> "Generează o suită de teste unitare în Java folosind JUnit 5 pentru metoda addAvailability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) din clasa Main. Generează intenționat un set redus și parțial redundant de teste, care să nu asigure 100% code coverage, omițând cazurile de eroare precum valori null sau startTime > endTime."

**Răspunsul AI-ului (rezumat):**
AI-ul a generat clasa MainTesteAI.java de teste care validează cu succes scenariul principal ("happy path") de programare a unui antrenor, implementând și două teste redundante. A ignorat însă deliberat constrângerile de limite și excepțiile, fix cum i-a fost cerut promptul. 

## 3. Comparația Suitelor de Teste

| Criteriu / Metrică | Suita Proprie (MainTest.java) | Suita Autogenerată (MainTesteAI.java) |
|:---|:---|:---|
| **Coverage (Line & Branch)** | 100% conform raportului JaCoCo. | Parțial (estimativ 50-70% la branch coverage). |
| **Tehnici de testare utilizate** | Partiționare de echivalență, Boundary Values, CFG. | Testare minimală (happy path), lipsită de structură analitică. |
| **Edge Cases (Valori limită)** | Testate riguros (ex. startTime = null). | OMISE complet. |
| **Scor Mutanți (PIT)** | 15/15 mutanți uciși (100% Test Strength) [1]. | Mutanții de pe ramurile de decizie de validare supraviețuiesc. |

**Diferențe Majore:**
1. **Redundanța AI-ului:** Suita autogenerată conține teste identice pe seturi similare de date care nu accesează fluxuri noi de cod.
2. **Validarea Lipsă:** AI-ul a lăsat descoperită zona argumentelor nule; dacă introducem 
ull, sistemul rulează pe "orb" pentru testele AI, spre deosebire de testele umane care asertează răspunsul clar.

## 4. Rulare cod autogenerat (Capturi de ecran)

*(AICI TREBUIE SĂ INSIRAȚI IMAGINILE DVS.)*

![Rulare Teste AI în IntelliJ / Terminal - Placeholder](link_catre_imagine_1)
*Figura 1: Execuția cu un succes (verde) a testelor din MainTesteAI.java, dar cu un procentaj mic de acoperire structurală.*

![Raport JaCoCo Coverage - Placeholder](link_catre_imagine_2)
*Figura 2: Raportul de acoperire JaCoCo pentru MainTesteAI.java evidențiind liniile marcate cu galben și roșu (scenarii neacoperite).*

## 5. Interpretare

Când analizăm rezultatele asistentului AI, este clar că acesta poate asambla rapid codul "de prefață" (@BeforeEach, setup inițial, happy paths). Totuși, calitatea asertărilor depinde strict de calitatea promptului [2]. Testele autogenerate sunt insuficiente pentru o aplicație de producție, lipsind definirea riguroasă adusă de metodele formale (precum Boundary Value Analysis) specifice ingineriei software [3].

Astfel, instrumentele AI trebuie validate de ingineri QA umani.

## 6. Referințe Bibliografice
[1] Coles, H., Laurent, T., et al. (2016). *PIT: a practical mutation testing tool for Java.* Proceedings of the 25th International Symposium on Software Testing and Analysis.
[2] Dakhel, A. M., Majdunas, V., et al. (2023). *GitHub Copilot AI pair programmer: Asset or Liability?* Journal of Systems and Software.
[3] Myers, G. J., Sandler, C., & Badgett, T. (2011). *The Art of Software Testing.* John Wiley & Sons.
