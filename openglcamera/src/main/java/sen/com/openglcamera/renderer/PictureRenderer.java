package sen.com.openglcamera.renderer;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sen.com.openglcamera.natives.BaseGLNative;
import sen.com.openglcamera.utils.BitmapUtils;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/13 13:40
 * Des    : 图片的渲染器
 */

public class PictureRenderer implements GLSurfaceView.Renderer {

    private final String mRootPath;
    private boolean isdrawFinish;
    private int mWidth;
    private int mHeight;
    //使用弱引用还是？待我想想
    private RenderLinstener renderLinstener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private void drawFinish(){
            if(!isdrawFinish &&renderLinstener!=null){
                isdrawFinish = true;
                //必须得在主线程下
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        renderLinstener.onDrawOneFrameFinish();
                    }
                },1200);

            }
    }


    public void setRenderListener(RenderLinstener listener){
        renderLinstener = listener;
    }


    public PictureRenderer() {
        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        BaseGLNative.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Bitmap bitmap = BitmapUtils.decodeFile(mRootPath + "test.jpg", 200, 300);
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        BaseGLNative.onBeforeSurfaceCreated(bitmap);
        // PictureSGLNativie.onSurfaceChanged(width, height);
        BaseGLNative.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        BaseGLNative.onDrawFrame(null,0,0);
        drawFinish();
    }

}
