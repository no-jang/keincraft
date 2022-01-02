package engine.ecs.component;

public abstract class ComponentBuilder<C extends Component> {
    protected boolean isInBuild = false;

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
}
