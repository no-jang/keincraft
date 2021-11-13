package common.util.math;

public final class Math {
    public static int clamp(int value, int min, int max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, value));
    }
}
