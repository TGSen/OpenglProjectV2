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
class FrameBuffer {

public:
    GLuint  mFrameBufferObj;
    GLint mPrevFrameBuffer;
    std::map<std::string,GLuint> mBuffers;
    std::vector<GLenum > mDrawBuffer;
public:
    FrameBuffer();
    ~FrameBuffer();
    void attachColorBuffer(const char * bufferName,GLenum attachMent,int width,int height);
    void attachDepthBuffer(const char * bufferName,int width,int height);
    void finish();
    void bind();
    void unBind();
    GLuint getBufferByName(const char* bufferName);
};


