//
// Created by Administrator on 2017/10/20.
//

#include <camera/sggl.h>
#include "camera/camera.h"
#include "camera/circle.h"

Camera::Camera (){
    isChangeVSFS = false;
    isChangeShape = false;
    fsPath = nullptr;
    vsPath = nullptr;
    float mWidth=0;
    float mHeight=0;
    //初始化为黑色
    mBgColor =glm::vec4(1.0f,0.0f,0.0f,0.0f);
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

   if(vertexBuffer){
       free(vertexBuffer);
   }
    LOGE("稀放完毕");
}

jobject Camera::getSurfaceTextureObject() {
    if (javaSurfaceTextureObj == NULL) {
        return NULL;
    }
    return javaSurfaceTextureObj;
}

void Camera::initVertex(float x,float y,float z) {
    mModelMatrix = glm::translate(x,y,z);
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(4);
    vertexBuffer->setColor(0,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(1,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(2,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(3,0.3f,0.3f,0.3f,1.0f);

    vertexBuffer->setTexcoord(0,0.0f,1.0f);
    vertexBuffer->setTexcoord(1,1.0f, 1.0f);
    vertexBuffer->setTexcoord(2,1.0f, 0.0f);
    vertexBuffer->setTexcoord(3,0.0f, 0.0f);

    vertexBuffer->setPosition(0,-1.0f, 1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(1,-1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(2,1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(3,1.0f, 1.0f, 0.0f,1.0f);


/////测试数据
//    vertexBuffer->setPosition(0,-1.0f, 1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(1,-1.0f, -1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(2,1.0f, 1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(3,1.0f, -1.0f, 0.0f,1.0f);
//
//    vertexBuffer->setTexcoord(0,0.0f,0.0f);
//    vertexBuffer->setTexcoord(1,1.0f, 0.0f);
//    vertexBuffer->setTexcoord(2,0.0f, 1.0f);
//    vertexBuffer->setTexcoord(3,1.0f, 1.0f);


    mShader = new SShader;
    mShader->init("resource/camera/camera_normal.vs", "resource/camera/camera_normal.fs");
//    mShader->init("Res/camera_filter_rgba.vs", "Res/camera_filter_customer.fs");
    //设置滤镜的分量，请查看camera_back&while.fs ,注意的是如果fs vs 没有这个属性不要设置，避免出现黑屏，或者是清屏颜色
    mShader->setUiformVec4("U_MultipleFilter",1.0f,1.0f,1.0f,1.0f);
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
    //mViewMatrix = glm::lookAt(carmeaPos,glm::vec3(0.0f,0.0f,0.0f),glm::vec3(0.0f,1.0f,1.0f));
//    mProjectionMatrix= glm::perspective(60.0f,width/height,0.1f,1000.0f);

}
void Camera::draw() {
    //画之前看看有没有更改了滤镜文件
    if (isChangeVSFS == true){
        LOGE("chang fs vs");
        mShader->init(vsPath,fsPath);
        mShader->setUiformVec4("U_MultipleFilter",1.0f,1.0f,1.0f,1.0f);
        delete vsPath;
        delete fsPath;
        isChangeVSFS = false;
    }

    if(isChangeShape){
        Circle *mCircle =new Circle;
        mCircle->initVertex(0.0f,0.0f,0.0f,200);
        mCircle->initMVP(mWidth,mHeight,mCameraPos);
        vertexBuffer =mCircle->vertexBuffer;
        mModelMatrix = mCircle->mModelMatrix;
        mViewMatrix = mCircle->mViewMatrix;
        mProjectionMatrix =mCircle->mProjectionMatrix;
        isChangeShape = false;
    }
    glEnable(GL_DEPTH_TEST);
    vertexBuffer->bind();
    mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                  glm::value_ptr(mProjectionMatrix));

    glDrawArrays(GL_TRIANGLE_FAN, 0, vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
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

void Camera::changeShape(int shape){
    currentShap =shape;
    isChangeShape = true;
}


