package client.tasks;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO Use wait and notify
public class TaskExecutor {
    private final ExecutorService executor;

    public TaskExecutor(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);
    }

    public TaskExecutor() {
        this(Runtime.getRuntime().availableProcessors() - 2);
    }

    public void execute(Deque<Node> nodes) {
        List<Callable<Object>> tasks = new ArrayList<>(nodes.size());
        for(Node node : nodes) {
            tasks.add(Executors.callable(node.getTask()));
        }

        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
