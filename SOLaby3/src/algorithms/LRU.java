package algorithms;

import java.util.*;

public class LRU implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "LRU";
    }

    @Override
    public int simulate(List<Integer> referenceString, int frameCount) {
        Set<Integer> frames = new HashSet<>();
        Map<Integer, Integer> lastUsed = new HashMap<>();
        int faults = 0;

        for (int time = 0; time < referenceString.size(); time++) {
            int page = referenceString.get(time);

            if (!frames.contains(page)) {
                // Page fault
                if (frames.size() == frameCount) {
                    // RAM pełny – znajdź najmniej używaną stronę
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
                faults++;
            }

            // Zawsze aktualizuj ostatnie użycie
            lastUsed.put(page, time);
        }

        return faults;
    }
}
