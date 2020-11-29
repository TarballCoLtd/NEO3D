package com.alyxferrari.neo3d.obj;
public class DirectionalLight {
	/** Max number of lights in a single environment.
	 */
	protected static final int MAX_IN_SHADER = 8;
	/** Direction of this light;
	 */
	protected Vector3D direction;
	/** Strength of this light;
	 */
	protected float strength;
	public DirectionalLight(Vector3D direction, float strength) {
		this.direction = direction;
		this.strength = strength;
	}
	/**
	 * @return The direction of this light;
	 */
	public Vector3D getDirection() {
		return direction;
	}
	/**
	 * @return The strength of this light.
	 */
	public float getStrength() {
		return strength;
	}
}