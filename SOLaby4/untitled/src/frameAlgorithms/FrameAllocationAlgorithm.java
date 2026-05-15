package frameAlgorithms;

import java.util.List;

public interface FrameAllocationAlgorithm {
    void allocateFrames(List<Process> processes, int totalFrames);
}
