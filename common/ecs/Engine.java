package common.ecs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Engine for the entity component system.
 * <p>
 * Holds every entity system and family
 *
 * @see Entity
 * @see EntitySystem
 * @see Family
 */
public class Engine {
    private final List<Entity> entities;
    private final Map<Class<? extends EntitySystem>, EntitySystem> systems;
    private final Map<Family, List<Entity>> families;

    /**
     * Creates new empty engine.
     */
    public Engine() {
        entities = new ArrayList<>();
        systems = new HashMap<>();
        families = new HashMap<>();
    }

    /**
     * Creates new empty entity.
     *
     * @return new entity
     * @see Entity
     */
    public Entity createEntity() {
        return new Entity();
    }

    /**
     * Registers an entity.
     * <p>
     * If families that match exists, the entity is added to them.
     * An entity can only be registered once.
     *
     * @param entity entity to register.
     * @throws IllegalArgumentException Throws IllegalArgumentException if entity is already registered
     * @see Entity
     */
    public void addEntity(Entity entity) {
        if (entities.contains(entity)) {
            throw new IllegalArgumentException("Entity is already registered: " + entity);
        }

        entities.add(entity);

        // Add this entity to his corresponding families if they already exist
        families.forEach((family, familyEntities) -> {
            if (family.matches(entity)) {
                familyEntities.add(entity);
            }
        });
    }

    /**
     * Registers a collection of entities.
     * <p>
     * If families that match exists, the entities are added to their corresponding families.
     * Entities can only be registered ones.
     *
     * @param entities collection of entities
     * @throws IllegalArgumentException Throws if entity is already registered
     * @see Entity
     * @see #addEntity(Entity)
     */
    public void addAllEntities(Collection<Entity> entities) {
        for (Entity entity : entities) {
            addEntity(entity);
        }
    }

    /**
     * Unregisters an entity
     * <p>
     * Finds families matching the enitity and removes it from them
     * Entity must be registered.
     *
     * @param entity entity to unregister
     * @throws IllegalArgumentException Throws if entity is not found
     * @see Entity
     */
    public void removeEntity(Entity entity) {
        if (!entities.contains(entity)) {
            throw new IllegalArgumentException("Entity was not found: " + entity);
        }

        entities.remove(entity);

        // Removes entity from corresponding families
        families.forEach((family, familyEntities) -> {
            if (family.matches(entity)) {
                familyEntities.remove(entity);
            }
        });
    }

    /**
     * Unregisters a collection of entities
     * Finds families matching the enitity and removes it from them
     * Entity must be registered.
     *
     * @param entities collection of entities
     * @throws IllegalArgumentException Throws if entity is not found
     * @see Entity
     * @see #removeEntity(Entity)
     */
    public void removeAllEntities(Collection<Entity> entities) {
        for (Entity entity : entities) {
            removeEntity(entity);
        }
    }

    /**
     * Clears all entities and their corresponding families
     */
    public void clearEntities() {
        entities.clear();
        families.forEach((family, familyEntities) -> {
            familyEntities.clear();
        });
    }

    /**
     * Returns all entities from the corresponding family
     *
     * @param family family the entities will be gathered from
     * @return list of entities in the family
     * @see Entity
     * @see Family
     */
    public List<Entity> getEntities(Family family) {
        List<Entity> entities = families.get(family);

        // If family does not exist, create new and set all entities corresponding
        if (entities == null) {
            entities = new ArrayList<>();
            families.put(family, entities);

            for (Entity entity : this.entities) {
                if (family.matches(entity)) {
                    entities.add(entity);
                }
            }
        }

        return entities;
    }

    /**
     * Get all entities
     *
     * @return all entities
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Register an entity system
     * <p>
     * Removes the old system of the same type if exists first
     *
     * @param system entity system to register
     * @see EntitySystem
     */
    public void addSystem(EntitySystem system) {
        Class<? extends EntitySystem> type = system.getClass();
        EntitySystem oldSystem = getSystem(type);

        if (oldSystem != null) {
            removeSystem(oldSystem);
        }

        systems.put(type, system);

        system.addedToEngine(this);
    }

    /**
     * Register a collection of entity systems
     *
     * @param systems entity systems to register
     * @see EntitySystem
     * @see #addSystem(EntitySystem)
     */
    public void addAllSystems(Collection<EntitySystem> systems) {
        for (EntitySystem system : systems) {
            addSystem(system);
        }
    }

    /**
     * Unregister an entity system
     * <p>
     * The entity system to remove must be registered
     *
     * @param system entity system to unregister
     * @throws IllegalArgumentException Throws if entity system was not registered
     * @see EntitySystem
     */
    public void removeSystem(EntitySystem system) {
        Class<? extends EntitySystem> type = system.getClass();
        if (!systems.containsKey(type)) {
            throw new IllegalArgumentException("System was not found: " + system);
        }

        systems.remove(type);

        system.removeFromEngine(this);
    }

    /**
     * Unregisters collection of entity systems
     * <p>
     * The entity systems to remove must be registered
     *
     * @param systems entity systems to remove
     * @throws IllegalArgumentException Throws if entity system was not registered
     * @see EntitySystem
     * @see #removeSystem(EntitySystem)
     */
    public void removeAllSystems(Collection<EntitySystem> systems) {
        for (EntitySystem system : systems) {
            removeSystem(system);
        }
    }

    /**
     * Unregisters all entity systems
     *
     * @see EntitySystem
     */
    public void clearSystems() {
        systems.clear();
    }

    /**
     * Returns entity system of specified type
     *
     * @param type type of entity system
     * @return entity system of type
     * @see EntitySystem
     */
    public EntitySystem getSystem(Class<? extends EntitySystem> type) {
        return systems.get(type);
    }

    /**
     * Returns all entity systems as map with type as key
     *
     * @return map of entity systems
     * @see EntitySystem
     */
    public Map<Class<? extends EntitySystem>, EntitySystem> getSystems() {
        return systems;
    }

    /**
     * Calls the update method of all registered EntitySystems
     *
     * @see EntitySystem#update(Engine)
     */
    public void update() {
        for (EntitySystem system : systems.values()) {
            system.update(this);
        }
    }
}
