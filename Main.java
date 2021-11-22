import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        long window;

        int vbo;
        int vao;
        int program;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            GLFW.glfwInit();
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

            window = GLFW.glfwCreateWindow(900, 900, "keincraft", 0L, 0L);

            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );

            GLFW.glfwMakeContextCurrent(window);

            GL.createCapabilities();
            GL11.glViewport(0, 0, 900, 900);

            String vertexSource = Files.readString(Path.of("shaders/base.vert"));
            String fragmentSource = Files.readString(Path.of("shaders/base.frag"));

            int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
            int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

            GL20.glShaderSource(vertexShader, vertexSource);
            GL20.glShaderSource(fragmentShader, vertexSource);

            GL20.glCompileShader(vertexShader);
            GL20.glCompileShader(fragmentShader);

            int vertexSuccess = GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS);
            int fragmentSuccess = GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS);

            if (vertexSuccess != GL11.GL_TRUE) {
                System.out.println(GL20.glGetShaderInfoLog(vertexShader));
            }

            if (fragmentSuccess != GL11.GL_TRUE) {
                System.out.println(GL20.glGetShaderInfoLog(fragmentSuccess));
            }

            program = GL20.glCreateProgram();
            GL20.glAttachShader(program, vertexShader);
            GL20.glAttachShader(program, fragmentShader);
            GL20.glLinkProgram(program);

            if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
                System.out.println(GL20.glGetProgramInfoLog(fragmentSuccess));
            }

            GL20.glDeleteShader(vertexShader);
            GL20.glDeleteShader(fragmentShader);

            float[] vertices = {
                    -0.5f, -0.5f, 0.0f,
                    0.5f, -0.5f, 0.0f,
                    0.0f, 0.5f, 0.0f
            };

            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);

            vbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
            GL20.glEnableVertexAttribArray(0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        }

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.0f, 1.0f, 0, 1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            GL20.glUseProgram(program);
            GL30.glBindVertexArray(vao);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwTerminate();
    }
}