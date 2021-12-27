package engine.memory.resource;

import engine.memory.destroy.Destroy;
import engine.memory.holder.Holder;

public interface DestroyResource<H extends Holder<Resource<H>>> extends Resource<H>, Destroy {
    @Override
    default void destroy() {
        getHolder().removeResource(this);
    }
}
