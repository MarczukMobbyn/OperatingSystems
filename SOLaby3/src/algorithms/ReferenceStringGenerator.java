package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReferenceStringGenerator {
    public static List<Integer> generate(int totalPages, int length, int phaseSize) {
        Random rand = new Random();
        List<Integer> result = new ArrayList<>();

        int numPhases = length / phaseSize;
        List<Integer> basePages = new ArrayList<>();

        // Wybierz różne grupy bazowe (unikalne lub losowo rozrzucone)
        for (int i = 0; i < numPhases; i++) {
            int base = rand.nextInt(totalPages - 5);
            basePages.add(base);
        }

        for (int i = 0; i < numPhases; i++) {
            int basePage = basePages.get(i);
            for (int j = 0; j < phaseSize; j++) {
                result.add(basePage + rand.nextInt(5)); // operujemy na 5 stronach
            }
        }

        return result;
    }

}