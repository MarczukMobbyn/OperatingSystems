
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        List<Process> original = TestSetFactory.getRandomProcessList(50, 20, 20);


        System.out.println("Generated processes:");
        for (Process p : original) {
            System.out.println(p);
        }


        // Przekazujemy każdemu algorytmowi osobną kopię
        FCFS.run(deepCopy(original));
        SJF.run(deepCopy(original));
        SRTF.run(deepCopy(original));
        RoundRobin.run(deepCopy(original), 2, 1);
    }

    public static List<Process> deepCopy(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.id, p.arrivalTime, p.burstTime));
        }
        return copy;
    }
}
