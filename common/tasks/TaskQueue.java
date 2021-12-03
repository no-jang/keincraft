package common.tasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
    private final TaskGraph graph;
    private final BlockingQueue<Node> queue;

    public TaskQueue(TaskGraph graph) {
        this.graph = graph;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void resolve() {
        queue.clear();

        for (Node node : graph.getStandaloneNodes()) {
            addNode(node);
        }
    }

    private boolean addNode(Node node) {
        boolean allPredecessors = true;
        for (Node predecessor : node.getPredecessors()) {
            if (!addNode(predecessor)) {
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

        node.setFinished(false);
        queue.add(node);
        return true;
    }

    public BlockingQueue<Node> getQueue() {
        return queue;
    }
}
