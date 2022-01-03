package engine.graphics.instance;

import engine.core.component.Component;

public class InstanceReporter extends Component {
    private final long handle;

    public InstanceReporter(long handle) {
        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }
}
