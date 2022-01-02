package engine.ecs.entity;

import engine.ecs.component.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Entity {
    private final List<Component> components;

    public Entity() {
        this.components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void addComponents(Collection<Component> components) {
        this.components.addAll(components);
    }

    public List<Component> getComponents() {
        return components;
    }
}
