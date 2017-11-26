/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/26 14:35
 * Des    : 
 */
//

#ifndef OPENGLPROJECTV2_FACEDETECTION_H
#define OPENGLPROJECTV2_FACEDETECTION_H

#include "sggl.h"
class FaceDetection {
public:
    bool isCanSaveImage;
    FaceDetection(const char * path);
    ~FaceDetection();

    void detectionFace(void *pixels, int& width, int& height, vector<Rect> &faces);


private:
    //加载分类器
    CascadeClassifier *classifier;
    Mat nv21ToRGB(void *pixels, int width, int height);
    void getFace(Mat src,vector<Rect> &faces);
};


#endif //OPENGLPROJECTV2_FACEDETECTION_H
