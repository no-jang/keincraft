package engine.ecs.component;

import engine.ecs.engine.Engine;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class ComponentBuilder<C extends Component> {
    protected boolean isInBuild = false;

    @Nullable
    private Engine engine;

    public ComponentBuilder() {
        this(null);
    }

    public ComponentBuilder(@Nullable Engine engine) {
        this.engine = engine;
    }

    protected void preBuild() {

    }

    public C build() {
        if (isInBuild) {
            throw new IllegalArgumentException("ComponentBuilder can't be called for new build while in build");
        }
        isInBuild = true;
        preBuild();
        C built = doBuild();
        postBuild();
        isInBuild = false;

        return built;
    }

    protected void postBuild() {

    }

    protected abstract C doBuild();

    protected Engine getEngine() {
        if (engine == null) {
            throw new IllegalStateException("Can't use Engine when it is not available");
        }
        return engine;
    }

    void setEngine(Engine engine) {
        this.engine = engine;
    }
}
