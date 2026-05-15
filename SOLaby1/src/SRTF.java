import java.util.*;

public class SRTF {
    public static void run(List<Process> inputProcesses) {
        List<Process> processes = new ArrayList<>(inputProcesses);
        List<Process> completed = new ArrayList<>();

        int currentTime = 0;
        Process currentProcess = null;
        int contextSwitches = 0;

        List<String> ganttLog = new ArrayList<>();

        while (completed.size() < inputProcesses.size()) {

            List<Process> available = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                currentTime++; //brak dostepnych procesow
                continue;
            }

            // Wybieramy proces z najmniejszym remainingTime
            Process shortest = Collections.min(available, Comparator.comparingInt(p -> p.remainingTime));

            // Jeśli zmienił się aktualnie wykonywany proces to liczymy jako przełączenie kontekstu
            if (currentProcess != shortest) {
                if (currentProcess != null) {
                    contextSwitches++;
                }
                ganttLog.add("Time " + currentTime + ": P" + shortest.id);
                currentProcess = shortest;
            }

            if (shortest.startTime == -1) {
                shortest.startTime = currentTime;
            }

            // Wykonujemy 1 jednostkę czasu
            shortest.remainingTime--;
            currentTime++;

            // Jeśli się skończył to zapisujemy finishTime i dodajemy do zakończonych
            if (shortest.remainingTime == 0) {
                shortest.finishTime = currentTime;
                completed.add(shortest);
            }
        }

        printResults("SRTF", completed, contextSwitches, ganttLog);
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

            // Sprawdzenie maksymalnego czasu oczekiwania
            waitingTime = p.getWaitingTime();
            if (waitingTime > maxWaitingTime) {
                maxWaitingTime = waitingTime;
            }
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
