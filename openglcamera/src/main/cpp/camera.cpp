//
// Created by Administrator on 2017/10/20.
//

#include <camera/sggl.h>
#include "camera/camera.h"
#include <camera/NormalShape.h>
#include "camera/MultipleShape.h"



Camera::Camera (){
    isChangeVSFS = false;
    isChangeShape = false;
    isInitFinish = false;
    fsPath = nullptr;
    vsPath = nullptr;
    cameraShape = nullptr;
     mWidth=0;
     mHeight=0;
    mMultipleCount = 4;
    currentShap = Normal;
    //初始化为黑色
    mBgColor = glm::vec4(0.0f, 0.0f, 0.0f, 0.0f);
    mShapSize =1.0f;
}

Camera::~Camera(){
    if (fsPath!= nullptr){
        delete fsPath;
    }
    if(vsPath!= nullptr){
        delete vsPath;
    }
    if(mShader!= nullptr){
        delete mShader;
    }
    std::map<ShapeType, CameraShape *>::iterator iterators;
    for (iterators = mShapeTypeMap.begin(); iterators != mShapeTypeMap.end(); ++iterators) {
        if (iterators->second != nullptr) {
            delete (iterators->second);
            iterators->second = nullptr;
        }
    }
    LOGE("稀放完毕");
}

jobject Camera::getSurfaceTextureObject() {
    if (javaSurfaceTextureObj == NULL) {
        return NULL;
    }
    return javaSurfaceTextureObj;
}

void Camera::initVertex(float x, float y, float z, int count) {
//    mModelMatrix = glm::translate(x,y,z);
//    vertexBuffer = new VertexBuffer;
//    vertexBuffer->setSize(4);
//    vertexBuffer->setColor(0,0.3f,0.3f,0.3f,1.0f);
//    vertexBuffer->setColor(1,0.3f,0.3f,0.3f,1.0f);
//    vertexBuffer->setColor(2,0.3f,0.3f,0.3f,1.0f);
//    vertexBuffer->setColor(3,0.3f,0.3f,0.3f,1.0f);
//
//    vertexBuffer->setTexcoord(0,0.0f,1.0f);
//    vertexBuffer->setTexcoord(1,1.0f, 1.0f);
//    vertexBuffer->setTexcoord(2,1.0f, 0.0f);
//    vertexBuffer->setTexcoord(3,0.0f, 0.0f);
//
//    vertexBuffer->setPosition(0,-1.0f, 1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(1,-1.0f, -1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(2,1.0f, -1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(3,1.0f, 1.0f, 0.0f,1.0f);
    //将上面的操作放到一个形状相关的类里 CameraShape ,这样可以方便拓展
    initShapeData(x, y, z, 4,mShapSize);
    mShader = new SShader;
    mShader->init("resource/camera/camera_normal.vs", "resource/camera/camera_normal.fs");
//    mShader->init("Res/camera_filter_rgba.vs", "Res/camera_filter_customer.fs");
    //设置滤镜的分量，请查看camera_back&while.fs ,注意的是如果fs vs 没有这个属性不要设置，避免出现黑屏，或者是清屏颜色
    mShader->setUiformVec4("U_MultipleFilter",1.0f,1.0f,1.0f,1.0f);
}

void Camera::initShapeData(float x, float y, float z, int count, float shapSize) {
    //没次进来都要这样，外部调用时都要更新
    isInitFinish = false;
    auto iterators = mShapeTypeMap.find(currentShap);
    if (iterators == mShapeTypeMap.end()) {
        LOGE("cameraShape->initShapeData,没有该形状，new");
        //找不到就创建
        CameraShape *shape;
        switch (currentShap) {
            case Normal:
                shape = new NormalShape;
                shape->initShapeData(x, y, z, mMultipleCount, shapSize);
                break;
            case Circle:
                shape = new MultipleShape;
                shape->initShapeData(x, y, z, 200, shapSize);
                break;
            case Multiple:
                shape = new MultipleShape;
                shape->initShapeData(x, y, z, mMultipleCount, shapSize);
                break;

        }
        cameraShape = shape;
        mShapeTypeMap.insert(std::pair<ShapeType, CameraShape *>(currentShap, shape));
        LOGE("cameraShape->mShapeTypeMap.insert");
    } else {
        //重新赋值
        LOGE("cameraShape->initShapeData,有该形状");
        cameraShape = iterators->second;
        if (cameraShape->vertexBuffer->mVertexCount != count) {
            //如果边数不等于的话重建
            LOGE("cameraShape->initShapeData ，形状不一样");
            cameraShape->initShapeData(x, y, z, mMultipleCount, shapSize);
        } else {
            //边数相等，看大小有没相等
            if (cameraShape->shapeSize != shapSize) {
                LOGE("cameraShape->changeShapeSize(shapSize)");
                cameraShape->changeShapeSize(shapSize);
            }

        }
    }

    isInitFinish = true;

}


void Camera::createSurfaceTextureObject(JNIEnv *env) {
    glGenTextures(1, &textureId);
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    //创建 java SurfaceTexture 并绑定textureId
    const char *stClassPath = "android/graphics/SurfaceTexture";
    const jclass surfaceTextureClass = env->FindClass(stClassPath);
    if (surfaceTextureClass == nullptr) {
        LOGE("surfaceTextureClass ==0");
    }
    const jmethodID constructor = env->GetMethodID(surfaceTextureClass, "<init>", "(I)V");
    if (constructor == nullptr) {
        LOGE("surfaceTextureClass constructor ==0");
    }
    jobject obj = env->NewObject(surfaceTextureClass, constructor, textureId);
    if (obj == nullptr) {
        LOGE("surfaceTextureClass obj ==0");
    }
    javaSurfaceTextureObj = env->NewGlobalRef(obj);

}

void Camera::initMVP(float width, float height, glm::vec3 carmeaPos) {
    mWidth = width;
    mHeight = height;
    mCameraPos = carmeaPos;
    cameraShape->initMVP(width, height, carmeaPos);

}
void Camera::draw() {
    //画之前看看有没有更改了滤镜形状
    if (isChangeShape) {
        LOGE("camera->mShapSize %f",mShapSize);
        initShapeData(0.0f,0.0f,0.0f,mMultipleCount,mShapSize);
        cameraShape->initMVP(mWidth,mHeight,mCameraPos);
        isChangeShape = false;
    }

    //画之前看看有没有更改了滤镜文件
    if (isChangeVSFS == true){
        LOGE("chang fs vs");
        mShader->init(vsPath,fsPath);
        mShader->setUiformVec4("U_MultipleFilter",0.2f,0.2f,1.0f,1.0f);
        delete vsPath;
        delete fsPath;
        isChangeVSFS = false;
    }

    //OpenGl设定
//    glEnable(GL_BLEND);             //启用混合功能，将图形颜色同周围颜色相混合
//    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

    //深度测试
    glEnable(GL_DEPTH_TEST);
    glHint(GL_NEAREST_MIPMAP_LINEAR,GL_NICEST);
    cameraShape->vertexBuffer->bind();
    mShader->bind(glm::value_ptr(cameraShape->mModelMatrix),
                  glm::value_ptr(cameraShape->mViewMatrix),
                  glm::value_ptr(cameraShape->mProjectionMatrix));

    glDrawArrays(GL_TRIANGLE_FAN, 0, cameraShape->getDrawCount());
    cameraShape->vertexBuffer->unBind();
}
//修改 shader 变量参数
void Camera::changeFilter(float cr,float cg, float cb , float ca){
    mShader->setUiformVec4("U_MultipleFilter",cr,cg,cb,ca);

}

//修改 vs shader ,和fs shader
void Camera::changeVSFS(const char* vspath, const char*fspath){
    //给camera vsPath ，fsPath 中赋值，到时用完后得 清内存
    size_t vsSize = strlen(vspath);
    vsPath = (char *) malloc(vsSize);
    memset(vsPath, 0, vsSize + 1);
    memcpy(vsPath, vspath, vsSize);

    size_t fsSize = strlen(fspath);
    fsPath = (char *) malloc(fsSize);
    memset(fsPath, 0, fsSize + 1);
    memcpy(fsPath, fspath, fsSize);
    LOGE("changeVSFS:%s,%s",vsPath,fsPath);
    isChangeVSFS = true;
}

bool Camera::changeShape(int shape, int count) {
    if (!isInitFinish) {
        LOGE("changeShape.....last not finish");
        return false;
    }
    if (currentShap != shape) {
        LOGE("changeShape.....ing");
        currentShap = (ShapeType) shape;
        mMultipleCount = count;
        isChangeShape = true;
        return true;
    }

}

void Camera::changeShapeDrawCount(int count){
    if (count>3){
        mMultipleCount =count;
        isChangeShape = true;
        cameraShape->changeDrawCount(count);
    }

}

void Camera::changeShapeSize(float size){
    mShapSize =size;
    cameraShape->changeShapeSize(size);
}


