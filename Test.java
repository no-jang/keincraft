import client.ecs.Engine;
import client.ecs.Entity;
import client.ecs.EntitySystem;
import client.ecs.Family;
import client.ecs.component.Component;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Engine engine = new Engine();

        MovementSystem movementSystem = new MovementSystem();

        engine.addSystem(movementSystem);

        for(int i = 0; i < 100000; i++) {
            Entity entity = engine.createEntity();
            entity.add(new PositionComponent(0, 0));
            entity.add(new MovementComponent(1, 2));

            engine.addEntity(entity);
        }

        for(int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            engine.update();
            long end = System.currentTimeMillis();
            long delta = end - start;
            System.out.println(delta);
        }
    }

    public static class PositionComponent implements Component {
        public float x;
        public float y;

        public PositionComponent(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class MovementComponent implements Component {
        public float velocityX;
        public float velocityY;

        public MovementComponent(float velocityX, float velocityY) {
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    public static class MovementSystem extends EntitySystem {
        private List<Entity> entities;

        @Override
        public void addedToEngine(Engine engine) {
            entities = engine.getEntities(Family.all(PositionComponent.class, MovementComponent.class).build());
        }

        @Override
        public void removeFromEngine(Engine engine) {
            entities = null;
        }

        @Override
        public void update(Engine engine) {
            for(Entity entity : entities) {
                PositionComponent position = entity.get(PositionComponent.class);
                MovementComponent movement = entity.get(MovementComponent.class);

                position.x += movement.velocityX;
                position.y += movement.velocityY;
            }
        }
    }
}
