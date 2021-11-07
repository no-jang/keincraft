package client.render.util;

import java.util.List;

public final class ArrayUtil {
    public static float[] toPrimitive(List<Float> list) {
        float[] array = new float[list.size()];
        for(int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
