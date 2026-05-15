package frameAlgorithms;

import java.util.List;

public class PFFAllocation implements FrameAllocationAlgorithm {

    private final double LOWER_THRESHOLD;
    private final double UPPER_THRESHOLD;
    private final double HIGH_UPPER_THRESHOLD;
    private final int WINDOW_SIZE;

    public PFFAllocation(double lower, double upper, double highUpper, int window) {
        this.LOWER_THRESHOLD = lower;
        this.UPPER_THRESHOLD = upper;
        this.HIGH_UPPER_THRESHOLD = highUpper;
        this.WINDOW_SIZE = window;
    }

    @Override
    public void allocateFrames(List<Process> processes, int totalFrames) {
        int numProcesses = processes.size();
        int minFramesPerProcess = Math.max(1, totalFrames / 2);

        // Przydział początkowy proporcjonalny z minimalną gwarancją
        int sumPages = 0;
        for (Process p : processes) sumPages += p.getUniquePageCount();
        int assigned = 0;

        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            int frames = (int) Math.round((double) p.getUniquePageCount() / sumPages * totalFrames);
            if (frames < minFramesPerProcess) frames = minFramesPerProcess;
            if (i == processes.size() - 1) frames = totalFrames - assigned; // ostatni proces dostaje resztę
            p.setFrameCount(frames);
            assigned += frames;
            p.setActive(true);
            p.resetWindowStats();
        }

        while (true) {
            boolean postep = false;
            boolean jakiekolwiekReferencje = false;

            // 1. Obsługa aktywnych procesów
            for (Process p : processes) {
                if (p.isActive() && p.hasReferences()) {
                    jakiekolwiekReferencje = true;
                    postep = true;
                    p.handleNextReference();

                    if (p.getWindowCounter() >= WINDOW_SIZE) {
                        double ppf = (double) p.getWindowFaults() / WINDOW_SIZE;
                        if (ppf > HIGH_UPPER_THRESHOLD) {
                            p.setActive(false);
                            System.out.printf("Proces %d WSTRZYMANY (PPF=%.2f > %.2f)\n", p.getId(), ppf, HIGH_UPPER_THRESHOLD);
                            p.resetWindowStats();
                            p.setFrameCount(0);
                        } else if (ppf > UPPER_THRESHOLD) {
                            int free = totalFrames - usedFrames(processes);
                            if (free > 0) {
                                p.setFrameCount(p.getFrameCount() + 1);
                            }
                            p.resetWindowStats();
                        } else if (ppf < LOWER_THRESHOLD && p.getFrameCount() > minFramesPerProcess) {
                            p.setFrameCount(p.getFrameCount() - 1);
                            p.resetWindowStats();
                        } else {
                            p.resetWindowStats();
                        }
                    }

                    // Jeśli skończył się ciąg referencji – zwalniamy ramki!
                    if (!p.hasReferences()) {
                        int zwalniane = p.getFrameCount();
                        if (zwalniane > 0) {
                            p.setFrameCount(0);
                            postep = true;
                        }
                        p.setActive(false);
                        System.out.printf("Proces %d ZAKOŃCZONY (zwalnia %d ramek)\n", p.getId(), zwalniane);
                    }
                } else if (p.hasReferences()) {
                    jakiekolwiekReferencje = true;
                }
            }

            // 2. Wznawianie wstrzymanych procesów – tylko jeśli jest minimalny przydział do podziału
            int freeFrames = totalFrames - usedFrames(processes);
            boolean wznowiono = true;
            while (wznowiono && freeFrames >= minFramesPerProcess) {
                wznowiono = false;
                for (Process p : processes) {
                    if (!p.isActive() && p.hasReferences() && freeFrames >= minFramesPerProcess) {
                        int toGive = Math.min(minFramesPerProcess, freeFrames);
                        p.setActive(true);
                        p.setFrameCount(toGive);
                        freeFrames -= toGive;
                        p.resetWindowStats();
                        System.out.printf("Proces %d WZNOWIONY (dostaje %d ramek)\n", p.getId(), toGive);
                        wznowiono = true;
                        postep = true;
                    }
                }
            }

            if (!jakiekolwiekReferencje) break;
            if (!postep) break;
        }
    }

    private int usedFrames(List<Process> processes) {
        return processes.stream().mapToInt(Process::getFrameCount).sum();
    }
}
