package engine.memory.resource;

import engine.memory.holder.Holder;

public interface DestroyDiResource<H extends Holder<Resource<H>>> extends DestroyResource<H>, DiResource<H> {
    @Override
    default void destroy() {
        for (H holder : getHolders()) {
            holder.removeResource(this);
        }
    }
}
