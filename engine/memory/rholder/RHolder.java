package engine.memory.rholder;

import engine.memory.holder.Holder;
import engine.memory.resource.Resource;

public interface RHolder<H, R> extends Holder<R>, Resource<H> {
}
