package sen.com.openglcamera.renderer;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
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

public class CameraRendererV1 implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {
    GLSurfaceView mGLSurfaceView;
    private Context mContext;
    private CameraOldVersion mCamera;
    private SurfaceTexture mSurfaceTexture;

    public CameraRendererV1(CameraSGLSurfaceView cameraSGLSurfaceView) {
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
        GLES20.glEnable(GL10.GL_POINT_SMOOTH);
        GLES20.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
        GLES20.glEnable(GL10.GL_LINE_SMOOTH);
        GLES20.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
        GLES20.glEnable(GL10.GL_MULTISAMPLE);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ritio = (float)width/(float)height;
        gl.glFrustumf(-ritio, ritio,-1f,1f,3f,7f);
        CameraSGLNative.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null &&!CameraSGLNative.isStop) {
            mSurfaceTexture.updateTexImage();
            CameraSGLNative.onDrawFrame(mCamera.getCurrentData(),
                    mCamera.getPreViewSize().width,
                    mCamera.getPreViewSize().height);
        }else {
            mCamera.stopPreview();
            Log.e("sen_","java stop");
        }

    }

    public void init(CameraOldVersion camera) {
        mCamera = camera;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if(!CameraSGLNative.isStop) {
            mGLSurfaceView.requestRender();
        }

    }


    public void requestCameraFocus() {
        if(mCamera!=null){
            mCamera.requestCameraFocus();
        }
    }
}
