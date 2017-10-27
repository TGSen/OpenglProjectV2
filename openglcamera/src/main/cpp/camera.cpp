//
// Created by Administrator on 2017/10/20.
//

#include <sggl.h>
#include "camera.h"




jobject Camera::getSurfaceTextureObject() {
    if (javaSurfaceTextureObj == NULL) {
        return NULL;
    }
    return javaSurfaceTextureObj;
}

void Camera::init(float x,float y,float z) {
    mModelMatrix = glm::translate(x,y,z);
    vertexBuffer = new VertexBuffer;
    vertexBuffer->setSize(4);
    vertexBuffer->setColor(0,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(1,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(2,0.3f,0.3f,0.3f,1.0f);
    vertexBuffer->setColor(3,0.3f,0.3f,0.3f,1.0f);

//    vertexBuffer->setPosition(0,-1.0f, 1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(1,-1.0f, -1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(2,1.0f, 1.0f, 0.0f, 1.0f);
//    vertexBuffer->setPosition(3,1.0f, -1.0f, 0.0f,1.0f);
//
//    vertexBuffer->setTexcoord(0,0.0f,0.0f);
//    vertexBuffer->setTexcoord(1,1.0f, 0.0f);
//    vertexBuffer->setTexcoord(2,0.0f, 1.0f);
//    vertexBuffer->setTexcoord(3,1.0f, 1.0f);

    vertexBuffer->setTexcoord(0,0.0f,1.0f);
    vertexBuffer->setTexcoord(1,1.0f, 1.0f);
    vertexBuffer->setTexcoord(2,1.0f, 0.0f);
    vertexBuffer->setTexcoord(3,0.0f, 0.0f);

    vertexBuffer->setPosition(0,-1.0f, 1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(1,-1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(2,1.0f, -1.0f, 0.0f, 1.0f);
    vertexBuffer->setPosition(3,1.0f, 1.0f, 0.0f,1.0f);




    mShader = new SShader;
//    mShader->init("Res/camera_normal.vs", "Res/camera_normal.fs");
    mShader->init("Res/camera_back&while.vs", "Res/camera_back&while.fs");
    //设置滤镜的分量，请查看camera_back&while.fs
    mShader->setUiformVec4("U_MultipleFilter",1.0f,1.0f,1.0f,1.0f);
    //光照，目前先不用
//    mShader->setUiformVec4("U_LightPos",0.0f,0.0f,1.0f,1.0f);
//    mShader->setUiformVec4("U_LightAmbient",1.0f,1.0f,1.0f,1.0f);
//    mShader->setUiformVec4("U_LightDiffuse",.0f,1.0f,1.0f,1.0f);
//    mShader->setUiformVec4("U_LightOpt",32.0f,0.0f,0.0f,1.0f);
//    mShader->setUiformVec4("U_AmbientMaterial",0.1f,0.1f,0.1f,1.0f);
//    mShader->setUiformVec4("U_DiffuseMaterial",0.6f,0.6f,0.6f,1.0f);
}


void Camera::createSurfaceTextureObject(JNIEnv *env) {
    glGenTextures(1, &textureId);
    glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureId);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    const char *stClassPath = "android/graphics/SurfaceTexture";
    const jclass surfaceTextureClass = env->FindClass(stClassPath);
    if (surfaceTextureClass == 0) {
    }

//    // find the constructor that takes an int
    const jmethodID constructor = env->GetMethodID(surfaceTextureClass, "<init>", "(I)V");
    if (constructor == 0) {
    }

    jobject obj = env->NewObject(surfaceTextureClass, constructor, textureId);
    if (obj == 0) {
    }
    javaSurfaceTextureObj = env->NewGlobalRef(obj);

}
void Camera::drawModel(glm::mat4 &mViewMatrix, glm::mat4 &mProjectionMatrix) {
    glEnable(GL_DEPTH_TEST);
    //由于光照需要摄像机的位置
//    mShader->setUiformVec4("U_CameraPos", x, y, z, 1.0);
    vertexBuffer->bind();
//    glm::mat4 it = glm::inverseTranspose(mModelMatrix);

    mShader->bind(glm::value_ptr(mModelMatrix), glm::value_ptr(mViewMatrix),
                  glm::value_ptr(mProjectionMatrix));


//    mShader->setOESTextureId(textureId);
//    glUniformMatrix4fv(glGetUniformLocation(mShader->mProgram, "IT_ModelMatrix"), 1, GL_FALSE,
//                       glm::value_ptr(it));

    glDrawArrays(GL_TRIANGLE_FAN, 0, vertexBuffer->mVertexCount);
    vertexBuffer->unBind();
}

void Camera::changeFilter(float cr,float cg, float cb , float ca){
    mShader->setUiformVec4("U_MultipleFilter",cr,cg,cb,ca);

}



