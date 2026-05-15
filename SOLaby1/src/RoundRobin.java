import java.util.*;

public class RoundRobin {
    public static void run(List<Process> inputProcesses, int quantum, int contextSwitchCost) {
        List<Process> processes = new ArrayList<>(inputProcesses);
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int index = 0; // wskazuje, które procesy już dodaliśmy do kolejki
        int contextSwitches = 0;

        Process currentProcess = null;
        List<Process> completed = new ArrayList<>();
        List<String> ganttLog = new ArrayList<>();

        while (!queue.isEmpty() || index < processes.size()) {
            // Dodaj procesy które właśnie nadeszły
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process p = queue.poll();

            // Liczymy context switch
            if (currentProcess != null && currentProcess != p) {
                contextSwitches++;
                currentTime += contextSwitchCost; // dodajemy koszt przełączenia kontekstu
            }
            currentProcess = p;

            if (p.startTime == -1) {
                p.startTime = currentTime;
            }

            // Obliczamy ile czasu ten proces może teraz działać
            int timeSlice = Math.min(quantum, p.remainingTime);

            ganttLog.add("Time " + currentTime + "–" + (currentTime + timeSlice) + ": P" + p.id);

            // Wykonujemy proces
            currentTime += timeSlice;
            p.remainingTime -= timeSlice;

            // Dodaj nowe procesy które pojawiłły się w czasie działania obecnego
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (p.remainingTime > 0) {
                queue.add(p); // wraca do kolejki
            } else {
                p.finishTime = currentTime;
                completed.add(p);
            }
        }

        printResults("Round Robin (quantum = " + quantum + ")", completed, contextSwitches, ganttLog);
    }

    private static void printResults(String algorithm, List<Process> processes, int contextSwitches, List<String> ganttLog) {
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
            if (waitingTime > maxWaitingTime) maxWaitingTime = waitingTime;
        }

        System.out.println("Average Waiting Time: " + (double) totalWaiting / processes.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaround / processes.size());
        System.out.println("Max Waiting Time: " + maxWaitingTime);
        System.out.println("Context Switches: " + contextSwitches);

        if (!ganttLog.isEmpty()) {
            System.out.println("\nGantt Chart:");
            for (String entry : ganttLog) {
                System.out.println(entry);
            }
        }
    }
}
