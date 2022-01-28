package engine.util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;

public final class Conditions {
    public static void argumentGreaterThan(int value, int greaterThan, String message) {
        if (value < greaterThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void argumentLessThan(int value, int lessThan, String message) {
        if (value > lessThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void argumentGreaterEqualThan(int value, int greaterThan, String message) {
        if (value <= greaterThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void argumentLessEqualThan(int value, int lessThan, String message) {
        if (value >= lessThan) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void argumentNotNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void argumentNotNull(@Nullable Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void elementNotNull(@Nullable Object value, String message) {
        if (value == null) {
            throw new NoSuchElementException(message);
        }
    }
}
