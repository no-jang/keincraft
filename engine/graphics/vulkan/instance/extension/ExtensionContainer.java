package engine.graphics.vulkan.instance.extension;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.helper.enums.HasValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExtensionContainer<T extends HasValue<String>> {
    private final ImmutableList<T> availableExtensions;
    private final ImmutableList<T> requestedExtensions;

    public ExtensionContainer(List<T> availableExtensions, List<T> requestedExtensions) {
        this.availableExtensions = new DefaultImmutableList<>(availableExtensions);
        this.requestedExtensions = new DefaultImmutableList<>(requestedExtensions);
    }

    public ImmutableList<T> getAvailableExtensions() {
        return availableExtensions;
    }

    public ImmutableList<T> getRequestedExtensions() {
        return requestedExtensions;
    }

    public static class Builder<T extends HasValue<String>> {
        private final List<T> availableExtensions;
        private final List<T> requestedExtensions;

        public Builder(List<T> availableExtensions) {
            this.availableExtensions = availableExtensions;
            this.requestedExtensions = new ArrayList<>();
        }

        public Builder<T> required(T extension) {
            if (!availableExtensions.contains(extension)) {
                throw new IllegalArgumentException("Requested " + extension + " is not available");
            }

            requestedExtensions.add(extension);

            return this;
        }

        public Builder<T> required(Collection<T> extensions) {
            for (T extension : extensions) {
                required(extension);
            }

            return this;
        }

        public Builder<T> optional(T extension) {
            if (!availableExtensions.contains(extension)) {
                return this;
            }

            requestedExtensions.add(extension);

            return this;
        }

        public Builder<T> optional(Collection<T> extensions) {
            for (T extension : extensions) {
                required(extension);
            }

            return this;
        }

        public ExtensionContainer<T> build() {
            return new ExtensionContainer<>(availableExtensions, requestedExtensions);
        }
    }
}
