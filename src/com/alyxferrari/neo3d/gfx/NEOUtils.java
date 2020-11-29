package com.alyxferrari.neo3d.gfx;
public class NEOUtils {
	public static final float PI = (float) Math.PI;
	private NEOUtils() {}
	public static float hypot3(float x, float y, float z) {
		return (float) Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
}