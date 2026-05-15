package algorithms;

import java.util.*;

public class SecondChance implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "Second Chance";
    }

    @Override
    public int simulate(List<Integer> referenceString, int frameCount) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> frames = new HashSet<>();
        Map<Integer, Boolean> referenceBits = new HashMap<>();
        int faults = 0;

        for (int page : referenceString) {
            // Strona jest już w pamięci
            if (frames.contains(page)) {
                referenceBits.put(page, true); // oznacz jako używaną
                continue;
            }

            // Błąd strony
            faults++;

            if (frames.size() == frameCount) {
                // Szukamy strony do usunięcia (bit = 0)
                while (true) {
                    int front = queue.peek();
                    if (referenceBits.get(front)) {
                        // Druga szansa: ustaw bit = 0 i przenieś na koniec kolejki
                        referenceBits.put(front, false);
                        queue.poll();
                        queue.offer(front);
                    } else {
                        // Usuń tę stronę
                        queue.poll();
                        frames.remove(front);
                        referenceBits.remove(front);
                        break;
                    }
                }
            }

            // Dodaj nową stronę
            queue.offer(page);
            frames.add(page);
            referenceBits.put(page, true);
        }

        return faults;
    }
}
