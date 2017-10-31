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
    glm::vec3 mCameraPos;
    glm::vec4 mBgColor;
    int currentShap;
    float mWidth;
    float mHeight;
    //矩阵相关
    glm::mat4 mModelMatrix;
    glm::mat4 mViewMatrix;
    glm::mat4 mProjectionMatrix;
    bool isChangeVSFS;//检查是否更改了vs 和fs
    bool isChangeShape;
    char* vsPath;
    char* fsPath;
    //初始化顶点
    void initVertex(float x,float y,float z);
    //初始化矩阵
    void initMVP( float width,float height,glm::vec3 carmeaPos);
    //画
    void draw();
    //Java 中SurfaceTexture
    jobject javaSurfaceTextureObj;
    jobject getSurfaceTextureObject();
    void createSurfaceTextureObject(JNIEnv *env);
    //修改 shader 变量参数
    void changeFilter(float cr,float cg, float cb , float ca);
    //修改 vs shader ,和fs shader
    void changeVSFS(const char* vsPath, const char*fsPath);
    //修改 shape 形状
    void changeShape(int shape);

};


#endif //OPENGLSTUDYV1_CAMERA_H
