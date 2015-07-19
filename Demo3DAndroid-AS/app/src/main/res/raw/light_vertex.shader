uniform mat4 u_MVP;
attribute vec4 a_Position;
attribute vec4 a_Color;
varying vec4 v_Color;
varying float v_distance;

void main() {
   v_Color = a_Color;
   gl_Position = u_MVP * a_Position;
   v_distance = 1.0 - ( sqrt(  gl_Position.x * gl_Position.x + gl_Position.y * gl_Position.y + gl_Position.z * gl_Position.z ) / 255.0 );
}
