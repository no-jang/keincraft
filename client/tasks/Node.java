package client.tasks;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String name;
    private final Task task;
    private final List<Node> successors;

    private boolean finished;

    public Node(String name, Task task) {
        this.name = name;
        this.task = task;
        this.successors = new ArrayList<>();

        this.finished = false;
    }

    public boolean allSuccessorsFinished() {
        boolean finished = true;
        for(Node node : successors) {
            if(!node.finished) {
                finished = false;
                break;
            }
        }

        return finished;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getName() {
        return name;
    }

    public Task getTask() {
        return task;
    }

    public List<Node> getSuccessors() {
        return successors;
    }

    void finish() {
        this.finished = true;
    }
}
