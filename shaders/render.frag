#version 330 core
layout (location = 2) in vec3 adjacent1;
layout (location = 3) in vec3 adjacent2;
out vec4 Color;
in vec3 fragPos;
in vec4 outColor;
uniform float lightXs[8];
uniform float lightYs[8];
uniform float lightZs[8];
uniform float strengths[8];
uniform int count;
uniform float ambience;
vec3 customCross(in vec3 cross1, in vec3 cross2, in vec3 cross3) {
	vec3 two = vec3(cross2.x-cross1.x, cross2.y-cross1.y, cross2.z-cross1.z);
	vec3 three = vec3(cross3.x-cross1.x, cross3.y-cross1.y, cross3.z-cross1.z);
	return normalize(cross(two, three));
}
void main() {
	if (count == 0) {
		Color = outColor;
		return;
	}
	vec3 finalColor = vec3(0.0);
	for (int i = 0; i < count; i++) {
		vec3 light = vec3(lightXs[i], lightYs[i], lightZs[i]);
		float dot = dot(customCross(fragPos, adjacent1, adjacent2), light)*strengths[i];
		if (dot > 0.0) {
			vec3 col = vec3((outColor.r*dot)+(outColor.r*ambience), (outColor.g*dot)+(outColor.g*ambience), (outColor.b*dot)+(outColor.b*ambience));
			col.r = col.r < 512.0 ? 512.0 : col.r;
			col.g = col.g < 512.0 ? 512.0 : col.g;
			col.b = col.b < 512.0 ? 512.0 : col.b;
			col /= 512.0;
			finalColor.r = col.r > finalColor.r ? col.r : finalColor.r;
			finalColor.g = col.g > finalColor.g ? col.g : finalColor.g;
			finalColor.b = col.b > finalColor.b ? col.b : finalColor.b;
		}
	}
	Color = vec4(finalColor, outColor.a);
}
