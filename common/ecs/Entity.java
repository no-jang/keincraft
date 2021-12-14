package common.ecs;

import common.ecs.component.Component;
import common.ecs.component.ComponentType;
import common.util.Bits;

import java.util.HashMap;
import java.util.Map;

/**
 * An entity is a list of components.
 *
 * @see Component
 */
public class Entity {
    private final Map<ComponentType, Component> components;
    private final Bits componentBits;

    /**
     * Creates new entity with no components
     */
    public Entity() {
        this.components = new HashMap<>();

        this.componentBits = new Bits();
    }

    /**
     * Adds a component and removes the old one if available
     *
     * @param component component to remove
     * @see Component
     */
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

    /**
     * Removes a component and removes the old one if available
     *
     * @param component component to remove
     * @see Component
     */
    public void remove(Component component) {
        ComponentType type = ComponentType.getFor(component);
        Component removeComponent = get(type);

        if (removeComponent != null) {
            components.remove(type);
            componentBits.clear(type.getIndex());
        }
    }

    /**
     * Clears all components
     */
    public void clear() {
        components.clear();
        componentBits.clear();
    }

    /**
     * Gets one component by type
     *
     * @param type type of the component
     * @param <T>  type of the component
     * @return component by type
     */
    public <T extends Component> T get(Class<T> type) {
        return get(ComponentType.getFor(type));
    }

    /**
     * Gets one component by ComponentType
     *
     * @param type component type of component
     * @param <T>  type of component
     * @return component by component type
     */
    public <T extends Component> T get(ComponentType type) {
        return (T) components.get(type);
    }

    /**
     * Returns bitset with bits set as identifier of component identifiers
     *
     * @return bitset
     */
    public Bits getComponentBits() {
        return componentBits;
    }

    /**
     * All components with ComponentType
     *
     * @return all components
     */
    public Map<ComponentType, Component> getComponents() {
        return components;
    }
}
