import algorithms.*;

import java.util.*;

public class PageReplacementSimulator {
    public void run(int totalPages, int refLength, int frameCount, int phaseSize) {
        //List<Integer> referenceString = ReferenceStringGenerator.generate(totalPages, refLength, phaseSize);
        List<Integer> referenceString = List.of( 1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5);

        List<PageReplacementAlgorithm> algorithms = List.of(
                new FIFO(), new OPT(), new LRU(), new SecondChance(), new RandomReplacement()
        );


        System.out.println("Ciąg odwołań: " + referenceString);
        System.out.println("RAM: " + frameCount + " ramek");
        System.out.println("Wyniki:");

        for (PageReplacementAlgorithm algo : algorithms) {
            int faults = algo.simulate(referenceString, frameCount);
            System.out.println("  ➤ " + algo.getName() + ": " + faults + " błędów stron");
        }
    }
}