package com.alyxferrari.neo3d.obj;
import java.util.*;
import java.nio.*;
import com.alyxferrari.neo3d.gfx.*;
import static org.lwjgl.opengl.GL46.*;
/** Represents a 3D environment.
 * @author Alyx Ferrari
 * @since 1.0 alpha
 */
public class Environment3D {
	/** List of objects in this environment.
	 */
	protected Object3D[] objects;
	/** List of lights in this environment.
	 */
	protected DirectionalLight[] lights;
	/** Used internally. Part of NEO3D's face sorting algorithm.
	 */
	protected Polygon3D[] polygons;
	/** Ambient lighting.
	 */
	protected float ambience;
	public Environment3D() {
		this.objects = null;
	}
	/** Constructs a new environment with the specified objects.
	 * @param objects The objects with which to construct this environment.
	 */
	public Environment3D(Object3D[] objects) {
		this(objects, new DirectionalLight[0], 1.0f);
	}
	public Environment3D(Object3D[] objects, DirectionalLight[] lights, float ambience) {
		if (objects == null) {
			throw new IllegalArgumentException("The objects must not be null.");
		}
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				throw new IllegalArgumentException("The objects must not be null.");
			}
		}
		this.objects = objects;
		if (lights == null) {
			throw new IllegalArgumentException("The lights must not be null.");
		}
		if (lights.length > DirectionalLight.MAX_IN_SHADER) {
			throw new IllegalArgumentException("The maximum number of directional lights is " + DirectionalLight.MAX_IN_SHADER + ".");
		}
		for (int i = 0; i < lights.length; i++) {
			if (lights[i] == null) {
				throw new IllegalArgumentException("The lights must not be null.");
			}
		}
		this.lights = lights;
		this.ambience = ambience;
		rebuildPolygons();
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
		rebuildPolygons();
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
		rebuildPolygons();
		return this;
	}
	/** Used internally. Part of NEO3D's face sorting algorithm.
	 */
	public Polygon3D[] getPolygons() {
		return polygons;
	}
	/**
	 * @return How many lights are in this environment.
	 */
	public int getLightsLength() {
		return lights.length;
	}
	protected void rebuildPolygons() {
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
	public void rebuildLights() {
		float[] xs = new float[DirectionalLight.MAX_IN_SHADER];
		float[] ys = new float[DirectionalLight.MAX_IN_SHADER];
		float[] zs = new float[DirectionalLight.MAX_IN_SHADER];
		float[] strengths = new float[DirectionalLight.MAX_IN_SHADER];
		for (int i = 0; i < lights.length; i++) {
			xs[i] = lights[i].getDirection().x;
			ys[i] = lights[i].getDirection().y;
			zs[i] = lights[i].getDirection().z;
			strengths[i] = lights[i].getStrength();
		}
		glUniform1fv(glGetUniformLocation(NEOEngine.getShader(), "lightXs"), FloatBuffer.wrap(xs));
		glUniform1fv(glGetUniformLocation(NEOEngine.getShader(), "lightYs"), FloatBuffer.wrap(ys));
		glUniform1fv(glGetUniformLocation(NEOEngine.getShader(), "lightZs"), FloatBuffer.wrap(zs));
		glUniform1fv(glGetUniformLocation(NEOEngine.getShader(), "strengths"), FloatBuffer.wrap(strengths));
		glUniform1f(glGetUniformLocation(NEOEngine.getShader(), "ambience"), ambience);
		glUniform1i(glGetUniformLocation(NEOEngine.getShader(), "count"), lights.length);
	}
}