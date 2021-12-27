package engine.memory.ownable;

import engine.memory.owner.Owner;

public interface Ownable<P extends Owner<? extends Ownable<P>>> {
    P owner();
}
