package client.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGraph {
    private final List<Task> tasks;
    private final List<Task> standaloneTasks;
    private final Map<Task, String> names;
    private final Map<Task, List<Task>> successors;

    public TaskGraph() {
        tasks = new ArrayList<>();
        standaloneTasks = new ArrayList<>();
        names = new HashMap<>();
        successors = new HashMap<>();
    }

    public void task(Task task) {
        tasks.add(task);
        standaloneTasks.add(task);
    }

    public void name(Task task, String name) {
        names.put(task, name);
    }

    public void link(Task from, Task to) {
        standaloneTasks.remove(to);

        List<Task> successors = this.successors.computeIfAbsent(from, k -> new ArrayList<>());

        successors.add(to);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getStandaloneTasks() {
        return standaloneTasks;
    }

    public Map<Task, String> getNames() {
        return names;
    }

    public Map<Task, List<Task>> getSuccessors() {
        return successors;
    }
}
