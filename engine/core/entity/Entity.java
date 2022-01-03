package engine.core.entity;

import engine.core.Engine;
import engine.core.component.Component;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected final Engine engine;
    protected final List<Component> components;

    public Entity(Engine engine) {
        this.engine = engine;

        components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }
}
