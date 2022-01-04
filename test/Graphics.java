package test;

import engine.core.Engine;
import engine.core.entity.Entity;
import engine.core.entity.EntityRegistry;

public class Graphics extends Entity {
    private final Instance instance;
    private final Device device;

    public Graphics(Engine engine) {
        super(engine);

        EntityRegistry entityRegistry = engine.getEntityRegistry();

        InstanceReporter.Builder instanceReporterBuilder = entityRegistry.createEntity(InstanceReporter.builder(engine))
                .severities("test severitiy");

        instance = entityRegistry.createEntity(Instance.builder(engine))
                .applicationName("test app")
                .engineName("test engine")
                .with(instanceReporterBuilder, builder -> builder
                        .severities("test severity"))
                .make();


        Device device1 = entityRegistry.createEntity(Device.builder(engine))
                .queues("bli bla blup")
                .make();
    }
}
