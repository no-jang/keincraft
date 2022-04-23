package kengine.example.api;

import org.tinylog.Logger;

public abstract class ExampleApi {
    public abstract void example();

    public void test() {
        Logger.info("test");
    }
}
