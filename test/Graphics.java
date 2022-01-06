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

        instance = entityRegistry.addEntity(Instance.builder(engine)
                .applicationName("test app")
                .engineName("test engine")
                .severities("test severity")
                .build());


        device = entityRegistry.addEntity(Device.builder(engine)
                .queues("bli bla blup")
                .build());
    }

    public Instance getInstance() {
        return instance;
    }

    public Device getDevice() {
        return device;
    }
}
