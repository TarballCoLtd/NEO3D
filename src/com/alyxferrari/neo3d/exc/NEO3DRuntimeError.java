package com.alyxferrari.neo3d.exc;
public class NEO3DRuntimeError extends RuntimeException {
	private static final long serialVersionUID = 1L;
	protected NEO3DRuntimeError(String message) {
		super(message);
	}
}