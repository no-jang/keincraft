package client.ecs;

import client.ecs.component.Component;
import client.ecs.component.ComponentType;
import client.util.Bits;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private final Map<ComponentType, Component> components;
    private final Bits componentBits;

    public Entity() {
        this.components = new HashMap<>();

        this.componentBits = new Bits();
    }

    public void add(Component component) {
        ComponentType type = ComponentType.getFor(component);
        Component oldComponent = get(type);

        if (component == oldComponent) {
            return;
        }

        if (oldComponent != null) {
            remove(oldComponent);
        }

        components.put(type, component);
        componentBits.set(type.getIndex());
    }

    public void remove(Component component) {
        ComponentType type = ComponentType.getFor(component);
        Component removeComponent = get(type);

        if (removeComponent != null) {
            components.remove(type);
            componentBits.clear(type.getIndex());
        }
    }

    public void clear() {
        components.clear();
        componentBits.clear();
    }

    public <T extends Component> T get(Class<T> type) {
        return get(ComponentType.getFor(type));
    }

    public <T extends Component> T get(ComponentType type) {
        return (T) components.get(type);
    }

    public Bits getComponentBits() {
        return componentBits;
    }

    public Map<ComponentType, Component> getComponents() {
        return components;
    }
}
