attribute vec4 poistion;
attribute vec4 color;
attribute vec4 normal;
attribute vec2 textcoord;
uniform mat4 ModelMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;
varying vec4 V_Normal;
varying vec4 V_color;
varying vec2 V_textcoord;
void main(){
    V_color = color;
     V_textcoord = textcoord;
    gl_Position = ProjectionMatrix*ModelMatrix*ViewMatrix*poistion;
}