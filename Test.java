import engine.core.Engine;
import engine.core.entity.EntityRegistry;
import engine.graphics.instance.Instance;
import engine.graphics.instance.properties.InstanceExtension;
import engine.graphics.instance.properties.InstanceLayer;
import engine.graphics.instance.properties.Version;
import engine.memory.MemoryContext;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Engine engine = new Engine();
        EntityRegistry entityRegistry = engine.getEntityRegistry();

        entityRegistry.addEntity(new MemoryContext(engine));

        Instance instance = entityRegistry.addEntity(Instance.builder(engine)
                .applicationName("test application")
                .engineName("test engine")
                .applicationVersion(new Version(1, 0, 0))
                .engineVersion(new Version(1, 0, 0))
                .vulkanVersion(new Version(1, 2, 0))
                .extensions(extensions -> extensions
                        .required(InstanceExtension.DEBUG_REPORT))
                .layers(layers -> layers
                        .required(InstanceLayer.KHRONOS_VALIDATION))
                .build());
    }
}
