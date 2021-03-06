package sen.com.openglcamera.activity;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.camera.CameraOldVersion;
import sen.com.openglcamera.fragment.CameraInfoFragmentV2;
import sen.com.openglcamera.natives.BaseGLNative;
import sen.com.openglcamera.view.CameraButtonView;
import sen.com.openglcamera.view.CameraSGLSurfaceView;

public class CameraMainActivity extends AppCompatActivity implements View.OnClickListener, CameraInfoFragmentV2.OnSettingChangeLinstener {
    CameraSGLSurfaceView mSGlSurfaceView;
    private int mCameraId;
    private CameraOldVersion mCamera;
    private CameraButtonView takePicture;
    private final String LTag = "sen_";
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化AssetManager
        BaseGLNative.initAssetManager(getAssets(),BaseGLNative.NATIVE_SENCE_CAMERA);
        rootView = View.inflate(this, R.layout.activity_camera_opengl,null);
        setContentView(rootView);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSGlSurfaceView = (CameraSGLSurfaceView) rootView.findViewById(R.id.camera_glview);
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        mCamera = new CameraOldVersion(this);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
        mCamera.setTakePicturePath(path);
        if (mCamera.openCamera(mCameraId)) {
            mSGlSurfaceView.init(mCamera);
            takePicture = (CameraButtonView) findViewById(R.id.takePicture);
            takePicture.setOnClickListener(this);
        }

    }
    //对button需要做多次点击才行，要不会弹出多个
    //对camera setting
    public void settingView(View view){

        CameraSettingInfo info =mCamera.getCameraSettingInfo();
        //获取当前的设置
        CurrentCameInfo currentCameInfo = mCamera.getCurrentSettingInfo();
        CameraInfoFragmentV2 dialog = new CameraInfoFragmentV2();
        Bundle bundle = new Bundle();
        bundle.putSerializable("CameraSettingInfo",info);
        bundle.putSerializable("CurrentCameInfo",currentCameInfo);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "CameraInfoFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onDestroy() {
        if(mSGlSurfaceView!=null){
            mSGlSurfaceView.destroyDrawingCache();
        }
        if(mCamera!=null){
            mCamera.releaseCamera();
            mCamera=null;
        }
        BaseGLNative.releaseNative(BaseGLNative.NATIVE_SENCE_CAMERA);
        super.onDestroy();

    }
    //点击屏幕对焦
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() ==MotionEvent.ACTION_DOWN){
            if (mCamera!=null){
                mCamera.requestCameraFocus();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.takePicture:
                if (mCamera != null)
                    mCamera.takePhoto();
                break;
        }

    }

    @Override
    public void onSettingChange(CurrentCameInfo currentCameInfo) {
        mCamera.setCameraInfo(currentCameInfo);
//        Log.e("sen_",currentCameInfo.toString());
    }
}


