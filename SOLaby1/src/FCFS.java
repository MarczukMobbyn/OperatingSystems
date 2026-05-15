import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFS {
    public static void run(List<Process> inputProcesses) {
        List<Process> processes = new ArrayList<>(inputProcesses);
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.startTime = currentTime;
            currentTime += p.burstTime;
            p.finishTime = currentTime;
        }

        printResults("FCFS", processes);
    }

    private static void printResults(String algorithm, List<Process> processes) {
        System.out.println("\n=== " + algorithm + " ===");
        int totalWaiting = 0;
        int totalTurnaround = 0;
        for (Process p : processes) {
            System.out.println("Process " + p.id +
                    " | Waiting Time: " + p.getWaitingTime() +
                    " | Turnaround Time: " + p.getTurnaroundTime());
            totalWaiting += p.getWaitingTime();
            totalTurnaround += p.getTurnaroundTime();
        }
        System.out.println("Average Waiting Time: " + (double) totalWaiting / processes.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaround / processes.size());
    }
}
