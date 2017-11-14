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
    Picture ();
    ~Picture();
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
        //修改 shader 变量参数
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
};


#endif //OPENGLPROJECTV2_PICTURE_H
