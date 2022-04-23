package kengine.example;

import kengine.example.api.ExampleApi;
import org.tinylog.Logger;

public class ExampleImpl extends ExampleApi {
    @Override
    public void example() {
        Logger.info("Hello world!");
    }
}
