/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 17:45
 * Des    : 加入fbo ,这个可以作为第一阶段的opengl 和Android 坐标有冲突的，纠正正确的显示，
 *         第二点，可以作为下一阶段加入特效的输出
 */
//



#include <camera/PictureShape.h>
#include <camera/MVPMatrix.h>
#include "camera/Picture.h"

Picture::Picture (){

}

Picture::~Picture(){
    if (fsPath!= nullptr){
        delete fsPath;
    }
    if(vsPath!= nullptr){
        delete vsPath;
    }
    if(mShader!= nullptr){
        delete mShader;
    }

    LOGE("稀放完毕");
}



void Picture::initVertex(float x, float y, float z, int count) {
    //将上面的操作放到一个形状相关的类里 CameraShape ,这样可以方便拓展
    initShapeData(x, y, z, 4,mShapSize);
    mShader = new SShader;
    mShader->init("resource/camera/picture_normal.vs", "resource/camera/picture_normal.fs");
//    mShader->init("Res/camera_filter_rgba.vs", "Res/camera_filter_customer.fs");
    //设置滤镜的分量，请查看camera_back&while.fs ,注意的是如果fs vs 没有这个属性不要设置，避免出现黑屏，或者是清屏颜色
    mShader->setUiformVec4("U_MultipleFilter",1.0f,1.0f,1.0f,1.0f);
    mShader->setTexture("U_Texture", textureId);
}

void Picture::initShapeData(float x, float y, float z, int count, float shapSize) {
    //没次进来都要这样，外部调用时都要更新
    isInitFinish = false;
    auto iterators = mShapeTypeMap.find(currentShap);
    if (iterators == mShapeTypeMap.end()) {
        LOGE("cameraShape->initShapeData,没有该形状，new");
        //找不到就创建
        CameraShape *shape;
        switch (currentShap) {
            case Normal:
                shape = new PictureShape;
                shape->initShapeData(x, y, z, mMultipleCount, shapSize);
                break;
            case Circle:
                shape = new MultipleShape();
                shape->initShapeData(x, y, z, 300, shapSize);
                break;
            case Multiple:
                shape = new MultipleShape();
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
            //边数相等，看大小有没相等,normal就不需要检查了
            if (cameraShape->shapeSize != shapSize && currentShap!=Normal) {
                LOGE("cameraShape->changeShapeSize(shapSize)");
                changeShapeSize(shapSize);
            }

        }

    }

    isInitFinish = true;
}

void Picture::initMVPMatrix(float width, float height,float reqWidth, float reqHeight,glm::vec3 carmeaPos,float rotateAngle,float ratio) {
    mWidth = width;
    mHeight = height;
    mReqHeight = reqHeight;
    mReqWidth = reqWidth;
    mCameraPos = carmeaPos;
    mRotateAngle = rotateAngle;
    cameraShape->initMVPMatirx(width, height,reqWidth, reqHeight,carmeaPos,rotateAngle,ratio);

}


void Picture::draw() {
    //画之前看看有没有更改了滤镜形状
    if (isChangeShape) {
        LOGE("camera->mShapSize %f",mShapSize);
        //一般如果存在Shape 都不会变mvp 的
      initShapeData(0.0f,0.0f,0.0f,mMultipleCount,mShapSize);
        if(cameraShape->mMvpMatrix== nullptr){
            initMVPMatrix(mWidth,mHeight,mReqWidth,mReqHeight,mCameraPos,mRotateAngle,constShapSize);
        }
        isChangeShape = false;
    }

    //画之前看看有没有更改了滤镜文件
    if (isChangeVSFS == true){
        LOGE("chang fs vs");
        mShader->init(vsPath,fsPath);
        mShader->setUiformVec4("U_MultipleFilter",0.2f,0.2f,1.0f,1.0f);
        //修改文件同时修改filterZoom ,一般从上次改变的开始
        //    changeFileterZoom(mFilterZoom);
        delete vsPath;
        delete fsPath;
        vsPath = nullptr;
        fsPath = nullptr;
        isChangeVSFS = false;

    }

    if(isChangeFilterColor){
        changeFilterColor();
        isChangeFilterColor = false;
    }

    //OpenGl设定
//    glEnable(GL_BLEND);             //启用混合功能，将图形颜色同周围颜色相混合
//    glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);

    //深度测试
    glEnable(GL_DEPTH_TEST);
    glHint(GL_NEAREST_MIPMAP_LINEAR,GL_NICEST);
    glClearColor(mBgColor.r, mBgColor.g, mBgColor.b,mBgColor.a);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    cameraShape->vertexBuffer->bind();
    mShader->bind(glm::value_ptr(cameraShape->mMvpMatrix->mModelMatrix),
                  glm::value_ptr(cameraShape->mMvpMatrix->mViewMatrix),
                  glm::value_ptr(cameraShape->mMvpMatrix->mProjectionMatrix));
    if(currentShap ==Normal){
        glDrawArrays(GL_TRIANGLE_STRIP, 0, cameraShape->getDrawCount());
    }else{
        //以0.5 0.5 作为中心
        glDrawArrays(GL_TRIANGLE_FAN, 0, cameraShape->getDrawCount());
    }

    cameraShape->vertexBuffer->unBind();
}
//修改 shader 变量参数
void Picture::changeFilter(float cr,float cg, float cb , float ca){
    isChangeFilterColor = true;
    mFilterColor = glm::vec4(cr,cg,cb,ca);
}

//修改 shader 变量参数
void Picture::changeFilterColor(){
    mShader->setUiformVec4("U_MultipleFilter",mFilterColor.r,mFilterColor.g,mFilterColor.b,mFilterColor.a);

}

//修改 vs shader ,和fs shader
void Picture::changeVSFS(const char* vspath, const char*fspath){
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

bool Picture::changeShape(int shape, int count) {
    if (currentShap != shape) {
        if (!isInitFinish) {
            LOGE("changeShape.....last not finish");
            return false;
        }
        LOGE("changeShape.....ing");
        currentShap = (ShapeType) shape;
        mMultipleCount = count;
        isChangeShape = true;
        return true;
    }
    return false;

}

void Picture::changeShapeDrawCount(int count){
    if (count>3){
        mMultipleCount =count;
        isChangeShape = true;
        cameraShape->changeDrawCount(count);
    }

}

void Picture::changeShapeSize(float size){
    mShapSize = constShapSize - size;
    LOGE("CameraShape:: changeShapeSize%f", size);
    //每次用初始化为单位矩阵glm::mat4(1.0f)
    glm::mat4  scale = glm::scale(glm::mat4(1.0f), glm::vec3(size, size, 1.0f));
    //还需要旋转过来,图片不需要转
    cameraShape->mMvpMatrix->mModelMatrix = glm::rotate(scale, cameraShape->mMvpMatrix->rotateAngle,glm::vec3(0.0f, 0.0f, 1.0f));
}
//修改路径的区域
void Picture::changeFileterZoom(float zoom){
    //以后还得封装一个参数的才行
    if(currentShap == Normal){
        LOGE("temp x %f",zoom);
        mShader->setUiformVec4("FileterZoom",zoom,0.0f,1.0f,0.0f);
    }else{
        LOGE("temp y %f",zoom);
        mShader->setUiformVec4("FileterZoom",0.0f,zoom,2.0f,1.0f);
    }

    mFilterZoom = zoom;

}

void Picture::setTexture(const char*name,GLuint textureId){
    mShader->setTexture(name,textureId);
}

void Picture::changeBgColor(glm::vec4 bgcolor){
    mBgColor = bgcolor;
}

void Picture::addTextEffect(void *data){
    dataBit = (unsigned char *) data;
}