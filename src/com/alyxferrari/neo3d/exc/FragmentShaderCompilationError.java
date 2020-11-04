package com.alyxferrari.neo3d.exc;
public class FragmentShaderCompilationError extends ShaderCompilationError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "Fragment shader compilation failed.";
	public FragmentShaderCompilationError(String message, String glError) {
		super(message, glError);
	}
}