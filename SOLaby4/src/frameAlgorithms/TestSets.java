package frameAlgorithms;

import java.util.*;

public class TestSets {

    // 1. WADA EQUAL: duży i mały proces
    // totalFrames = 9
    public static List<List<Integer>> setWadaEqual() {
        return List.of(
                Arrays.asList(1,2,1,2,1,2,1,2,1,2), // 2 strony, 10 odwołań
                Arrays.asList(10,11,12,13,14,15,10,11,12,13,14,15,10,11,12,13,14,15) // 6 stron, 18 odwołań
        );
    }

    // 2. WADA PROPORTIONAL: fazy lokalności
    // totalFrames = 6
    public static List<List<Integer>> setWadaProportional() {
        return List.of(
                Arrays.asList(1,2,3,4,1,2,3,4, 5,6,5,6, 1,2,1,2),  // Faza: 1-4, potem 5-6, potem 1-2
                Arrays.asList(7,8,7,8,7,8, 9,10,11,12, 7,8,9,10)   // Najpierw 7-8, potem 9-12
        );
    }

    // 3. WADA PFF: częsta zmiana lokalności, dużo wstrzymań
    // totalFrames = 6
    public static List<List<Integer>> setWadaPFF() {
        return List.of(
                Arrays.asList(1,2,3,1,2,3,1,2,3,4,5,6,1,2,3,4,5,6,1,2,3,4,5,6), // ciągłe zmiany lokalności
                Arrays.asList(7,8,9,7,8,9,10,11,12,10,11,12,7,8,9,10,11,12)      // podobnie
        );
    }

    // 4. WADA WORKING SET: duże okno, procesy mieszają strony
    // totalFrames = 10
    public static List<List<Integer>> setWadaWorkingSet() {
        return List.of(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5),
                Arrays.asList(20,21,22,23,24,25,26,27,28,29,30,20,21,22,23,24,25,26,27,28,29,30)
        );
    }

    // 5. WADA KAŻDEGO: brak lokalności
    // totalFrames = 6
    public static List<List<Integer>> setBrakLokalnosci() {
        return List.of(
                Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18),
                Arrays.asList(101,102,103,104,105,106,107,108,109,110,111,112)
        );
    }

}
