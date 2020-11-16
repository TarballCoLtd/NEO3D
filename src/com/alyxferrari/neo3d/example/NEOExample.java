package com.alyxferrari.neo3d.example;
import com.alyxferrari.neo3d.*;
import com.alyxferrari.neo3d.gfx.*;
import com.alyxferrari.neo3d.obj.*;
import java.awt.*;
import java.io.*;
public class NEOExample {
	public static void main(String[] args) throws IOException {
		//Vector3D[] vertices = {new Vector3D(-1.0f, 0.0f, -1.0f, new NEOColor(1.0f, 0.0f, 0.0f)), new Vector3D(1.0f, 0.0f, -1.0f, new NEOColor(0.0f, 1.0f, 0.0f)), new Vector3D(0.0f, 0.0f, 1.0f, new NEOColor(0.0f, 0.0f, 1.0f))};
		//Vector3D[] vertices2 = {new Vector3D(-1.0f, 1.0f, -1.0f, new NEOColor(1.0f, 0.0f, 0.0f)), new Vector3D(1.0f, 1.0f, -1.0f, new NEOColor(0.0f, 1.0f, 0.0f)), new Vector3D(0.0f, 1.0f, 1.0f, new NEOColor(0.0f, 0.0f, 1.0f))};
		//Polygon3D[] polygons = {new Polygon3D(vertices), new Polygon3D(vertices2)};
		//Object3D[] objects = {new Object3D(polygons)};
		Object3D[] objects = {Object3D.loadFromModel("C:/cat.obj", new NEOColor(1.0f, 0.4f, 0.4f))};
		Environment3D environment = new Environment3D(objects);
		NEOEngine.initialize(environment, ComputeDevice.GPU, "NEO3D example", new Dimension(800, 600));
		NEOEngine.startRender();
	}
}