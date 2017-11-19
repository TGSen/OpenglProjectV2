//
// Created by Administrator on 2017/10/20.
//

#ifndef OPENGLSTUDYV1_CAMERA_H
#define OPENGLSTUDYV1_CAMERA_H

#include <camera/sggl.h>
#include <camera/vertexbuffer.h>
#include <camera/shader.h>
#include "CameraSGLNative.h"
#include "CameraShape.h"
#include "ShapeType.h"
class Camera{
private:
    float mShapSize;
public:
    Camera ();
    ~Camera();
    GLuint textureId;
    SShader *mShader;
    CameraShape * cameraShape;
    glm::vec3 mCameraPos;
    glm::vec4 mBgColor;
    float mWidth;
    float mHeight;
    float mFilterZoom;
    int mMultipleCount;

    bool isChangeVSFS;//检查是否更改了vs 和fs
    bool isChangeShape;
    bool isInitFinish; //如果在运行过程中，变化顶点的话，那就等变化完毕后才能操作
    std::map<ShapeType,CameraShape *> mShapeTypeMap;
    enum ShapeType currentShap;
    char* vsPath;
    char* fsPath;
    //初始化shader 顶点
    void initVertex(float x,float y,float z, int count);
    //初始化形状数据顶点,和形状大小
    void initShapeData(float x, float y, float z, int count, float shapeSize);
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
    bool changeShape(int shape, int count);
    //修改要画的顶点个数
    void changeShapeDrawCount(int count);
    //修改背景
    void changeBgColor(glm::vec4 bgcolor);
    //修改size
    void changeShapeSize(float size);
    //修改滤镜区域
    void changeFileterZoom(float zoom);
};


#endif //OPENGLSTUDYV1_CAMERA_H
