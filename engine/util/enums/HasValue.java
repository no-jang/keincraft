package engine.util.enums;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface HasValue<T> {
    @Nullable
    static <T, E extends Enum<E> & HasValue<T>> E getByValue(T value, Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();
        if (constants == null) {
            throw new IllegalArgumentException("Should not be reached: class is not an enum");
        }

        for (E entry : constants) {
            if (entry.getValue().equals(value)) {
                return entry;
            }
        }

        return null;
    }

    T getValue();
}
