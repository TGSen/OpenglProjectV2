package sen.com.openglcamera.renderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sen.com.openglcamera.camera.CameraOldVersion;
import sen.com.openglcamera.natives.CameraSGLNative;
import sen.com.openglcamera.view.CameraSGLSurfaceView;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:06
 * Des    :
 */

public class CameraRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    GLSurfaceView mGLSurfaceView;
    private Context mContext;
    private CameraOldVersion mCamera;
    private SurfaceTexture mSurfaceTexture;

    public CameraRenderer(CameraSGLSurfaceView cameraSGLSurfaceView) {
        mGLSurfaceView = cameraSGLSurfaceView;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSurfaceTexture = CameraSGLNative.getSurfaceTexture();
        if(mSurfaceTexture!=null){
            Log.e("sen_","mSurfaceTexture is not null");
            mSurfaceTexture.setOnFrameAvailableListener(this);
        }
        mCamera.setPreviewTexture(mSurfaceTexture);
        mCamera.startPreview();

        CameraSGLNative.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        CameraSGLNative.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null &&!CameraSGLNative.isStop) {
            mSurfaceTexture.updateTexImage();

            CameraSGLNative.onDrawFrame();
        }else {
            mCamera.stopPreview();
            Log.e("sen_","java stop");
        }

    }

    public void init(CameraSGLSurfaceView cameraSGLSurfaceView, CameraOldVersion camera, Activity context) {
        mContext = context;
        mGLSurfaceView = cameraSGLSurfaceView;
        mCamera = camera;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if(!CameraSGLNative.isStop) {
            mGLSurfaceView.requestRender();
        }

    }





}
