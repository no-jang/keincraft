package engine.memory;

import org.lwjgl.system.MemoryStack;

public class MemoryContext {
    private static MemoryStack stack = MemoryStack.stackPush();

    public static void newStack() {
        MemoryStack.stackPop();
        stack = MemoryStack.stackPush();
    }

    public static MemoryStack getStack() {
        return stack;
    }
}
