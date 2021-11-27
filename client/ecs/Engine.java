package client.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Engine {
    private final List<Entity> entities;
    private final Map<Class<? extends EntitySystem>, EntitySystem> systems;
    private final Map<Family, List<Entity>> families;

    public Engine() {
        entities = new ArrayList<>();
        systems = new HashMap<>();
        families = new HashMap<>();
    }

    public Entity createEntity() {
        return new Entity();
    }

    public void addEntity(Entity entity) {
        if (entities.contains(entity)) {
            throw new IllegalArgumentException("Entity is already registered: " + entity);
        }

        entities.add(entity);

        families.forEach((family, familyEntities) -> {
            if (family.matches(entity)) {
                familyEntities.add(entity);
            }
        });
    }

    public void addAllEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            addEntity(entity);
        }
    }

    public void removeEntity(Entity entity) {
        if (!entities.contains(entity)) {
            throw new IllegalArgumentException("Entity was not found: " + entity);
        }

        entities.remove(entity);

        families.forEach((family, familyEntities) -> {
            if (family.matches(entity)) {
                familyEntities.remove(entity);
            }
        });
    }

    public void removeAllEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            removeEntity(entity);
        }
    }

    public void clearEntities() {
        entities.clear();
    }

    public List<Entity> getEntities(Family family) {
        List<Entity> entities = families.get(family);

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

    public List<Entity> getEntities() {
        return entities;
    }

    public void addSystem(EntitySystem system) {
        Class<? extends EntitySystem> type = system.getClass();
        EntitySystem oldSystem = getSystem(type);

        if (oldSystem != null) {
            removeSystem(oldSystem);
        }

        systems.put(type, system);

        system.addedToEngine(this);
    }

    public void addAllSystems(List<EntitySystem> systems) {
        for (EntitySystem system : systems) {
            addSystem(system);
        }
    }

    public void removeSystem(EntitySystem system) {
        Class<? extends EntitySystem> type = system.getClass();
        if (!systems.containsKey(type)) {
            throw new IllegalArgumentException("System was not found: " + system);
        }

        systems.remove(type);

        system.removeFromEngine(this);
    }

    public void removeAllSystems(List<EntitySystem> systems) {
        for (EntitySystem system : systems) {
            removeSystem(system);
        }
    }

    public void clearSystems() {
        systems.clear();
    }

    public EntitySystem getSystem(Class<? extends EntitySystem> type) {
        return systems.get(type);
    }

    public Map<Class<? extends EntitySystem>, EntitySystem> getSystems() {
        return systems;
    }

    public void update() {
        for (EntitySystem system : systems.values()) {
            system.update(this);
        }
    }
}
