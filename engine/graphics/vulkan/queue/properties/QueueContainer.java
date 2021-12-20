package engine.graphics.vulkan.queue.properties;

import engine.collections.container.DefaultDoubleContainer;
import engine.collections.container.DoubleContainer;
import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.queue.QueueFamily;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class QueueContainer extends DefaultDoubleContainer<QueueFamily, QueueInfo> {
    public QueueContainer(ImmutableList<QueueFamily> available, ImmutableList<QueueInfo> requested) {
        super(available, requested);
    }

    public static class Builder extends DefaultDoubleContainer.AbstractBuilder<QueueFamily, QueueInfo> {
        public Builder(List<QueueFamily> available) {
            super(available);
        }

        @Override
        public DoubleContainer.Builder<QueueFamily, QueueInfo> required(QueueInfo request) {
            if (requested.stream().anyMatch(queueInfo -> queueInfo.getFamily().getIndex() == request.getFamily().getIndex())) {
                return this;
            }

            return super.required(request);
        }

        @Override
        public DoubleContainer.Builder<QueueFamily, QueueInfo> optional(QueueInfo request) {
            if (requested.stream().anyMatch(queueInfo -> queueInfo.getFamily().getIndex() == request.getFamily().getIndex())) {
                return this;
            }

            return super.optional(request);
        }

        public Builder required(QueueFamily family) {
            required(new QueueInfo(family, List.of(1.0f)));
            return this;
        }

        public Builder required(QueueFamily family, float priority) {
            required(new QueueInfo(family, List.of(priority)));
            return this;
        }

        public Builder required(QueueFamily family, List<Float> priorities) {
            required(new QueueInfo(family, priorities));
            return this;
        }

        public Builder optional(QueueFamily family) {
            optional(new QueueInfo(family, List.of(1.0f)));
            return this;
        }

        public Builder optional(QueueFamily family, float priority) {
            optional(new QueueInfo(family, List.of(priority)));
            return this;
        }

        public Builder optional(QueueFamily family, List<Float> priorities) {
            optional(new QueueInfo(family, priorities));
            return this;
        }

        public QueueFamily required(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            required(family);

            return family;
        }

        public QueueFamily required(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter, float priority) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            required(family, priority);

            return family;
        }

        public QueueFamily required(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter, List<Float> priorities) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            required(family, priorities);

            return family;
        }

        public QueueFamily optional(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            optional(family);

            return family;
        }

        public QueueFamily optional(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter, float priority) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            optional(family, priority);

            return family;
        }

        public QueueFamily optional(Function<Stream<QueueFamily>, Stream<QueueFamily>> filter, List<Float> priorities) {
            Stream<QueueFamily> stream = available.stream();
            stream = filter.apply(stream);
            QueueFamily family = stream
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Failed to find queue family"));

            optional(family, priorities);

            return family;
        }

        @Override
        protected boolean isAvailable(QueueInfo request) {
            return available.contains(request.getFamily());
        }

        @Override
        public QueueContainer build() {
            return new QueueContainer(available, new DefaultImmutableList<>(requested));
        }
    }
}
