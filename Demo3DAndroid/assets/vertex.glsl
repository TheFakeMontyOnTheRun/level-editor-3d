// This matrix member variable provides a hook to manipulate
// the coordinates of the objects that use this vertex shader
uniform mat4 uMVPMatrix;
	        
attribute vec4 vPosition;
attribute vec4 a_color;
varying vec4 v_color;
void main() {

	// the matrix must be included as a modifier of gl_Position
	gl_Position = uMVPMatrix * vPosition;
	v_color = vec4 (a_color.r, a_color.g, a_color.b, a_color.a);
}
