package engine.util.enums;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.EnumSet;

public interface Maskable {
    @Nullable
    static <E extends Enum<E> & Maskable> E fromBit(int bit, Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();
        if (constants == null) {
            throw new IllegalArgumentException("Should not be reached: class is not an enum");
        }

        for (E entry : constants) {
            if (entry.getBit() == bit) {
                return entry;
            }
        }

        return null;
    }

    static <E extends Enum<E> & Maskable> EnumSet<E> fromBitMask(int bitmask, Class<E> enumClass) {
        EnumSet<E> entries = EnumSet.noneOf(enumClass);

        E[] constants = enumClass.getEnumConstants();
        if (constants == null) {
            throw new IllegalArgumentException("Should not be reached: class is not an enum");
        }

        for (E entry : constants) {
            if ((entry.getBit() & bitmask) != 0) {
                entries.add(entry);
            }
        }

        return entries;
    }

    @SafeVarargs
    static <E extends Enum<E> & Maskable> int toBitMask(E... enumValues) {
        int bitmask = 0;

        for (E entry : enumValues) {
            bitmask = bitmask | entry.getBit();
        }

        return bitmask;
    }

    static <E extends Enum<E> & Maskable> int toBitMask(Collection<E> enumValues) {
        int bitmask = 0;

        for (E entry : enumValues) {
            bitmask = bitmask | entry.getBit();
        }

        return bitmask;
    }

    int getBit();
}
