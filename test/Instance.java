package test;

import engine.core.Engine;
import engine.core.entity.Entity;

public class Instance extends Entity {
    public Instance(Engine engine) {
        super(engine);
    }

    public static Builder builder(Engine engine) {
        return new Builder(engine);
    }

    public static class Builder extends Entity.Builder<Builder, Instance> {
        public Builder(Engine engine) {
            super(engine);
        }

        public Builder applicationName(String name) {

        }

        public Builder engineName(String name) {

        }

        @Override
        protected Instance build() {
            return null;
        }

        @Override
        public int getMaxEntityCount() {
            return 1;
        }
    }
}
