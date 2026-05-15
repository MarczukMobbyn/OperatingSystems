package frameAlgorithms;

import java.util.List;

public class ProportionalFrameAllocation implements FrameAllocationAlgorithm {
    @Override
    public void allocateFrames(List<Process> processes, int totalFrames) {
        int sumPages = 0;
        for (Process p : processes) {
            sumPages += p.getUniquePageCount();
        }

        int totalAssigned = 0;
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            int processPages = p.getUniquePageCount();
            int frames = (int) Math.round((double) processPages / sumPages * totalFrames);
            if (frames < 1) frames = 1;
            if (i == processes.size() - 1) {
                frames = totalFrames - totalAssigned;
            }
            p.setFrameCount(frames);
            totalAssigned += frames;
        }
    }
}
