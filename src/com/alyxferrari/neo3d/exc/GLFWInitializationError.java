package com.alyxferrari.neo3d.exc;
public class GLFWInitializationError extends GLFWError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "GLFW initialization failed. Check that your graphics driver and graphics card support OpenGL.";
	public GLFWInitializationError(String message) {
		super(message);
	}
}