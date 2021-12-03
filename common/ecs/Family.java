package common.ecs;

import common.ecs.component.Component;
import common.ecs.component.ComponentType;
import common.util.Bits;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Family {
    private static final Map<Integer, Family> families = new HashMap<>();
    private static final Builder builder = new Builder();
    private static final Bits zeroBits = new Bits();

    private final Bits all;
    private final Bits one;
    private final Bits exclude;
    private final int hash;

    private Family(int hash, Bits all, Bits one, Bits exclude) {
        this.all = all;
        this.one = one;
        this.exclude = exclude;
        this.hash = hash;
    }

    @SafeVarargs
    public static Builder all(Class<? extends Component>... componentTypes) {
        return builder.reset().all(componentTypes);
    }

    @SafeVarargs
    public static Builder one(Class<? extends Component>... componentTypes) {
        return builder.reset().one(componentTypes);
    }

    @SafeVarargs
    public static Builder exclude(Class<? extends Component>... componentTypes) {
        return builder.reset().exclude(componentTypes);
    }

    private static int getFamilyHash(Bits all, Bits one, Bits exclude) {
        return Objects.hash(all, one, exclude);
    }

    public boolean matches(Entity entity) {
        Bits componentBits = entity.getComponentBits();

        if (!componentBits.containsAll(all)) {
            return false;
        }

        if (!one.isEmpty() && !one.intersects(componentBits)) {
            return false;
        }

        return exclude.isEmpty() || !exclude.intersects(componentBits);
    }

    public int getHash() {
        return hash;
    }

    public static class Builder {
        private Bits all;
        private Bits one;
        private Bits exclude;

        private Builder() {
            all = zeroBits;
            one = zeroBits;
            exclude = zeroBits;
        }

        public Builder reset() {
            all = zeroBits;
            one = zeroBits;
            exclude = zeroBits;
            return this;
        }

        @SafeVarargs
        public final Builder all(Class<? extends Component>... componentTypes) {
            all = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        @SafeVarargs
        public final Builder one(Class<? extends Component>... componentTypes) {
            one = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        @SafeVarargs
        public final Builder exclude(Class<? extends Component>... componentTypes) {
            exclude = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        public Family build() {
            int hash = getFamilyHash(all, one, exclude);
            Family family = families.get(hash);

            if (family == null) {
                family = new Family(hash, all, one, exclude);
                families.put(hash, family);
            }

            return family;
        }
    }
}
