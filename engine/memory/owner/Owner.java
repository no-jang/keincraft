package engine.memory.owner;

import engine.memory.ownable.Ownable;

import java.util.List;

public interface Owner {
    List<Ownable<?>> owning();
}
