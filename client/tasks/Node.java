package client.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Node {
    private final String name;
    private final Task task;

    private Supplier<Boolean> condition;
    private List<Node> predecessors;

    private boolean finished;

    public Node(String name, Task task) {
        this.name = name;
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public Task getTask() {
        return task;
    }

    public List<Node> getPredecessors() {
        if (predecessors == null) predecessors = new ArrayList<>();
        return predecessors;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isCondition() {
        if (condition == null) {
            return true;
        }

        return condition.get();
    }

    public void setCondition(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    public boolean allPredecessorsFinished() {
        boolean finished = true;

        for (Node node : predecessors) {
            if (!node.finished) {
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
