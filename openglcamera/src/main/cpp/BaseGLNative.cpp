/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 14:10
 * Des    : 从新整理一下c++架构
 */
//
#include <camera/BaseSences.h>
#include <camera/PictureSence.h>
#include <camera/utils.h>
#include <camera/sggl.h>
#include "camera/sggl.h"
#include "camera/CameraSence.h"
#include "camera/BaseGLNative.h"
#include "camera/NativeSencesType.h"
BaseSences *mBaseSences;
AAssetManager *aAssetManager;
std::map<NativeSencesType ,BaseSences *> mSencesManager;
BaseGLNative::BaseGLNative() {
    aAssetManager = nullptr;
}

BaseGLNative::~BaseGLNative() {
    if (aAssetManager != nullptr) {
        free(aAssetManager);
    }
}

void BaseGLNative::onSurfaceCreated() {}

void BaseGLNative::onBeforeSurfaceCreated(JNIEnv *env, jobject bitmapObj) {};

void BaseGLNative::onSurfaceChanged(int width, int height) {}

void BaseGLNative::onDrawFrame() {}

void BaseGLNative::releaseNative(JNIEnv *env) {


}

void BaseGLNative:: changeFilter(jint r, jint g, jint b, jint a) {}

void BaseGLNative::changeVSFS(const char *vs, const char *fs) {}

void BaseGLNative::changeShape(int cameraShape, int count) {}

void BaseGLNative::changeBgColor(glm::vec4 bgcolor) {}

void BaseGLNative::changeShapeSize(float size) {}

void BaseGLNative::changeShapeDrawCount(int count) {}

void BaseGLNative::changeFileterZoom(float temp) {}

void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onBeforeSurfaceCreated(JNIEnv *env, jclass clzss, jobject jobj) {
    mBaseSences->onBeforeSurfaceCreated(env, jobj);
}

void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_initAssetManager
        (JNIEnv *env, jclass clzss, jobject assetManager,jint senceType) {
    aAssetManager = AAssetManager_fromJava(env, assetManager);
    if(senceType ==CAMERA){
        mBaseSences = new CameraSence;
        mBaseSences->onBeforeSurfaceCreated(env, nullptr);
        mSencesManager.insert(std::pair<NativeSencesType ,BaseSences *>(CAMERA,mBaseSences));
    }else if(senceType ==PICTURE){
        mBaseSences = new PictureSence;
        mSencesManager.insert(std::pair<NativeSencesType ,BaseSences *>(PICTURE,mBaseSences));
    }

};


unsigned char *loadFile(const char *path, int &fileSize) {
    unsigned char *file = nullptr;
    if (path == nullptr) {
        return nullptr;
    }
    fileSize = 0;
    //android 读取内部资源的方法
    AAsset *asset = AAssetManager_open(aAssetManager, path, AASSET_MODE_UNKNOWN);
    if (asset == nullptr)
        return nullptr;
    //读取成功
    fileSize = AAsset_getLength(asset);
    //开辟内存 +1 为了file[fileSize] = '\0';
    file = new unsigned char[fileSize + 1];
    AAsset_read(asset, file, fileSize);
    //为了程序的健壮性
    file[fileSize] = '\0';
    //关闭
    AAsset_close(asset);
    return file;
}

void writeFileToSdcard(const char *path, const char *outPath) {
    unsigned char *file = nullptr;
    if (path == nullptr) {
        return ;
    }
    int fileSize = 0;
    //android 读取内部资源的方法
    AAsset *asset = AAssetManager_open(aAssetManager, path, AASSET_MODE_UNKNOWN);
    if (asset == nullptr)
        return ;
    //读取成功
    fileSize = AAsset_getLength(asset);
    //开辟内存 +1 为了file[fileSize] = '\0';
    file = new unsigned char[fileSize + 1];
    AAsset_read(asset, file, fileSize);
    //为了程序的健壮性
    file[fileSize] = '\0';
    FILE *outFile = fopen(outPath,"w");
    fwrite(file,1,fileSize,outFile);
    fclose(outFile);
    free(file);
    //关闭
    AAsset_close(asset);
    LOGE("out File ok");
}


JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onSurfaceCreated
        (JNIEnv *env, jclass clzss) {
    mBaseSences->onSurfaceCreated();
    //需要开启的话就初始化
    if(mBaseSences->isNeedEyeTracker){
        mBaseSences->initEyeTracker();
    }
};


JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onSurfaceChanged
        (JNIEnv *env, jclass clzss, jint width, jint height) {
    mBaseSences->onSurfaceChanged(width, height);
};

JNIEXPORT void JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_onDrawFrame
        (JNIEnv *env, jclass clzss,jbyteArray data,jint width,jint height) {
    if(data) {
        jbyte *cameraData = env->GetByteArrayElements(data, NULL);
        mBaseSences->onDrawFrame(data,width, height);
        env->ReleaseByteArrayElements(data,cameraData,0);
    }else{
        mBaseSences->onDrawFrame(nullptr,width, height);
    }
};

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_releaseNative(JNIEnv *env, jclass type,jint senceType) {
    if(aAssetManager){
        free(aAssetManager);
        aAssetManager = nullptr;
    }
    if(!mSencesManager.empty()){
        if(senceType ==CAMERA){
            auto iterators = mSencesManager.find(CAMERA);
            if (iterators != mSencesManager.end()) {
                iterators->second->releaseNative(env);
                delete iterators->second;
                iterators->second= nullptr;
                mSencesManager.erase(CAMERA);
            }
        }else if(senceType ==PICTURE){
            auto iterators = mSencesManager.find(PICTURE);
            if (iterators != mSencesManager.end()) {
                delete iterators->second;
                iterators->second= nullptr;
                mSencesManager.erase(PICTURE);
            }
        }
    }

}


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeFileter(JNIEnv *env, jclass type,
                                                               jint r, jint g, jint b, jint a,
                                                               jint max) {
    float rc = (float) r / (float) max;
    float gc = (float) g / (float) max;
    float bc = (float) b / (float) max;
    float ac = (float) a / (float) max;
    LOGE("rc:%f**gc:%f**bc:%f**ac:%f", rc, gc, bc, ac);
    mBaseSences->changeFilter(rc, gc, bc, ac);
}

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeVSFS(JNIEnv *env, jclass type,
                                                            jstring vs_, jstring fs_) {

    const char *vs = env->GetStringUTFChars(vs_, 0);
    const char *fs = env->GetStringUTFChars(fs_, 0);
    if (vs != nullptr && fs != nullptr) {
        mBaseSences->changeVSFS(vs, fs);
    }
    env->ReleaseStringUTFChars(vs_, vs);
    env->ReleaseStringUTFChars(fs_, fs);
}


//修改形状
JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShape(JNIEnv *env, jclass type,
                                                             jint cameraShape, jint count) {
    if (count < 3)
        count = 3;
    mBaseSences->changeShape(cameraShape, count);
}

//修改背景颜色
JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeBgColor(JNIEnv *env, jclass type,
                                                               jfloat r, jfloat g, jfloat b,
                                                               jfloat a) {
    //检验数据
    r = checkData(r);
    g = checkData(g);
    b = checkData(b);
    a = checkData(a);
    mBaseSences->changeBgColor(glm::vec4(r, g, b, a));
}

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShapeSize(JNIEnv *env, jclass type,
                                                                 jint size, jint max) {

    if (size > 0) {
        //先减少 ，后变大
        mBaseSences->changeShapeSize(1.0f - float(size) / float(max));
    }
}

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeShapeCount(JNIEnv *env, jclass type,
                                                                  jint count) {
    //组成一个面至少3个顶点
    if (count >= 4) {
        mBaseSences->changeShapeDrawCount(count);
    }

}


JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_onChangeFileterZoom(JNIEnv *env, jclass type,
                                                                   jint current, jint max) {

    if (current >= 0 && max > 0) {
        //将中间设置为0
        if (current == max / 2) {
            current = 0;
        } else {
            current -= max;
        }
        float temp = float(current) / float(max);

        mBaseSences->changeFileterZoom(temp);
    }
}

JNIEXPORT jobject JNICALL Java_sen_com_openglcamera_natives_BaseGLNative_getSurfaceTexture
        (JNIEnv *env, jclass jcla){
    return mBaseSences->getSurfaceTexture();

}

JNIEXPORT void JNICALL
Java_sen_com_openglcamera_natives_BaseGLNative_addTextEffect(JNIEnv *env, jclass type,
                                                             jobject bitmap) {
    if(bitmap){
        LOGE("sen______0");
        void *piexl;
        float width,height;
        AndroidBitmap_lockPixels(env,bitmap,&piexl);
        AndroidBitmapInfo info;
        AndroidBitmap_getInfo(env,bitmap,&info);
        width = info.width;
        height = info.height;
        mBaseSences->addTextEffect(piexl,width,height);
        AndroidBitmap_unlockPixels(env,bitmap);
    }


}