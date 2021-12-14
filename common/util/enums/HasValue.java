package common.util.enums;

public interface HasValue<T> {
    static <T, E extends Enum<E> & HasValue<T>> T getValue(E enumValue) {
        return enumValue.getValue();
    }

    static <T, E extends Enum<E> & HasValue<T>> E getByValue(T value, Class<E> enumType) {
        for (E entry : enumType.getEnumConstants()) {
            if (entry.getValue() != null && entry.getValue().equals(value)) {
                return entry;
            }
        }

        return null;
    }

    T getValue();
}
