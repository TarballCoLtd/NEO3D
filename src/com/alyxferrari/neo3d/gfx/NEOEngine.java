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
	/** Current 3D compute device.
	 */
	protected static ComputeDevice device = ComputeDevice.CPU;
	/** Current OpenGL shader ID.
	 */
	protected static int shader = 0;
	/** Radius of imaginary sphere around origin.
	 */
	protected static float camDist = 3.0f;
	/** FOV in radians.
	 */
	protected static float viewAngle = (float) Math.toRadians(160);
	/** X coordinate of imaginary sphere around origin.
	 */
	protected static float viewAngleX = 0.0f;
	/** Y coordinate of imaginary sphere around origin.
	 */
	protected static float viewAngleY = 0.0f;
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
	protected static final float SENSITIVITY = 100.0f;
	protected static final float SCALE = 100.0f;
	private NEOEngine() {}
	/** Initializes OpenGL and GLFW and sets up the NEO3D renderer with a blank scene.
	 * @throws IOException If loading the shader files fails.
	 */
	public static void initialize() throws IOException {
		initialize(new Environment3D(), ComputeDevice.CPU, null, new Dimension(800, 600));
	}
	/** Initializes OpenGL and GLFW and sets up the NEO3D renderer.
	 * @param environment The 3D environment.
	 * @param device The device on which to do the 3D projection.
	 * @param title Title for the window.
	 * @param size Desired size for the window.
	 * @throws IOException If loading the shader files fails.
	 */
	public static void initialize(Environment3D environment, ComputeDevice device, String title, Dimension size) throws IOException {
		if (window == NULL) {
			if (glfwInit()) {
				glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
				window = glfwCreateWindow((int)size.getWidth(), (int)size.getHeight(), title == null ? WINDOW_SUFFIX : title + " -- " + WINDOW_SUFFIX, NULL, NULL); // is the title null? if so, don't add the dashes
				if (window != NULL) {
					NEOEngine.environment = environment;
					NEOEngine.device = device;
					width = (int) size.getWidth();
					height = (int) size.getHeight();
					glfwSetCursorPosCallback(window, NEOEngine::cursorPositionCallback);
					glfwSetScrollCallback(window, NEOEngine::scrollCallback);
					glfwSetMouseButtonCallback(window, NEOEngine::mouseButtonCallback);
					glfwMakeContextCurrent(window);
					GL.createCapabilities();
					if (device == ComputeDevice.CPU) {
						shader = ShaderUtils.createProgram(new File("shaders/cpu/cpurender.vert"), new File("shaders/cpu/cpurender.frag"));
					} else if (device == ComputeDevice.GPU) {
						shader = ShaderUtils.createProgram(new File("shaders/gpu/gpurender.vert"), new File("shaders/gpu/gpurender.frag"));
					} else {
						throw new IllegalArgumentException("Compute device must not be null.");
					}
					glfwMakeContextCurrent(NULL);
					return;
				}
				// if the window is null
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
			int viewAnglesID;
			int sinViewAnglesID;
			int cosViewAnglesID;
			int windowID;
			int camDistID;
			int sinViewAngle2ID;
			Runnable bufferPopulation;
			if (device == ComputeDevice.CPU) { 
				bufferPopulation = () -> {
					glBufferData(GL_ARRAY_BUFFER, cpuComputeAttribs(), GL_DYNAMIC_DRAW);
				};
			} else {
				viewAnglesID = glGetUniformLocation(shader, "viewAngles");
				sinViewAnglesID = glGetUniformLocation(shader, "sinViewAngles");
				cosViewAnglesID = glGetUniformLocation(shader, "cosViewAngles");
				windowID = glGetUniformLocation(shader, "window");
				camDistID = glGetUniformLocation(shader, "camDist");
				sinViewAngle2ID = glGetUniformLocation(shader, "sinViewAngle2");
				bufferPopulation = () -> {
					float[] attribs = gpuComputeAttribs();
					glUniform2f(viewAnglesID, viewAngleX, viewAngleY);
					glUniform2f(sinViewAnglesID, (float)Math.sin(viewAngleX), (float)Math.sin(viewAngleY));
					glUniform2f(cosViewAnglesID, (float)Math.cos(viewAngleX), (float)Math.cos(viewAngleY));
					glUniform2f(windowID, width, height);
					glUniform1f(camDistID, camDist);
					glUniform1f(sinViewAngle2ID, (float)Math.sin(viewAngle/2.0f));
					glBufferData(GL_ARRAY_BUFFER, attribs, GL_DYNAMIC_DRAW);
				};
			}
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glfwShowWindow(window);
			glUseProgram(shader);
			long lastFpsTime = 0L;
			long lastLoopTime = System.nanoTime();
			int fps = 0;
			while (!glfwWindowShouldClose(window)) {
				long now = System.nanoTime();
				long updateLength = now-lastLoopTime;
				lastLoopTime = now;
				lastFpsTime += updateLength;
				if (lastFpsTime >= 1000000000) {
					System.out.println("FPS: " + fps);
					lastFpsTime = 0;
					fps = 0;
				}
				processInput(window);
				glClear(GL_COLOR_BUFFER_BIT);
				bufferPopulation.run(); // populates buffers and uniforms based on compute device (see above)
				glDrawArrays(GL_TRIANGLES, 0, 3);
				glfwSwapBuffers(window);
				glfwPollEvents();
				fps++;
			}
			terminate();
			return;
		}
		throw new NEO3DNotInitializedException(NEO3DNotInitializedException.RECOMMENDED_MESSAGE);
	}
	protected static void setVertexAttributePointers() {
		if (device == ComputeDevice.CPU) {
			// location = 0
			// 3 values for point
			// primitive type
			// is normalized?
			// how many bytes for all attribs together
			// offset of this attrib
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * SizeOf.FLOAT, 0);
			glEnableVertexAttribArray(0);
		} else if (device == ComputeDevice.GPU) {
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * SizeOf.FLOAT, 0);
			glEnableVertexAttribArray(0);
		}
	}
	protected static float[] toArray(ArrayList<Float> array) {
		float[] ret = new float[array.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = array.get(i);
		}
		return ret;
	}
	protected static void calculateViewAngles() {
		viewAngleX = -((mouseX-width)/2)/SENSITIVITY;
		viewAngleY = -((mouseY-height)/2)/SENSITIVITY;
		if (viewAngleY > Math.PI) {
			viewAngleY = (float) Math.PI;
		} else if (viewAngleY < -Math.PI) {
			viewAngleY = (float) -Math.PI;
		}
	}
	protected static float[] gpuComputeAttribs() {
		calculateViewAngles();
		ArrayList<Float> attribs = new ArrayList<Float>();
		Object3D[] objects = environment.getObjects();
		for (int x = 0; x < objects.length; x++) {
			Polygon3D[] polygons = objects[x].getPolygons();
			for (int y = 0; y < polygons.length; y++) {
				Vector3D[] vertices = polygons[y].getVertices();
				for (int z = 0; z < vertices.length; z++) {
					attribs.add(vertices[z].getX());
					attribs.add(vertices[z].getY());
					attribs.add(vertices[z].getZ());
				}
			}
		}
		return toArray(attribs);
	}
	protected static float[] cpuComputeAttribs() {
		calculateViewAngles();
		ArrayList<Float> attribs = new ArrayList<Float>();
		Object3D[] objects = environment.getObjects();
		for (int x = 0; x < objects.length; x++) {
			Polygon3D[] polygons = objects[x].getPolygons();
			for (int y = 0; y < polygons.length; y++) {
				Vector3D[] vertices = polygons[y].getVertices();
				for (int z = 0; z < vertices.length; z++) {
					Vector3D vertex = vertices[z];
					float zAngle = (float) Math.atan(vertex.getZ()/vertex.getX());
					if (vertex.getX() == 0.0f && vertex.getZ() == 0.0f) {
						zAngle = 0.0f;
					}
					float mag = (float) Math.hypot(vertex.getX(), vertex.getZ());
					float xTransform = (float)(mag*SCALE*Math.cos(viewAngleX-zAngle));
					float yTransform = (float)(mag*SCALE*Math.sin(viewAngleX-zAngle)*Math.sin(viewAngleY)+vertex.getY()*SCALE*Math.cos(viewAngleY));
					if (vertex.getX() < 0.0f) {
						xTransform *= -1.0f;
						yTransform *= -1.0f;
					}
					if (vertex.getZ()*Math.cos(viewAngleX)*Math.cos(viewAngleY)+vertex.getX()*Math.sin(viewAngleX)*Math.cos(viewAngleY)-vertex.getY()*Math.sin(viewAngleY) < camDist) {
						Vector3D cam = getCameraPositionActual();
						float distance = hypot3(cam.getX()-vertex.getX(), cam.getY()-vertex.getY(), cam.getZ()-vertex.getZ());
						float theta = (float) Math.asin((Math.hypot(xTransform, yTransform)/SCALE)/distance);
						float camScale = (float)(distance*Math.cos(theta)*Math.sin(viewAngle/2.0f));
						float ptX = width/2.0f+xTransform/camScale;
						float ptY = height/2.0f-yTransform/camScale;
						ptX /= width;
						ptY /= height;
						ptX *= 2.0f;
						ptY *= 2.0f;
						ptX -= 1.0f;
						ptY -= 1.0f;
						ptY *= -1.0f;
						attribs.add(ptX);
						attribs.add(ptY);
					}
				}
			}
		}
		return toArray(attribs);
	}
	protected static float hypot3(float x, float y, float z) {
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}
	public static Vector3D getCameraPositionActual() {
		float x = (float)(Math.sin(viewAngleX)*Math.cos(viewAngleY)*camDist);
		float y = (float)-((Math.sin(viewAngleY)*camDist));
		float z = (float)(Math.cos(viewAngleX)*Math.cos(viewAngleY)*camDist);
		return new Vector3D(x, y, z);
	}
	protected static void processInput(long window) {
		if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
			glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		}
	}
	protected static void cursorPositionCallback(long window, double xpos, double ypos) {
		mouseX = (float) xpos;
		mouseY = (float) ypos;
	}
	protected static void framebufferSizeCallback(long window, int width, int height) {
		glViewport(0, 0, width, height);
		NEOEngine.width = width;
		NEOEngine.height = height;
	}
	protected static void scrollCallback(long window, double xoffset, double yoffset) {
		if (yoffset > 0) {
			camDist /= 1.2f;
		} else if (yoffset < 0) {
			camDist *= 1.2f;
		}
	}
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
	/** Terminates GLFW and stops rendering. Must be called from the main thread.
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