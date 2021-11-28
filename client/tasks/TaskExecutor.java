package client.tasks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class TaskExecutor {
    private final TaskGraph graph;
    private final List<TaskThread> threads;
    //private final Map<Task, Node> nodes;
    private final Deque<Node> queue;

    public TaskExecutor(TaskGraph graph, int threadCount) {
        this.graph = graph;

        int graphSize = graph.getTasks().size();
        //this.nodes = new HashMap<>();
        this.queue = new ArrayDeque<>(graphSize);

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
        for(Task task : graph.getStandaloneTasks()) {
            addTask(graph, task);
        }
    }

    private void addTask(TaskGraph graph, Task task) {
        String name = graph.getNames().get(task);

        queue.addLast(new Node(name, task));

        List<Task> successors = graph.getSuccessors().get(task);
        if(successors == null) {
            return;
        }

        for(Task successor : successors) {
            addTask(graph, successor);
        }
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

    Node popNode() {
        synchronized (queue) {
            Node node;
            do {
                if(queue.size() == 0) {
                    return null;
                }

                node = queue.pop();
                if(!node.allSuccessorsFinished()) {
                    queue.addLast(node);
                    node = null;
                }
            } while (node == null);
            return node;
        }
    }
}
