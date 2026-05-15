# 💻 Operating Systems - Monorepo

A collection of simulation projects developed as part of the **Operating Systems** coursework. The project is structured as a monorepo, where each module (assignment list) covers a key topic in system algorithms and mechanisms.

## 📂 Module Overview

### Lista 1: CPU Scheduling
A simulator comparing the performance of CPU scheduling algorithms for processes with diverse arrival times and burst requirements.
* **Algorithms:** FCFS, SJF (non-preemptive), SRTF (preemptive SJF), and Round Robin (RR - with time quantum and context switch cost).
* **Metrics and features:** Calculation of Average Waiting Time (AWT) and Average Turnaround Time (ATT), context switch counting, potential process starvation detection, and textual Gantt chart visualization.

### Lista 2: Disk Scheduling
A simulator analyzing the efficiency of hard drive head positioning algorithms to minimize seek time.
* **Algorithms:** FCFS, SSTF, SCAN (elevator algorithm), and C-SCAN (Circular SCAN).
* **Real-Time Support:** EDF (Earliest Deadline First) and FD-SCAN (Feasible Deadline SCAN) – an advanced variant that rejects requests impossible to fulfill in time while handling regular requests "on the way".
* **Metrics:** Total head movement expressed in cylinders.

### Lista 3: Page Replacement
A virtual memory simulator examining system behavior under a limited pool of RAM page frames.
* **Algorithms:** FIFO, LRU (Least Recently Used), OPT (theoretical optimal algorithm used as a benchmark), Random, and Second Chance (using a reference bit).
* **Metrics and features:** Page Fault counting, result averaging for the random algorithm, and a reference string generator accounting for program phase locality.

### Lista 4: Frame Allocation
A multi-process environment simulating the distribution of available physical memory (frames) among concurrently running programs.
* **Algorithms:** Static (Equal, Proportional) and Dynamic (PFF - Page Fault Frequency, Working Set Model).
* **Metrics and features:** Each process maintains an independent LRU instance. The system tracks and counts thrashing events, executes dynamic suspension (swapping out) of overly resource-heavy processes, and automatically resumes them.

### Lista 5: Load Balancing
A simulation of distributed task migration and load balancing algorithms in a multiprocessor architecture.
* **Strategies:**
  1. *Strategy 1:* A processor randomly probes up to *z* neighbors and migrates the task if the target's load does not exceed threshold *p*.
  2. *Strategy 2:* In case of an assigned task overloading the node, the processor randomly searches for a free node until successful or options are exhausted.
  3. *Strategy 3 (Work Stealing):* Active task stealing – underloaded processors (below threshold *r*) independently query overloaded units and take over their heaviest tasks.
* **Metrics:** Total number of queries, number of migrations, and the standard deviation of the load (system balance indicator).

---

## 🏃 How to run?
Each folder (`SOLaby1` do `SOLaby5`) represents an independent module with predefined test sets (edge cases and random generators).

1. Open the project in IntelliJ IDEA.
2. Right-click the `src` folder in the selected module and choose **Mark Directory as -> Sources Root**.
3. Run the entry class (e.g., `Main.java` or `TestRunner.java`) using the green *Run* arrow.
