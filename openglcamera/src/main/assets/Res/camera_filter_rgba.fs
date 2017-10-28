#extension GL_OES_EGL_image_external : require
#ifdef GL_ES
precision mediump float;
#endif


uniform vec4 U_LightAmbient;//环境光分量
uniform vec4 U_AmbientMaterial;//环境光材质
uniform vec4 U_LightPos;//光源位置
uniform vec4 U_LightDiffuse;//漫反射分量
uniform vec4 U_DiffuseMaterial;//漫反射光材质
uniform vec4 U_LigthSpecular;//镜面反射分量
uniform vec4 U_SpecularMaterial;//材质的反射系数
uniform vec4 U_CameraPos ;//镜面反射是根据视点来的，站在不同地方，看去反光点是不一样的
uniform vec4 U_LightOpt;
uniform samplerExternalOES  U_Texture;//纹理贴图
uniform sampler2D U_Texture2;
varying vec2 V_textcoord;
varying vec4 V_color;
varying vec4 V_Normal;
varying vec4 V_WorldPos;
uniform vec4 U_MultipleFilter;
//const vec3 monoMultiplier = vec3(0.299, 0.587, 0.114);
void main(){
    //增加一些滤镜效果
    // gl_FragColor =color;
    //gl_FragColor = vec4((1.0 - gl_FragColor.rgb), gl_FragColor.w);
        //gl_FragColor = V_color;
  //gl_FragColor = V_color*texture2D(U_Texture, V_textcoord);
 // gl_FragColor = texture2D(U_Texture, V_textcoord.xy);

 //黑白滤镜
  vec4 colorBase = texture2D(U_Texture, V_textcoord.xy);
  //混合之后的效果
  float blendColorR = dot(colorBase.r,U_MultipleFilter.x);
  float blendColorG = dot(colorBase.g,U_MultipleFilter.y);
  float blendColorB = dot(colorBase.b,U_MultipleFilter.z);
  float blendColorA = dot(colorBase.a,U_MultipleFilter.w);
   gl_FragColor = vec4(blendColorR, blendColorG, blendColorB, blendColorA);

}