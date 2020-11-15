#version 330 core
layout (location = 0) in vec3 pos;
layout (location = 1) in vec4 color;
out vec4 outColor;
uniform vec2 viewAngles;
uniform vec2 sinViewAngles;
uniform vec2 cosViewAngles;
uniform vec2 window;
uniform float camDist;
uniform float sinViewAngle2;
void main() {
	float zAngle = 0.0f;
	if (pos.x != 0.0f || pos.z != 0.0f) {
		zAngle = atan(pos.z/pos.x);
	}
	float mag = sqrt(pow(pos.x, 2)+pow(pos.z, 2));
	float xTransform = mag*100*cos(viewAngles.x-zAngle);
	float yTransform = mag*100*sin(viewAngles.x-zAngle)*sinViewAngles.y+pos.y*100*cosViewAngles.y;
	if (pos.x < 0.0f) {
		xTransform *= -1.0f;
		yTransform *= -1.0f;
	}
	if (pos.z*cosViewAngles.x*cosViewAngles.y+pos.x*sinViewAngles.x*cosViewAngles.y-pos.y*sinViewAngles.y < camDist) {
		float x = sinViewAngles.x*cosViewAngles.y*camDist;
		float y = -(sinViewAngles.y*camDist);
		float z = cosViewAngles.x*cosViewAngles.y*camDist;
		float distance = sqrt(pow(x-pos.x, 2)+pow(y-pos.y, 2)+pow(z-pos.z, 2));
		float theta = asin((sqrt(pow(xTransform, 2)+pow(yTransform, 2))/100)/distance);
		float camScale = distance*cos(theta)*sinViewAngle2;
		float ptX = window.x/2.0f+xTransform/camScale;
		float ptY = window.y/2.0f-yTransform/camScale;
		ptX /= window.x;
		ptY /= window.y;
		ptX *= 2.0f;
		ptY *= 2.0f;
		ptX -= 1.0f;
		ptY -= 1.0f;
		ptY *= -1.0f;
		gl_Position = vec4(ptX, ptY, 0.0f, 1.0f);
	} else {
		gl_Position = vec4(0.0f, 0.0f, 0.0f, 1.0f);
	}
	outColor = color;
}