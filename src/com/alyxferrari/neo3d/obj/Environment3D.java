package com.alyxferrari.neo3d.obj;
import java.util.*;
/** Represents a 3D environment.
 * @author Alyx Ferrari
 * @since 1.0 alpha
 */
public class Environment3D {
	/** List of objects in this environment.
	 */
	protected Object3D[] objects;
	public Environment3D() {
		this.objects = null;
	}
	/** Constructs a new environment with the specified objects.
	 * @param objects The objects with which to construct this environment.
	 */
	public Environment3D(Object3D[] objects) {
		if (objects == null) {
			throw new IllegalArgumentException("The objects must not be null.");
		}
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				throw new IllegalArgumentException("The objects must not be null.");
			}
		}
		this.objects = objects;
	}
	/**
	 * @return This environment's objects.
	 */
	public Object3D[] getObjects() {
		if (objects == null) {
			return null;
		}
		return Arrays.copyOf(objects, objects.length);
	}
	/**
	 * @param index The index of the desired object.
	 * @return The {@code index}-th object in this environment.
	 */
	public Object3D getObject(int index) {
		if (objects == null) {
			return null;
		}
		return objects[index];
	}
	/** Sets this environment's object.
	 * @param objects The new objects.
	 * @return The Environment on which this method was called.
	 */
	public Environment3D setObjects(Object3D[] objects) {
		if (objects == null) {
			throw new IllegalArgumentException("The objects must not be null.");
		}
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				throw new IllegalArgumentException("The objects must not be null.");
			}
		}
		this.objects = objects;
		return this;
	}
	/** Sets the {@code index}-th object in this environment.
	 * @param object The new object.
	 * @param index The index of the object which should be overwritten.
	 * @return The Environment on which this method was called.
	 */
	public Environment3D setObject(Object3D object, int index) {
		if (objects == null) {
			throw new NullPointerException("This environment's object array is currently null.");
		}
		if (object == null) {
			throw new IllegalArgumentException("The object must not be null.");
		}
		objects[index] = object;
		return this;
	}
}