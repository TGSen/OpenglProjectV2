package sen.com.openglcamera.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.mediacodec.VideoRecoderV2;
import sen.com.openglcamera.utils.BitmapUtils;

/**
 * Author : 唐家森
 * Version: 1.0
 * Des    : 相机的操作，拍照，预览
 * 目前在NDK 层做了加载Asseset/Res 的加载GLSL 代码，和图片bmp
 */

public class CameraOldVersion implements Camera.PreviewCallback {
    private Activity mActivity;
    private int mCameraId;
    private Camera mCamera;
    private final String LTag = "sen_";

    private int screen;
    private final static int SCREEN_PORTRAIT = 0;
    private final static int SCREEN_LANDSCAPE_LEFT = 90;
    private final static int SCREEN_LANDSCAPE_RIGHT = 270;
    private boolean takePicture;
    private String rootPicPath;
    private CameraSettingInfo cameraSettingInfo;
    private CurrentCameInfo currentCameInfo;

    private boolean isRecoder ;
    private VideoRecoderV2 videoRecoder;
    private byte[] callbackBuffer;
    private SurfaceTexture surfaceTexture;
    private byte[] currentData;

    public Camera.Size getPreViewSize (){
        return mCamera.getParameters().getPreviewSize();
    }

    //获取所有CameInfo
    public CameraSettingInfo getCameraSettingInfo(){
        return cameraSettingInfo;
   }

    public CurrentCameInfo getCurrentSettingInfo(){
        return currentCameInfo;
    }

    public void setCameraInfo(CurrentCameInfo cameInfo){
        if(mCamera!=null){
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                boolean isChange =false;
                //有变化才设置
                if (cameInfo.getPicWith()>0 && cameInfo.getPicHeigth()>0&&
                        currentCameInfo.getPicWith()!=cameInfo.getPicWith()&&
                        currentCameInfo.getPicHeigth()!=cameInfo.getPicHeigth()){
                    Log.e("sen_","pic change");
                    parameters.setPictureSize(cameInfo.getPicWith(),cameInfo.getPicHeigth());
                    isChange =true;
                }
                if(cameInfo.getPreWith()>0 && cameInfo.getPreHeigth()>0&&
                        currentCameInfo.getPreWith()!=cameInfo.getPreWith()&&
                        currentCameInfo.getPreHeigth()!=cameInfo.getPreHeigth()){
                    parameters.setPreviewSize(cameInfo.getPreWith(),cameInfo.getPreHeigth());
                    Log.e("sen_","pre change");
                    isChange =true;
                }

                if(isChange){
                    //有变化才从新
                    mCamera.stopPreview();
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                }
                currentCameInfo = (CurrentCameInfo) cameInfo.clone();

            }catch (Exception e){
                e.printStackTrace();
                Log.e("sen_",e.getMessage());
            }
        }
    }

    public CameraOldVersion(Activity activity) {
        mActivity = activity;
    }

    public void setTakePicturePath(String path){
        this.rootPicPath = path;
    }

    public boolean openCamera(int cameraId) {
        try {
            cameraSettingInfo = new CameraSettingInfo();
            currentCameInfo = new CurrentCameInfo();
            mCameraId = cameraId;
            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");
          //  parameters.setPreviewFormat(ImageFormat.JPEG); //YUV 预览图像的像素格式
            setPreviewOrientation(parameters);
            setPreviewSize( parameters);
            setPictureSize(parameters);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(parameters);

            mCamera.setPreviewCallbackWithBuffer(this);
            callbackBuffer = new byte[getPreViewSize ().width*getPreViewSize ().height*3/2];
            mCamera.addCallbackBuffer(callbackBuffer);
          //  mCamera.addCallbackBuffer(new byte[getPreViewSize ().width*getPreViewSize ().height*3/2]);
         //   mCamera.addCallbackBuffer(new byte[getPreViewSize ().width*getPreViewSize ().height*3/2]);
        } catch (Exception e) {
            Log.e(LTag,e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //设置图片分辨率，拍照用到
    private void setPictureSize(Camera.Parameters parameters) {
        Log.e("sen_","setPictureSize");
        List<Camera.Size> supportedPictureSize = parameters.getSupportedPictureSizes();
        if (supportedPictureSize.size()<0)
            return;
        cameraSettingInfo.setSupportedPictureSize(supportedPictureSize);
        //默认取这个值
        int index = Math.min(supportedPictureSize.size()/2+1,supportedPictureSize.size());
        currentCameInfo.setPicIndex(index);
        currentCameInfo.setPicWith(supportedPictureSize.get(index).width);
        currentCameInfo.setPicHeigth(supportedPictureSize.get(index).height);
        parameters.setPictureSize(currentCameInfo.getPicWith(),currentCameInfo.getPicHeigth());
    }
    //设置预览的分辨率
    private void setPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        if (supportedPreviewSizes.size()<0)
            return;
        cameraSettingInfo.setSupportedPreviewSizes(supportedPreviewSizes);
        int index = Math.min(supportedPreviewSizes.size()/2+1,supportedPreviewSizes.size());
        currentCameInfo.setPreIndex(index);
        currentCameInfo.setPreWith(supportedPreviewSizes.get(index).width);
        currentCameInfo.setPreHeigth(supportedPreviewSizes.get(index).height);
        parameters.setPreviewSize(currentCameInfo.getPreWith(),currentCameInfo.getPreHeigth());
    }
    //设置预览的角度
    private void setPreviewOrientation(Camera.Parameters parameters) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        screen = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                screen = SCREEN_PORTRAIT;
                break;
            case Surface.ROTATION_90: // 横屏 左边是头部(home键在右边)
                screen = SCREEN_LANDSCAPE_LEFT;
                break;
            case Surface.ROTATION_180:
                screen = 180;
                break;
            case Surface.ROTATION_270:// 横屏 头部在右边
                screen = SCREEN_LANDSCAPE_RIGHT;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + screen) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - screen + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }



    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }
    //设置Camera surfaceTexture
    public void setPreviewTexture(SurfaceTexture surfaceTexture) {
        if (mCamera != null) {
            try {
                this.surfaceTexture = surfaceTexture;
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //设置Camera setPreviewHolder
    public void setPreviewHolder(SurfaceHolder holder) {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    // 自动对焦
    public void requestCameraFocus() {
        if (mCamera != null) {
            mCamera.autoFocus(null);
        }
    }

    //拍照，到时改造使用HandlerThread
    public void takePhoto() {
        if (mCamera == null || takePicture) {
            return;
        }
        takePicture = true;
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                camera.startPreview();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BitmapUtils.saveBitmap(rootPicPath,data);
                        takePicture =false;

                    }
                }).start();
            }
        });
    }

    public boolean getCameraTakePicStuta(){
        return takePicture;
    }


    public void setVideoRecoder(VideoRecoderV2 videoRecoder) {
        this.videoRecoder = videoRecoder;
    }

    public void startRecoder() {
        isRecoder = true;
        if(videoRecoder!=null){
            videoRecoder.startRecoder();
        }
    }

    public void stopRecoder(){
//        isRecoder =false;
//        if(videoRecoder!=null){
//            videoRecoder.stopRecoder();
//        }
    }

    public byte[] getCurrentData() {
        synchronized (CameraOldVersion.class){
            return currentData;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(isRecoder && videoRecoder!=null){

            videoRecoder.encodeData(data);
        }

    }

    public Camera getCameraInstance() {
        return mCamera;
    }
}
