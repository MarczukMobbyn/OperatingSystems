package frameAlgorithms;

import java.util.*;

public class WorkingSetAllocation implements FrameAllocationAlgorithm {

    private final int WINDOW_SIZE;      // Δt – rozmiar okna (liczba odwołań do wyznaczania WSS)
    private final int RECOMPUTE_FREQ;   // c – jak często aktualizować WSS (np. c = Δt / 2)

    public WorkingSetAllocation(int windowSize, int recomputeFreq) {
        this.WINDOW_SIZE = windowSize;
        this.RECOMPUTE_FREQ = recomputeFreq;
    }

    @Override
    public void allocateFrames(List<Process> processes, int totalFrames) {
        // Inicjalizacja
        processes.forEach(p -> {
            p.setActive(true);
            p.setFrameCount(1); // na start każdy dostaje 1 ramkę (by nie mieć 0)
            p.resetWindowStats();
        });

        Map<Process, LinkedList<Integer>> recentReferences = new HashMap<>();
        Map<Process, Integer> wss = new HashMap<>();
        for (Process p : processes) recentReferences.put(p, new LinkedList<>());

        boolean running = true;
        int tick = 0;
        while (running) {
            running = false;
            // 1. Obsługa aktywnych procesów
            for (Process p : processes) {
                if (p.isActive() && p.hasReferences()) {
                    running = true;
                    int nextRef = p.getReferenceString().poll();
                    // Zbieraj odwołania do okna
                    LinkedList<Integer> refList = recentReferences.get(p);
                    refList.add(nextRef);
                    if (refList.size() > WINDOW_SIZE) refList.pollFirst();
                    // LRU obsługa
                    boolean fault = p.accessLRU(nextRef);
                    if (fault) p.setPageFaults(p.getPageFaults() + 1);

                    // Statystyki (opcjonalnie)
                    p.registerReference(fault);
                }
            }

            tick++;
            // 2. Co RECOMPUTE_FREQ kroków aktualizuj WSS i przydziel ramki
            if (tick % RECOMPUTE_FREQ == 0) {
                // Licz aktualny WSS każdego procesu
                int sumWSS = 0;
                for (Process p : processes) {
                    if (p.isActive() && p.hasReferences()) {
                        Set<Integer> unique = new HashSet<>(recentReferences.get(p));
                        int thisWSS = Math.max(1, unique.size()); // minimum 1
                        wss.put(p, thisWSS);
                        sumWSS += thisWSS;
                    }
                }

                // 3. Jeśli suma WSS <= liczba ramek – każdy dostaje swoje
                if (sumWSS <= totalFrames) {
                    for (Process p : processes) {
                        if (p.isActive() && p.hasReferences()) {
                            int newCount = wss.getOrDefault(p, 1);
                            p.setFrameCount(newCount);
                        }
                    }
                } else {
                    // 4. Jeśli się nie mieści – wstrzymaj proces(y)
                    // Tu: wstrzymujemy ten o największym WSS
                    Process toSuspend = null;
                    int maxWss = -1;
                    for (Process p : processes) {
                        if (p.isActive() && p.hasReferences()) {
                            int wssVal = wss.getOrDefault(p, 1);
                            if (wssVal > maxWss) {
                                maxWss = wssVal;
                                toSuspend = p;
                            }
                        }
                    }
                    if (toSuspend != null) {
                        toSuspend.setActive(false);
                        int zwalnia = toSuspend.getFrameCount();
                        toSuspend.setFrameCount(0);
                        System.out.printf("Proces %d WSTRZYMANY (WSS=%d)\n", toSuspend.getId(), maxWss);
                    }
                    // Rozdziel pozostałe ramki proporcjonalnie do WSS
                    int sumWSS2 = 0;
                    for (Process p : processes) {
                        if (p.isActive() && p.hasReferences())
                            sumWSS2 += wss.getOrDefault(p, 1);
                    }
                    int freeFrames = totalFrames;
                    for (Process p : processes) {
                        if (p.isActive() && p.hasReferences()) {
                            int share = Math.max(1, wss.getOrDefault(p, 1) * totalFrames / sumWSS2);
                            share = Math.min(share, freeFrames);
                            p.setFrameCount(share);
                            freeFrames -= share;
                        }
                    }
                }

                // 5. Wznów wstrzymane procesy, gdy są wolne ramki
                int freeFrames = totalFrames - processes.stream().filter(Process::isActive).mapToInt(Process::getFrameCount).sum();
                for (Process p : processes) {
                    if (!p.isActive() && p.hasReferences() && freeFrames > 0) {
                        int give = Math.min(wss.getOrDefault(p, 1), freeFrames);
                        if (give > 0) {
                            p.setActive(true);
                            p.setFrameCount(give);
                            System.out.printf("Proces %d WZNOWIONY (dostaje %d ramek)\n", p.getId(), give);
                            freeFrames -= give;
                        }
                    }
                }
            }

            // Zakończone procesy zwalniają ramki
            for (Process p : processes) {
                if (p.isActive() && !p.hasReferences() && p.getFrameCount() > 0) {
                    int zwalnia = p.getFrameCount();
                    p.setFrameCount(0);
                    p.setActive(false);
                    System.out.printf("Proces %d ZAKOŃCZONY (zwalnia %d ramek)\n", p.getId(), zwalnia);
                }
            }

            // Czy wszyscy już nie mają referencji?
            running = processes.stream().anyMatch(p -> p.hasReferences());
        }
    }
}
