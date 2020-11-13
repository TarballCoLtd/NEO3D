package com.alyxferrari.neo3d.exc;
public class GLFWWindowCreationError extends GLFWError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "GLFW window creation failed.";
	public GLFWWindowCreationError(String message) {
		super(message);
	}
}