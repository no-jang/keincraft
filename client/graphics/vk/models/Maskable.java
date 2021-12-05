package client.graphics.vk.models;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

public interface Maskable {
    static <T extends Enum<T> & Maskable> T fromBit(int bit, Class<T> enumType) {
        for (T entry : enumType.getEnumConstants()) {
            if (entry.getBit() == bit) {
                return entry;
            }
        }

        throw new IllegalArgumentException("Unrecognized bit value: " + bit);
    }

    static <T extends Enum<T> & Maskable> int toBit(final T enumValue) {
        if (enumValue == null) {
            return 0;
        }

        return enumValue.getBit();
    }

    static <T extends Enum<T> & Maskable> EnumSet<T> fromBitMask(final int bitmask, final Class<T> enumType) {
        EnumSet<T> entries = EnumSet.noneOf(enumType);

        for (T entry : enumType.getEnumConstants()) {
            if ((entry.getBit() & bitmask) != 0) {
                entries.add(entry);
            }
        }

        return entries;
    }

    @SafeVarargs
    static <T extends Enum<T> & Maskable> int toBitMask(final T... enumValues) {
        int bitMask = 0;

        for (T entry : enumValues) {
            bitMask = bitMask | entry.getBit();
        }

        return bitMask;
    }

    static <T extends Enum<T> & Maskable> int toBitMask(final Collection<T> enumCollection) {
        int bitMask = 0;

        for (T entry : enumCollection) {
            bitMask = bitMask | entry.getBit();
        }

        return bitMask;
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
