package com.alyxferrari.neo3d.obj;
import com.alyxferrari.neo3d.exc.*;
public class NEOColor {
	protected float red;
	protected float green;
	protected float blue;
	protected float alpha;
	public NEOColor(float value) {
		this(value, value, value);
	}
	public NEOColor(float red, float green, float blue) {
		this(red, green, blue, 1.0f);
	}
	public NEOColor(float red, float green, float blue, float alpha) {
		if (red >= 0.0f && red <= 1.0f && green >= 0.0f && green <= 1.0f && blue >= 0.0f && blue <= 1.0f && alpha >= 0.0f && alpha <= 1.0f) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.alpha = alpha;
			return;
		}
		throw new ColorOutOfBoundsException(ColorOutOfBoundsException.RECOMMENDED_MESSAGE);
	}
	public float getRed() {
		return red;
	}
	public float getGreen() {
		return green;
	}
	public float getBlue() {
		return blue;
	}
	public float getAlpha() {
		return alpha;
	}
	public NEOColor setRed(float red) {
		if (red < 0.0f || red > 1.0f) {
			throw new ColorOutOfBoundsException(ColorOutOfBoundsException.RECOMMENDED_MESSAGE);
		}
		this.red = red;
		return this;
	}
	public NEOColor setGreen(float green) {
		if (green < 0.0f || green > 1.0f) {
			throw new ColorOutOfBoundsException(ColorOutOfBoundsException.RECOMMENDED_MESSAGE);
		}
		this.green = green;
		return this;
	}
	public NEOColor setBlue(float blue) {
		if (blue < 0.0f || blue > 1.0f) {
			throw new ColorOutOfBoundsException(ColorOutOfBoundsException.RECOMMENDED_MESSAGE);
		}
		this.blue = blue;
		return this;
	}
	public NEOColor setAlpha(float alpha) {
		if (alpha < 0.0f || alpha > 1.0f) {
			throw new ColorOutOfBoundsException(ColorOutOfBoundsException.RECOMMENDED_MESSAGE);
		}
		this.alpha = alpha;
		return this;
	}
}