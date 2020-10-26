package com.alyxferrari.neo3d.obj;
import java.util.*;
/** Represents an object in 3D space.
 * @author Alyx Ferrari
 * @since 1.0 alpha
 */
public class Object3D {
	/** List of polygons in this object.
	 */
	protected Polygon3D[] polygons;
	/** Constructs an object with the specified polygons.
	 * @param polygons The polygons with which to construct this object.
	 */
	public Object3D(Polygon3D[] polygons) {
		if (polygons == null) {
			throw new IllegalArgumentException("The polygons must not be null.");
		}
		for (int i = 0; i < polygons.length; i++) {
			if (polygons[i] == null) {
				throw new IllegalArgumentException("The polygons must not be null.");
			}
		}
		this.polygons = polygons;
	}
	/**
	 * @return This object's polygons.
	 */
	public Polygon3D[] getPolygons() {
		return Arrays.copyOf(polygons, polygons.length);
	}
	/**
	 * @param index The index of the desired polygon.
	 * @return The {@code index}-th polygon in this object.
	 */
	public Polygon3D getPolygon(int index) {
		return polygons[index];
	}
	/** Sets this object's polygons.
	 * @param polygons The new polygons.
	 * @return The Object3D on which this method was called.
	 */
	public Object3D setPolygons(Polygon3D[] polygons) {
		if (polygons == null) {
			throw new IllegalArgumentException("The polygons must not be null.");
		}
		for (int i = 0; i < polygons.length; i++) {
			if (polygons[i] == null) {
				throw new IllegalArgumentException("The polygons must not be null.");
			}
		}
		this.polygons = polygons;
		return this;
	}
	/** Sets the {@code index}-th polygon in this object.
	 * @param polygon The new polygon.
	 * @param index The index of the polygon which should be overwritten.
	 * @return The Object3D on which this method was called.
	 */
	public Object3D setPolygon(Polygon3D polygon, int index) {
		if (polygon == null) {
			throw new IllegalArgumentException("The polygon must not be null.");
		}
		polygons[index] = polygon;
		return this;
	}
}