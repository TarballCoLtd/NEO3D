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
	protected static Environment3D environment = null;
	protected static final String WINDOW_SUFFIX = "powered by " + NEO3D.LIB_NAME + " " + NEO3D.LIB_VERSION;
	protected static long window = NULL;
	
	protected static float camDist = 3.0f;
	protected static float viewAngle = (float) Math.toRadians(80);
	protected static float viewAngleX = 0.0f;
	protected static float viewAngleY = 0.0f;
	protected static float mouseX = 0.0f;
	protected static float mouseY = 0.0f;
	protected static int width = 0;
	protected static int height = 0;
	
	protected static final float SENSITIVITY = 100.0f;
	protected static final float SCALE = 100.0f;
	private NEOEngine() {}
	public static void initialize() throws IOException {
		initialize(new Environment3D(), ComputeDevice.CPU, null, new Dimension(800, 600));
	}
	public static void initialize(Environment3D environment, ComputeDevice device, String title, Dimension size) throws IOException {
		if (window == NULL) {
			if (glfwInit()) {
				glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
				window = glfwCreateWindow((int)size.getWidth(), (int)size.getHeight(), title == null ? WINDOW_SUFFIX : title + " -- " + WINDOW_SUFFIX, NULL, NULL); // is the title null? if so, don't add the dashes
				if (window != NULL) {
					width = (int) size.getWidth();
					height = (int) size.getHeight();
					//glfwSetCursorPosCallback(window, NEOEngine::cursorPositionCallback);
					glfwMakeContextCurrent(window);
					GL.createCapabilities();
					Shaders.cpu = ShaderUtils.createProgram(new File("shaders/cpu/cpurender.vert"), new File("shaders/cpu/cpurender.frag"));
					glfwMakeContextCurrent(NULL);
					NEOEngine.environment = environment;
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
	/** Starts rendering the current environment.
	 */
	public static void startRender() {
		if (window != NULL) {
			glfwMakeContextCurrent(window);
			GL.createCapabilities();
			glViewport(0, 0, width, height);
			glfwSetFramebufferSizeCallback(window, NEOEngine::framebufferSizeCallback);
			int vbo = glGenBuffers();
			int vao = glGenVertexArrays();
			float[] attribs = cpuComputeAttribs();
			glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, attribs, GL_DYNAMIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * SizeOf.FLOAT, 0);
			glEnableVertexAttribArray(0);
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glfwShowWindow(window);
			while (!glfwWindowShouldClose(window)) {
				processInput(window);
				glClear(GL_COLOR_BUFFER_BIT);
				float[] data = cpuComputeAttribs();
				glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW);
				glUseProgram(Shaders.cpu);
				glDrawArrays(GL_TRIANGLES, 0, 3);
				glfwSwapBuffers(window);
				glfwPollEvents();
			}
			terminate();
			return;
		}
		throw new NEO3DNotInitializedException(NEO3DNotInitializedException.RECOMMENDED_MESSAGE);
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
		if (Math.abs((mouseY-height)/2) > Math.PI/2*SENSITIVITY) {
			if (viewAngleY < 0) {
				viewAngleY = (float)(-Math.PI/2*SENSITIVITY);
			} else {
				viewAngleY = (float)(Math.PI/2*SENSITIVITY);
			}
		}
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
					float xTransform = 0.0f;
					float yTransform = 0.0f;
					if (vertex.getX() < 0.0f) {
						xTransform = (float)(-mag*SCALE*Math.cos(viewAngleX-zAngle));
						yTransform = (float)(-mag*SCALE*Math.sin(viewAngleX-zAngle)*Math.sin(viewAngleY)+vertex.getY()*SCALE*Math.cos(viewAngleY));
					} else {
						xTransform = (float)(mag*SCALE*Math.cos(viewAngleX-zAngle));
						yTransform = (float)(mag*SCALE*Math.sin(viewAngleX-zAngle)*Math.sin(viewAngleY)+vertex.getY()*SCALE*Math.cos(viewAngleY));
					}
					if (vertex.getZ()*Math.cos(viewAngleX)*Math.cos(viewAngleY)+vertex.getX()*Math.sin(viewAngleX)*Math.cos(viewAngleY)-vertex.getY()*Math.sin(viewAngleY) < camDist) {
						Vector3D cam = getCameraPositionActual();
						float distance = hypot3(cam.getX()-vertex.getX(), cam.getY()-vertex.getY(), cam.getZ()-vertex.getZ());
						float theta = (float) Math.asin((Math.hypot(xTransform, yTransform)/SCALE)/distance);
						float camScale = (float)(distance*Math.cos(theta)*Math.sin(viewAngle/2.0f));
						float ptX = width/2.0f+xTransform/camScale;
						float ptY = height/2.0f-yTransform/camScale;
						ptX = ptX / width;
						ptY = ptY / height;
						ptX *= 2.0f;
						ptY *= 2.0f;
						ptX -= 1.0f;
						ptY -= 1.0f;
						ptY *= -1.0f;
						attribs.add(ptX);
						attribs.add(ptY);
						attribs.add(0.0f);
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
			glfwSetWindowShouldClose(window, true);
		}
		if (glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}
		if (glfwGetKey(window, GLFW_KEY_F2) == GLFW_PRESS) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
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