/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 17:45
 * Des    :  这个跟Camera 很相似，到时也得抽取封装一下
 */
//

#ifndef OPENGLPROJECTV2_PICTURE_H
#define OPENGLPROJECTV2_PICTURE_H
#include "camera/shader.h"
#include "sggl.h"
#include <camera/ShapeType.h>
#include <camera/CameraShape.h>
#include <camera/NormalShape.h>
#include <camera/MultipleShape.h>
class Picture {
    private:
        float mShapSize;
    public:

    GLuint textureId;
        SShader *mShader;
        CameraShape * cameraShape;
        glm::vec3 mCameraPos;
        glm::vec4 mBgColor;
        glm::vec4 mFilterColor;
        float mWidth;
        float mHeight;
        float mResWidth ;
        float mResHeight;
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
        unsigned char *dataBit;
       // vector<dataBit> vector1;
public:
    Picture();

    ~Picture();
        //初始化shader 顶点
        void initVertex(float x,float y,float z, int count);
        //初始化形状数据顶点,和形状大小
        bool initShapeData(float x, float y, float z, int count, float shapeSize);
        //初始化矩阵
        void initMVP( float width,float height,float reqWidth,float reqHeight,glm::vec3 carmeaPos);
        //画
        void draw();
        //修改 shader 变量参数
        void changeFilterColor();
        void changeFilter(float cr,float cg, float cb , float ca);
        //修改 vs shader ,和fs shader
        void changeVSFS(const char* vsPath, const char*fsPath);
        //修改 shape 形状
        bool changeShape(int shape, int count);
        //修改要画的顶点个数
        void changeShapeDrawCount(int count);

        //修改size
        void changeShapeSize(float size);
        //修改滤镜区域
        void changeFileterZoom(float zoom);
        void setTexture(const char *name,GLuint textureId);

        void changeBgColor(glm::vec4 bgcolor);
        void addTextEffect(void *data);
};


#endif //OPENGLPROJECTV2_PICTURE_H
