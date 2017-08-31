#ifdef GL_ES
precision mediump float;
#endif


//input from vertex shader
varying vec4 vColor;
varying vec3 v_normal;

void main() {
    vec3 light_dir = normalize(vec3(0.0, -.2, -1.0));
    vec3 ambient = vec3(.1,.1,.1);
    vec3 light_amount = ambient;
    float NdotL = clamp(dot(v_normal, light_dir), 0.0, 1.0);
    light_amount += NdotL;
    light_amount = clamp(light_amount, 0.0, 1.0);
	gl_FragColor = vColor * vec4(light_amount, 1.0);
}