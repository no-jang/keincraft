package engine.memory.ownable;

import engine.memory.owner.Owner;

public interface Ownable<T extends Owner> {
    T owner();
}
