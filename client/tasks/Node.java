package client.tasks;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Task task;
    private final List<Task> successors;

    public Node(Task task) {
        this.task = task;
        this.successors = new ArrayList<>();
    }

    public void addSuccessor(Task task) {
        successors.add(task);
    }

    public Task getTask() {
        return task;
    }

    public List<Task> getSuccessors() {
        return successors;
    }
}
