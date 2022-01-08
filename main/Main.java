package main;

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
        // TODO Write client
    }

    public static void runServer() {
        // TODO Write server
    }
}