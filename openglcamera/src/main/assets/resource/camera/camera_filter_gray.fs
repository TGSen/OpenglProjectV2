#extension GL_OES_EGL_image_external : require
#ifdef GL_ES
precision mediump float;
#endif


uniform samplerExternalOES  U_Texture;//纹理贴图
varying vec2 V_textcoord;
varying vec4 V_color;
varying vec4 V_Normal;
varying vec4 V_WorldPos;
uniform vec4 U_MultipleFilter;
void main(){
    //增加一些滤镜效果
    // gl_FragColor =color;
    //gl_FragColor = vec4((1.0 - gl_FragColor.rgb), gl_FragColor.w);
        //gl_FragColor = V_color;
  //gl_FragColor = V_color*texture2D(U_Texture, V_textcoord);
 // gl_FragColor = texture2D(U_Texture, V_textcoord.xy);

 //黑白滤镜
   //黑白滤镜
    vec4 colorBase = texture2D(U_Texture, V_textcoord.xy);
    //混合之后的效果
    //这个有点模糊了，不应该将透明度加上的，欧了
   // float blendColor = (colorBase.r+colorBase.g+colorBase.b+colorBase.a)/4.0f;
    float blendColor = (colorBase.r+colorBase.g+colorBase.b)/3.0;
     gl_FragColor = vec4(blendColor, blendColor, blendColor, 1.0);

}