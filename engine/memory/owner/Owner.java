package engine.memory.owner;

import engine.memory.ownable.Ownable;

import java.util.List;

public interface Owner<C extends Ownable<? extends Owner<C>>> {
    List<C> owning();
}
