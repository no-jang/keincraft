package test;

import engine.core.event.Event;

public class TestEvent extends Event {
    private final String value;

    public TestEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
