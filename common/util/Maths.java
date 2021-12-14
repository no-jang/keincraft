package common.util;

public final class Maths {

    /**
     * Returns value clamped to the inclusive range of min and max.
     *
     * @param value value to be clamped
     * @param min   lower bound of the result
     * @param max   upper bound of the result
     * @return clamped value
     */
    public static int clamp(int value, int min, int max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, value));
    }
}
