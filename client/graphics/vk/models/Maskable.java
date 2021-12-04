package client.graphics.vk.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Maskable {
    static <T extends Enum<T> & Maskable> T fromBit(int bit, Class<T> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> enumValue.getBit() == bit)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unrecognized bit value: " + bit));
    }

    static <T extends Enum<T> & Maskable> int toBit(final T enumValue) {
        return Optional.ofNullable(enumValue)
                .map(Maskable.class::cast)
                .map(Maskable::getBit)
                .orElse(0);
    }

    static <T extends Enum<T> & Maskable> Set<T> fromBitMask(final int bitmask, final Class<T> enumType) {
        return Arrays.stream(enumType.getEnumConstants())
                .filter(enumValue -> (enumValue.getBit() & bitmask) != 0)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(enumType)));
    }

    @SafeVarargs
    static <T extends Enum<T> & Maskable> int toBitMask(final T... enumValues) {
        final Stream<T> enumStream = Optional.ofNullable(enumValues).stream()
                .flatMap(Arrays::stream);

        return toBitMask(enumStream);
    }

    static <T extends Enum<T> & Maskable> int toBitMask(final Collection<T> enumCollection) {
        final Stream<T> enumStream = Optional.ofNullable(enumCollection)
                .orElseGet(Collections::emptySet)
                .stream();

        return toBitMask(enumStream);
    }

    static <T extends Enum<T> & Maskable> int toBitMask(final Stream<T> enumStream) {
        return Optional.ofNullable(enumStream)
                .orElseGet(Stream::empty)
                .map(Maskable.class::cast)
                .mapToInt(Maskable::getBit)
                .reduce((a, b) -> a | b)
                .orElse(0);
    }

    int getBit();
}
