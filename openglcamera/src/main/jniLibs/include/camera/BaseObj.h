/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/12/3 21:20
 * Des    : 基类
 */
//

#ifndef OPENGLPROJECTV2_BASEOBJ_H
#define OPENGLPROJECTV2_BASEOBJ_H

#include <camera/vertexbuffer.h>
#include <camera/shader.h>
#include <camera/ShapeType.h>
#include <camera/CameraShape.h>
#include <camera/NormalShape.h>
#include <camera/MultipleShape.h>
#include "camera/PictureShape.h"
#include "camera/MVPMatrix.h"

class BaseObj {
public:
    BaseObj();
    ~BaseObj();
public:
    float constShapSize; //这个不变初始化时大小
    float mShapSize; //当前大小
    GLuint textureId;
    SShader *mShader;
    CameraShape * cameraShape;
    glm::vec3 mCameraPos;
    glm::vec4 mBgColor;
    glm::vec4 mFilterColor;
    float mRotateAngle ;
    float mWidth;
    float mHeight;
    float mReqWidth ;
    float mReqHeight;
    float mFilterZoom;
    int mMultipleCount;
    bool isChangeVSFS;//检查是否更改了vs 和fs
    bool isChangeShape;
    bool isChangeFilterColor;
    bool isInitFinish; //如果在运行过程中，变化顶点的话，那就等变化完毕后才能操作
    std::map<ShapeType,CameraShape *> mShapeTypeMap;
    enum ShapeType currentShap;
    char* vsPath;
    char* fsPath;
};


#endif //OPENGLPROJECTV2_BASEOBJ_H
