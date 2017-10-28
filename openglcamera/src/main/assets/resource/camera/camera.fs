#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES U_Texture;
varying vec2 V_textcoord;
void main()
{
  vec4 vCameraColor = texture2D(U_Texture, V_textcoord);
  float fGrayColor = (0.3*vCameraColor.r + 0.59*vCameraColor.g + 0.11*vCameraColor.b);
  gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);
}
