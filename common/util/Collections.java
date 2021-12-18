package common.util;

import java.util.List;

public final class Collections {
    public static <T> void checkRequiredOptional(List<T> available, List<T> required, List<T> optional, List<T> enabled) {
        // Check every available if it should be added
        for (T t : available) {
            // If is required add to enabled and remove from required layer
            int requiredIndex = required.indexOf(t);
            if (requiredIndex != -1) {
                required.remove(requiredIndex);
                enabled.add(t);
                continue;
            }

            // If is optional add to enabled and remove from optional layer
            int optionalIndex = optional.indexOf(t);
            if (optionalIndex != -1) {
                optional.remove(optionalIndex);
                enabled.add(t);
            }
        }
    }
}
