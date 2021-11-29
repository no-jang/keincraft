package client.tasks;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// TODO Use wait and notify
public class TaskExecutor {
    private final List<TaskThread> threads;
    private final Object queueGuard = new Object();
    private Deque<Node> queue;

    public TaskExecutor(int threadCount) {
        this.threads = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            threads.add(new TaskThread(i, this));
        }
    }

    public TaskExecutor() {
        this(Runtime.getRuntime().availableProcessors() - 2);
    }

    public void execute(Deque<Node> queue) {
        synchronized (queueGuard) {
            if (this.queue != null) {
                throw new IllegalStateException("Task executor already executes a queue");
            }

            this.queue = queue;
        }
    }

    public void executeWait(Deque<Node> queue) {
        execute(queue);

        boolean finished;
        do {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            finished = true;
            for (TaskThread thread : threads) {
                if (!thread.isFinished()) {
                    finished = false;
                }
            }
        } while (!finished);
    }

    public void start() {
        for (TaskThread thread : threads) {
            thread.start();
        }
    }

    public void stop() {
        for (TaskThread thread : threads) {
            thread.stopLoop();
        }
    }

    Node pollNode() {
        Node node;
        do {
            synchronized (queueGuard) {
                if (queue.size() == 0) {
                    queue = null;
                    return null;
                }

                node = queue.poll();
                if (!node.allPredecessorsFinished()) {
                    queue.addLast(node);
                    node = null;
                }
            }
        } while (node == null);
        return node;
    }
}
