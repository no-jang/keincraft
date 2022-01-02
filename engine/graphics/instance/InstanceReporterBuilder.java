package engine.graphics.instance;

import engine.ecs.entity.EntityComponentBuilder;
import engine.graphics.instance.properties.MessageSeverity;
import engine.graphics.util.VkFunction;
import engine.memory.MemoryContext;
import engine.util.enums.Maskable;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.EXTDebugReport;
import org.lwjgl.vulkan.VkDebugReportCallbackCreateInfoEXT;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstanceReporterBuilder extends EntityComponentBuilder<InstanceReporter> {
    private final List<MessageSeverity> severities;

    @Nullable
    private VkDebugReportCallbackCreateInfoEXT createInfo;

    public InstanceReporterBuilder() {
        severities = new ArrayList<>();
    }

    public InstanceReporterBuilder severities(MessageSeverity... severities) {
        Collections.addAll(this.severities, severities);
        return this;
    }

    @Override
    protected void preBuild() {
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();
            createInfo = VkDebugReportCallbackCreateInfoEXT.malloc(stack)
                    .sType$Default()
                    .pNext(0)
                    .flags(Maskable.toBitMask(severities))
                    .pfnCallback(new InstanceLogger())
                    .pUserData(0);

            InstanceBuilder instanceBuilder = getEntityBuilder(InstanceBuilder.class);
            instanceBuilder.getCreateInfo().pNext(createInfo);
    }

    @Override
    protected InstanceReporter doBuild() {
        MemoryStack stack = getEngine().getEntityRegistry().getEntity(MemoryContext.class).getStack();
        Instance instance = getEntity(Instance.class);

            LongBuffer handleBuffer = stack.mallocLong(1);
            VkFunction.execute(() -> EXTDebugReport.vkCreateDebugReportCallbackEXT(instance.getHandle(), createInfo, null, handleBuffer));
            return new InstanceReporter(handleBuffer.get(0));
        }
}
