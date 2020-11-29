package com.alyxferrari.neo3d.obj;
import java.util.*;
import java.io.*;
import com.alyxferrari.neo3d.gfx.*;
import com.mokiat.data.front.parser.*;
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
	 * @return How many polygons are in this object.
	 */
	public int getPolygonsLength() {
		return polygons.length;
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
		NEOEngine.getEnvironment().rebuildPolygons();
		return this;
	}
	/** Sets the {@code index}-th polygon in this object.
	 * @param polygon The new polygon.
	 * @param index The index of the polygon which should be overwritten.
	 * @return The Object3D on which this method was called.
	 */
	public Object3D setPolygon(Polygon3D polygon, int index) {
		if (polygons == null) {
			throw new NullPointerException("This object's polygon array is currently null.");
		}
		if (polygon == null) {
			throw new IllegalArgumentException("The polygon must not be null.");
		}
		polygons[index] = polygon;
		NEOEngine.getEnvironment().rebuildPolygons();
		return this;
	}
	public static Object3D loadFromModel(String path) throws IOException {
		return loadFromModel(new File(path), new NEOColor(0.0f));
	}
	public static Object3D loadFromModel(String path, NEOColor polygonColor) throws IOException {
		return loadFromModel(new File(path), polygonColor);
	}
	public static Object3D loadFromModel(File path) throws IOException {
		return loadFromModel(path, new NEOColor(0.0f));
	}
	public static Object3D loadFromModel(File path, NEOColor polygonColor) throws IOException {
		IOBJParser parser = new OBJParser();
		OBJModel model = parser.parse(new FileInputStream(path));
		ArrayList<Polygon3D> polygons = new ArrayList<Polygon3D>();
		for (OBJObject object : model.getObjects()) {
			for (OBJMesh mesh : object.getMeshes()) {
				for (OBJFace face : mesh.getFaces()) {
					List<OBJDataReference> references = face.getReferences();
					if (references.size() == 3) {
						OBJVertex vFirst = model.getVertex(references.get(0));
						OBJVertex vSecond = model.getVertex(references.get(1));
						OBJVertex vThird = model.getVertex(references.get(2));
						Vector3D[] vectors = {new Vector3D(vFirst.x, vFirst.y, vFirst.z), new Vector3D(vSecond.x, vSecond.y, vSecond.z), new Vector3D(vThird.x, vThird.y, vThird.z)};
						polygons.add(new Polygon3D(vectors, polygonColor));
					}
				}
			}
		}
		Polygon3D[] ret = new Polygon3D[polygons.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = polygons.get(i);
		}
		return new Object3D(ret);
	}
	public Object3D setRainbow() {
		for (int i = 0; i < polygons.length; i++) {
			polygons[i].setRainbow();
		}
		return this;
	}
}