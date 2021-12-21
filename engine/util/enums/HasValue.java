package engine.util.enums;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface HasValue<T> {
    static <T, E extends Enum<E> & HasValue<T>> T getValue(E enumValue) {
        return enumValue.getValue();
    }

    @Nullable
    static <T, E extends Enum<E> & HasValue<T>> E getByValue(T value, Class<E> enumType) {
        E[] constants = enumType.getEnumConstants();
        if (constants == null) {
            throw new IllegalStateException("Should not be reached: class is not an enum");
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
