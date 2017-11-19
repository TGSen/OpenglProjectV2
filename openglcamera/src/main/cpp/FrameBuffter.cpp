/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/5 13:46
 * Des    : 
 */
//
#include "camera/FrameBuffer.h"
FrameBuffer::FrameBuffer() {
   glGenFramebuffers(1,&mFrameBufferObj);
}

FrameBuffer::~FrameBuffer() {

}

void FrameBuffer::attachColorBuffer(const char *bufferName, GLenum attachMent, int width,
                                     int height) {

    GLuint  colorBuffer;
    glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferObj);
    glGenTextures(1,&colorBuffer);
    glBindTexture(GL_TEXTURE_2D,colorBuffer);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,width,height,0,GL_RGBA,GL_UNSIGNED_BYTE, nullptr);
    glBindTexture(GL_TEXTURE_2D,0);
    glFramebufferTexture2D(GL_FRAMEBUFFER,attachMent,GL_TEXTURE_2D,colorBuffer,0);

    mDrawBuffer.push_back(attachMent);
    mBuffers.insert(std::pair<std::string,GLuint>(bufferName,colorBuffer));
    glBindFramebuffer(GL_FRAMEBUFFER,0);
}

void FrameBuffer::attachDepthBuffer(const char *bufferName, int width, int height) {
    glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferObj);
    GLuint depthMap;
    glGenTextures(1,&depthMap);
    glBindTexture(GL_TEXTURE_2D,depthMap);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexImage2D(GL_TEXTURE_2D,0,GL_DEPTH_COMPONENT,width,height,0,GL_DEPTH_COMPONENT,GL_FLOAT,
                 nullptr);
    glBindTexture(GL_TEXTURE_2D,0);
    glFramebufferTexture2D(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_TEXTURE_2D,depthMap,0);
   mBuffers.insert(std::pair<std::string,GLuint>(bufferName,depthMap));
    glBindFramebuffer(GL_FRAMEBUFFER,0);
}

void FrameBuffer::finish() {
    int count = mDrawBuffer.size();
    if(count>0){
       GLenum *buffers = new GLenum[count];
        int i = 0;
        while (i<count){
            buffers[i] = mDrawBuffer[i];
            i++;
        }
        glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferObj);
        //opengl3.0 的方法
       // glDrawBuffers(count,buffers);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }
}

void FrameBuffer::bind() {
    //查询上一个bind
    glGetIntegerv(GL_FRAMEBUFFER_BINDING,&mPrevFrameBuffer);
    glBindFramebuffer(GL_FRAMEBUFFER,mFrameBufferObj);
    glClearColor(0.0f,0.0f,0.0f,0.0f);
    glClear(GL_COLOR_BUFFER_BIT| GL_DEPTH_BUFFER_BIT);

}

void FrameBuffer::unBind() {
    //绑定回原来那个，最开始的话就是系统默认的那个
    glBindFramebuffer(GL_FRAMEBUFFER,mPrevFrameBuffer);
}

GLuint FrameBuffer::getBufferByName(const char *bufferName) {
    auto iter = mBuffers.find(bufferName);
    if(iter !=mBuffers.end()){
        return iter->second;
    }
    return 0;
}



