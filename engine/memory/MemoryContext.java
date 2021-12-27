package engine.memory;

import org.lwjgl.system.MemoryStack;

// TODO New memory system
public class MemoryContext {
    public static final MemoryStack stack = MemoryStack.stackPush();

    public static MemoryStack getStack() {
        return stack;
    }
}
