uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 a_color;
varying vec4 v_color;

attribute vec2 a_TexCoordinate;
varying vec2 v_TexCoordinate;

void main() {

	vec4 i_position = uMVPMatrix * vPosition;
	gl_Position = i_position;
	v_TexCoordinate = a_TexCoordinate;
 
	float diffuse = 1.0;//max(dot( i_position, vec4( -0.5, -0.5, -0.5, 1) ), 0.1);
	//v_color = vec4( 1.0, 1.0, 1.0, 1.0 );
	v_color = vec4 (a_color.r * diffuse, a_color.g * diffuse, a_color.b * diffuse, a_color.a * diffuse);
}
