package com.alyxferrari.neo3d.obj;
import java.util.*;
/** Represents a polygon in 3D space.
 * @author Alyx Ferrari
 * @since 1.0 alpha
 */
public class Polygon3D {
	/** List of vertices in this polygon.
	 */
	protected Vector3D[] vertices;
	/** Constructs a new polygon containing the specified vertices.
	 * @param vertices The vertices with which to construct this polygon.
	 */
	public Polygon3D(Vector3D[] vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Vertices must not be null.");
		}
		if (vertices.length != 3) {
			throw new IllegalArgumentException("In this version of NEO3D, only triangles are supported.");
		}
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] == null) {
				throw new IllegalArgumentException("Vertices must not be null.");
			}
		}
		this.vertices = vertices;
	}
	/**
	 * @return This polygon's vertices.
	 */
	public Vector3D[] getVertices() {
		return Arrays.copyOf(vertices, vertices.length);
	}
	/**
	 * @param index The vertex to return.
	 * @return The {@code index}-th vertex of this polygon.
	 */
	public Vector3D getVertex(int index) {
		return vertices[index];
	}
	/** Sets this polygon's vertices
	 * @param vertices This polygon's new vertices.
	 * @return The Polygon3D on which this method was called.
	 */
	public Polygon3D setVertices(Vector3D[] vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("Vertices must not be null.");
		}
		if (vertices.length != 3) {
			throw new IllegalArgumentException("In this version of NEO3D, only triangles are supported.");
		}
		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i] == null) {
				throw new IllegalArgumentException("Vertices must not be null.");
			}
		}
		return this;
	}
	/** Sets the {@code index}-th vertex of this polygon to a new vertex.
	 * @param vertex This polygon's new vertex.
	 * @param index The vertex to overwrite.
	 * @return The Polygon3D on which this method was called.
	 */
	public Polygon3D setVertex(Vector3D vertex, int index) {
		if (vertex == null) {
			throw new IllegalArgumentException("The vertex must not be null.");
		}
		vertices[index] = vertex;
		return this;
	}
}