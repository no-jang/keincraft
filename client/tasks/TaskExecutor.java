package client.tasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskExecutor {
    private final TaskThread[] threads;
    private final TaskQueue queue;

    private final Lock lock;
    private final Condition finishCondition;
    private final Condition queueCondition;

    public TaskExecutor(TaskQueue queue, int poolSize) {
        this.queue = queue;

        threads = new TaskThread[poolSize];
        for(int i = 0; i < poolSize; i++) {
            threads[i] = new TaskThread();
        }

        lock = new ReentrantLock();
        finishCondition = lock.newCondition();
        queueCondition = lock.newCondition();
    }

    public TaskExecutor(TaskQueue queue) {
        this(queue, Runtime.getRuntime().availableProcessors() - 2);
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

        lock.lock();
        queueCondition.signalAll();
        lock.unlock();
    }

    public void execute() {
        if(!queue.getQueue().isEmpty()) {
            throw new IllegalStateException("Queue is not empty");
        }

        queue.resolve();

        lock.lock();
        queueCondition.signalAll();
        lock.unlock();

        while (true) {
            lock.lock();
            finishCondition.awaitUninterruptibly();
            lock.unlock();

            boolean finished = true;
            for(TaskThread thread : threads) {
                if (thread.isExecuting()) {
                    finished = false;
                    break;
                }
            }

            if(finished) {
                return;
            }
        }
    }

    private class TaskThread extends Thread {
        private Node node;
        private boolean running;

        @Override
        public void run() {
            running = true;
            while (running) {
                BlockingQueue<Node> queue = TaskExecutor.this.queue.getQueue();
                node = queue.poll();

                if(node == null) {
                    lock.lock();
                    finishCondition.signalAll();
                    queueCondition.awaitUninterruptibly();
                    lock.unlock();
                    continue;
                }

                if(!node.allPredecessorsFinished()) {
                    queue.add(node);
                    continue;
                }

                node.getTask().run();
                node.setFinished(true);
            }
        }

        public void exit() {
            running = false;
        }

        public boolean isExecuting() {
            return node != null;
        }
    }
}
