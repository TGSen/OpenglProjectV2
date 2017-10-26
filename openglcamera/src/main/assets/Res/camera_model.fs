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
const vec3 monoMultiplier = vec3(0.299, 0.587, 0.114);
void main(){
    vec4 color = vec4(0.0f,0.0f,0.0f,0.0f);
      //--计算环境光
    vec4 ambientColor = U_LightAmbient*U_AmbientMaterial;
    ////----计算漫反射
    vec3 lightPos = U_LightPos.xyz;
    vec3 L = lightPos;
    //入射光线单位化
    L = normalize(L);
    ////法向量单位化
    vec3 n = normalize(V_Normal.xyz);
    float deffuseIntensity = max(0.0,dot(L,n)); //dot 向量L,n之间的点积
   //  float deffuseIntensity = dot(L,n);
     //计算最终漫反射光
    vec4 diffuseColor = U_LightDiffuse *U_DiffuseMaterial*deffuseIntensity;
    //----end
    //定义镜面反射光为黑色
    vec4 specularColor = vec4(0.0,0.0,0.0,0.0);
    if(deffuseIntensity!=0.0){
    //在漫反射光不为0的情况下，可能才具备镜面反射光
       vec3 relectDir = normalize(reflect(-L,n));
       vec3 viewDir = normalize(U_CameraPos.xyz- V_WorldPos.xyz);
       specularColor = U_LigthSpecular*U_SpecularMaterial*pow(max(0.0,dot(viewDir,relectDir)),U_LightOpt.x);
    }

    //设置光照和贴图混合
    if(U_LightOpt.w==1.0){
        color = ambientColor+diffuseColor*texture2D(U_Texture,V_textcoord.xy)+specularColor;
    }else{
        color = ambientColor+diffuseColor*texture2D(U_Texture,V_textcoord.xy);
    }
    //增加一些滤镜效果
    // gl_FragColor =color;
    //gl_FragColor = vec4((1.0 - gl_FragColor.rgb), gl_FragColor.w);
        //gl_FragColor = V_color;
  //gl_FragColor = V_color*texture2D(U_Texture, V_textcoord);
 // gl_FragColor = texture2D(U_Texture, V_textcoord.xy);
  vec4 colorBase = texture2D(U_Texture, V_textcoord.xy);
  float monoColor = dot(colorBase.rgb,monoMultiplier);
   gl_FragColor = vec4(monoColor, monoColor, monoColor, 1.0);

}