package algorithms;

import java.util.*;

public class OPT implements PageReplacementAlgorithm {

    @Override
    public String getName() {
        return "OPT";
    }

    @Override
    public int simulate(List<Integer> referenceString, int frameCount) {
        Set<Integer> frames = new HashSet<>();
        int faults = 0;

        for (int i = 0; i < referenceString.size(); i++) {
            int currentPage = referenceString.get(i);

            if (!frames.contains(currentPage)) {
                if (frames.size() == frameCount) {
                    // Szukamy strony do usunięcia
                    int pageToRemove = -1;
                    int farthestUse = -1;

                    for (int page : frames) {
                        int nextUse = findNextUse(referenceString, i + 1, page);
                        if (nextUse == -1) {
                            // strona nie wystąpi więcej – usuń ją
                            pageToRemove = page;
                            break;
                        }
                        if (nextUse > farthestUse) {
                            farthestUse = nextUse;
                            pageToRemove = page;
                        }
                    }

                    frames.remove(pageToRemove);
                }

                frames.add(currentPage);
                faults++;
            }
        }

        return faults;
    }

    private int findNextUse(List<Integer> ref, int startIndex, int page) {
        for (int i = startIndex; i < ref.size(); i++) {
            if (ref.get(i) == page) {
                return i;
            }
        }
        return -1; // nie występuje więcej
    }
}