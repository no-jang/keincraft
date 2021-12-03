package common.ecs.component;

import common.util.Bits;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class which remembers all types of components and give them a unique identifier
 */
public final class ComponentType {
    private static final Map<Class<? extends Component>, ComponentType> componentTypes = new HashMap<>();
    private static int typeIndex;

    private final int index;

    private ComponentType() {
        index = typeIndex++;
    }

    /**
     * Gets the ComponentType for the corresponding component
     *
     * @param component component to get the type for
     * @return component type
     * @see Component
     */
    public static ComponentType getFor(Component component) {
        return getFor(component.getClass());
    }

    /**
     * Gets the ComponentType from the class of the component
     *
     * @param componentType component class
     * @return ComponentType
     * @see Component
     */
    public static ComponentType getFor(Class<? extends Component> componentType) {
        ComponentType type = componentTypes.get(componentType);

        if (type == null) {
            type = new ComponentType();
            componentTypes.put(componentType, type);
        }

        return type;
    }

    /**
     * Gets the unique identifier for a component
     *
     * @param componentType type of the component
     * @return unique identifier of the component
     */
    public static int getIndexFor(Class<? extends Component> componentType) {
        return getFor(componentType).getIndex();
    }

    /**
     * Gets the Bits for the identifier of a component
     *
     * @param componentTypes type of the component
     * @return identifier as Bits of the component
     */
    @SafeVarargs
    public static Bits getBitsFor(Class<? extends Component>... componentTypes) {
        Bits bits = new Bits();

        for (Class<? extends Component> componentType : componentTypes) {
            bits.set(getIndexFor(componentType));
        }

        return bits;
    }

    /**
     * Gets unique identifier for the component type
     *
     * @return unique identifier
     */
    public int getIndex() {
        return index;
    }
}
