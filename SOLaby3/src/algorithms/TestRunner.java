package algorithms;

import java.util.*;

public class TestRunner {

    private static final List<PageReplacementAlgorithm> ALGORITHMS = List.of(
            new FIFO(),
            new OPT(),
            new LRU(),
            new SecondChance(),
            new RandomReplacement()
    );

    public static void main(String[] args) {
        runAllTests();
    }

    public static void runAllTests() {
        int[] totalPagesOptions = {10, 20, 50};
        int[] refLengthOptions = {20, 50, 100};
        int[] phaseSizes = {5, 10, 20};
        int[] frameCounts = {2, 3, 4, 5, 6};

        for (int totalPages : totalPagesOptions) {
            for (int refLength : refLengthOptions) {
                for (int phaseSize : phaseSizes) {
                    for (int frameCount : frameCounts) {

                        if (frameCount >= totalPages) continue; // pomiń bezsensowny przypadek

                        List<Integer> referenceString = ReferenceStringGenerator.generate(
                                totalPages, refLength, phaseSize);

                        System.out.println("=".repeat(70));
                        System.out.printf("Test: %d stron | %d odwołań | liczba odwołań w fazie: %d | RAM: %d ramek%n",
                                totalPages, refLength, phaseSize, frameCount);
                        System.out.println("=".repeat(70));

                        System.out.println("Ciąg odwołań: " + referenceString);

                        for (PageReplacementAlgorithm algo : ALGORITHMS) {
                            if (algo.getName().equals("RAND")) {
                                double avg = runRandAverage(algo, referenceString, frameCount);
                                System.out.printf("  %-15s: średnio %.2f błędów stron (z 10 losowań)%n", algo.getName(), avg);
                            } else {
                                int faults = algo.simulate(referenceString, frameCount);
                                System.out.printf("  %-15s: %d błędów stron%n", algo.getName(), faults);
                            }
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    private static double runRandAverage(PageReplacementAlgorithm randAlgo, List<Integer> referenceString, int frameCount) {
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += randAlgo.simulate(referenceString, frameCount);
        }
        return total / 10.0;
    }
}
