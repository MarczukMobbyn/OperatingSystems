package algorithms;

import java.util.List;

public interface PageReplacementAlgorithm {
    int simulate(List<Integer> referenceString, int frameCount);
    String getName(); // dla czytelnych wyników
}