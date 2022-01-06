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

    public static class Builder extends Entity.Builder<Instance> {
        private final Engine engine;

        public Builder(Engine engine) {
            this.engine = engine;
        }

        public Builder applicationName(String name) {

        }

        public Builder engineName(String name) {

        }

        public Builder severities(String... strings) {

        }

        @Override
        public Instance build() {
            return null;
        }
    }
}
