package sen.com.openglcamera.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import sen.com.openglcamera.camera.CameraOldVersion;
import sen.com.openglcamera.renderer.CameraRenderer;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:09
 * Des    :
 */

public class CameraSGLSurfaceView extends GLSurfaceView {
    private CameraRenderer readyRenderer;
    public CameraSGLSurfaceView(Context context) {
        this(context,null);
    }

    public CameraSGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init(CameraOldVersion mCamera) {
        readyRenderer = new CameraRenderer(this);
        readyRenderer.init( mCamera);
        setEGLContextClientVersion(2);
        setRenderer(readyRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    //点击屏幕对焦
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() ==MotionEvent.ACTION_DOWN){
            if (readyRenderer!=null){
                readyRenderer.requestCameraFocus();
            }
        }
        return super.onTouchEvent(event);
    }



}
