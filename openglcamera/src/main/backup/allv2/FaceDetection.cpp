/**
 * 
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/26 14:35
 * Des    : 
 */
//

#include "camera/FaceDetection.h"
FaceDetection::FaceDetection(const char * path){
    classifier = new  CascadeClassifier(path);
    free(&path);
}
FaceDetection::~FaceDetection(){
    if(classifier){
        delete classifier;
    }
}

void FaceDetection::detectionFace(void *pixels, int &width, int &height, vector<Rect> &faces){
    //先将nv21 转rgb
    Mat srcMat = nv21ToRGB(pixels,width, height);
    width = srcMat.cols;
    height = srcMat.rows;
    getFace(srcMat,faces);
    srcMat.release();
}

void FaceDetection::getFace(Mat src,vector<Rect> &faces){
    if (classifier) {
        //图片灰化
        cvtColor(src, src, CV_BGR2GRAY);
        //直线图均衡化，增强对比效果
        equalizeHist(src, src);
        //开始识别，并将识别到的脸部区域写入faces 向量中
        classifier->detectMultiScale(src, faces);
        src.release();
        int size = faces.size();
        for (int i = 0; i < size; i++) {
            faces[i] = faces[i];
        }
    }
}

Mat FaceDetection::nv21ToRGB(void *pixels, int width, int height){
    //获得的是nv21格式 nv21格式 y数据的高就是图像高，uv数据的高加起来只有图像的一半
    Mat nv21(height + height / 2, width, CV_8UC1, pixels);
    //把nv21格式的数据转为rgb
    cvtColor(nv21, nv21, CV_YUV2RGB_NV21);
    //前置摄像头 需要旋转90度 或者 -270度
    //获得原图像宽高
    Size src_sz = nv21.size();
    //旋转后宽高
    Size dst_sz(src_sz.height, src_sz.width);
    //宽高的最大值
    int len = max(nv21.cols, nv21.rows);
    //获得仿射变换矩阵 该怎么旋转的一个信息记录者
    Mat matrix = getRotationMatrix2D(Point2f(len / 2.0, len / 2.0),-90.0, 1.0);

    //旋转
    warpAffine(nv21, nv21, matrix, dst_sz);
    if(isCanSaveImage){
        imwrite("sdcard/nv21.png", nv21);
    }
    matrix.release();
    return nv21;
};