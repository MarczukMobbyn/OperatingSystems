import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Request> reqs = TestSets.randomSet(100, 200, 0.2, 50);

        int diskSize = 200;
        int startPosition = 53;

        System.out.println("Lista żądań:");
        for (Request req : reqs) {
            System.out.println(req);
        }
        System.out.println();

        DiskSchedulerSimulator sim = new DiskSchedulerSimulator(reqs, diskSize, startPosition);
        sim.runFCFS();
        System.out.println();

        sim.runSSTF();
        System.out.println();

        sim.runSCAN(false);
        System.out.println();

        sim.runSCAN(true);
        System.out.println();

        sim.runCSCAN();
        System.out.println();

        sim.runEDF();
        System.out.println();

        sim.runFDScan();
        System.out.println();
    }
}
