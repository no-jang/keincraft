package client.tasks;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

// TODO Use wait and notify
public class TaskExecutor {
    private final TaskThread[] threads;
    private final LinkedBlockingDeque<Node> queue;
    private final ExecutorService executor;

    public TaskExecutor(int poolSize) {
        executor = Executors.newFixedThreadPool(poolSize);

        queue = new LinkedBlockingDeque<>();

        threads = new TaskThread[poolSize];
        for(int i = 0; i < poolSize; i++) {
            threads[i] = new TaskThread(this, i);
        }
    }

    public TaskExecutor() {
        this(Runtime.getRuntime().availableProcessors() - 2);
    }

    public void start() {
        for(TaskThread thread : threads) {
            thread.start();
        }
    }

    public void stop() {
        for(TaskThread thread : threads) {
            thread.exit();
        }
    }

    public void execute(List<Node> nodes) {
        synchronized (queue) {
            queue.addAll(nodes);
            queue.notify();
        }
    }

    public void executeWait(List<Node> nodes) {
        execute(nodes);

        synchronized (queue) {
            while (!queue.isEmpty() ) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for(TaskThread thread : threads) {
                    if(!thread.isFinished()) {
                        continue;
                    }
                }
            }
        }
    }

    Node pollNode() throws InterruptedException {
            synchronized (queue) {
                while (queue.isEmpty()) {
                    queue.wait();
                }

                return queue.poll();
            }
    }
/*
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

    Node pollNode() {
        Node node;
        do {
            synchronized (queueGuard) {
                node = queue.poll();
                if (!node.allPredecessorsFinished()) {
                    queue.addLast(node);
                    node = null;
                }
            }
        } while (node == null);
        return node;
    }*/
}
