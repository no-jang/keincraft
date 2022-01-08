package test.engine.core.container;

import engine.core.container.Container;
import engine.core.container.DefaultContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class ContainerTest {
    private Container container;

    @BeforeEach
    public void beforeEach() {
        container = new DefaultContainer();
    }

    @Test
    public void testBindFromInstance() {
        String instance = "test";

        container.bind(String.class).fromInstance(instance);
        container.resolveBindings();

        String injected = container.get(String.class);
        assertThat(injected).isEqualTo(instance);
    }
}
