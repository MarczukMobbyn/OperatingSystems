import java.util.*;

public class TestSets {

    public static List<Request> simpleExample() {
        return Arrays.asList(
                new Request(98),
                new Request(183),
                new Request(37),
                new Request(122),
                new Request(14),
                new Request(124),
                new Request(65),
                new Request(67)
        );
    }

    public static List<Request> withRealTime() {
        return Arrays.asList(
                new Request(98),
                new Request(183),
                new Request(37),
                new Request(122),
                new Request(14, 70),   // RT
                new Request(124, 30),  // RT
                new Request(65, 20),   // RT
                new Request(67)
        );
    }

    public static List<Request> realTimePressure() {
        return Arrays.asList(
                new Request(10, 20),
                new Request(15, 15),
                new Request(190, 30),
                new Request(30),
                new Request(160),
                new Request(180),
                new Request(70),
                new Request(20, 50)
        );
    }

    public static List<Request> randomSet(int count, int diskSize, double rtProbability, int maxDeadline) {
        Random rand = new Random();
        List<Request> requests = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int cylinder = rand.nextInt(diskSize);
            if (rand.nextDouble() < rtProbability) {
                int deadline = rand.nextInt(maxDeadline) + 1;
                requests.add(new Request(cylinder, deadline));
            } else {
                requests.add(new Request(cylinder));
            }
        }

        return requests;
    }
}