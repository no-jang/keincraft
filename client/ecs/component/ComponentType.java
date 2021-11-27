package client.ecs.component;

import client.util.Bits;

import java.util.HashMap;
import java.util.Map;

public final class ComponentType {
    private static final Map<Class<? extends Component>, ComponentType> componentTypes = new HashMap<>();
    private static int typeIndex;

    private final int index;

    public ComponentType() {
        index = typeIndex++;
    }

    public static ComponentType getFor(Component component) {
        return getFor(component.getClass());
    }

    public static ComponentType getFor(Class<? extends Component> componentType) {
        ComponentType type = componentTypes.get(componentType);

        if (type == null) {
            type = new ComponentType();
            componentTypes.put(componentType, type);
        }

        return type;
    }

    public static int getIndexFor(Class<? extends Component> componentType) {
        return getFor(componentType).getIndex();
    }

    @SafeVarargs
    public static Bits getBitsFor(Class<? extends Component>... componentTypes) {
        Bits bits = new Bits();

        for (Class<? extends Component> componentType : componentTypes) {
            bits.set(getIndexFor(componentType));
        }

        return bits;
    }

    public int getIndex() {
        return index;
    }
}
