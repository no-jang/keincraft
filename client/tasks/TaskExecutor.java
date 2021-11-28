package client.tasks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TaskExecutor {
    private final TaskGraph graph;
    private final List<TaskThread> threads;
    private final Queue<Task> queue;

    public TaskExecutor(TaskGraph graph, int threadCount) {
        this.graph = graph;
        this.queue = new ArrayDeque<>(graph.getTasks().size());

        this.threads = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            threads.add(new TaskThread(i, this));
        }
    }

    public TaskExecutor(TaskGraph graph) {
        this(graph, Runtime.getRuntime().availableProcessors() - 2);
    }

    public void execute() {
        queue.addAll(graph.getTasks());

        for (TaskThread thread : threads) {
            thread.start();
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
                if (thread.getTask() != null) {
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

    Task popTask() {
        synchronized (queue) {
            return queue.poll();
        }
    }
}
