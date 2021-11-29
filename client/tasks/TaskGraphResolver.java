package client.tasks;

import java.util.ArrayDeque;
import java.util.Deque;

public class TaskGraphResolver {
    private final TaskGraph graph;

    public TaskGraphResolver(TaskGraph graph) {
        this.graph = graph;
    }

    public Deque<Node> resolve() {
        Deque<Node> queue = new ArrayDeque<>(graph.getStandaloneNodes().size());

        for (Node node : graph.getStandaloneNodes()) {
            addNode(node, queue);
        }

        return queue;
    }

    private boolean addNode(Node node, Deque<Node> queue) {
        boolean allPredecessors = true;
        for (Node predecessor : node.getPredecessors()) {
            if (!addNode(predecessor, queue)) {
                allPredecessors = false;
                break;
            }
        }

        if (!allPredecessors) {
            return false;
        }

        if (!node.isCondition()) {
            return false;
        }

        queue.addLast(node);
        return true;
    }
}
