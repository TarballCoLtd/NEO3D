package com.alyxferrari.neo3d.gfx;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import com.alyxferrari.neo3d.obj.*;
import com.alyxferrari.neo3d.*;
import java.io.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;
public class NEOEngine {
	protected static Environment3D environment = null;
	protected static long window = NULL;
	private NEOEngine() {}
	public static void initialize() throws IOException {
		initialize(new Environment3D());
	}
	public static void initialize(Environment3D environment) throws IOException {
		if (window == NULL) {
			if (glfwInit()) {
				window = glfwCreateWindow(800, 600, "", NULL, NULL);
				if (window == NULL) {
					glfwTerminate();
					throw new IllegalStateException("Unable to create GLFW window.");
				}
				float data[] = {-0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f};
				glfwMakeContextCurrent(window);
				GL.createCapabilities();
				glViewport(0, 0, 800, 600);
				glfwSetFramebufferSizeCallback(window, NEOEngine::framebufferSizeCallback);
				int shader = ShaderUtils.createProgram(new File("neoshader.vert"), new File("neoshader.frag"));
				int vbo = glGenBuffers();
				int vao = glGenVertexArrays();
				glBindVertexArray(vao);
				glBindBuffer(GL_ARRAY_BUFFER, vbo);
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
				// location = 0
				// 3 values for position
				// type of data
				// is normalized?
				// space between vertices, so 4 bytes (float) times 3 floats per vertex and 3 floats per color - 0 means OpenGL determines it
				// where the data begins in the array
				glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * SizeOf.FLOAT, 0);
				glEnableVertexAttribArray(0);
				// location = 1
				// 3 values for color
				// type of data
				// is normalized?
				// space between vertices, so 4 bytes (float) times 3 floats per vertex and 3 floats per color
				// array offset (halfway through for the color)
				glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * SizeOf.FLOAT, 3 * SizeOf.FLOAT);
				glEnableVertexAttribArray(1);
				glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				while (!glfwWindowShouldClose(window)) {
					// input
					processInput(window);
					// rendering
					glClear(GL_COLOR_BUFFER_BIT);
					glUseProgram(shader);
					glDrawArrays(GL_TRIANGLES, 0, 3);
					// vsync stuff
					glfwSwapBuffers(window);
					glfwPollEvents();
				}
				terminate();
				return;
			}
			throw new IllegalStateException("Unable to initialize GLFW.");
		}
	}
	protected static void processInput(long window) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetWindowShouldClose(window, true);
		}
		if (glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}
		if (glfwGetKey(window, GLFW_KEY_F2) == GLFW_PRESS) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
	}
	protected static void framebufferSizeCallback(long window, int width, int height) {
		glViewport(0, 0, width, height);
	}
	public static void initialize(Environment3D environment, PrintStream errorCallback) throws IOException {
		GLFWErrorCallback.createPrint(errorCallback);
		initialize(environment);
	}
	/** Sets the current environment.
	 */
	public static void setEnvironment(Environment3D environment) {
		if (environment == null) {
			throw new IllegalArgumentException("The environment must not be null.");
		}
		NEOEngine.environment = environment;
	}
	/**
	 * @return The current environment.
	 */
	public static Environment3D getEnvironment() {
		return environment;
	}
	/** Sets the error callback stream. GLFW errors, should they appear, will be written to this stream.
	 */
	public static void setErrorCallback(PrintStream errorCallback) {
		GLFWErrorCallback.createPrint(errorCallback);
	}
	/** Terminates GLFW and stops rendering.
	 */
	public static void terminate() {
		if (window != NULL) {
			glfwSetWindowShouldClose(window, true);
			try {Thread.sleep(1);} catch (InterruptedException ex) {}
			glfwFreeCallbacks(window);
			glfwDestroyWindow(window);
			glfwTerminate();
			window = NULL;
		}
	}
}