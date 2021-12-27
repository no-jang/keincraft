package engine.memory.rholder;

import engine.memory.holder.DHolder;
import engine.memory.holder.Holder;
import engine.memory.resource.DResource;
import engine.memory.resource.Resource;

public interface DRHolder<H extends Holder<Resource<H>>, R> extends DHolder<R>, DResource<H> {
    @Override
    default void destroy() {
        DHolder.super.destroy();
        DResource.super.destroy();
    }
}
