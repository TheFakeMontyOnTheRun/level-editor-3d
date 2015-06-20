uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 a_color;
varying vec4 v_color;

void main() {

	vec4 i_position = uMVPMatrix * vPosition;
	gl_Position = i_position;

	float diffuse = 1.0;

	v_color = vec4 (a_color.r * diffuse, a_color.g * diffuse, a_color.b * diffuse, a_color.a * diffuse);
}
