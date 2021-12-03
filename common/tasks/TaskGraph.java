package common.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TaskGraph {
    private final Map<Task, Node> nodes;
    private final List<Node> standaloneNodes;

    public TaskGraph() {
        nodes = new HashMap<>();
        standaloneNodes = new ArrayList<>();
    }

    public void task(String name, Task task) {
        Node node = new Node(name, task);

        synchronized (nodes) {
            nodes.put(task, node);
            standaloneNodes.add(node);
        }
    }

    public void link(Task task, Task predecessor) {
        synchronized (nodes) {
            Node predecessorNode = nodes.get(predecessor);
            standaloneNodes.remove(predecessorNode);
            nodes.get(task).getPredecessors().add(predecessorNode);
        }
    }

    public void condition(Task task, Supplier<Boolean> condition) {
        synchronized (nodes) {
            nodes.get(task).setCondition(condition);
        }
    }

    public synchronized List<Node> getStandaloneNodes() {
        return standaloneNodes;
    }

    public synchronized Map<Task, Node> getNodes() {
        return nodes;
    }
}
