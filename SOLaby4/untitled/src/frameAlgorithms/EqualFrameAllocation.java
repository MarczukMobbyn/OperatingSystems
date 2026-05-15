package frameAlgorithms;

import java.util.List;

public class EqualFrameAllocation implements FrameAllocationAlgorithm {
    @Override
    public void allocateFrames(List<Process> processes, int totalFrames) {
        int framesPerProcess = totalFrames / processes.size();
        for (Process p : processes) {
            p.setFrameCount(framesPerProcess);
        }
    }
}
