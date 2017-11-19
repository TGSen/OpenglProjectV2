package sen.com.openglcamera.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import sen.com.openglcamera.renderer.PictureRenderer;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:38
 * Des    :
 */

public class PictureSGLSurfaceView extends GLSurfaceView{

    private PictureRenderer readyRenderer;
    public PictureSGLSurfaceView(Context context) {
        this(context,null);
    }

    public PictureSGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        readyRenderer = new PictureRenderer();
        setEGLContextClientVersion(2);
        setRenderer(readyRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


}
