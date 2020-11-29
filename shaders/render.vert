#version 330 core
#define SCALE 100
layout (location = 0) in vec3 pos;
layout (location = 1) in vec4 color;
out vec3 fragPos;
out vec4 outColor;
uniform vec3 camPos;
uniform vec2 viewAngles;
uniform vec2 sinViewAngles;
uniform vec2 cosViewAngles;
uniform vec2 window;
uniform float camDist;
uniform float sinViewAngle2;
uniform float distance;
void main() {
	if (pos.z*cosViewAngles.x*cosViewAngles.y+pos.x*sinViewAngles.x*cosViewAngles.y-pos.y*sinViewAngles.y < camDist) {
		float zAngle = 0.0;
		if (pos.x != 0.0f || pos.z != 0.0f) {
			zAngle = atan(pos.z/pos.x);
		}
		float mag = sqrt(pow(pos.x, 2)+pow(pos.z, 2));
		float xTransform;
		float yTransform;
		if (pos.x < 0.0f) {
			xTransform = -mag*SCALE*cos(viewAngles.x+zAngle);
			yTransform = -mag*SCALE*sin(viewAngles.x+zAngle)*sinViewAngles.y+pos.y*SCALE*cosViewAngles.y;
		} else {
			xTransform = mag*SCALE*cos(viewAngles.x+zAngle);
			yTransform = mag*SCALE*sin(viewAngles.x+zAngle)*sinViewAngles.y+pos.y*SCALE*cosViewAngles.y;
		}
		float distance = sqrt(pow(camPos.x-pos.x, 2)+pow(camPos.y-pos.y, 2)+pow(camPos.z-pos.z, 2));
		float theta = asin((sqrt(pow(xTransform, 2)+pow(yTransform, 2))/100)/distance);
		float camScale = distance*cos(theta)*sinViewAngle2;
		float ptX = (((window.x/2.0f+xTransform/camScale)/window.x)*8.0f)-4.0f;
		float ptY = -((((window.y/2.0f-yTransform/camScale)/window.y)*8.0f)-4.0f);
		gl_Position = vec4(ptX, ptY, 0.0f, 1.0f);
	} else {
		gl_Position = vec4(0.0f, 0.0f, 0.0f, 1.0f);
	}
	outColor = color;
	fragPos = pos;
}