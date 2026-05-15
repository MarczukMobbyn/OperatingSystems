public class Process {
    public int id;
    public int arrivalTime;
    public int burstTime;
    public int remainingTime;
    public int startTime = -1;
    public int finishTime = -1;

    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public int getWaitingTime() {
        return (finishTime - arrivalTime - burstTime);
    }

    public int getTurnaroundTime() {
        return (finishTime - arrivalTime);
    }

    @Override
    public String toString() {
        return "P" + id + " (arrival: " + arrivalTime + ", burst: " + burstTime + ")";
    }
}
