package main;

import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        boolean isServer = false;

        for (String arg : args) {
            if (Objects.equals(arg, "--server")) {
                isServer = true;
                break;
            }
        }

        if (isServer) {
            runServer();
        } else {
            runClient();
        }
    }

    public static void runClient() {
        System.out.println("Run client");
        System.out.println(new File("").getAbsolutePath());
        // TODO Write client
    }

    public static void runServer() {
        System.out.println("Run server");
        // TODO Write server
    }
}