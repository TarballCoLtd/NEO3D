package com.alyxferrari.neo3d.exc;
public class ShaderCompilationError extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "Shader compilation failed.";
	protected String glError;
	public ShaderCompilationError(String message, String glError) {
		super(message);
		this.glError = glError;
	}
	public String getOpenGLErrorLog() {
		return glError;
	}
}