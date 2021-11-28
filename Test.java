import client.tasks.TaskExecutor;
import client.tasks.TaskGraph;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        TaskGraph graph = new TaskGraph();
        graph.task(() -> {
            for(int i = 0; i < 1000000; i++) {
                System.out.println("1 " + i);
            }
        });

        graph.task(() -> {
            for(int i = 0; i < 1000000; i++) {
                System.out.println("2 " + i);
            }
        });

        graph.task(() -> {
            for(int i = 0; i < 1000000; i++) {
                System.out.println("3 " + i);
            }
        });

        graph.task(() -> {
            for(int i = 0; i < 1000000; i++) {
                System.out.println("4 " + i);
            }
        });

        graph.task(() -> {
            for(int i = 0; i < 1000000; i++) {
                System.out.println("5 " + i);
            }
        });

        TaskExecutor executor = new TaskExecutor(graph);
        executor.executeAndWait();
        executor.stop();
    }
}
