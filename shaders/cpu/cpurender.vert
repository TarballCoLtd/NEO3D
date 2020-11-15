#version 330 core
layout (location = 0) in vec2 pos;
layout (location = 1) in vec4 color;
uniform vec2 window;
out vec4 outColor;
void main() {
	float ptX = ((pos.x/window.x)*2.0f)-1.0f;
	float ptY = -(((pos.y/window.y)*2.0f)-1.0f);
	gl_Position = vec4(ptX, ptY, 0.0, 1.0);
	outColor = color;
}