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
    Camera ();
    ~Camera();
    GLuint textureId;
    VertexBuffer *vertexBuffer;
    SShader *mShader;
    glm::mat4 mModelMatrix;
    bool isChangeVSFS;//检查是否更改了vs 和fs
    char* vsPath;
    char* fsPath;
    void init(float x,float y,float z);
    void drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix);
    //Java 中SurfaceTexture
    jobject javaSurfaceTextureObj;
    jobject getSurfaceTextureObject();
    void createSurfaceTextureObject(JNIEnv *env);
    //修改 shader 变量参数
    void changeFilter(float cr,float cg, float cb , float ca);
    //修改 vs shader ,和fs shader
    void changeVSFS(const char* vsPath, const char*fsPath);
};


#endif //OPENGLSTUDYV1_CAMERA_H
