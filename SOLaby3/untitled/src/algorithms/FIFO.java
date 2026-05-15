package algorithms;
import java.util.*;

public class FIFO implements PageReplacementAlgorithm {
    public String getName() {
        return "FIFO";
    }

    public int simulate(List<Integer> referenceString, int frameCount) {
        Set<Integer> frames = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        int faults = 0;

        for (int page : referenceString) {
            if (!frames.contains(page)) {
                if (frames.size() == frameCount) {
                    int removed = queue.poll();
                    frames.remove(removed);
                }
                frames.add(page);
                queue.offer(page);
                faults++;
            }
        }
        return faults;
    }
}