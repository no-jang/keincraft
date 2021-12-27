package engine.memory.reference;

import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public interface Reference<P extends Owner<? extends Ownable<P>>, C extends Ownable<? extends Owner<C>>> extends Owner<C>, Ownable<P> {
}
