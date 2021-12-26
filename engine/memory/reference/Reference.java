package engine.memory.reference;

import engine.memory.ownable.Ownable;
import engine.memory.owner.Owner;

public interface Reference<T extends Owner> extends Owner, Ownable<T> {

}
