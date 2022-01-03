package engine.memory;

import org.lwjgl.system.MemoryStack;

public class MemoryContext {
    private final MemoryStack stack = MemoryStack.stackPush();

    public MemoryStack getStack() {
        return stack;
    }
}
