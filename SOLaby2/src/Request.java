public class Request {
    public int cylinder;         // nr cylindra
    public boolean isRealTime;   // czy to żądanie czasu rzeczywistego
    public int deadline;         // deadline w jednostkach "ruchu głowicy"

    public Request(int cylinder) {
        this.cylinder = cylinder;
        this.isRealTime = false;
        this.deadline = Integer.MAX_VALUE; // brak deadline'u
    }

    public Request(int cylinder, int deadline) {
        this.cylinder = cylinder;
        this.deadline = deadline;
        this.isRealTime = true;
    }

    @Override
    public String toString() {
        return String.format("Request[cylinder=%d, realTime=%b, deadline=%d]",
                cylinder, isRealTime, deadline);
    }
}
