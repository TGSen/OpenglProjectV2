//
// Created by Administrator on 2017/10/20.
//

#ifndef OPENGLSTUDYV1_CAMERA_H
#define OPENGLSTUDYV1_CAMERA_H

#include <sggl.h>
#include <vertexbuffer.h>
#include <shader.h>
#include "CameraSGLNative.h"

class Camera{
public:
    GLuint textureId;
    VertexBuffer *vertexBuffer;
    SShader *mShader;
    glm::mat4 mModelMatrix;
    void init(float x,float y,float z);
    void drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix);
    //Java 中SurfaceTexture
    jobject javaSurfaceTextureObj;
    jobject getSurfaceTextureObject();
    void createSurfaceTextureObject(JNIEnv *env);
};


#endif //OPENGLSTUDYV1_CAMERA_H
