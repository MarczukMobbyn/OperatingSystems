package frameAlgorithms;

import java.util.*;

public class Process {
    private int id;
    private Queue<Integer> referenceString;  // odwołania do stron
    private LRUInstance lru;                 // Twój "żywy" LRU
    private int frameCount;
    private int pageFaults = 0;
    private int uniqePageCount = 0; // liczba unikalnych stron
    private boolean active = true;
    private int lastNonZeroFrameCount = 0;  // Dodaj do innych pól


    // --- Obsługa okna dla PFF ---
    private int windowFaults = 0;
    private int windowCounter = 0;

    // --- Statystyki szamotania ---
    private int thrashingEvents = 0;     // Liczba wykrytych szamotań
    private int windowPageFaults = 0;    // Błędy w aktualnym oknie szamotania
    private int windowCounterThrash = 0; // Odwołania w oknie szamotania
    private final int thrashWindow;      // Rozmiar okna szamotania
    private final int thrashThreshold;   // Próg szamotania (np. 5 przy oknie 10)

    public Process(int id, List<Integer> referenceString, int frameCount) {
        this(id, referenceString, frameCount, 10, 5); // domyślne wartości: okno 10, próg 5
    }

    public Process(int id, List<Integer> referenceString, int frameCount, int thrashThreshold) {
        this(id, referenceString, frameCount, 10, thrashThreshold);
    }

    public Process(int id, List<Integer> referenceString, int frameCount, int thrashWindow, int thrashThreshold) {
        this.id = id;
        this.referenceString = new LinkedList<>(referenceString);
        this.frameCount = frameCount;
        this.lru = new LRUInstance(frameCount);
        Set<Integer> uniquePages = new HashSet<>(referenceString);
        this.uniqePageCount = uniquePages.size();
        this.thrashWindow = thrashWindow;
        this.thrashThreshold = thrashThreshold;
    }

    /**
     * Obsługuje jedno odwołanie do strony:
     * - LRU obsługuje odwołanie
     * - Inkrementuje błędy stron
     * - Liczy statystyki dla PFF (okno)
     * - Liczy statystyki szamotania (oddzielne okno, np. co 10 odwołań)
     */
    public boolean handleNextReference() {
        if (!active || referenceString.isEmpty()) return false;
        int page = referenceString.poll();
        boolean fault = lru.access(page);
        if (fault) pageFaults++;

        // Statystyka PFF (okno)
        registerReference(fault);

        // --- Statystyka szamotania ---
        windowCounterThrash++;
        if (fault) windowPageFaults++;
        if (windowCounterThrash >= thrashWindow) {
            if (windowPageFaults > thrashThreshold) thrashingEvents++;
            windowCounterThrash = 0;
            windowPageFaults = 0;
        }
        return fault;
    }

    public boolean hasReferences() {
        return !referenceString.isEmpty();
    }

    public int getPageFaults() {
        return pageFaults;
    }

    public boolean accessLRU(int page) {
        return lru.access(page);
    }

    public void setPageFaults(int pageFaults) {
        this.pageFaults = pageFaults;
    }

    public int getId() {
        return id;
    }

    public void setFrameCount(int newFrameCount) {
        this.frameCount = newFrameCount;
        lru.setFrameCount(newFrameCount);
        if (newFrameCount > 0) {
            this.lastNonZeroFrameCount = newFrameCount;
        }
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getLastNonZeroFrameCount() {
        return lastNonZeroFrameCount;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getUniquePageCount() {
        return uniqePageCount;
    }

    public Queue<Integer> getReferenceString() {
        return referenceString;
    }

    public void setReferenceString(List<Integer> list) {
        this.referenceString = new LinkedList<>(list);
        Set<Integer> uniquePages = new HashSet<>(list);
        this.uniqePageCount = uniquePages.size();
    }

    // --- Dla PFF ---
    public void resetWindowStats() {
        windowFaults = 0;
        windowCounter = 0;
    }

    public void registerReference(boolean pageFault) {
        windowCounter++;
        if (pageFault) windowFaults++;
    }

    public int getWindowFaults() {
        return windowFaults;
    }

    public int getWindowCounter() {
        return windowCounter;
    }

    // --- Dla szamotania ---
    public int getThrashingEvents() {
        return thrashingEvents;
    }

}
