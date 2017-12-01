package sen.com.openglcamera.fragment.picture;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import sen.com.openglcamera.R;
import sen.com.openglcamera.fragment.BaseFragment;
import sen.com.openglcamera.natives.BaseGLNative;
import sen.com.openglcamera.utils.BitmapUtils;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/27 22:22
 * Des    : 这个Fragment 是特效
 *         目前有文字贴图
 */

public class EffectsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    private double currentIndex;

    @Override
    protected void onLoadData() {

    }

    @Override
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_effects, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioButton btnTextEffect = (RadioButton) view.findViewById(R.id.btnTextEffect);
        RadioButton btnPicture = (RadioButton) view.findViewById(R.id.btnPicture);
        btnTextEffect.setOnCheckedChangeListener(this);
        btnPicture.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked)
            return;
        switch (buttonView.getId()) {

            case R.id.btnTextEffect:
                currentIndex = 0;
                showEidtDialog();
                break;
            case R.id.btnPicture:
                currentIndex = 1;
                break;
        }
    }

    private void showEidtDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("文字以空格作为下一行");

        builder.setCancelable(false);
        builder.setTitle("请输入文字");
        LayoutInflater inflater =mActivity.getLayoutInflater();
        View   dialog = inflater.inflate(R.layout.layout_eidtext_dialog,(ViewGroup) mActivity.findViewById(R.id.dialog));
        final EditText editText = (EditText) dialog.findViewById(R.id.editText);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              //  Toast.makeText(mActivity, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                Bitmap bitmap = BitmapUtils.creatBitmapFromText("我是唐家森",mActivity);
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                if(bitmap!=null){
                    BitmapUtils.saveBitmap(path,bitmap);
                    BaseGLNative.addTextEffect(bitmap);
                }
              //
            }
        });
        builder.setView(dialog);
        builder.show();
    }

}
