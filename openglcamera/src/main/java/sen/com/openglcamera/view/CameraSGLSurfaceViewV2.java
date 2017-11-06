package sen.com.openglcamera.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import sen.com.openglcamera.camera.CameraNewVersion;
import sen.com.openglcamera.renderer.CameraRendererV2;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/11 13:09
 * Des    :
 */

public class CameraSGLSurfaceViewV2 extends GLSurfaceView {
    private CameraRendererV2 sRenderer;

    public CameraSGLSurfaceViewV2(Context context) {
        this(context, null);
    }

    public CameraSGLSurfaceViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init(CameraNewVersion mCamera) {
        //设置opengl 的渲染环境版本2，如果需要是3.0的话，那么在c++ 层去做
        setEGLContextClientVersion(2);
        sRenderer = new CameraRendererV2(this);
        sRenderer.init(this, mCamera);
        setRenderer(sRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


}
