package engine.memory.resource;

import java.util.List;

public interface DiResource<H> extends Resource<H> {
    List<H> getHolders();

    @Override
    default H getHolder() {
        return getHolders().get(0);
    }
}
