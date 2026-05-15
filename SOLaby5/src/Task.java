// Klasa reprezentująca pojedyncze zadanie
public class Task {
    private int id;                 // numer zadania (lp)
    private int load;               // obciążenie zadania
    private int executionTime;      // czas wykonania zadania
    private int arrivalTime;        // czas pojawienia się zadania

    public Task(int id, int load, int executionTime, int arrivalTime) {
        this.id = id;
        this.load = load;
        this.executionTime = executionTime;
        this.arrivalTime = arrivalTime;
    }

    // Ustawia numer zadania
    public void setId(int id) {
        this.id = id;
    }

    // Pobiera numer zadania
    public int getId() {
        return id;
    }

    // Pobiera czas wykonania zadania
    public int getExecutionTime() {
        return executionTime;
    }

    // Pobiera obciążenie zadania
    public int getLoad() {
        return load;
    }

    // Ustawia czas wykonania zadania
    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    // Ustawia obciążenie zadania
    public void setLoad(int load) {
        this.load = load;
    }

    // Ustawia czas pojawienia się zadania
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    // Pobiera czas pojawienia się zadania
    public int getArrivalTime() {
        return arrivalTime;
    }

    // Wykonuje jedno "odliczenie" czasu wykonania
    public void execute() {
        executionTime--;
    }

    public String toString() {
        return String.format("Id: %3d, Load: %3d, Arrival: %3d", id, load, arrivalTime);
    }
}
