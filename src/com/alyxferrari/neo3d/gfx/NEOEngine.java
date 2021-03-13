package com.alyxferrari.neo3d.gfx;
import org.lwjgl.opengl.*;
import com.alyxferrari.neo3d.obj.*;
import com.alyxferrari.neo3d.*;
import com.alyxferrari.neo3d.exc.*;
import java.util.*;
import java.awt.*;
import java.io.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;
public class NEOEngine {
	/** Current 3D environment.
	 */
	protected static Environment3D environment = null;
	/** Window title suffix.
	 */
	protected static final String WINDOW_SUFFIX = "powered by " + NEO3D.LIB_NAME + " " + NEO3D.LIB_VERSION;
	/** GLFW window ID. Don't modify unless you know what you're doing.
	 */
	protected static long window = NULL;
	/** Current OpenGL shader ID. Don't modify unless you know what you're doing.
	 */
	protected static int shader = 0;
	/** Radius of imaginary sphere around the origin.
	 */
	protected static float camDist = 1000.0f;
	/** FOV in radians (not very accurate).
	 */
	protected static float viewAngle = (float) Math.toRadians(80);
	/** Coordinates of camera position on imaginary sphere around the origin.
	 */
	protected static ViewAngle viewAngles = new ViewAngle(0.0f, 0.0f);
	/** Mouse x position.
	 */
	protected static float mouseX = 0.0f;
	/** Mouse y position.
	 */
	protected static float mouseY = 0.0f;
	/** Current width of GLFW window.
	 */
	protected static int width = 0;
	/** Current height of GLFW window.
	 */
	protected static int height = 0;
	/** Current position of camera in 3D space.
	 */
	protected static Vector3D cam = new Vector3D(0.0f);
	protected static final float SENSITIVITY = 100.0f;
	protected static final float SCALE = 100.0f;
	private NEOEngine() {}
	/** Initializes OpenGL and GLFW and sets up the NEO3D renderer with a blank scene.
	 * @throws IOException If loading the shader files fails.
	 */
	public static void initialize() throws IOException {
		initialize(new Environment3D(), null, new Dimension(800, 600));
	}
	/** Initializes OpenGL and GLFW and sets up the NEO3D renderer with the specified environment.
	 * @param environment The 3D environment.
	 * @param device The device on which to do the 3D projection.
	 * @param title Title for the window.
	 * @param size Desired size for the window.
	 * @throws IOException If loading the shader files fails.
	 */
	public static void initialize(Environment3D environment, String title, Dimension size) throws IOException {
		if (window == NULL) {
			if (glfwInit()) {
				glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
				window = glfwCreateWindow((int)size.getWidth(), (int)size.getHeight(), title == null || title.equals("") ? WINDOW_SUFFIX : title + " -- " + WINDOW_SUFFIX, NULL, NULL); // is the title null? if so, don't add the dashes
				if (window != NULL) {
					NEOEngine.environment = environment;
					width = (int) size.getWidth();
					height = (int) size.getHeight();
					glfwSetCursorPosCallback(window, NEOEngine::cursorPositionCallback);
					glfwSetScrollCallback(window, NEOEngine::scrollCallback);
					glfwSetMouseButtonCallback(window, NEOEngine::mouseButtonCallback);
					glfwMakeContextCurrent(window);
					GL.createCapabilities();
					shader = ShaderUtils.createProgram(new File("shaders/render.vert"), new File("shaders/render.frag"));
					glfwMakeContextCurrent(NULL);
					return;
				}
				// if window == NULL (0x0)
				glfwTerminate();
				throw new GLFWWindowCreationError(GLFWWindowCreationError.RECOMMENDED_MESSAGE);
			}
			// if !glfwInit()
			throw new GLFWInitializationError(GLFWInitializationError.RECOMMENDED_MESSAGE);
		}
	}
	/** Starts rendering the current environment. This method blocks until the window is closed.
	 */
	public static void startRender() {
		if (window != NULL) {
			glfwMakeContextCurrent(window);
			GL.createCapabilities();
			glfwSwapInterval(0);
			glViewport(0, 0, width, height);
			glfwSetFramebufferSizeCallback(window, NEOEngine::framebufferSizeCallback);
			int vbo = glGenBuffers();
			int vao = glGenVertexArrays();
			glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			setVertexAttributePointers();
			//environment.rebuildLights();
			
			int viewAnglesID = glGetUniformLocation(shader, "viewAngles");
			int sinViewAnglesID = glGetUniformLocation(shader, "sinViewAngles");
			int cosViewAnglesID = glGetUniformLocation(shader, "cosViewAngles");
			int windowID = glGetUniformLocation(shader, "window");
			int camDistID = glGetUniformLocation(shader, "camDist");
			int sinViewAngle2ID = glGetUniformLocation(shader, "sinViewAngle2");
			int camPosID = glGetUniformLocation(shader, "camPos");
			Runnable bufferPopulator = () -> {
				float[] attribs = computeAttribs();
				glUniform2f(viewAnglesID, viewAngles.viewAngleX, viewAngles.viewAngleY);
				glUniform2f(sinViewAnglesID, viewAngles.sinViewAngleX, viewAngles.sinViewAngleY);
				glUniform2f(cosViewAnglesID, viewAngles.cosViewAngleX, viewAngles.cosViewAngleY);
				glUniform2f(windowID, width, height);
				glUniform1f(camDistID, camDist);
				glUniform1f(sinViewAngle2ID, (float)Math.sin(viewAngle/2.0f));
				glUniform3f(camPosID, cam.getX(), cam.getY(), cam.getZ());
				glBufferData(GL_ARRAY_BUFFER, attribs, GL_DYNAMIC_DRAW);
			};
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glfwShowWindow(window);
			glUseProgram(shader);
			long lastFpsTime = 0L;
			long lastLoopTime = System.nanoTime();
			int fps = 0;
			while (!glfwWindowShouldClose(window)) {
				long now = System.nanoTime(); // i hate this style of fps counter but whatever it works
				long updateLength = now-lastLoopTime;
				lastLoopTime = now;
				lastFpsTime += updateLength;
				if (lastFpsTime >= 1000000000) {
					System.out.println("FPS: " + fps);
					lastFpsTime = 0;
					fps = 0;
				}
				setCameraPosition();
				processInput(window);
				glClear(GL_COLOR_BUFFER_BIT);
				bufferPopulator.run(); // populates buffers and uniforms based on compute device (see above)
				glDrawArrays(GL_TRIANGLES, 0, 2082*3);
				glfwSwapBuffers(window);
				glfwPollEvents();
				fps++;
			}
			terminate();
			return;
		}
		throw new NEO3DNotInitializedException(NEO3DNotInitializedException.RECOMMENDED_MESSAGE);
	}
	/** Sets the vertex attribute pointers for the current compute device.
	 */
	protected static void setVertexAttributePointers() {
		// location = 0
		// how many floats in attrib
		// type of attrib data
		// normalized?
		// total size of all attribs
		// offset of this attrib
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 13 * SizeOf.FLOAT, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 13 * SizeOf.FLOAT, 3 * SizeOf.FLOAT);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 13 * SizeOf.FLOAT, 7 * SizeOf.FLOAT);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 13 * SizeOf.FLOAT, 10 * SizeOf.FLOAT);
		glEnableVertexAttribArray(3);
	}
	/** Used internally. Converts a float ArrayList to a primitive float array.
	 */
	protected static float[] toArray(ArrayList<Float> array) {
		float[] ret = new float[array.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = array.get(i);
		}
		return ret;
	}
	/** Calculates the view angles based on the mouse position.
	 */
	protected static void calculateViewAngles() {
		float viewAngleX = -((mouseX-width)/2)/SENSITIVITY;
		float viewAngleY = NEOUtils.clamp(-((mouseY-height)/2)/SENSITIVITY, -NEOUtils.PI*0.5f, NEOUtils.PI*0.5f);
		viewAngles = new ViewAngle(viewAngleX, viewAngleY);
	}
	/** Computes vertex attributes on the GPU.
	 * @return The vertex attributes to be passed onto the vertex shader.
	 */
	protected static float[] computeAttribs() {
		calculateViewAngles();
		ArrayList<Float> attribs = new ArrayList<Float>();
		for (int x = 0; x < environment.getPolygons().length; x++) {
			Vector3D[] vertices = environment.getPolygons()[x].getVertices();
			float average = NEOUtils.hypot3(cam.getX()-vertices[0].getX(), cam.getY()-vertices[0].getY(), cam.getZ()-vertices[0].getZ());
			average += NEOUtils.hypot3(cam.getX()-vertices[1].getX(), cam.getY()-vertices[1].getY(), cam.getZ()-vertices[1].getZ());
			average += NEOUtils.hypot3(cam.getX()-vertices[2].getX(), cam.getY()-vertices[2].getY(), cam.getZ()-vertices[2].getZ());
			average /= 3.0f;
			environment.getPolygons()[x].setDistance(average);
		}
		Arrays.sort(environment.getPolygons(), Collections.reverseOrder());
		// yeah this is terrible but hard-coding it is more efficient than doing loops in this case
		for (int x = 0; x < environment.getPolygons().length; x++) {
			Vector3D[] vertices = environment.getPolygons()[x].getVertices();
			// first vertex
			attribs.add(vertices[0].getX()); // first attribute
			attribs.add(vertices[0].getY());
			attribs.add(vertices[0].getZ());
			attribs.add(vertices[0].getColor().getRed()); // second attribute
			attribs.add(vertices[0].getColor().getGreen());
			attribs.add(vertices[0].getColor().getBlue());
			attribs.add(vertices[0].getColor().getAlpha());
			attribs.add(vertices[1].getX()); // third attribute
			attribs.add(vertices[1].getY());
			attribs.add(vertices[1].getZ());
			attribs.add(vertices[2].getX()); // fourth attribute
			attribs.add(vertices[2].getY());
			attribs.add(vertices[2].getZ());
			// second vertex
			attribs.add(vertices[1].getX()); // first attribute
			attribs.add(vertices[1].getY());
			attribs.add(vertices[1].getZ());
			attribs.add(vertices[1].getColor().getRed()); // second attribute
			attribs.add(vertices[1].getColor().getGreen());
			attribs.add(vertices[1].getColor().getBlue());
			attribs.add(vertices[1].getColor().getAlpha());
			attribs.add(vertices[0].getX()); // third attribute
			attribs.add(vertices[0].getY());
			attribs.add(vertices[0].getZ());
			attribs.add(vertices[2].getX()); // fourth attribute
			attribs.add(vertices[2].getY());
			attribs.add(vertices[2].getZ());
			// third vertex
			attribs.add(vertices[2].getX()); // first attribute
			attribs.add(vertices[2].getY());
			attribs.add(vertices[2].getZ());
			attribs.add(vertices[2].getColor().getRed()); // second attribute
			attribs.add(vertices[2].getColor().getGreen());
			attribs.add(vertices[2].getColor().getBlue());
			attribs.add(vertices[2].getColor().getAlpha());
			attribs.add(vertices[1].getX()); // third attribute
			attribs.add(vertices[1].getY());
			attribs.add(vertices[1].getZ());
			attribs.add(vertices[0].getX()); // fourth attribute
			attribs.add(vertices[0].getY());
			attribs.add(vertices[0].getZ());
		}
		return toArray(attribs);
	}
	/** Calculates the position of the camera in 3D space based on the view angles.
	 */
	public static void setCameraPosition() {
		float x = viewAngles.sinViewAngleX*viewAngles.cosViewAngleY*camDist;
		float y = -viewAngles.sinViewAngleY*camDist;
		float z = viewAngles.cosViewAngleX*viewAngles.cosViewAngleY*camDist;
		cam = new Vector3D(x, y, z);
	}
	/** GLFW input processing.
	 */
	protected static void processInput(long window) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
	/** GLFW cursor position callback.
	 */
	protected static void cursorPositionCallback(long window, double xpos, double ypos) {
		mouseX = (float) xpos;
		mouseY = (float) ypos;
	}
	/** GLFW framebuffer size callback.
	 */
	protected static void framebufferSizeCallback(long window, int width, int height) {
		glViewport(0, 0, width, height);
		NEOEngine.width = width;
		NEOEngine.height = height;
	}
	/** GLFW scroll callback.
	 */
	protected static void scrollCallback(long window, double xoffset, double yoffset) {
		if (yoffset > 0) {
			camDist /= 1.2f;
		} else if (yoffset < 0) {
			camDist *= 1.2f;
		}
	}
	/** GLFW mouse button callback.
	 */
	protected static void mouseButtonCallback(long window, int button, int action, int mods) {
		if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}
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
	/** Terminates GLFW and stops rendering. Must be called from the main thread. This is called automatically by startRender() when the window is closed.
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
	public static int getShader() {
		return shader;
	}
	public static boolean isInitialized() {
		return window != NULL;
	}
}