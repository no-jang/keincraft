import client.tasks.Task;
import client.tasks.TaskExecutor;
import client.tasks.TaskGraph;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        TaskGraph graph = new TaskGraph();
        Task task1 = () -> {
            for(int i = 0; i < 10; i++) {
                System.out.println("1 " + i);
            }
        };

        Task task2 = () -> {
            for(int i = 0; i < 10; i++) {
                System.out.println("2 " + i);
            }
        };

        Task task3 = () -> {
            for(int i = 0; i < 10; i++) {
                System.out.println("3 " + i);
            }
        };

        Task task4 = () -> {
            for(int i = 0; i < 10; i++) {
                System.out.println("4 " + i);
            }
        };

        Task task5 = () -> {
            for(int i = 0; i < 10; i++) {
                System.out.println("5 " + i);
            }
        };

        graph.task(task1);
        graph.task(task2);
        graph.task(task3);
        graph.task(task4);
        graph.task(task5);

        graph.name(task1, "1");
        graph.name(task2, "2");
        graph.name(task3, "3");
        graph.name(task4, "4");
        graph.name(task5, "5");

        // 5, 2, 3, 4
        graph.link(task5, task2);
        graph.link(task5, task3);
        graph.link(task2, task4);

        TaskExecutor executor = new TaskExecutor(graph);
        executor.executeAndWait();
        executor.stop();
    }
}
