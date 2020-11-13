package com.alyxferrari.neo3d.exc;
public class NEO3DNotInitializedException extends NEO3DRuntimeError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "NEO3D not initialized. Call initialize() prior to calling this method.";
	public NEO3DNotInitializedException(String message) {
		super(message);
	}
}