attribute vec4 poistion;
uniform mat4 ViewMatrix;
attribute vec2 textcoord;
attribute vec4 TextureCoordinate;
varying vec2 V_textcoord;
void main()
{
  V_textcoord = textcoord;
  gl_Position = poistion;
}