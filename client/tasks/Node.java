package client.tasks;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final String name;
    private final Task task;
    private final List<Node> predecessors;

    private boolean finished;

    public Node(String name, Task task) {
        this.name = name;
        this.task = task;
        this.predecessors = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Task getTask() {
        return task;
    }

    public List<Node> getPredecessors() {
        return predecessors;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean allPredecessorsFinished() {
        boolean finished = true;

        for(Node node : predecessors) {
            if(!node.finished) {
                finished = false;
                break;
            }
        }

        return finished;
    }

    void finish() {
        finished = true;
    }
}
