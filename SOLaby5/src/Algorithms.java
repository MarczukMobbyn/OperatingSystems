import java.util.*;

public class Algorithms {
    public static Random random = new Random();

    // Algorithm 1
    public static void algorithm1(ArrayList<Task> tasks, ArrayList<Processor> processors, int maxProbes, double threshold) {
        int migrations = 0;
        int queries = 0;
        int totalProbesToMigration = 0;
        double totalDeviation = 0;
        ArrayList<Double> earlyAverages = new ArrayList<>();
        ArrayList<Double> deviations = new ArrayList<>();
        int n = tasks.size();

        // Sortowanie zadań po czasie pojawienia się
        tasks.sort(Comparator.comparing(Task::getArrivalTime));

        while (!tasks.isEmpty()) {
            Task current = tasks.remove(0);

            int xIndex = random.nextInt(processors.size());
            Processor x = processors.get(xIndex);
            boolean assigned = false;
            int probesCount = 0;

            for (int i = 0; i < maxProbes; i++) {
                int yIndex;
                do {
                    yIndex = random.nextInt(processors.size());
                } while (yIndex == xIndex);

                queries++;
                probesCount++;
                Processor y = processors.get(yIndex);

                if (y.getLoad() + current.getLoad() < threshold) {
                    y.setLoad(y.getLoad() + current.getLoad());
                    y.getTasks().add(current);
                    migrations++;
                    totalProbesToMigration += probesCount;
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                x.getTasks().add(current);
                x.setLoad(x.getLoad() + current.getLoad());
            }

            // Statystyki
            double sum = 0;
            for (Processor p : processors) sum += p.getLoad();
            double averageLoad = sum / processors.size();
            earlyAverages.add(averageLoad);

            double sumSquares = 0;
            for (Processor p : processors) {
                double diff = p.getLoad() - averageLoad;
                sumSquares += diff * diff;
            }
            double deviation = Math.sqrt(sumSquares / processors.size());
            deviations.add(deviation);
        }

        double betterAverage = 0;
        for (double x : earlyAverages) betterAverage += x;
        for (double x : deviations) totalDeviation += x;

        System.out.println("Strategia 1:");
        System.out.println("Zapytania: " + queries);
        System.out.println("Migracje: " + migrations);
        System.out.println("Srednia lepsza: " + betterAverage / n);
        System.out.println("Odchylenie lepsze: " + totalDeviation / n);
        System.out.println();
    }

    // Algorithm 2
    public static void algorithm2(ArrayList<Task> tasks, ArrayList<Processor> processors, double threshold) {
        int migrations = 0;
        int queries = 0;
        int tasksWithoutMigration = 0;
        int tasksLocalImmediately = 0;
        double totalDeviation = 0;
        ArrayList<Double> earlyAverages = new ArrayList<>();
        ArrayList<Double> deviations = new ArrayList<>();
        int n = tasks.size();

        // Sortowanie zadań po czasie pojawienia się
        tasks.sort(Comparator.comparing(Task::getArrivalTime));

        for (int i = 0; i < tasks.size(); i++) {
            Task current = tasks.get(i);
            Processor x = processors.get(i % processors.size()); //Każdy procesor dostaje "swoje" zadanie

            if (x.getLoad() > threshold) {
                boolean migrated = false;
                Set<Integer> visited = new HashSet<>();

                while (visited.size() < processors.size() - 1) {
                    int yIndex = random.nextInt(processors.size());
                    if (yIndex == processors.indexOf(x) || visited.contains(yIndex)) continue;

                    visited.add(yIndex);
                    queries++;
                    Processor y = processors.get(yIndex);

                    if (y.getLoad() < threshold) {
                        y.getTasks().add(current);
                        y.setLoad(y.getLoad() + current.getLoad());
                        migrations++;
                        migrated = true;
                        break;
                    }
                }

                if (!migrated) {
                    x.getTasks().add(current);
                    x.setLoad(x.getLoad() + current.getLoad());
                    tasksWithoutMigration++;
                }

            } else {
                x.getTasks().add(current);
                x.setLoad(x.getLoad() + current.getLoad());
                tasksLocalImmediately++;
            }

            // Statystyki
            double sum = 0;
            for (Processor p : processors) sum += p.getLoad();
            double averageLoad = sum / processors.size();
            earlyAverages.add(averageLoad);

            double sumSquares = 0;
            for (Processor p : processors) {
                double diff = p.getLoad() - averageLoad;
                sumSquares += diff * diff;
            }
            double deviation = Math.sqrt(sumSquares / processors.size());
            deviations.add(deviation);
        }

        double betterAverage = 0;
        for (double x : earlyAverages) betterAverage += x;
        for (double x : deviations) totalDeviation += x;

        System.out.println("Strategia 2:");
        System.out.println("Zapytania: " + queries);
        System.out.println("Migracje: " + migrations);
        System.out.println("Srednia lepsza: " + betterAverage / n);
        System.out.println("Odchylenie lepsze: " + totalDeviation / n);
        System.out.println("Zadania bez migracji (brak dostępnych): " + tasksWithoutMigration);
        System.out.println("Zadania lokalne od razu (x.obc <= prog): " + tasksLocalImmediately);
        System.out.println();
    }

    // Algorithm 3
    public static void algorithm3(ArrayList<Task> tasks, ArrayList<Processor> processors, double threshold, double minThreshold) {
        int migrations = 0;
        int queries = 0;
        double totalDeviation = 0;
        ArrayList<Double> earlyAverages = new ArrayList<>();
        ArrayList<Double> deviations = new ArrayList<>();
        int n = tasks.size();

        // Sortowanie zadań po czasie pojawienia się
        tasks.sort(Comparator.comparing(Task::getArrivalTime));

        while (!tasks.isEmpty()) {
            ArrayList<Processor> lowLoaded = new ArrayList<>();
            ArrayList<Processor> highLoaded = new ArrayList<>();

            for (Processor p : processors) {
                if (p.getLoad() < minThreshold) lowLoaded.add(p);
                else if (p.getLoad() > threshold) highLoaded.add(p);
            }

            for (Processor p : lowLoaded) {
                Iterator<Processor> it = highLoaded.iterator();

                while (it.hasNext()) {
                    Processor w = it.next();

                    while (p.getLoad() < minThreshold && w.getLoad() > threshold && !w.getTasks().isEmpty()) {
                        // Szukamy zadania o największym obciążeniu
                        Task max = Collections.max(w.getTasks(), Comparator.comparingInt(Task::getLoad));

                        if (p.getLoad() + max.getLoad() > minThreshold) break;

                        w.getTasks().remove(max);
                        w.setLoad(w.getLoad() - max.getLoad());

                        p.getTasks().add(max);
                        p.setLoad(p.getLoad() + max.getLoad());
                        migrations++;
                    }

                    if (w.getLoad() <= threshold || w.getTasks().isEmpty()) {
                        it.remove();
                    }
                }
            }

            Task current = tasks.remove(0);
            int xIndex = random.nextInt(processors.size());
            Processor x = processors.get(xIndex);

            if (x.getLoad() + current.getLoad() < threshold) {
                x.getTasks().add(current);
                x.setLoad(x.getLoad() + current.getLoad());
            } else {
                boolean migrated = false;
                int probesCount = 0;

                while (probesCount < processors.size() - 1) {
                    int yIndex = random.nextInt(processors.size());
                    if (yIndex == xIndex) continue;

                    queries++;
                    probesCount++;
                    Processor y = processors.get(yIndex);

                    if (y.getLoad() + current.getLoad() < threshold) {
                        y.getTasks().add(current);
                        y.setLoad(y.getLoad() + current.getLoad());
                        migrations++;
                        migrated = true;
                        break;
                    }
                }

                if (!migrated) {
                    x.getTasks().add(current);
                    x.setLoad(x.getLoad() + current.getLoad());
                }

                // Statystyki
                double sum = 0;
                for (Processor p : processors) sum += p.getLoad();
                double averageLoad = sum / processors.size();
                earlyAverages.add(averageLoad);

                double sumSquares = 0;
                for (Processor p : processors) {
                    double diff = p.getLoad() - averageLoad;
                    sumSquares += diff * diff;
                }
                double deviation = Math.sqrt(sumSquares / processors.size());
                deviations.add(deviation);
            }
        }
        double betterAverage = 0;
        for (double x : earlyAverages) betterAverage += x;
        for (double x : deviations) totalDeviation += x;

        System.out.println("Strategia 3:");
        System.out.println("Zapytania: " + queries);
        System.out.println("Migracje: " + migrations);
        System.out.println("Srednia lepsza: " + betterAverage / n);
        System.out.println("Odchylenie lepsze: " + totalDeviation / n);
        System.out.println();

    }
}
