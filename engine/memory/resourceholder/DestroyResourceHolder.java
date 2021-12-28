package engine.memory.resourceholder;

import engine.memory.holder.DestroyHolder;
import engine.memory.holder.Holder;
import engine.memory.resource.DestroyResource;
import engine.memory.resource.Resource;

public interface DestroyResourceHolder<H extends Holder<Resource<H>>, R> extends DestroyHolder<R>, DestroyResource<H> {
    @Override
    default void destroy() {
        DestroyHolder.super.destroy();
        DestroyResource.super.destroy();
    }
}
