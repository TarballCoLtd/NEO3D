package com.alyxferrari.neo3d.exc;
public class ShaderCompilationError extends NEO3DRuntimeError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "Shader compilation failed.";
	protected String glError;
	protected ShaderCompilationError(String message, String glError) {
		super(message);
		this.glError = glError;
	}
	public String getOpenGLErrorLog() {
		return glError;
	}
}