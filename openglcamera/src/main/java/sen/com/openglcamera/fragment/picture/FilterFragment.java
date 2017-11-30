package sen.com.openglcamera.fragment.picture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;

import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.FilterInfo;
import sen.com.openglcamera.fragment.BaseFragment;
import sen.com.openglcamera.natives.BaseGLNative;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/31 16:45
 * Des    : 这个Fragment 是可以修改滤镜效果
 *         目前有无滤镜，美白，灰度，自定义
 *
 */

public class FilterFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBarOne,seekBarTwo,seekBarThree,seekFilterZoom;
    private List<FilterInfo> filterList;
    private double currentIndex;

    @Override
    protected void onLoadData() {

    }

    @Override
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_filter, container, false);
        return rootView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId()==R.id.seekFilterZoom){
            BaseGLNative.onChangeFileterZoom(seekFilterZoom.getProgress(), seekFilterZoom.getMax());
            return;
        }
        //只要seekBar有变化，都全部获取当前的进度
        //说明一点，如果当前，为美白效果的话，seekBarOne ，seekBarTwo 分别代表亮度，和对比度
        //如果为自定义效果，那么就为分别为红绿蓝
        int one = seekBarOne.getProgress();
        int two = seekBarTwo.getProgress();
        int three = seekBarThree.getProgress();

        //处理数据
        if (currentIndex == 3) {
            //自定义
            BaseGLNative.onChangeFileter(one,two,three,100,seekBarOne.getMax());
        } else if (currentIndex == 2) {
            //只需要，处理一下，不必要变化很大
            BaseGLNative.onChangeFileter(one/5,two/5,1,100,seekBarOne.getMax());
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
        filterList = FilterInfo.getFilterInfos(FilterInfo.PICTURE_DATA_TYPE);
        RadioButton btnNormal = (RadioButton) view.findViewById(R.id.btnNormal);
        RadioButton btnGray = (RadioButton) view.findViewById(R.id.btnGray);
        RadioButton btnSkinWhile = (RadioButton) view.findViewById(R.id.btnSkinWhile);
        RadioButton btnCustomer = (RadioButton) view.findViewById(R.id.btnCustomer);
        RadioButton btnRed = (RadioButton) view.findViewById(R.id.btnRed);
        RadioButton btnGreen = (RadioButton) view.findViewById(R.id.btnGreen);
        RadioButton btnBlue = (RadioButton) view.findViewById(R.id.btnBlue);

        btnNormal.setOnCheckedChangeListener(this);
        btnGray.setOnCheckedChangeListener(this);
        btnSkinWhile.setOnCheckedChangeListener(this);
        btnCustomer.setOnCheckedChangeListener(this);

        btnRed.setOnCheckedChangeListener(this);
        btnGreen.setOnCheckedChangeListener(this);
        btnBlue.setOnCheckedChangeListener(this);

        seekBarOne = (SeekBar) view.findViewById(R.id.seekBarOne);
        seekBarTwo = (SeekBar) view.findViewById(R.id.seekBarTwo);
        seekBarThree = (SeekBar) view.findViewById(R.id.seekBarThree);
        seekFilterZoom = (SeekBar) view.findViewById(R.id.seekFilterZoom);

        setSeekBarView(false,false,false);
        seekBarOne.setOnSeekBarChangeListener(this);
        seekBarTwo.setOnSeekBarChangeListener(this);
        seekBarThree.setOnSeekBarChangeListener(this);
        seekFilterZoom.setOnSeekBarChangeListener(this);
        seekBarOne.setMax(100);
        seekBarTwo.setMax(100);
        seekBarThree.setMax(100);
        seekFilterZoom.setMax(100);

     //   btnCustomer.setVisibility(View.VISIBLE);
        //滤镜的区域大小
//        seekFilterZoom.setProgress(50);
//        BaseGLNative.onChangeFileterZoom(seekFilterZoom.getProgress(), seekFilterZoom.getMax());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked)
            return;
        switch (buttonView.getId()) {

            case R.id.btnNormal:
                setSeekBarView(false,false,false);
                currentIndex = 0;
                BaseGLNative.onChangeVSFS(filterList.get(0).getVsPath(),filterList.get(0).getFsPath());
                break;
            case R.id.btnGray:
                currentIndex = 1;
                setSeekBarView(false,false,false);
                BaseGLNative.onChangeVSFS(filterList.get(1).getVsPath(),filterList.get(1).getFsPath());
                break;
            case R.id.btnSkinWhile:
                currentIndex = 2;
                setSeekBarView(false,false,false);
                BaseGLNative.onChangeVSFS(filterList.get(2).getVsPath(),filterList.get(2).getFsPath());
                break;
            case R.id.btnCustomer:
                currentIndex = 3;
                setSeekBarView(true,true,true);
                BaseGLNative.onChangeVSFS(filterList.get(3).getVsPath(),filterList.get(3).getFsPath());
                break;

            case R.id.btnRed:
                if( currentIndex != 3){
                    BaseGLNative.onChangeVSFS(filterList.get(3).getVsPath(),filterList.get(3).getFsPath());
                    setSeekBarView(false,false,false);
                }
                BaseGLNative.onChangeFileter(100,0,0,1,100);
                currentIndex = 3;
                break;
            case R.id.btnGreen:
                if( currentIndex != 3){
                    BaseGLNative.onChangeVSFS(filterList.get(3).getVsPath(),filterList.get(3).getFsPath());
                    setSeekBarView(false,false,false);
                }
                BaseGLNative.onChangeFileter(0,100,0,1,100);
                currentIndex = 3;
                break;
            case R.id.btnBlue:
                if( currentIndex != 3){
                    BaseGLNative.onChangeVSFS(filterList.get(3).getVsPath(),filterList.get(3).getFsPath());
                    setSeekBarView(false,false,false);
                }
                BaseGLNative.onChangeFileter(0,0,100,1,100);
                currentIndex = 3;
                break;


        }
    }

    public void setSeekBarView(boolean isOne,boolean isTwo,boolean isThree) {
        seekBarOne.setVisibility(isOne ? View.VISIBLE : View.GONE);
        seekBarTwo.setVisibility(isTwo ? View.VISIBLE : View.GONE);
        seekBarThree.setVisibility(isThree ? View.VISIBLE : View.GONE);
    }


}
