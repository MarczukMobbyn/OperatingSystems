import java.util.*;

public class SJF {
    public static void run(List<Process> inputProcesses) {
        List<Process> processes = new ArrayList<>(inputProcesses);
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        List<Process> completed = new ArrayList<>();

        int currentTime = 0;

        while (!processes.isEmpty()) {

            List<Process> available = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                currentTime++; //brak dostepnych procesow
                continue;
            }

            // wybieramy procse z najkrotszym burn time
            Process next = Collections.min(available, Comparator.comparingInt(p -> p.burstTime));


            next.startTime = currentTime;
            currentTime += next.burstTime;
            next.finishTime = currentTime;

            completed.add(next);
            processes.remove(next);
        }

        printResults("SJF", completed);
    }

    private static void printResults(String algorithm, List<Process> processes) {
        System.out.println("\n=== " + algorithm + " ===");

        int totalWaiting = 0;
        int totalTurnaround = 0;
        int maxWaitingTime = 0;

        for (Process p : processes) {
            int waitingTime = p.getWaitingTime();
            int turnaroundTime = p.getTurnaroundTime();

            System.out.println("Process " + p.id +
                    " | Waiting Time: " + waitingTime +
                    " | Turnaround Time: " + turnaroundTime);

            totalWaiting += waitingTime;
            totalTurnaround += turnaroundTime;

            //sprawdzenie czy to najwiekszy czas oczekiwania
            waitingTime = p.getWaitingTime();
            if (waitingTime > maxWaitingTime) {
                maxWaitingTime = waitingTime;
            }
        }

        System.out.println("Average Waiting Time: " + (double) totalWaiting / processes.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaround / processes.size());
        System.out.println("Max Waiting Time: " + maxWaitingTime);

        //Sprawdzanie zagłodzenia procesów
        int starvationThreshold = 20;

        boolean foundStarved = false;
        for (Process p : processes) {
            if (p.getWaitingTime() >= starvationThreshold) {
                if (!foundStarved) {
                    System.out.println("\nProcesy uznane za potencjalnie zagłodzone (czekały ≥ " + starvationThreshold + " jednostek):");
                    foundStarved = true;
                }
                System.out.println("P" + p.id + " | Waiting Time: " + p.getWaitingTime());
            }
        }
    }

}

