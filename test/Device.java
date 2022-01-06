package test;

import engine.core.Engine;
import engine.core.entity.Entity;
import engine.core.entity.EntityRegistry;

import java.util.function.Supplier;

public class Device extends Entity {
    private final Instance instance;

    public Device(Engine engine, Instance instance) {
        super(engine);
        this.instance = instance;
    }

    public static Builder builder(Engine engine) {
        return new Builder(engine);
    }

    public static class Builder extends Entity.Builder<Device> {
        private final Engine engine;
        private final Supplier<Instance> instanceSupplier;

        public Builder(Engine engine) {
            this.engine = engine;

            EntityRegistry entityRegistry = engine.getEntityRegistry();
            instanceSupplier = entityRegistry.getEntityProvider(Instance.class);
        }

        public Builder queues(String... queues) {

        }

        @Override
        public Device build() {
            Instance instance = instanceSupplier.get();
            Device device = new Device(engine, instance);
            device.withAscending(instance);
            return device;
        }
    }
}
