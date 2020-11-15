package com.alyxferrari.neo3d.obj;
/** Represents a point in 3D space.
 * @author Alyx Ferrari
 * @since 1.0 alpha
 */
public class Vector3D {
	/** This vector's x value.
	 */
	protected float x;
	/** This vector's y value.
	 */
	protected float y;
	/** This vector's z value.
	 */
	protected float z;
	/** This vector's color.
	 */
	protected NEOColor color;
	/** Constructs a new vector at the origin.
	 */
	public Vector3D() {
		this(0.0f, 0.0f, 0.0f);
	}
	/** Constructs a new vector at (position, position, position) in 3D space.
	 * @param position The x, y, and z position for this vector.
	 */
	public Vector3D(float position) {
		this(position, position, position);
	}
	/** Constructs a new vector at (x, y, z) in 3D space.
	 * @param x The x position.
	 * @param y The y position.
	 * @param z The z position.
	 */
	public Vector3D(float x, float y, float z) {
		this(x, y, z, new NEOColor(0.0f));
	}
	/** Constructs a new vector at (x, y, z) in 3D space with the specified color.
	 * @param x The x position.
	 * @param y The y position.
	 * @param z The z position.
	 * @param color The color.
	 */
	public Vector3D(float x, float y, float z, NEOColor color) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
	}
	/**
	 * @return This vector's x value.
	 */
	public float getX() {
		return x;
	}
	/**
	 * @return This vector's y value.
	 */
	public float getY() {
		return y;
	}
	/**
	 * @return This vector's z value.
	 */
	public float getZ() {
		return z;
	}
	/** Sets this vector's x value.
	 * @param x The vector's new x value.
	 * @return The Vector3D on which this method was called.
	 */
	public Vector3D setX(float x) {
		this.x = x;
		return this;
	}
	/** Sets this vector's y value.
	 * @param y The vector's new y value.
	 * @return The Vector3D on which this method was called.
	 */
	public Vector3D setY(float y) {
		this.y = y;
		return this;
	}
	/** Sets this vector's z value.
	 * @param z The vector's new z value.
	 * @return The Vector3D on which this method was called.
	 */
	public Vector3D setZ(float z) {
		this.z = z;
		return this;
	}
	/**
	 * @return This vector's color.
	 */
	public NEOColor getColor() {
		return color;
	}
	/** Sets this vector's color.
	 * @param color The vector's new color.
	 * @return The Vector3D on which this method was called.
	 */
	public Vector3D setColor(NEOColor color) {
		this.color = color;
		return this;
	}
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}