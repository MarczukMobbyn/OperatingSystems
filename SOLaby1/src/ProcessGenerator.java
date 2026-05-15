import java.util.*;

public class ProcessGenerator {
    private static Random random = new Random();

    public static List<Process> generateProcesses(int count, int maxArrival, int maxBurst) {
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int arrivalTime = random.nextInt(maxArrival);
            int burstTime = 1 + random.nextInt(maxBurst);
            processes.add(new Process(i, arrivalTime, burstTime));
        }
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        return processes;
    }
}
