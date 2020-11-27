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
	/** Used internally. Part of NEO3D's face sorting algorithm.
	 */
	protected Polygon3D[] polygons;
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
		rebuild();
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
	 * @return How many objects are in this environment.
	 */
	public int getObjectsLength() {
		return objects.length;
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
		rebuild();
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
		rebuild();
		return this;
	}
	/** Used internally. Part of NEO3D's face sorting algorithm.
	 */
	public Polygon3D[] getPolygons() {
		return polygons;
	}
	protected void rebuild() {
		ArrayList<Polygon3D> ret = new ArrayList<Polygon3D>();
		for (int x = 0; x < objects.length; x++) {
			for (int y = 0; y < objects[x].polygons.length; y++) {
				ret.add(objects[x].polygons[y]);
			}
		}
		polygons = new Polygon3D[ret.size()];
		for (int i = 0; i < polygons.length; i++) {
			polygons[i] = ret.get(i);
		}
	}
}