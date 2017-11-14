package sen.com.openglcamera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import java.io.File;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.camera.CameraOldVersion;
import sen.com.openglcamera.fragment.CameraInfoFragmentV2;
import sen.com.openglcamera.natives.CameraSGLNative;
import sen.com.openglcamera.utils.PermissionsUitls;
import sen.com.openglcamera.view.CameraButtonView;
import sen.com.openglcamera.view.CameraSGLSurfaceView;

import static sen.com.openglcamera.R.id.takePicture;

public class PictureVideoActivity extends AppCompatActivity implements View.OnClickListener, CameraInfoFragmentV2.OnSettingChangeLinstener, CompoundButton.OnCheckedChangeListener {
    CameraSGLSurfaceView mSGlSurfaceView;
    private int mCameraId;
    private CameraOldVersion mCamera;
    private CameraButtonView cameraButtonView;
    private final String LTag = "sen_";
    private View rootView;
    String[] mPermissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CAMERA_WRITE_CODE = 2;
    private static final int APP_SETTINGS_CODE = 0x002;
    private int requestPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //初始化AssetManager
        CameraSGLNative.initAssetManager(getAssets());
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
            cameraButtonView = (CameraButtonView) findViewById(takePicture);
            cameraButtonView.setCameraInstence(mCamera);
            cameraButtonView.setOnClickListener(this);
        }
        RadioButton recoderPicture = (RadioButton) findViewById(R.id.recoderPicture);
        RadioButton recoderVideo = (RadioButton) findViewById(R.id.recoderVideo);
        recoderPicture.setOnCheckedChangeListener(this);
        recoderPicture.setChecked(true);
        recoderVideo.setOnCheckedChangeListener(this);
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
        if(PermissionsUitls.hasPermissions(this,mPermissions)){
            if (mCamera != null) {
                mCamera.startPreview();
            }
        }else{
            ActivityCompat.requestPermissions(PictureVideoActivity.this,
                    mPermissions,
                    REQUEST_CAMERA_WRITE_CODE);
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
        CameraSGLNative.releaseNative();
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.takePicture:
               if (PermissionsUitls.hasPermissions(this, mPermissions)) {
                   cameraButtonView.handleClick();
               } else {
                   ActivityCompat.requestPermissions(PictureVideoActivity.this,
                           mPermissions,
                           REQUEST_CAMERA_WRITE_CODE);
               }
            break;
       }

    }

    @Override
    public void onSettingChange(CurrentCameInfo currentCameInfo) {
        mCamera.setCameraInfo(currentCameInfo);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        switch (buttonView.getId()) {
            case R.id.recoderVideo:
                cameraButtonView.setChangeStuats(CameraButtonView.MODE_VIDEO);
                break;
            case R.id.recoderPicture:
                cameraButtonView.setChangeStuats(CameraButtonView.MODE_PICTRUE);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CAMERA_WRITE_CODE == requestCode) {
            if (grantResults.length > 1  && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //保证camera 和写入权限都有，这种情况下肯定camera 有的
               // cameraButtonView.handleClick();
            } else {
                requestPermissions++;
                if (requestPermissions >= 2) {
                    //被拒两次后就去设置，这种情况有可能是禁止后不再询问
                    Snackbar.make(rootView, "请设置Camera,存储权限",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivityForResult(
                                            new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                    .setData(Uri.fromParts("package", getPackageName(), null)),
                                            APP_SETTINGS_CODE);
                                }
                            })
                            .show();
                } else {
                    //再次尝试申请一次
                    ActivityCompat.requestPermissions(PictureVideoActivity.this,
                            mPermissions,
                            REQUEST_CAMERA_WRITE_CODE);

                }
            }
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_SETTINGS_CODE) {
            //当去设置camera camera 有可能被取消了
            if (PermissionsUitls.hasPermissions(this, mPermissions)) {

            } else {
                Snackbar.make(rootView, "请设置必要权限",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("设置", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivityForResult(
                                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .setData(Uri.fromParts("package", getPackageName(), null)),
                                        APP_SETTINGS_CODE);
                            }
                        })
                        .show();
            }
        }
    }
}


