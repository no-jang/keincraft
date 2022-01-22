package main;

import engine.core.logger.Logger;

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
        Logger.trace(Logger.INJECT, "Test");
        Logger.debug("Test");
        Logger.info(Logger.INJECT, "Test");
        Logger.warn("Test");
        Logger.error(Logger.INJECT, "Test");
        // TODO Write client
    }

    public static void runServer() {
        System.out.println("Run server");
        // TODO Write server
    }
}