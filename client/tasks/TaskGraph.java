package client.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskGraph {
    private final List<Task> tasks;

    public TaskGraph() {
        tasks = new ArrayList<>();
    }

    public void task(Task task) {
        tasks.add(task);
    }

    public void link(Task from, Task to) {

    }

    public List<Task> getTasks() {
        return tasks;
    }
}
