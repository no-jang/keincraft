package client.graphics.vk.memory;

import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class MemoryContext {
    private static final Map<Thread, MemoryStack> stacks = new HashMap<>();

    public static void newStacks() {
        synchronized (stacks) {
            stacks.clear();
        }
    }

    public static MemoryStack getStack() {
        Thread currentThread = Thread.currentThread();

        synchronized (stacks) {
            MemoryStack stack = stacks.get(currentThread);

            if (stack == null) {
                stack = MemoryStack.stackPush();
                stacks.put(currentThread, stack);
            }

            return stack;
        }
    }
}
