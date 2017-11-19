/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/15 17:57
 * Des    : 全屏四边形，今天后续的内容学习来自网络
 */
//

#ifndef OPENGLPROJECTV2_FULLSCREENQUAD_H
#define OPENGLPROJECTV2_FULLSCREENQUAD_H

#include "vertexbuffer.h"
#include "shader.h"
class FullScreenQuad {
public :
    VertexBuffer *mVertexBuffer;
    SShader *mShader;
public:
    void init();
    void draw();
    void drawToLeftTop();
    void drawToRightTop();
    void drawToLeftBottom();
    void drawToRightBottom();
};


#endif //OPENGLPROJECTV2_FULLSCREENQUAD_H
