import java.util.Comparator;

// Komparator do sortowania zadań malejąco według obciążenia
public class TaskLoadDescendingComparator implements Comparator<Task> {
    @Override
    public int compare(Task t1, Task t2) {
        return Integer.compare(t2.getLoad(), t1.getLoad());
    }
}
