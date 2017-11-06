package sen.com.openglcamera.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import sen.com.openglcamera.R;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/30 11:47
 * Des    : 闪屏页，其实啥也没做，只是做了一个主题加载
 *          为了冷启动，就是显示图标而不是白屏
 */

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CAMERA_CODE = 0;
    private View rootView;
    String [] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_splash,null);
        setContentView(rootView);
        checkPermission();
    }

    private void checkPermission() {
        // 检查摄像头权限是否已经有效
        if (EasyPermissions.hasPermissions(this,permissions)) {
            // 摄像头权限还未得到用户的同意
            // 权限有效
            hasPaermissions();

        } else {
            againRequestPermission("申请");
            //不喜欢这样式
//            EasyPermissions.requestPermissions(this, "需要摄像头,存储权限",
//                    REQUEST_CAMERA_CODE, permissions);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }


    private void againRequestPermission(String btnMsg) {
            // 如果用户已经拒绝过，那么提供额外的权限说明
        // 摄像头权限已经被拒绝
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])|| ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[1])) {
//            Snackbar.make(rootView, "需要Camera,存储权限",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(btnMsg, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(SplashActivity.this,
//                                    permissions,
//                                    REQUEST_CAMERA_CODE);
//                        }
//                    })
//                    .show();
//        }else{
//            ActivityCompat.requestPermissions(SplashActivity.this,
//                    permissions,
//                    REQUEST_CAMERA_CODE);
//        }

        Snackbar.make(rootView, "需要Camera,存储权限",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(btnMsg, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SplashActivity.this,
                                    permissions,
                                    REQUEST_CAMERA_CODE);
                        }
                    })
                    .show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //这里响应的是AppSettingsDialog点击取消按钮的效果
                againRequestPermission("再次申请");
            } else {
                hasPaermissions();
            }
        }else{
            againRequestPermission("再次申请");
        }
    }

    private void hasPaermissions() {
        Intent intent = new Intent(this,VideoRecoderActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(perms.size()==permissions.length){
            hasPaermissions();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        againRequestPermission("申请");
    }
}
