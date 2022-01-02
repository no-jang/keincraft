package engine.memory;

import engine.ecs.entity.Entity;
import org.lwjgl.system.MemoryStack;

public class MemoryContext extends Entity {
    private final MemoryStack stack = MemoryStack.stackPush();

    public MemoryStack getStack() {
        return stack;
    }
}
