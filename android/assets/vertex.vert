//our attributes
attribute vec3 a_position;
attribute vec4 a_color;
attribute vec3 a_normal;

//our camera matrix
uniform mat4 u_projTrans;

uniform mat4 u_worldTrans;

//send the color out to the fragment shader
varying vec4 vColor;
varying vec3 v_normal;

void main() {
	vColor = a_color;
	gl_Position = u_projTrans * u_worldTrans * vec4(a_position.xyz, 1.0) ;
	v_normal = normalize((u_worldTrans * vec4(a_normal, 0.0)).xyz);
}