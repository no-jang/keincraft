package engine.core.event;

import engine.core.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRegistry {
    private final Engine engine;
    private final Map<Class<?>, List<EventHandler<?>>> handlers;
    private final Map<String, List<Event>> queuedEvents;

    public EventRegistry(Engine engine) {
        this.engine = engine;

        handlers = new HashMap<>();
        queuedEvents = new HashMap<>();
    }

    public <E extends Event> EventHandler<E> addHandler(Class<E> eventClass, EventHandler<E> handler) {
        List<EventHandler<?>> handlers = this.handlers.computeIfAbsent(eventClass, k -> new ArrayList<>());
        handlers.add(handler);
        return handler;
    }

    public <E extends Event> void removeHandler(Class<E> eventClass, EventHandler<E> handler) {
        List<EventHandler<?>> handlers = this.handlers.get(eventClass);
        if (handlers == null) {
            return;
        }

        handlers.remove(handler);
    }

    public <E extends Event> void queueEvent(String key, E event) {
        List<Event> events = queuedEvents.computeIfAbsent(key, k -> new ArrayList<>());
        events.add(event);
    }

    public void fireQueue(String key) {
        List<Event> queue = queuedEvents.get(key);
        if (queue == null) {
            return;
        }

        for (Event event : queue) {
            fireEvent(event);
        }

        queuedEvents.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void fireEvent(E event) {
        List<EventHandler<?>> handlers = this.handlers.get(event.getClass());
        if (handlers == null) {
            return;
        }

        for (EventHandler<?> handler : handlers) {
            ((EventHandler<E>) handler).handle(event);
        }
    }
}
