package kengine.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ExampleImplTest {
    @Test
    public void testExample() throws IOException {
        ExampleImpl example = new ExampleImpl();
        example.example();
    }
}
