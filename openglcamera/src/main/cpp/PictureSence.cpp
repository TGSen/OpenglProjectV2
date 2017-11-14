/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:59
 * Des    : 
 */
//
#include "camera/PictureSence.h"
#include "camera/Picture.h"

glm::vec3 carmeaPoss(0.0f, 0.0f, 2.6f);
Picture *mPicture;

PictureSence::PictureSence() {}

PictureSence::~PictureSence() {}

void PictureSence::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {
    mPicture = new Picture;
    mPicture->textureId = createTexture2dFromBitmap(env, bitmapObj);
}

void PictureSence::onSurfaceCreated() {
    LOGE("  mPicture-> init ");
    mPicture->initVertex(0.0f, 0.0f, 0.0f, 4);
}

void PictureSence::onSurfaceChanged(float width, float height) {
    glViewport(0, 0, width, height);
    mPicture->initMVP(width, height, carmeaPoss);
}

//绘制场景
void PictureSence::onDrawFrame() {
    float time = getTime();
    glClearColor(mPicture->mBgColor.r, mPicture->mBgColor.g, mPicture->mBgColor.b,
                 mPicture->mBgColor.a);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    mPicture->draw();
    //良好习惯，当绘制完毕后，将程序置为0 号程序
    glUseProgram(0);
}

//增加一个方法，这个方法在onSurfaceCreated 之前调用

void PictureSence::releaseNative(JNIEnv *env) {
}

void PictureSence::changeFilter(jint r, jint g, jint b, jint a) {
   // mPicture->changeFilter(r,g,b,a);
}

void PictureSence::changeVSFS(const char *vs, const char *fs) {
    mPicture->changeVSFS(vs,fs);
}

void PictureSence::changeShape(int cameraShape, int count) {
   // mPicture->changeShape(cameraShape,count);
}

void PictureSence::changeBgColor(glm::vec4 bgcolor) {
   // mPicture->changeBgColor( bgcolor);
}

void PictureSence::changeShapeSize(float size) {
  //  mPicture->changeShapeSize(size);
}

void PictureSence::changeShapeDrawCount(int count) {
    //mPicture->changeShapeDrawCount(count);
}

void PictureSence::changeFileterZoom(float temp) {
  //  changeFileterZoom(temp);
}

