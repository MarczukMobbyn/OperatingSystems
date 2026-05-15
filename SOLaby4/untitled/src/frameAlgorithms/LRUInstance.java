package frameAlgorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pojedyncza instancja LRU – obsługuje pojedynczy proces i działa na żywo (krok po kroku).
 */
public class LRUInstance {
    private Set<Integer> frames = new HashSet<>();
    private Map<Integer, Integer> lastUsed = new HashMap<>();
    private int frameCount;
    private int time = 0;

    public LRUInstance(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Obsługuje pojedyncze odwołanie do strony.
     * @param page numer strony
     * @return true jeśli wystąpił błąd strony, false jeśli strona już była w ramkach
     */
    public boolean access(int page) {
        boolean pageFault = false;

        if (!frames.contains(page)) {
            pageFault = true;
            if (frames.size() == frameCount) {
                // RAM pełny – znajdź stronę najmniej używaną (LRU)
                int lruPage = -1;
                int oldestTime = Integer.MAX_VALUE;
                for (int p : frames) {
                    int usedTime = lastUsed.getOrDefault(p, -1);
                    if (usedTime < oldestTime) {
                        oldestTime = usedTime;
                        lruPage = p;
                    }
                }
                frames.remove(lruPage);
                lastUsed.remove(lruPage);
            }
            frames.add(page);
        }
        lastUsed.put(page, time++);
        return pageFault;
    }

    /**
     * Zmień liczbę ramek (np. przy dynamicznym przydziale).
     * Jeśli obecnie trzymasz więcej stron niż ramek, usuń najmniej używane.
     * @param newCount nowa liczba ramek
     */
    public void setFrameCount(int newCount) {
        this.frameCount = newCount;
        while (frames.size() > frameCount) {
            // Usuwaj najdawniej nieużywaną stronę
            int lruPage = -1;
            int oldestTime = Integer.MAX_VALUE;
            for (int p : frames) {
                int usedTime = lastUsed.getOrDefault(p, -1);
                if (usedTime < oldestTime) {
                    oldestTime = usedTime;
                    lruPage = p;
                }
            }
            frames.remove(lruPage);
            lastUsed.remove(lruPage);
        }
    }

    public int getFrameCount() {
        return frameCount;
    }
}
