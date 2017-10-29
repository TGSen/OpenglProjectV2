#extension GL_OES_EGL_image_external : require
#ifdef GL_ES
precision mediump float;
#endif
uniform samplerExternalOES  U_Texture;//纹理贴图
varying vec2 V_textcoord;
varying vec4 V_color;
void main(){
    gl_FragColor = V_color;
}