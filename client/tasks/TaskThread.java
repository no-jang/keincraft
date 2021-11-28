package client.tasks;

public class TaskThread extends Thread {
    private static final ThreadGroup tasksGroup = new ThreadGroup("tasks");

    private final TaskExecutor executor;

    private boolean running;
    private Node node;

    public TaskThread(int index, TaskExecutor executor) {
        super(tasksGroup, "tasks" + index);

        this.executor = executor;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            node = executor.popNode();
            if (node == null) {
                continue;
            }

            node.getTask().run();
            node.finish();
        }
    }

    public void stopLoop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public Node getNode() {
        return node;
    }
}
