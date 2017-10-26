#ifdef GL_ES
precision mediump float;
#endif

varying vec4 V_color;
uniform sampler2D U_Texture;
uniform sampler2D U_Texture2;
varying vec2 V_textcoord;
uniform vec4 U_LightPos;
uniform vec4 U_LightAmbient;//环境光的分量
uniform vec4 U_AmbientMaterial;//材质环境光反射系数
uniform vec4 U_LightDiffuse;//漫反射分量
uniform vec4 U_DiffuseMaterial;//材质漫反射系数
varying vec3 V_Normal;
varying vec3 V_WorldPos;
void main(){
    vec4 color = vec4(0.0,0.0,0.0,0.0);
    //点光源设置
    //受光点离光源的距离
    float distances = 0.0;
    float constantFactor = 1.0;//常数衰减系数
    float linearFactor = 0.0;//线性衰减系数
    float quadricFactor = 0.0;//平方衰减系数
    vec3 L = U_LightPos.xyz - V_WorldPos;
    distances =length(L);
    float attenuation = 1.0/(constantFactor+linearFactor*distances+quadricFactor*quadricFactor*distances);
    vec4 ambientColor =U_LightAmbient *U_AmbientMaterial;
    vec3 lightPos = U_LightPos.xyz;

    //vec3 L = lightPos;
    L = normalize(L);
    vec3 n = normalize(V_Normal.xyz);
    float diffuseIntensity = max(0.0,dot(L,n));
    vec4 diffuseColor = U_LightDiffuse* U_DiffuseMaterial*diffuseIntensity*attenuation*4.0;
    color = ambientColor+diffuseColor;
    gl_FragColor = color*V_color;
}