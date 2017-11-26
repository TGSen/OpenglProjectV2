//
//

#include "camera/EyesTracker.h"
#include "camera/sggl.h"
EyesTracker::EyesTracker(const char *face, const char *alignment) {
    Ptr<CascadeDetectorAdapter> mainDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(face));
    Ptr<CascadeDetectorAdapter> trackingDetector = makePtr<CascadeDetectorAdapter>(
            makePtr<CascadeClassifier>(face));
    DetectionBasedTracker::Parameters detectorParams;
    tracker = makePtr<DetectionBasedTracker>(mainDetector, trackingDetector, detectorParams);
    faceAlignment = makePtr<seeta::FaceAlignment>(alignment);
    isCanSaveImage = true;
}

//开启追踪器
void EyesTracker::startTracking() {
    tracker->run();
}

//关闭追踪器
void EyesTracker::stopTracking() {
    tracker->stop();
}

void EyesTracker::detectEyes(void *pixels, int& width, int& height, vector<Rect2f> &eyes) {
    Mat mat = nv21ToRGB(pixels,  width,  height);
    width = mat.cols;
    height = mat.rows;
    getEyes(mat, eyes);
}


Mat EyesTracker::nv21ToRGB(void *pixels, int width, int height){
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


void EyesTracker::getEyes(Mat src, vector<Rect2f> &eyes) {
    cvtColor(src, src, CV_RGB2GRAY);
    if(isCanSaveImage){
        isCanSaveImage = false;
        imwrite("sdcard/gray.png", src);
    }
    //增加对比度 让物体更突出
    equalizeHist(src, src);
    tracker->process(src);
    vector<Rect> faces;
    tracker->getObjects(faces);

    //找到人脸
    if (faces.size()) {
        LOGE("找到了******");
        Rect face = faces[0];
        seeta::ImageData image_data(src.cols, src.rows);
        image_data.data = src.data;

        //因为能识别5个点 分别为 两个眼睛 鼻子和嘴巴左右两侧
        seeta::FacialLandmark points[5];
        seeta::FaceInfo faceInfo;
        seeta::Rect rect;
        rect.x = face.x;
        rect.y = face.y;
        rect.width = face.width;
        rect.height = face.height;
        faceInfo.bbox = rect;
        faceAlignment->PointDetectLandmarks(image_data, faceInfo, points);
        for (int i = 0; i < 5; ++i) {
            eyes.push_back(Rect2f(points[i].x, points[i].y, 0, 0));
        }
    }else{
        LOGE("木有找到了******");
    }
}