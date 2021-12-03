package common.ecs;

import common.ecs.component.Component;
import common.ecs.component.ComponentType;
import common.util.Bits;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A family describes a group of components
 *
 * @see Component
 */
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

    /**
     * Returns family Builder where entity must have all the specified components
     *
     * @param componentTypes component types entity must have
     * @return family builder
     * @see Builder
     */
    @SafeVarargs
    public static Builder all(Class<? extends Component>... componentTypes) {
        return builder.reset().all(componentTypes);
    }

    /**
     * Returns family Builder where entity must have one of the specified components
     *
     * @param componentTypes component types entity must have one of
     * @return family builder
     * @see Builder
     */
    @SafeVarargs
    public static Builder one(Class<? extends Component>... componentTypes) {
        return builder.reset().one(componentTypes);
    }

    /**
     * Returns family Builder where entity must not have one of the specified components
     *
     * @param componentTypes component types entity must not have one of
     * @return family Builder
     * @see Builder
     */
    @SafeVarargs
    public static Builder exclude(Class<? extends Component>... componentTypes) {
        return builder.reset().exclude(componentTypes);
    }

    private static int getFamilyHash(Bits all, Bits one, Bits exclude) {
        return Objects.hash(all, one, exclude);
    }

    /**
     * Checks if entity matches the family
     *
     * @param entity entity to test
     * @return true if entity matched this family
     */
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

    /**
     * Returns family hash from all, one and exclude bitsets
     *
     * Used to have a unique identifier of the family
     *
     * @return family hash
     */
    public int getHash() {
        return hash;
    }

    /**
     * Builder for building Families
     */
    public static class Builder {
        private Bits all;
        private Bits one;
        private Bits exclude;

        private Builder() {
            all = zeroBits;
            one = zeroBits;
            exclude = zeroBits;
        }

        /**
         * Sets all bitsets to zero
         *
         * @return this
         */
        public Builder reset() {
            all = zeroBits;
            one = zeroBits;
            exclude = zeroBits;
            return this;
        }

        /**
         * Sets the components an entity must have to match the family
         *
         * @param componentTypes component types an entity must have
         * @return this
         */
        @SafeVarargs
        public final Builder all(Class<? extends Component>... componentTypes) {
            all = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        /**
         * Sets the components an entity must have one of to match the family
         *
         * @param componentTypes component types an entity must have one of
         * @return this
         */
        @SafeVarargs
        public final Builder one(Class<? extends Component>... componentTypes) {
            one = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        /**
         * Sets the components an entity must not have one of to match the family
         *
         * @param componentTypes component types an entity must not have one of
         * @return this
         */
        @SafeVarargs
        public final Builder exclude(Class<? extends Component>... componentTypes) {
            exclude = ComponentType.getBitsFor(componentTypes);
            return this;
        }

        /**
         * Gets the family with corresponding all, one and exclude bitsets
         *
         * @return family
         */
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
