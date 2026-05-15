package frameAlgorithms;

import algorithms.ReferenceStringGenerator;

import java.util.ArrayList;
import java.util.List;

public class FrameAllocationSimulator {
    public static void main(String[] args) {
        int numProcesses = 5;
        int totalFrames = 10;
        int[] pageStarts = {0, 100, 200, 300, 400};
        int[] pageCounts = {30, 50, 15, 25, 40};
        int[] lengths = {100, 100, 100, 100, 100};
        int minPhase = 8, maxPhase = 20;
        int thrashWindow = 10;
        int thrashThreshold = thrashWindow / 2;

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            List<Integer> refString = ReferenceStringGenerator.generateWithRandomPhases(
                    pageStarts[i], pageCounts[i], lengths[i], minPhase, maxPhase
            );
            processes.add(new Process(i, refString, 0, thrashWindow, thrashThreshold));
        }


        /*List<List<Integer>> testSet = TestSets.setWadaProportional();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < testSet.size(); i++) {
            processes.add(new Process(i, testSet.get(i), 0, thrashWindow, thrashThreshold));
        }

         */

        for (Process p : processes) {
            System.out.println("Proces " + p.getId() + ": ciąg odwołań = " + p.getReferenceString());
        }
        System.out.println();

        List<FrameAllocationAlgorithm> algorithms = List.of(
                new EqualFrameAllocation(),
                new ProportionalFrameAllocation(),
                new PFFAllocation(0.1, 0.3, 0.5, 10),
                new WorkingSetAllocation(10,5)
        );
        String[] algNames = {"Equal", "Proportional", "PFF", "Working Set"};

        for (int a = 0; a < algorithms.size(); a++) {
            List<Process> procsCopy = new ArrayList<>();
            for (Process p : processes) {
                procsCopy.add(new Process(p.getId(), new ArrayList<>(p.getReferenceString()), 0, thrashWindow, thrashThreshold));
            }

            // UWAGA: dla PFF i WSS wszystko dzieje się w środku allocateFrames (dynamicznie).
            if (algorithms.get(a) instanceof PFFAllocation || algorithms.get(a) instanceof WorkingSetAllocation) {
                algorithms.get(a).allocateFrames(procsCopy, totalFrames);
            } else {
                // Equal / Proportional – najpierw podział ramek...
                algorithms.get(a).allocateFrames(procsCopy, totalFrames);
                // ...potem globalna naprzemienna symulacja
                boolean running = true;
                while (running) {
                    running = false;
                    for (Process p : procsCopy) {
                        if (p.hasReferences()) {
                            p.handleNextReference();
                            running = true;
                        }
                    }
                }
            }

            System.out.println("\n== WYNIKI SYMULACJI (" + algNames[a] + ") ==");
            for (Process p : procsCopy) {
                System.out.println("Proces " + p.getId() +
                        ": błędów stron = " + p.getPageFaults() +
                        ", liczba ramek = " + p.getLastNonZeroFrameCount() +
                        ", unikalnych stron = " + p.getUniquePageCount() +
                        ", szamotań = " + p.getThrashingEvents());
            }
            System.out.println();

        }
    }
}
