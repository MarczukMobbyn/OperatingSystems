import java.util.ArrayList;
import java.util.Random;

// Klasa pomocnicza do generowania procesorów i zadań
public class TaskGenerator {
    public static Random random = new Random();

    // Generuje listę procesorów
    public static ArrayList<Processor> generateProcessors(int n) {
        ArrayList<Processor> processors = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            processors.add(new Processor(i));
        }
        return processors;
    }

    // Generuje listę zadań
    public static ArrayList<Task> generateTasks(int taskCount, int maxLoad) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= taskCount; i++) {
            int load = random.nextInt(maxLoad - 3 + 1) + 3;                // obciążenie z zakresu [3, maxLoad]
            int executionTime = random.nextInt(100 - 1 + 1) + 1;           // czas wykonania z zakresu [1, 100]
            int arrivalTime = random.nextInt(70 - 1 + 1) + 1;              // czas pojawienia się z zakresu [1, 70]
            tasks.add(new Task(i, load, executionTime, arrivalTime));
        }
        return tasks;
    }
}
