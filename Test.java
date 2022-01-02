import engine.ecs.engine.Engine;
import engine.ecs.entity.EntityRegistry;
import engine.graphics.instance.Instance;
import engine.graphics.instance.InstanceBuilder;
import engine.graphics.instance.InstanceReporterBuilder;
import engine.graphics.instance.properties.InstanceExtension;
import engine.graphics.instance.properties.InstanceLayer;
import engine.graphics.instance.properties.MessageSeverity;
import engine.graphics.instance.properties.Version;
import engine.memory.MemoryContext;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Engine engine = new Engine();
        EntityRegistry entityRegistry = engine.getEntityRegistry();

        entityRegistry.addEntity(new MemoryContext());

        Instance instance = entityRegistry.createEntity(new InstanceBuilder(
                new Version(1, 0, 0)
        ), builder -> builder
                .applicationName("test application")
                .engineName("test engine")
                .applicationVersion(new Version(1, 0, 0))
                .engineVersion(new Version(1, 0, 0))
                .extensions(extensions -> extensions
                        .required(InstanceExtension.DEBUG_REPORT))
                .layers(layers -> layers
                        .required(InstanceLayer.KHRONOS_VALIDATION))
                .component(new InstanceReporterBuilder(), reporter -> reporter
                        .severities(MessageSeverity.VERBOSE, MessageSeverity.INFO, MessageSeverity.WARNING, MessageSeverity.PERFORMANCE_WARNING, MessageSeverity.ERROR)));
    }
}
