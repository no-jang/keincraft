package test;

import engine.core.Engine;
import engine.core.entity.Entity;

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

    public static class Builder extends Entity.Builder<Builder, Device> {
        private final Supplier<Instance> instanceSupplier;

        public Builder(Engine engine) {
            super(engine);

            instanceSupplier = getEntity(Instance.class);
        }

        public Builder queues(String... queues) {

        }

        @Override
        protected Device build() {
            Instance instance = instanceSupplier.get();
            withAscending(instance);

            Device device = new Device(instance);
            return device;
        }
    }
}
