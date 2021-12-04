package client.graphics.vk.models;

import java.util.Arrays;
import java.util.Objects;

public interface HasValue<T> {
    static <T, E extends Enum<E> & HasValue<T>> T getValue(E enumValue) {
        return enumValue.getValue();
    }

    static <T, E extends Enum<E> & HasValue<T>> E getByValue(T value, Class<E> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> Objects.nonNull(enumValue.getValue()))
                .filter(enumValue -> value.equals(enumValue.getValue()))
                .findFirst()
                .orElse(null);
    }

    T getValue();
}
