precision mediump float;
varying vec4 v_color;
varying float v_distance;

void main() {
	gl_FragColor = v_color;
	gl_FragColor.rgb *= v_distance;
}
