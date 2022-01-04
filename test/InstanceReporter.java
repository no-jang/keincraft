package test;

import engine.core.Engine;
import engine.core.entity.Entity;

public class InstanceReporter extends Entity {
    public InstanceReporter(Engine engine) {
        super(engine);
    }

    public static Builder builder(Engine engine) {
        return new Builder(engine);
    }

    public static class Builder extends Entity.Builder<Builder, InstanceReporter> {
        public Builder(Engine engine) {
            super(engine);
        }

        public Builder severities(String... severities) {

        }

        @Override
        protected InstanceReporter build() {
            return null;
        }
    }
}
