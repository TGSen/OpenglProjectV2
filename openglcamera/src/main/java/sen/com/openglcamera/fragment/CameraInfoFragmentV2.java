package sen.com.openglcamera.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.bean.FilterInfo;
import sen.com.openglcamera.bean.ItemCameraSetting;
import sen.com.openglcamera.commadapter.RecycleCommonAdapter;


/**
 * Author : 唐家森
 * Version: 1.0
 * Des    : 这里先不保存用户的设置，也就是每次进来或许从新设置，等我想要的功能完毕后再来完善
 * 注意一点：由于我这里使用的是DialogFragment 它是Frament (不是v4包下的)，那么获取到的getChildFragmentManger,肯定不是v4 的framentManger
 *如果结合viewpager +frament来实现的话，而v4 包中FragmentPagerAdapter，支持v4 的FramentManger
 * 解决方案：
 * 1.可以修改为viewpager+view
 * 2.viewpager +frament,但是需要使用v13的FragmentPagerAdapter，当然可以从v13取出这个类来，而不必引入这么大jar
 * 该项目就采用方案二，但是直接引用了，但是不能使用v4 的frament,该项目就不支持3.0以前的了，哈哈哈
 *
 */

public class CameraInfoFragmentV2 extends DialogFragment implements OnClickListener {
    private Activity mActivity;
    private Dialog dialog;
    //设置RecyclerView.RecycledViewPool 让RecyclerView 共用一个view 池 提高性能
    RecyclerView.RecycledViewPool mViewPool = new RecyclerView.RecycledViewPool() {
        @Override
        public RecyclerView.ViewHolder getRecycledView(int viewType) {
            RecyclerView.ViewHolder scrap = super.getRecycledView(viewType);
            return scrap;
        }

        @Override
        public void putRecycledView(RecyclerView.ViewHolder scrap) {
            super.putRecycledView(scrap);
        }

        @Override
        public String toString() {
            return "ViewPool in adapter " + Integer.toHexString(hashCode());
        }
    };
    private RecycleCommonAdapter pictureAdapter;
    private RecycleCommonAdapter preViewAdapter;
    private RecycleCommonAdapter filterAdapter;
    private CurrentCameInfo oldCurrentCameInfo;
    private CurrentCameInfo newCurrentCameInfo;
    private List<ItemCameraSetting> picList;
    private List<ItemCameraSetting> preList;
    private List<FilterInfo> filterList;
    private TextView tvSaveSetting;
    private OnSettingChangeLinstener mLinstener;
    private ViewStub viewStubRGB;
    private View layoutView;
    private TextView tv_adjust_info;
    private int currentFilter;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private String mTiltes[] = {"形色","滤镜","PreViewSize","PictureSize"};
    private List<Fragment> fragments;

    public interface OnSettingChangeLinstener {
        void onSettingChange(CurrentCameInfo currentCameInfo);
    }

    public void setOnSettingChangeLinstener(OnSettingChangeLinstener linstener) {
        mLinstener = linstener;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mActivity = (Activity) activity;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        ImageButton btnSave = (ImageButton) view.findViewById(R.id.btnSave);
        btnClose.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        mTablayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)view. findViewById(R.id.viewPager);

        for (int i = 0;i<mTiltes.length;i++){
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
        fragments = new ArrayList<>();
        initFragmentData();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), mTiltes, fragments);
        //tablayout 和viewpager 联动
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setCurrentItem(0);
    }

    private void initFragmentData() {
        initViewPagerData();
        fragments.add(new ShapeFragment());
        fragments.add(new FilterFragment());
        PreAndPicSizeFragment preViewSize = new PreAndPicSizeFragment();
        Bundle bundle = new Bundle();
        ArrayList list = new ArrayList();
        list.add(preList);
        bundle.putParcelableArrayList("PreViewInfo", list);
        bundle.putInt("CurrentCameInfo", newCurrentCameInfo.getPreIndex());
        preViewSize.setArguments(bundle);
        fragments.add(preViewSize);

        PreAndPicSizeFragment picSize = new PreAndPicSizeFragment();
        Bundle bundlePic = new Bundle();
        list.clear();
        list.add(picList);
        bundlePic.putParcelableArrayList("PreViewInfo", list);
        bundlePic.putInt("CurrentCameInfo", newCurrentCameInfo.getPicIndex());
        picSize.setArguments(bundlePic);
        fragments.add(picSize);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
        initDialog();
        return dialog;
    }

    private void initViewPagerData() {
        // // 获取数据
        Bundle bundle = getArguments();
        CameraSettingInfo cameraSettingInfos = (CameraSettingInfo) bundle.getSerializable("CameraSettingInfo");
        oldCurrentCameInfo = (CurrentCameInfo) bundle.getSerializable("CurrentCameInfo");
        newCurrentCameInfo = (CurrentCameInfo) oldCurrentCameInfo.clone();
        Iterator<Camera.Size> picIterator = cameraSettingInfos.getSupportedPictureSize().iterator();
        picList = new ArrayList<>();
        int index = 0;
        while (picIterator.hasNext()) {
            Camera.Size next = picIterator.next();
            ItemCameraSetting setting = new ItemCameraSetting(next.width, next.height);
            setting.name = next.width + "x" + next.height;
            setting.index = index;
            index++;
            picList.add(setting);
        }
        Iterator<Camera.Size> preIterator = cameraSettingInfos.getSupportedPreviewSizes().iterator();
        preList = new ArrayList<>();
        index = 0;
        while (preIterator.hasNext()) {
            Camera.Size next = preIterator.next();
            ItemCameraSetting setting = new ItemCameraSetting(next.width, next.height);
            setting.name = next.width + "x" + next.height;
            setting.index = index;
            index++;
            preList.add(setting);
        }

    }


    private void initDialog() {
        dialog = new Dialog(mActivity, R.style.Dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setCanceledOnTouchOutside(false); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogAnimation);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay()
                .getHeight() * 2 / 3;
        lp.dimAmount = 0.0f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                dialog.dismiss();
                break;
            case R.id.btnSave:
                //应该需要保存用户的选择，这里先不做
                dialog.dismiss();
                break;
        }

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private String[] titleList;
        private List<Fragment> fragmentList;

        public ViewPagerAdapter(FragmentManager fm, String[] titleList, List<Fragment> fragmentList) {
            super(fm);
            this.titleList = titleList;
            this.fragmentList = fragmentList;
        }




        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList[position];
        }
    }




}
