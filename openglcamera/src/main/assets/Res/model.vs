attribute vec4 poistion;
attribute vec4 color;
attribute vec4 normal;
attribute vec2 textcoord;
uniform mat4 ModelMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ProjectionMatrix;
uniform mat4 IT_ModelMatrix;
varying vec4 V_Normal;
varying vec4 V_color;
varying vec2 V_textcoord;
varying vec4 V_WorldPos; //世界坐标
void main(){
    V_color = color;
     V_textcoord = textcoord;
     V_Normal = IT_ModelMatrix*normal;
     V_WorldPos = ModelMatrix*poistion;
    gl_Position = ProjectionMatrix*ModelMatrix*ViewMatrix*poistion;
}