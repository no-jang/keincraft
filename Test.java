import engine.core.Engine;
import engine.core.entity.EntityRegistry;
import test.Device;
import test.Instance;
import test.InstanceReporter;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Engine engine = new Engine();
        EntityRegistry entityRegistry = engine.getEntityRegistry();

        InstanceReporter.Builder instanceReporterBuilder = entityRegistry.createEntity(InstanceReporter.builder(engine))
                .severities("test severitiy");

        Instance.Builder instanceBuilder = entityRegistry.createEntity(Instance.builder(engine))
                .applicationName("test app")
                .engineName("test engine")
                .with(instanceReporterBuilder, builder -> builder
                        .severities("test severity"));

        Instance instance1 = instanceBuilder.make();

        Instance instance2 = instanceBuilder.make();

        Device device1 = entityRegistry.createEntity(Device.builder(engine))
                .queues("bli bla blup")
                // .with(instance1) Pick first found
                .make();

        Device device2 = entityRegistry.createEntity(Device.builder(engine))
                .queues("bluap")
                .with(instance2)
                .make();

/*        Engine engine = new Engine();
        EventRegistry eventRegistry = engine.getEventRegistry();

        EventHandler<TestEvent> handler = eventRegistry.addHandler(TestEvent.class, testEvent -> {
            System.out.println("1 " + testEvent.getValue());
        });

        eventRegistry.addHandler(TestEvent.class, testEvent -> {
            System.out.println("2 " + testEvent.getValue());
        });

        eventRegistry.queueEvent("test", new TestEvent("2"));
        eventRegistry.queueEvent("test", new TestEvent("1"));*/

        //eventRegistry.removeHandler(TestEvent.class, handler);

/*        eventRegistry.fireQueue("test");
        eventRegistry.fireQueue("test");*/

        /*ComponentRegistry registry = new ComponentRegistry();

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
                        .severities(MessageSeverity.VERBOSE, MessageSeverity.INFO, MessageSeverity.WARNING, MessageSeverity.PERFORMANCE_WARNING, MessageSeverity.ERROR)));*/
    }
}
