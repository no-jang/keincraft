package engine.util.enums;

import engine.collections.ImmutableCollection;

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

    static <T extends Enum<T> & Maskable> int toBit(T enumValue) {
        return enumValue.getBit();
    }

    static <T extends Enum<T> & Maskable> EnumSet<T> fromBitMask(int bitmask, Class<T> enumType) {
        EnumSet<T> entries = EnumSet.noneOf(enumType);

        for (T entry : enumType.getEnumConstants()) {
            if ((entry.getBit() & bitmask) != 0) {
                entries.add(entry);
            }
        }

        return entries;
    }

    @SafeVarargs
    static <T extends Enum<T> & Maskable> int toBitMask(T... enumValues) {
        int bitMask = 0;

        for (T entry : enumValues) {
            bitMask = bitMask | entry.getBit();
        }

        return bitMask;
    }

    static <T extends Enum<T> & Maskable> int toBitMask(Collection<T> enumCollection) {
        int bitMask = 0;

        for (T entry : enumCollection) {
            bitMask = bitMask | entry.getBit();
        }

        return bitMask;
    }

    static <T extends Enum<T> & Maskable> int toBitMask(ImmutableCollection<T> enumCollection) {
        return toBitMask(enumCollection.toMutable());
    }

    static <T extends Enum<T> & Maskable> int toBitMask(Stream<T> enumStream) {
        return Optional.of(enumStream)
                .orElseGet(Stream::empty)
                .map(Maskable.class::cast)
                .mapToInt(Maskable::getBit)
                .reduce((a, b) -> a | b)
                .orElse(0);
    }

    int getBit();
}
