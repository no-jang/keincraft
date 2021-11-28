package client.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskGraph {
    private final Map<Task, Node> nodes;
    private final List<Node> standaloneNodes;

    public TaskGraph() {
        nodes = new HashMap<>();
        standaloneNodes = new ArrayList<>();
    }

    public void task(String name, Task task) {
        Node node = new Node(name, task);
        nodes.put(task, node);
        standaloneNodes.add(node);
    }

    public void link(Task task, Task predecessor) {
        Node predecessorNode = nodes.get(predecessor);
        standaloneNodes.remove(predecessorNode);
        nodes.get(task).getPredecessors().add(predecessorNode);
    }

    public Map<Task, Node> getNodes() {
        return nodes;
    }

    public List<Node> getStandaloneNodes() {
        return standaloneNodes;
    }
}
