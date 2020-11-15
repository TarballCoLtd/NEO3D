package com.alyxferrari.neo3d.gfx;
class ViewAngle {
	float viewAngleX;
	float viewAngleY;
	float sinViewAngleX;
	float sinViewAngleY;
	float cosViewAngleX;
	float cosViewAngleY;
	public ViewAngle(float viewAngleX, float viewAngleY) {
		this.viewAngleX = viewAngleX;
		this.viewAngleY = viewAngleY;
		sinViewAngleX = (float) Math.sin(viewAngleX);
		sinViewAngleY = (float) Math.sin(viewAngleY);
		cosViewAngleX = (float) Math.cos(viewAngleX);
		cosViewAngleY = (float) Math.cos(viewAngleY);
	}
}