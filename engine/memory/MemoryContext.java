package engine.memory;

import engine.core.Engine;
import engine.core.entity.Entity;
import org.lwjgl.system.MemoryStack;

public class MemoryContext extends Entity {
    private final MemoryStack stack = MemoryStack.stackPush();

    public MemoryContext(Engine engine) {
        super(engine);
    }

    public MemoryStack getStack() {
        return stack;
    }
}
