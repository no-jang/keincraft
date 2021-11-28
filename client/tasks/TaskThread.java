package client.tasks;

public class TaskThread extends Thread {
    private static final ThreadGroup tasksGroup = new ThreadGroup("tasks");

    private final TaskExecutor executor;

    private boolean running;
    private Task task;

    public TaskThread(int index, TaskExecutor executor) {
        super(tasksGroup, "tasks" + index);

        this.executor = executor;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            task = executor.popTask();
            if (task == null) {
                continue;
            }

            task.run();
        }
    }

    public void stopLoop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public Task getTask() {
        return task;
    }
}
