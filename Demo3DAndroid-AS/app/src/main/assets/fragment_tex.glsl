precision mediump float;
varying vec4 v_color;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main() {
	//diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));
	//diffuse = diffuse + 0.3;
	gl_FragColor.rgba = ( v_color * texture2D(u_Texture, v_TexCoordinate) ).rgba;
	//gl_FragColor = vec4( v_color.x, v_color.y, v_color.z, 1.0 );
}
