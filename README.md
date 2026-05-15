# 💻 Operating Systems - Monorepo

Zbiór projektów symulacyjnych realizowanych w ramach przedmiotu **Systemy Operacyjne**. Projekt ma strukturę monorepo, w którym każdy moduł (lista) odpowiada za inne kluczowe zagadnienie z zakresu algorytmów i mechanizmów systemowych.

## 📂 Przegląd Modułów

### Lista 1: Planowanie czasu procesora (CPU Scheduling)
Symulator porównujący wydajność algorytmów przydziału czasu procesora dla procesów o zróżnicowanym czasie nadejścia i wymaganiach.
* **Algorytmy:** FCFS, SJF (bez wywłaszczania), SRTF (SJF z wywłaszczaniem), Round Robin (RR - z kwantem czasu i kosztem przełączenia kontekstu).
* **Metryki i funkcje:** Obliczanie średniego czasu oczekiwania (AWT) i przetwarzania (ATT), zliczanie przełączeń kontekstu, wykrywanie potencjalnego zagłodzenia procesów (starvation) oraz tekstowa wizualizacja w postaci wykresu Gantta.

### Lista 2: Planowanie dostępu do dysku (Disk Scheduling)
Symulator analizujący wydajność algorytmów pozycjonowania głowicy dysku twardego w celu minimalizacji seek time.
* **Algorytmy:** FCFS, SSTF, SCAN (algorytm windy), C-SCAN (Circular SCAN).
* **Obsługa Real-Time:** EDF (Earliest Deadline First) oraz FD-SCAN (Feasible Deadline SCAN) – zaawansowana odmiana odrzucająca żądania niemożliwe do spełnienia w czasie i obsługująca zwykłe żądania "po drodze".
* **Metryki:** Całkowite przemieszczenie głowicy (total head movement) wyrażone w cylindrach.

### Lista 3: Zastępowanie stron (Page Replacement)
Symulator wirtualnej pamięci badający zachowanie systemu przy ograniczonej puli ramek RAM.
* **Algorytmy:** FIFO, LRU (Least Recently Used), OPT (teoretyczny algorytm optymalny jako benchmark), Random (losowy) oraz Second Chance (Druga Szansa z wykorzystaniem bitu odniesienia).
* **Metryki i funkcje:** Zliczanie błędów braku strony (Page Faults), uśrednianie wyników dla algorytmu losowego, generator ciągów odwołań uwzględniający lokalność fazową programu.

### Lista 4: Przydział ramek (Frame Allocation)
Środowisko wieloprocesowe symulujące podział dostępnej pamięci fizycznej (ramek) pomiędzy współbieżnie działające programy.
* **Algorytmy:** Statyczne (Equal, Proportional) oraz Dynamiczne (PFF - Page Fault Frequency, Working Set Model).
* **Metryki i funkcje:** Każdy proces posiada niezależną instancję LRU. System śledzi i zlicza zdarzenia szamotania (thrashing) oraz realizuje dynamiczne usypianie (wstrzymywanie) zbyt zasobożernych procesów i ich automatyczne wybudzanie.

### Lista 5: Równoważenie obciążenia (Load Balancing)
Symulacja rozproszonych algorytmów migracji zadań i balansowania pracy w architekturze wieloprocesorowej.
* **Strategie:**
* 1. *Strategia 1:* Procesor losowo odpytuje do *z* sąsiadów i migruje zadanie, jeśli obciążenie celu nie przekroczy progu *p*.
  2. *Strategia 2:* W przypadku przeciążenia powierzonego zadania, procesor szuka losowo wolnego węzła aż do skutku lub wyczerpania opcji.
  3. *Strategia 3 (Work Stealing):* Aktywna kradzież zadań – niedociążone procesory (poniżej progu *r*) same odpytują jednostki przeciążone i przejmują ich zadania o największym loadzie.
* **Metryki:** Łączna liczba zapytań (queries), liczba migracji oraz odchylenie standardowe obciążenia (wskaźnik zbalansowania systemu).

---

## Jak uruchomić?
Każdy folder (`SOLaby1` do `SOLaby5`) stanowi niezależny moduł ze zdefiniowanymi zestawami testowymi (przypadki brzegowe oraz losowe generatory).

1. Otwórz projekt w IntelliJ IDEA.
2. Kliknij prawym przyciskiem myszy na folder `src` w wybranym module i wybierz **Mark Directory as -> Sources Root**.
3. Uruchom klasę startową (np. `Main.java` lub `TestRunner.java`) za pomocą zielonej strzałki *Run*.
