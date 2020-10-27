package com.alyxferrari.neo3d.gfx;
import org.lwjgl.glfw.*;
import com.alyxferrari.neo3d.obj.*;
import java.io.*;
public class Renderer {
	protected static Environment3D environment = null;
	protected boolean init = false;
	private Renderer() {}
	public static boolean initialize() {
		
	}
	public static boolean initialize(Environment3D environment) {
		
	}
	public static boolean initialize(Environment3D environment, PrintStream errorCallback) {
		GLFWErrorCallback.createPrint(errorCallback);
		initialize(environment);
	}
	public static void setEnvironment(Environment3D environment) {
		if (environment == null) {
			throw new IllegalArgumentException("The environment must not be null.");
		}
		Renderer.environment = environment;
	}
	public static Environment3D getEnvironment() {
		return environment;
	}
	public static void setErrorCallback(PrintStream errorCallback) {
		GLFWErrorCallback.createPrint(errorCallback);
	}
}