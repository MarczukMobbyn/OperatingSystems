import java.util.ArrayList;

// Klasa reprezentująca pojedynczy procesor
public class Processor {
    private int id;                          // numer procesora
    private int load;                        // aktualne obciążenie procesora
    private ArrayList<Task> tasks;           // lista zadań przypisanych do procesora

    public Processor(int id) {
        this.id = id;
        this.load = 0;
        this.tasks = new ArrayList<Task>();
    }

    // Ustawia aktualne obciążenie procesora
    public void setLoad(int load) {
        this.load = load;
    }

    // Pobiera aktualne obciążenie procesora
    public int getLoad() {
        return load;
    }

    // Pobiera listę zadań przypisanych do procesora
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // Pobiera numer procesora
    public int getId() {
        return id;
    }

    // Ustawia numer procesora
    public void setId(int id) {
        this.id = id;
    }

    // Ustawia całą listę zadań
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Dodaje zadanie do procesora
    public void addTask(Task task) {
        tasks.add(task);
    }
}
