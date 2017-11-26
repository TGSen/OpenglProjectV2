package sen.com.openglcamera.fragment.picture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import sen.com.openglcamera.R;
import sen.com.openglcamera.fragment.BaseFragment;
import sen.com.openglcamera.natives.BaseGLNative;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/31 16:45
 * Des    : 这个Fragment 是可以修改，预览的形状，和形状背景颜色
 *          1.形状，目前有普通，多边形
 *          2.背景颜色：黑和白
 *          3.如果是普通形状，由于铺满屏幕的四边形，修改白色和底色无法体现，多边行也是
 */

public class ShapeFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBarSize;
    private SeekBar seekBarCount;

    @Override
    protected void onLoadData() {

    }

    @Override
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_shader, container, false);
        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //只要seekBar有变化，都全部获取当前的进度
        switch (seekBar.getId()) {
            case R.id.seekBarSize:
                int size = seekBar.getProgress();
                //更改形状的长度
                BaseGLNative.onChangeShapeSize(size, seekBar.getMax());
                break;
            case R.id.seekBarCount:
                int count = seekBar.getProgress();
                if (count >= 3) {
                    //更改多边形边数
                    BaseGLNative.onChangeShapeCount(count);
                }
                break;
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RadioButton btnBlack = (RadioButton) view.findViewById(R.id.btnBlack);
        RadioButton btnWhile = (RadioButton) view.findViewById(R.id.btnWhile);
        RadioButton btnNormal = (RadioButton) view.findViewById(R.id.btnNormal);
        RadioButton btnCircle = (RadioButton) view.findViewById(R.id.btnCircle);
        RadioButton btnMultiple = (RadioButton) view.findViewById(R.id.btnMultiple);
//        RadioGroup radiogroud_bg = view.findViewById(R.id.radiogroud_bg);
//        radiogroud_bg.setVisibility(View.GONE);
        btnBlack.setOnCheckedChangeListener(this);
        btnWhile.setOnCheckedChangeListener(this);
        btnNormal.setOnCheckedChangeListener(this);
        btnCircle.setOnCheckedChangeListener(this);
        btnMultiple.setOnCheckedChangeListener(this);

        seekBarSize = (SeekBar) view.findViewById(R.id.seekBarSize);
        seekBarCount = (SeekBar) view.findViewById(R.id.seekBarCount);
        seekBarCount.setVisibility(View.GONE);
        seekBarSize.setVisibility(View.GONE);
        seekBarCount.setOnSeekBarChangeListener(this);
        seekBarSize.setOnSeekBarChangeListener(this);
        seekBarSize.setMax(100);
        seekBarCount.setMax(50);
        btnWhile.setChecked(true);
        btnNormal.setChecked(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked)
            return;
        switch (buttonView.getId()) {
            case R.id.btnBlack:
                BaseGLNative.onChangeBgColor(0.0f,0.0f,0.0f,0.0f);
                break;
            case R.id.btnWhile:
                BaseGLNative.onChangeBgColor(1.0f,1.0f,1.0f,1.0f);
                break;
            case R.id.btnNormal:
                setSeekBarView(false,false);
                BaseGLNative.onChangeShape(BaseGLNative.CameraShape.Normal.getValue(), 4);
                break;
            case R.id.btnCircle:
                setSeekBarView(false,true);
                BaseGLNative.onChangeShape(BaseGLNative.CameraShape.Circle.getValue(), 200);
                break;
            case R.id.btnMultiple:
                setSeekBarView(true,true);
                BaseGLNative.onChangeShape(BaseGLNative.CameraShape.Multiple.getValue(), 4);
                break;
        }
    }

    public void setSeekBarView(boolean isCount,boolean isSize) {
        seekBarCount.setVisibility(isCount ? View.VISIBLE : View.GONE);
        seekBarSize.setVisibility(isSize ? View.VISIBLE : View.GONE);
    }


}
