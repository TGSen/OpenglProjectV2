/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/12/3 21:20
 * Des    : 
 */
//

#include <camera/CameraShape.h>
#include "camera/BaseObj.h"
#include "camera/ShapeType.h"
#include "camera/sggl.h"
BaseObj::BaseObj(){
    isChangeVSFS = false;
    isChangeShape = false;
    isChangeFilterColor= false;
    isInitFinish = false;
    fsPath = nullptr;
    vsPath = nullptr;
    cameraShape = nullptr;
    mWidth=0;
    mHeight=0;
    mReqWidth = 0;
    mReqHeight =0;
    mFilterZoom =0;
    mMultipleCount = 4;
    currentShap = Normal;
    std::map<ShapeType,CameraShape *> mShapeTypeMap;
    //初始化为白色
    mBgColor = glm::vec4(1.0f, 1.0f, 1.0f, 1.0f);
    mFilterColor = glm::vec4(1.0f, 1.0f, 1.0f, 1.0f);
    mShapSize =0.5f;
}
BaseObj::~BaseObj(){

}
