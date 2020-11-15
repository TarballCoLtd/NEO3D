package com.alyxferrari.neo3d.exc;
public class ColorOutOfBoundsException extends NEO3DRuntimeError {
	private static final long serialVersionUID = 1L;
	public static final String RECOMMENDED_MESSAGE = "Color value must be between 0.0 and 1.0.";
	public ColorOutOfBoundsException(String message) {
		super(message);
	}
}