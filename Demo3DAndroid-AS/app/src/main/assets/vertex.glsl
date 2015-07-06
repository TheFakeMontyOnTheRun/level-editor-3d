uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 a_color;
varying vec4 v_color;

void main() {
	gl_Position = uMVPMatrix * vPosition;;
	v_color = a_color;
}
