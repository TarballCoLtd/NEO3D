package com.alyxferrari.neo3d.obj;
public class ObjectTemplate {
	private ObjectTemplate() {}
	public static Object3D getPyramid() {
		Vector3D[] tri1 = {new Vector3D(-1.0f, -1.0f, -1.0f), new Vector3D(1.0f, -1.0f, -1.0f), new Vector3D(-1.0f, -1.0f, 1.0f)};
		Vector3D[] tri2 = {new Vector3D(-1.0f, -1.0f, 1.0f), new Vector3D(1.0f, -1.0f, -1.0f), new Vector3D(1.0f, -1.0f, 1.0f)};
		Vector3D[] tri3 = {new Vector3D(-1.0f, -1.0f, 1.0f), new Vector3D(-1.0f, -1.0f, -1.0f), new Vector3D(0.0f, 1.0f, 0.0f)};
		Vector3D[] tri4 = {new Vector3D(-1.0f, -1.0f, -1.0f), new Vector3D(1.0f, -1.0f, -1.0f), new Vector3D(0.0f, 1.0f, 0.0f)};
		Vector3D[] tri5 = {new Vector3D(1.0f, -1.0f, -1.0f), new Vector3D(1.0f, -1.0f, 1.0f), new Vector3D(0.0f, 1.0f, 0.0f)};
		Vector3D[] tri6 = {new Vector3D(-1.0f, -1.0f, 1.0f), new Vector3D(1.0f, -1.0f, 1.0f), new Vector3D(0.0f, 1.0f, 0.0f)};
		Polygon3D[] polygons = {new Polygon3D(tri1).setRainbow(), new Polygon3D(tri2).setRainbow(), new Polygon3D(tri3).setRainbow(), new Polygon3D(tri4).setRainbow(), new Polygon3D(tri5).setRainbow(), new Polygon3D(tri6).setRainbow()};
		return new Object3D(polygons);
	}
}