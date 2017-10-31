package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import sen.com.openglcamera.R;
import sen.com.openglcamera.natives.CameraSGLNative;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/31 16:45
 * Des    : 这个Fragment 是可以修改，预览的形状，和形状背景颜色
 *          1.形状，目前有普通，多边形
 *          2.背景颜色：黑和白
 *          3.如果是普通形状，由于铺满屏幕的四边形，修改白色和底色无法体现，多边行也是
 */

public class ShapeFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    @Override
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_shader, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioButton btnBlack = (RadioButton) view.findViewById(R.id.btnBlack);
        RadioButton btnWhile = (RadioButton) view.findViewById(R.id.btnWhile);
        RadioButton btnNormal = (RadioButton) view.findViewById(R.id.btnNormal);
        RadioButton btnCircle = (RadioButton) view.findViewById(R.id.btnCircle);
        RadioButton btnMultiple = (RadioButton) view.findViewById(R.id.btnMultiple);

        btnBlack.setOnCheckedChangeListener(this);
        btnWhile.setOnCheckedChangeListener(this);
        btnNormal.setOnCheckedChangeListener(this);
        btnCircle.setOnCheckedChangeListener(this);
        btnMultiple.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked)
            return;
        switch (buttonView.getId()) {
            case R.id.btnBlack:
                CameraSGLNative.onChangeBgColor(0.0f,0.0f,0.0f,0.0f);
                break;
            case R.id.btnWhile:
                CameraSGLNative.onChangeBgColor(1.0f,1.0f,1.0f,1.0f);
                break;
            case R.id.btnNormal:
                CameraSGLNative.onChangeShape(CameraSGLNative.CameraShape.Normal.getValue());
                break;
            case R.id.btnCircle:
                CameraSGLNative.onChangeShape(CameraSGLNative.CameraShape.Circle.getValue());
                break;
            case R.id.btnMultiple:
                CameraSGLNative.onChangeShape(CameraSGLNative.CameraShape.Multiple.getValue());
                break;
        }
    }
}
