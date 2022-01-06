package engine.core.entity;

import engine.core.Engine;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EntityRegistry {
    private final Engine engine;

    private final Map<Class<?>, List<Entity>> entities;

    public EntityRegistry(Engine engine) {
        this.engine = engine;

        entities = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E addEntity(E entity) {
        List<E> entities = getEntitiesInternal((Class<E>) entity.getClass());

        E replaceEntity = checkEntityCount(entities);
        if (replaceEntity != null) {
            return replaceEntity;
        }

        entities.add(entity);
        return entity;
    }

    public <E extends Entity> E addEntity(Class<E> entityClass, Supplier<E> supplier) {
        List<E> entities = getEntitiesInternal(entityClass);

        E replaceEntity = checkEntityCount(entities);
        if (replaceEntity != null) {
            return replaceEntity;
        }

        E entity = supplier.get();
        entities.add(entity);
        return entity;
    }

    public <E extends Entity, B extends Entity.Builder<E>> E addEntityBuilder(Class<E> entityClass, B builder) {
        List<E> entities = getEntitiesInternal(entityClass);

        E replaceEntity = checkEntityCount(entities);
        if (replaceEntity != null) {
            return replaceEntity;
        }

        E entity = builder.build();
        entities.add(entity);
        return entity;
    }

    public <E extends Entity, B extends Entity.Builder<E>> E addEntityBuilder(Class<E> entityClass, Supplier<B> supplier) {
        List<E> entities = getEntitiesInternal(entityClass);

        E replaceEntity = checkEntityCount(entities);
        if (replaceEntity != null) {
            return replaceEntity;
        }

        E entity = supplier.get().build();
        entities.add(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> E getEntity(Class<E> entityClass) {
        List<E> entities = (List<E>) this.entities.get(entityClass);

        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("Entity was not added: " + entityClass);
        }

        return entities.get(0);
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> Supplier<E> getEntityProvider(Class<E> entityClass) {
        List<E> entities = (List<E>) this.entities.get(entityClass);

        return () -> {
            if (entities.isEmpty()) {
                throw new IllegalArgumentException("Entity was not added: " + entityClass);
            }

            return entities.get(0);
        };
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> List<E> getEntities(Class<E> entityClass) {
        List<E> entities = (List<E>) this.entities.get(entityClass);

        if (entities.isEmpty()) {
            throw new IllegalArgumentException("No entities of type were added: " + entityClass);
        }

        return entities;
    }

    @SuppressWarnings("unchecked")
    public <E extends Entity> Supplier<List<E>> getEntitiesProvider(Class<E> entityClass) {
        List<E> entities = (List<E>) this.entities.get(entityClass);

        return () -> {
            if (entities.isEmpty()) {
                throw new IllegalArgumentException("No entities of type are added: " + entityClass);
            }

            return entities;
        };
    }

    @SuppressWarnings("unchecked")
    private <E extends Entity> List<E> getEntitiesInternal(Class<E> entityClass) {
        return (List<E>) this.entities.computeIfAbsent(entityClass, k -> new ArrayList<>());
    }

    @Nullable
    private <E extends Entity> E checkEntityCount(List<E> entities) {
        if (entities.isEmpty()) {
            return null;
        }

        E entity = entities.get(0);

        int entityCount = entities.size();
        int maxEntityCount = entity.getMaxEntityCount();
        if (maxEntityCount == -1 || entityCount < maxEntityCount) {
            return null;
        }

        return entity;
    }
}
