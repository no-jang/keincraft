package engine.memory.resourceholder;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;

public interface ResourceHolder<H, R> extends Holder<R>, Resource<H> {
}
