package client.tasks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TaskExecutor {
    private final TaskGraph graph;
    private final List<TaskThread> threads;
    private final Deque<Node> queue;

    public TaskExecutor(TaskGraph graph, int threadCount) {
        this.graph = graph;

        this.queue = new ArrayDeque<>(graph.getNodes().size());

        this.threads = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            threads.add(new TaskThread(i, this));
        }
    }

    public TaskExecutor(TaskGraph graph) {
        this(graph, Runtime.getRuntime().availableProcessors() - 2);
    }

    public void execute() {
        addFromGraph(graph);

        for (TaskThread thread : threads) {
            thread.start();
        }
    }

    private void addFromGraph(TaskGraph graph) {
        for(Node node : graph.getStandaloneNodes()) {
            addNode(node);
        }
    }

    private void addNode(Node node) {
        for(Node predecessor : node.getPredecessors()) {
            addNode(predecessor);
        }

        queue.addLast(node);
    }

    public void executeAndWait() {
        execute();

        boolean finished;
        do {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            finished = true;
            for (TaskThread thread : threads) {
                if (thread.getNode() != null) {
                    finished = false;
                    break;
                }
            }

        } while (!finished);
    }

    public void stop() {
        for (TaskThread thread : threads) {
            thread.stopLoop();
        }
    }

    Node pollNode() {
        synchronized (queue) {
            Node node;
            do {
                if(queue.size() == 0) {
                    return null;
                }

                node = queue.poll();
                if(!node.allPredecessorsFinished()) {
                    queue.addLast(node);
                    node = null;
                }
            } while (node == null);
            return node;
        }
    }
}
