package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReferenceStringGenerator {

    /**
     * Dla każdego procesu fazy mają losową długość z podanego zakresu.
     * @param processPageStart początek zakresu stron tego procesu
     * @param processPageCount liczba różnych stron, z których korzysta proces
     * @param length długość całego ciągu odwołań
     * @param minPhaseSize minimalna długość fazy
     * @param maxPhaseSize maksymalna długość fazy
     * @return wygenerowany ciąg odwołań do stron
     */
    public static List<Integer> generateWithRandomPhases(
            int processPageStart,
            int processPageCount,
            int length,
            int minPhaseSize,
            int maxPhaseSize
    ) {
        Random rand = new Random();
        List<Integer> result = new ArrayList<>();
        int generated = 0;

        while (generated < length) {
            // Losowa długość fazy w zakresie [minPhaseSize, maxPhaseSize]
            int phaseLength = minPhaseSize + rand.nextInt(maxPhaseSize - minPhaseSize + 1);

            // Losowa baza fazy (obszar lokalności - startuje w losowym miejscu zakresu stron)
            int localSetStart = processPageStart + rand.nextInt(Math.max(1, processPageCount - 5 + 1));
            int localSetSize = 5; // możesz zwiększyć do 7-10 dla bardziej "rozlewnej" fazy

            // Generuj odwołania w fazie lokalności
            for (int i = 0; i < phaseLength && generated < length; i++) {
                result.add(localSetStart + rand.nextInt(localSetSize));
                generated++;
            }
        }
        return result;
    }
}
