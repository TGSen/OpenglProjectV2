/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/5 13:46
 * Des    : 
 */
//
#pragma once

#include "sggl.h"
class FrameBuffter {

public:
    FrameBuffter();
    ~FrameBuffter();
    float width;
    float height ;
    GLuint  mFbo;
    GLuint  mColorBuffer;
    GLuint  mRbo; //randerBuffer obj

public:
    void initFBO(float width, float height);
};


