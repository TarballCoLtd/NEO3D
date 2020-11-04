package com.alyxferrari.neo3d.example;
import com.alyxferrari.neo3d.gfx.*;
import com.alyxferrari.neo3d.obj.*;
import java.io.*;
public class NEOExample {
	public static void main(String[] args) throws IOException {
		Vector3D[] vertices = {new Vector3D(0.0f, 0.0f, 0.0f), new Vector3D(0.5f, 1.0f, 0.0f), new Vector3D(1.0f, 0.0f, 1.0f)};
		Polygon3D[] polygons = {new Polygon3D(vertices)};
		Object3D[] objects = {new Object3D(polygons)};
		Environment3D environment = new Environment3D(objects);
		NEOEngine.initialize(environment, System.err);
	}
}