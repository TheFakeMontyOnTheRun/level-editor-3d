uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 a_color;
varying vec4 v_color;
varying float v_distance;

void main() {
	gl_Position = uMVPMatrix * vPosition;;
	v_distance = 1.0 - ( sqrt( ( gl_Position.x * gl_Position.x + gl_Position.y * gl_Position.y + gl_Position.z * gl_Position.z ) ) / 255.0 );
	v_color = a_color;
}
