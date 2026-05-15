import java.util.*;

public class TestSetFactory {

    // Test na zagłodzenie w SJF
    public static List<Process> getStarvationTest() {
        List<Process> list = new ArrayList<>();
        list.add(new Process(0, 0, 20)); // długi proces przychodzi jako pierwszy
        list.add(new Process(1, 1, 2));
        list.add(new Process(2, 2, 2));
        list.add(new Process(3, 3, 2));
        list.add(new Process(4, 4, 2));
        return list;
    }

    // Test stresowy dla Round Robin z dużą liczbą przełączeń
    public static List<Process> getRRStressTest() {
        List<Process> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Process(i, 0, 3)); // wszystkie przychodzą na raz
        }
        return list;
    }

    // Test z różnymi czasami przyjścia i burstTime
    public static List<Process> getMixedArrivalTest() {
        List<Process> list = new ArrayList<>();
        list.add(new Process(0, 0, 8));
        list.add(new Process(1, 1, 4));
        list.add(new Process(2, 2, 9));
        list.add(new Process(3, 3, 5));
        return list;
    }

    // Test do wykazania różnic między SJF i SRTF
    public static List<Process> getCompareSJFvsSRTFTest() {
        List<Process> list = new ArrayList<>();
        list.add(new Process(0, 0, 10));
        list.add(new Process(1, 2, 2));
        list.add(new Process(2, 3, 1));
        list.add(new Process(3, 4, 2));
        return list;
    }

    // Generuje listę losowych procesów i sortuje po arrivalTime
    public static List<Process> getRandomProcessList(int count, int maxArrival, int maxBurst) {
        Random rand = new Random();
        List<Process> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int arrival = rand.nextInt(maxArrival + 1);
            int burst = rand.nextInt(maxBurst) + 1; // burst nie może być 0
            list.add(new Process(i, arrival, burst));
        }

        list.sort(Comparator.comparingInt(p -> p.arrivalTime));
        return list;
    }
}
