package common.ecs;

/**
 * An entity system is to process the components of an entity
 *
 * @see Entity
 * @see common.ecs.component.Component
 */
public class EntitySystem {

    /**
     * Called if entity system is added to engine
     *
     * @param engine engine where entity system was added to
     */
    public void addedToEngine(Engine engine) {

    }

    /**
     * Called if entity system is removed from engine
     *
     * @param engine engine where entity system was removed from
     */
    public void removeFromEngine(Engine engine) {

    }

    /**
     * Processes the components of entities
     *
     * Called by update function from Engine.
     *
     * @param engine engine of update call
     */
    public void update(Engine engine) {

    }
}
