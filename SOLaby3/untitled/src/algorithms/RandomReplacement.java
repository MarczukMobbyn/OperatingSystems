package algorithms;

import java.util.*;

public class RandomReplacement implements PageReplacementAlgorithm {

    private final Random random = new Random();

    @Override
    public String getName() {
        return "RAND";
    }

    @Override
    public int simulate(List<Integer> referenceString, int frameCount) {
        List<Integer> frames = new ArrayList<>();
        Set<Integer> frameSet = new HashSet<>();
        int faults = 0;

        for (int page : referenceString) {
            if (!frameSet.contains(page)) {
                faults++;

                if (frames.size() == frameCount) {
                    // RAM pełny – usuń losową stronę
                    int victimIndex = random.nextInt(frames.size());
                    int victim = frames.get(victimIndex);
                    frameSet.remove(victim);
                    frames.remove(victimIndex);
                }

                frames.add(page);
                frameSet.add(page);
            }
        }

        return faults;
    }
}
