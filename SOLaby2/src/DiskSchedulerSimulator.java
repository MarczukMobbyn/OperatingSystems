import java.util.*;

public class DiskSchedulerSimulator {
    private List<Request> requests;
    private int diskSize;
    private int startPosition;

    public DiskSchedulerSimulator(List<Request> requests, int diskSize, int startPosition) {
        this.requests = new ArrayList<>(requests);
        this.diskSize = diskSize;
        this.startPosition = startPosition;
    }


    public void runFCFS() {
        int head = startPosition;
        int totalMovement = 0;

        System.out.println("Running FCFS...");
        for (Request r : requests) {
            totalMovement += Math.abs(head - r.cylinder);
            head = r.cylinder;
        }

        System.out.println("Total head movement: " + totalMovement + " cylinders");
    }
    public int runSSTFWithResult(int customStartPosition, List<Request> requestList) {
        int head = customStartPosition;
        int totalMovement = 0;
        List<Request> remaining = new ArrayList<Request>(requestList);

        while (!remaining.isEmpty()) {
            Request closest = null;
            int shortestDistance = Integer.MAX_VALUE;

            for (Request r : remaining) {
                int distance = Math.abs(head - r.cylinder);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closest = r;
                }
            }

            totalMovement += shortestDistance;
            head = closest.cylinder;
            remaining.remove(closest);
        }

        return totalMovement;
    }


    public void runSSTF() {
        int total = runSSTFWithResult(startPosition, requests);
        System.out.println("Running SSTF...");
        System.out.println("Total head movement: " + total + " cylinders");
    }

    public int runSCANWithResults(int customStartPosition,boolean goRightFirst, List<Request> requestList) {
        int head = customStartPosition;
        int totalMovement = 0;

        List<Request> sorted = new ArrayList<>(requestList);
        Collections.sort(sorted, new RequestComparator());

        System.out.println("Running SCAN (" + (goRightFirst ? "right first" : "left first") + ")...");

        List<Request> left = new ArrayList<>();
        List<Request> right = new ArrayList<>();

        for (Request r : sorted) {
            if (r.cylinder < head) {
                left.add(r);
            } else {
                right.add(r);
            }
        }

        Collections.reverse(left); // żeby szło malejąco, np. 37, 14

        if (!goRightFirst) {
            // Najpierw w lewo
            for (Request r : left) {
                int distance = Math.abs(head - r.cylinder);
                System.out.println("Obsłużono (SCAN): " + r + " | przemieszczenie: " + distance);
                totalMovement += distance;
                head = r.cylinder;
            }

            if (!right.isEmpty()) {
                System.out.println("Przejazd do cylindra 0 | przemieszczenie: " + head);
                totalMovement += head;
                head = 0;
            }

            for (Request r : right) {
                int distance = Math.abs(head - r.cylinder);
                System.out.println("Obsłużono (SCAN): " + r + " | przemieszczenie: " + distance);
                totalMovement += distance;
                head = r.cylinder;
            }
        } else {
            // Najpierw w prawo
            for (Request r : right) {
                int distance = Math.abs(head - r.cylinder);
                System.out.println("Obsłużono (SCAN): " + r + " | przemieszczenie: " + distance);
                totalMovement += distance;
                head = r.cylinder;
            }

            if (!left.isEmpty()) {
                int toEnd = diskSize - 1 - head;
                System.out.println("Przejazd do końca dysku (" + (diskSize - 1) + ") | przemieszczenie: " + toEnd);
                totalMovement += toEnd;
                head = diskSize - 1;
            }

            for (Request r : left) {
                int distance = Math.abs(head - r.cylinder);
                System.out.println("Obsłużono (SCAN): " + r + " | przemieszczenie: " + distance);
                totalMovement += distance;
                head = r.cylinder;
            }
        }

        return totalMovement;
    }


    public void runSCAN(boolean goRightFirst) {
        int total = runSCANWithResults(startPosition,goRightFirst, requests);
        System.out.println("Total head movement: " + total + " cylinders");
    }

    public void runCSCAN() {
        int head = startPosition;
        int totalMovement = 0;

        List<Request> sorted = new ArrayList<Request>(requests);
        Collections.sort(sorted, new RequestComparator());

        System.out.println("Running C-SCAN...");

        List<Request> left = new ArrayList<Request>();
        List<Request> right = new ArrayList<Request>();

        for (int i = 0; i < sorted.size(); i++) {
            Request r = sorted.get(i);
            if (r.cylinder < head) {
                left.add(r);
            } else {
                right.add(r);
            }
        }

        // Obsługa prawej strony
        for (int i = 0; i < right.size(); i++) {
            Request r = right.get(i);
            totalMovement += Math.abs(head - r.cylinder);
            head = r.cylinder;
        }

        // DOCHODZIMY DO KOŃCA DYSKU (199)
        if (!right.isEmpty() && head < diskSize - 1) {
            totalMovement += (diskSize - 1 - head);
            head = diskSize - 1;
        }

        // Skok z 199 na 0 – nie liczony
        head = 0;

        // Obsługa lewej strony
        for (int i = 0; i < left.size(); i++) {
            Request r = left.get(i);
            totalMovement += Math.abs(head - r.cylinder);
            head = r.cylinder;
        }

        System.out.println("Total head movement: " + totalMovement + " cylinders");
    }

    public void runEDF() {
        int head = startPosition;
        int edfMovement = 0;

        List<Request> realTimeRequests = new ArrayList<Request>();
        List<Request> regularRequests = new ArrayList<Request>();

        for (Request r : requests) {
            if (r.isRealTime) {
                realTimeRequests.add(r);
            } else {
                regularRequests.add(r);
            }
        }

        System.out.println("Running EDF (Earliest Deadline First)...");

        while (!realTimeRequests.isEmpty()) {
            Request earliest = realTimeRequests.get(0);
            for (int i = 1; i < realTimeRequests.size(); i++) {
                if (realTimeRequests.get(i).deadline < earliest.deadline) {
                    earliest = realTimeRequests.get(i);
                }
            }

            int distance = Math.abs(head - earliest.cylinder);
            edfMovement += distance;
            head = earliest.cylinder;

            System.out.println("Obsłużono real-time: " + earliest + " | przemieszczenie: " + distance);
            realTimeRequests.remove(earliest);
        }

        System.out.println("head movement (EDF part): " + edfMovement + " cylinders");

        if (!regularRequests.isEmpty()) {
            System.out.println("Brak żądań real-time – przełączam na SSTF dla pozostałych.");
            int sstfMovement = runSSTFWithResult(head, regularRequests);
            System.out.println("Total head movement: " + sstfMovement + " cylinders (SSTF part)");
            System.out.println("Total head movement (EDF + SSTF): " + (edfMovement + sstfMovement) + " cylinders");
        }
    }

    public void runFDScan() {
        int head = startPosition;
        int totalMovement = 0;
        int currentTime = 0;

        List<Request> realTimeRequests = new ArrayList<>();
        List<Request> regularRequests = new ArrayList<>();

        for (Request r : requests) {
            if (r.isRealTime) {
                realTimeRequests.add(r);
            } else {
                regularRequests.add(r);
            }
        }

        Set<Request> processed = new HashSet<>();
        System.out.println("Running FD-SCAN...");

        // Obsługa real-time: RT-first z kolejnym wyborem feasible RT
        while (true) {
            Request nextRT = null;
            int shortestDeadline = Integer.MAX_VALUE;

            for (Request r : realTimeRequests) {
                if (processed.contains(r)) continue;
                int distance = Math.abs(head - r.cylinder);
                int timeToReach = currentTime + distance;

                if (timeToReach > r.deadline) {
                    System.out.println("Odrzucono RT: " + r + " | potrzebne: " + timeToReach + ", deadline: " + r.deadline);
                    processed.add(r);
                    continue;
                }

                if (r.deadline < shortestDeadline) {
                    nextRT = r;
                    shortestDeadline = r.deadline;
                }
            }

            if (nextRT == null) break;

            boolean goingRight = nextRT.cylinder >= head;
            List<Request> all = new ArrayList<>();
            all.addAll(realTimeRequests);
            all.addAll(regularRequests);
            all.sort(new RequestComparator());

            if (goingRight) {
                for (Request r : all) {
                    if (processed.contains(r)) continue;
                    if (r.cylinder < head || r.cylinder > nextRT.cylinder) continue;

                    int distance = Math.abs(head - r.cylinder);
                    int timeToReach = currentTime + distance;

                    if (r.isRealTime && timeToReach > r.deadline) {
                        System.out.println("Odrzucono RT: " + r + " | potrzebne: " + timeToReach + ", deadline: " + r.deadline);
                        processed.add(r);
                        continue;
                    }

                    System.out.println("Obsłużono: " + r + (r.isRealTime ? " (RT)" : "") + " | przemieszczenie: " + distance);
                    totalMovement += distance;
                    currentTime += distance;
                    head = r.cylinder;
                    processed.add(r);

                    if (r == nextRT) break;
                }
            } else {
                for (int i = all.size() - 1; i >= 0; i--) {
                    Request r = all.get(i);
                    if (processed.contains(r)) continue;
                    if (r.cylinder > head || r.cylinder < nextRT.cylinder) continue;

                    int distance = Math.abs(head - r.cylinder);
                    int timeToReach = currentTime + distance;

                    if (r.isRealTime && timeToReach > r.deadline) {
                        System.out.println("Odrzucono RT: " + r + " | potrzebne: " + timeToReach + ", deadline: " + r.deadline);
                        processed.add(r);
                        continue;
                    }

                    System.out.println("Obsłużono: " + r + (r.isRealTime ? " (RT)" : "") + " | przemieszczenie: " + distance);
                    totalMovement += distance;
                    currentTime += distance;
                    head = r.cylinder;
                    processed.add(r);

                    if (r == nextRT) break;
                }
            }
        }

        System.out.println("head movement (FD-SCAN RT): " + totalMovement + " cylinders");

        // SCAN dla pozostałych
        List<Request> remaining = new ArrayList<>();
        for (Request r : requests) {
            if (!processed.contains(r)) {
                remaining.add(r);
            }
        }

        if (!remaining.isEmpty())
        {
            System.out.println("Brak żądań real-time – przełączam na SCAN dla pozostałych.");
            remaining.sort(new RequestComparator());
            boolean scanRight = (head < diskSize / 2);

            int SCANmovment = runSCANWithResults(head,scanRight, remaining);
            totalMovement += SCANmovment;

            System.out.println("Obsłużono pozostałe żądania SCAN: " + SCANmovment + " cylinders");
        } else {
            System.out.println("Brak żądań do obsługi.");
            return;
        }

        System.out.println("Total head movement (FD-SCAN): " + totalMovement + " cylinders");
    }



    public class RequestComparator implements Comparator<Request> {
        @Override
        public int compare(Request r1, Request r2) {
            return Integer.compare(r1.cylinder, r2.cylinder);
        }
    }



}
