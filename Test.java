import common.tasks.Task;
import common.tasks.TaskExecutor;
import common.tasks.TaskGraph;
import common.tasks.TaskQueue;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        TaskGraph graph = new TaskGraph();
        Task task1 = () -> {
            for(int i = 0; i < 1; i++) {
                System.out.println("1 " + i);
            }
        };

        Task task2 = () -> {
            for(int i = 0; i < 1; i++) {
                System.out.println("2 " + i);
            }
        };

        Task task3 = () -> {
            for(int i = 0; i < 1; i++) {
                System.out.println("3 " + i);
            }
        };

        Task task4 = () -> {
            for(int i = 0; i < 1; i++) {
                System.out.println("4 " + i);
            }
        };

        Task task5 = () -> {
            for(int i = 0; i < 1; i++) {
                System.out.println("5 " + i);
            }
        };

        graph.task("1", task1);
        graph.task("2", task2);
        graph.task("3", task3);
        graph.task("4", task4);
        graph.task("5", task5);

        // 4, 2, 3, 5
        graph.link(task5, task2);
        graph.link(task5, task3);
        graph.link(task2, task4);

        graph.condition(task1, () -> true);
        graph.condition(task4, () -> true);

        TaskQueue queue = new TaskQueue(graph);
        TaskExecutor executor = new TaskExecutor(queue);
        executor.start();
        executor.execute();
        executor.stop();
    }
}
