//
// Created by liuxiang on 2017/11/15.
//

#ifndef BIGEYE_TRACK_H
#define BIGEYE_TRACK_H

#include <opencv2/opencv.hpp>
#include <vector>
#include "face_alignment.h"

using namespace cv;
using namespace std;

class CascadeDetectorAdapter : public DetectionBasedTracker::IDetector {
public:
    CascadeDetectorAdapter(cv::Ptr<CascadeClassifier> detector) : IDetector(), detector(detector) {
    }

    void detect(const Mat &Image, vector<Rect> &objects) {
        detector->detectMultiScale(Image, objects, scaleFactor, minNeighbours, 0, minObjSize,
                                   maxObjSize);
    }

    ~CascadeDetectorAdapter() {
    }

private:
    CascadeDetectorAdapter();

    Ptr<CascadeClassifier> detector;
};

class EyesTracker {
public:
    bool isCanSaveImage;//作为测试输出一次图像到sd 卡
    EyesTracker(const char *faceModel, const char *eyeModel);

    void startTracking();

    void stopTracking();
    void detectEyes(void *pixels, int& width, int& height, vector<Rect2f> &eyes);

private:

    Mat nv21ToRGB(void *pixels, int width, int height);
    void getEyes(Mat src,vector<Rect2f> &faces);
private:
    Ptr<DetectionBasedTracker> tracker;
    Ptr<seeta::FaceAlignment> faceAlignment;


};


#endif //BIGEYE_TRACK_H
