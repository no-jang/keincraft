package engine.window.properties;

import org.lwjgl.system.MemoryUtil;

public class WindowException extends RuntimeException {
    private final int code;
    private final String description;

    public WindowException(int code, long descriptionPointer) {
        this(code, MemoryUtil.memUTF8(descriptionPointer));
    }

    public WindowException(int code, String description) {
        super(String.format("%s (%d)", description, code));

        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
