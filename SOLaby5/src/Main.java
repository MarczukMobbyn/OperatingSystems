import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

// Główna klasa programu, uruchamia symulacje trzech strategii
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = 50; //ilosc precesorów

        int taskCount = 500; //ilosc zadan/procesów

        int p = 70; // próg p

        int maxTaskLoad = 15; // maksymalne obciążenie dla zadania

        int maxProbes = 3; // maksymalna liczba prób sprawdzania obciążenia

        int r = 20; // minimalny próg r

        // Generowanie procesorów i zadań
        ArrayList<Processor> processors = TaskGenerator.generateProcessors(n);
        ArrayList<Task> tasks = TaskGenerator.generateTasks(taskCount, maxTaskLoad);

        // Sortowanie zadań według czasu pojawienia się
        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));

        for (Task t : tasks) {
            System.out.println(t);
        }

        // Kopiowanie danych do algorytmu 1
        ArrayList<Task> a1 = new ArrayList<>();
        for (Task t : tasks) {
            a1.add(new Task(t.getId(), t.getLoad(), t.getExecutionTime(), t.getArrivalTime()));
        }
        ArrayList<Processor> p1 = new ArrayList<>();
        for (Processor pr : processors) {
            p1.add(new Processor(pr.getId()));
        }
        Algorithms.algorithm1(a1, p1, maxProbes, p);

        // Kopiowanie danych do algorytmu 2
        ArrayList<Task> a2 = new ArrayList<>();
        for (Task t : tasks) {
            a2.add(new Task(t.getId(), t.getLoad(), t.getExecutionTime(), t.getArrivalTime()));
        }
        ArrayList<Processor> p2 = new ArrayList<>();
        for (Processor pr : processors) {
            p2.add(new Processor(pr.getId()));
        }
        Algorithms.algorithm2(a2, p2, p);

        // Kopiowanie danych do algorytmu 3
        ArrayList<Task> a3 = new ArrayList<>();
        for (Task t : tasks) {
            a3.add(new Task(t.getId(), t.getLoad(), t.getExecutionTime(), t.getArrivalTime()));
        }
        ArrayList<Processor> p3 = new ArrayList<>();
        for (Processor pr : processors) {
            p3.add(new Processor(pr.getId()));
        }
        Algorithms.algorithm3(a3, p3, p, r);

        // Przykładowe wartości:
        // Enter number of processors:
        // 50
        // Enter number of tasks:
        // 500
        // Enter threshold p:
        // 70
        // Enter maximum load for a task:
        // 15
        // Enter max number of load probes:
        // 3
        // Enter minimum threshold r:
        // 20
    }
}
