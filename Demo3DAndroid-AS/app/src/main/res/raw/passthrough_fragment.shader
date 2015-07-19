precision mediump float;
varying vec4 v_Color;
varying float v_distance;

void main() {
    gl_FragColor = v_Color;
    gl_FragColor.rgb *= v_distance;
}
