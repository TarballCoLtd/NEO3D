package com.alyxferrari.neo3d.exc;
public class VertexShaderCompilationError extends ShaderCompilationError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "Vertex shader compilation failed.";
	public VertexShaderCompilationError(String message, String glError) {
		super(message, glError);
	}
}