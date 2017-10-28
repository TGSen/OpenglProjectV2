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
 //美白
 float lightRatio = 0.25f; //亮度
 float contrastRatio = 0.26f;//对比度

 //美白亮度增加
 float light = 1.0f*lightRatio;
 //对比度也增加
 float contrast = 1.0f+contrastRatio;
  vec4 colorBase = texture2D(U_Texture, V_textcoord.xy);
  //混合之后的效果
  float colorR = colorBase.r;
  float colorG = colorBase.g;
  float colorB = colorBase.b;
  float colorA = colorBase.a;
   //美白,对rgb 处理
   float tempColorR =colorR+light;
   float tempColorG =colorG+light;
   float tempColorB =colorB+light;

   //颜色在0-1 之间
   if(tempColorR > 1.0f){
        colorR = 1.0f;
   }else if(tempColorR < 0.0f){
        colorR = 0.0f;
   }else{
        colorR = tempColorR;
   }

   if(tempColorG > 1.0f){
           colorG = 1.0f;
      }else if(tempColorG < 0.0f){
           colorG = 0.0f;
      }else{
           colorG = tempColorG;
      }

      if(tempColorB > 1.0f){
              colorB = 1.0f;
         }else if(tempColorB < 0.0f){
              colorB = 0.0f;
         }else{
              colorB = tempColorB;
         }

   //对比度处理 ，让白的更白点，黑更黑点，在中间点0.5就作为对比
   //(colorR-0.5f)*contrast 这个是变化量，在加回原来的
   tempColorR = (colorR-0.5f)*contrast+0.5f;
   tempColorG = (colorG-0.5f)*contrast+0.5f;
   tempColorB = (colorB-0.5f)*contrast+0.5f;

     if(tempColorR > 1.0f){
          colorR = 1.0f;
     }else if(tempColorR < 0.0f){
          colorR = 0.0f;
     }else{
          colorR = tempColorR;
     }

     if(tempColorG > 1.0f){
             colorG = 1.0f;
        }else if(tempColorG < 0.0f){
             colorG = 0.0f;
        }else{
             colorG = tempColorG;
        }

        if(tempColorB > 1.0f){
                colorB = 1.0f;
           }else if(tempColorB < 0.0f){
                colorB = 0.0f;
           }else{
                colorB = tempColorB;
        }
   gl_FragColor = vec4(colorR, colorG, colorB, colorA);

}