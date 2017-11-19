/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/5 13:46
 * Des    : 
 */
//
#include "camera/FrameBuffer.h"
FrameBuffter::FrameBuffter() {

}

FrameBuffter::~FrameBuffter() {

}


void FrameBuffter::initFBO(float width, float height) {
    this->width = width;
    this->height = height;
    //获取mFbo
//    glGenFramebuffers(1,&mmFbo);
//    //绑定mFbo
//    glBindFramebuffer(GL_FRAMEBUFFER,mmFbo);
//    //mmColorBuffer 生成一张纹理
//    glGenTextures(1,&mmColorBuffer);
//    glBindTexture(GL_TEXTURE_2D,mmColorBuffer);
//    //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//    //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//    //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//    //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//    glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE, nullptr);
//    glBindTexture(GL_TEXTURE_2D,0);
//
//    //然后将纹理绑定到mFbo，关联起来
//    //GL_COLOR_ATTACHMENT0 挂载点
//    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,mmColorBuffer,0);
//
//    //depth buffer
//    glGenRenderbuffers(1,&mRbo);
//    glBindRenderbuffer(GL_RENDERBUFFER,mRbo);
//    glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH24_STENCIL8_OES,width,height);
//    glBindRenderbuffer(GL_RENDERBUFFER,0);
//    //将depth buffer绑定到mFbo，关联起来
//    glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,mRbo);
//
//    //模板 buffer
//    glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_STENCIL_ATTACHMENT,GL_RENDERBUFFER,mRbo);
//
//    //最后检查有没成功
//    if(glCheckFramebufferStatus(GL_FRAMEBUFFER)==GL_FRAMEBUFFER_COMPLETE){
//        LOGE("glCheckFramebufferStatus ok");
//    }else{
//        LOGE("glCheckFramebufferStatus error");
//    }
//    glBindFramebuffer(GL_FRAMEBUFFER,0);

    glGenFramebuffers(1,&mFbo);
    glBindFramebuffer(GL_FRAMEBUFFER,mFbo);
    //color buffer
    glGenTextures(1,&mColorBuffer);
    glBindTexture(GL_TEXTURE_2D,mColorBuffer);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,256,256,0,GL_RGBA,GL_UNSIGNED_BYTE,nullptr);
    glBindTexture(GL_TEXTURE_2D,0);
    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,mColorBuffer,0);
    //depth buffer
    glGenRenderbuffers(1,&mRbo);
    glBindRenderbuffer(GL_RENDERBUFFER,mRbo);
    glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH24_STENCIL8_OES,256,256);
    glBindRenderbuffer(GL_RENDERBUFFER,0);
    glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,mRbo);
    LOGE("init depth stencil buffer");
    //stencil buffer    
    glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_STENCIL_ATTACHMENT,GL_RENDERBUFFER,mRbo);

    //check error
    int code=glCheckFramebufferStatus(GL_FRAMEBUFFER);
    if(code!=GL_FRAMEBUFFER_COMPLETE)
    {
        LOGE("create mFbo fail");
    }
    else
    {
        LOGE("create mFbo sucess");
    }
    glBindFramebuffer(GL_FRAMEBUFFER,0);
}