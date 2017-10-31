package fragment;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.bean.FilterInfo;
import sen.com.openglcamera.bean.ItemCameraSetting;
import sen.com.openglcamera.commadapter.OnItemOnclickLinstener;
import sen.com.openglcamera.commadapter.RecycleCommonAdapter;
import sen.com.openglcamera.commadapter.SViewHolder;
import sen.com.openglcamera.natives.CameraSGLNative;


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

public class CameraInfoFragmentV2 extends DialogFragment implements OnClickListener, SeekBar.OnSeekBarChangeListener {
    private Activity mActivity;
    private RecyclerView pictureSizeRecyView, previewSizeRecyView, filterRecyView;
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
    private SeekBar seekBarR;
    private SeekBar seekBarG;
    private SeekBar seekBarB;
    private SeekBar seekBarA;
    private View layoutView;
    private TextView tv_adjust_info;
    private int currentFilter;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private String mTiltes[] = {"形色","滤镜","PreViewSize","PictureSize"};


//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		if(isChecked){
//			if(seekBarR ==null ||seekBarG==null|| seekBarB ==null ||seekBarA==null){
//				layoutView = viewStubRGB.inflate();
//				seekBarR = (SeekBar) layoutView.findViewById(R.id.seekBarR);
//				seekBarG = (SeekBar) layoutView.findViewById(R.id.seekBarG);
//				seekBarB = (SeekBar) layoutView.findViewById(R.id.seekBarB);
//				seekBarA = (SeekBar) layoutView.findViewById(R.id.seekBarA);
//				seekBarR.setMax(100);
//				seekBarG.setMax(100);
//				seekBarB.setMax(100);
//				seekBarA.setMax(100);
//				seekBarR.setOnSeekBarChangeListener(this);
//				seekBarG.setOnSeekBarChangeListener(this);
//				seekBarB.setOnSeekBarChangeListener(this);
//				seekBarA.setOnSeekBarChangeListener(this);
//			}else {
//				layoutView.setVisibility(View.VISIBLE);
//			}
//		}else{
//			if(layoutView!=null)
//			layoutView.setVisibility(View.GONE);
//		}
//	}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //只要seekBar有变化，都全部获取当前的进度
        int progressR = seekBarR.getProgress();
        int progressG = seekBarG.getProgress();
        int progressB = seekBarB.getProgress();
        int progressA = seekBarA.getProgress();
        //处理比较特殊的数据，由于对比度可以是负数
        if (filterList.get(currentFilter).isSeekbarHalf()) {
            //如果是美白的话，对比度从中间开始，50< 为负数
            //那么第二个数
            if (progressG == 50) {
                progressG = 0;
            } else {
                progressG = progressG - 50;
            }
        }
        //底层根据这个来计算分量
        int max = seekBarA.getMax();
        CameraSGLNative.onChangeFileter(progressR, progressG, progressB, progressA, max);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }




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

        mTablayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager)view. findViewById(R.id.viewPager);
        for (int i = 0;i<mTiltes.length;i++){
            mTablayout.addTab(mTablayout.newTab().setText(mTiltes[i]));
        }
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShapeFragment());
        fragments.add(new ShapeFragment());
        fragments.add(new ShapeFragment());
        fragments.add(new ShapeFragment());
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),mTiltes,fragments);
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
        initDialog();
//		initData();
//		initLinster();
        return dialog;
    }



    private void initLinster() {
        pictureAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if (newCurrentCameInfo.getPicIndex() == position) {
                    return;
                }
                //改变样式
                pictureAdapter.notifyItemChanged(position);
                pictureAdapter.notifyItemChanged(newCurrentCameInfo.getPicIndex());
                //然后改变currentCameInfo数据
                newCurrentCameInfo.setPicIndex(position);
                newCurrentCameInfo.setPicWith(picList.get(position).getWith());
                newCurrentCameInfo.setPicHeigth(picList.get(position).getHeigth());

            }
        });

        preViewAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if (newCurrentCameInfo.getPreIndex() == position) {
                    return;
                }
                //改变样式
                preViewAdapter.notifyItemChanged(position);
                preViewAdapter.notifyItemChanged(newCurrentCameInfo.getPreIndex());
                newCurrentCameInfo.setPreIndex(position);
                newCurrentCameInfo.setPreWith(preList.get(position).getWith());
                newCurrentCameInfo.setPreHeigth(preList.get(position).getHeigth());
            }
        });

        filterAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                currentFilter = position;
                //改变样式
                filterAdapter.notifyItemChanged(position);
                filterAdapter.notifyItemChanged(newCurrentCameInfo.getFilterIndex());
                newCurrentCameInfo.setFilterIndex(position);
                FilterInfo info = filterList.get(position);
                CameraSGLNative.onChangeVSFS(info.getVsPath(), info.getFsPath());
                if (info.isShowSeekbar()) {
                    Log.e("sen_", info.getFilterName());
                    createSeekBar(true, info.getCountSeekBar(), info.getInfo());
                } else {
                    createSeekBar(false, 0, "");
                }
            }
        });

        tvSaveSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLinstener != null) {
                    mLinstener.onSettingChange(newCurrentCameInfo);
                    //不能在这里dimiss 要不onSettingChange 会执行完毕
                    dialog.dismiss();
                }
            }
        });


    }

    //目前作为测试
    private void createSeekBar(boolean isShow, int countSeekBar, String info) {
        if (!isShow) {
            if (layoutView != null)
                layoutView.setVisibility(View.GONE);
            return;
        }
        if (seekBarR == null || seekBarG == null || seekBarB == null || seekBarA == null) {
            layoutView = viewStubRGB.inflate();
            seekBarR = (SeekBar) layoutView.findViewById(R.id.seekBarR);
            seekBarG = (SeekBar) layoutView.findViewById(R.id.seekBarG);
            seekBarB = (SeekBar) layoutView.findViewById(R.id.seekBarB);
            seekBarA = (SeekBar) layoutView.findViewById(R.id.seekBarA);
            tv_adjust_info = (TextView) layoutView.findViewById(R.id.tv_adjust_info);
            tv_adjust_info.setText(info);
            if (countSeekBar == 2) {
                //只要前面两个好了
                seekBarB.setVisibility(View.GONE);
                seekBarA.setVisibility(View.GONE);
            }


            seekBarR.setMax(100);
            seekBarG.setMax(100);
            seekBarB.setMax(100);
            seekBarA.setMax(100);
            seekBarR.setOnSeekBarChangeListener(this);
            seekBarG.setOnSeekBarChangeListener(this);
            seekBarB.setOnSeekBarChangeListener(this);
            seekBarA.setOnSeekBarChangeListener(this);
        } else {
            layoutView.setVisibility(View.VISIBLE);
            tv_adjust_info.setText(info);
            if (countSeekBar == 2) {
                //只要前面两个好了
                seekBarB.setVisibility(View.GONE);
                seekBarA.setVisibility(View.GONE);
            } else {
                seekBarB.setVisibility(View.VISIBLE);
                seekBarA.setVisibility(View.VISIBLE);
            }
        }

        if (filterList.get(currentFilter).isSeekbarHalf()) {
            //如果是美白的话，对比度从中间开始，50< 为负数
            seekBarG.setProgress(50);
        } else {
            seekBarG.setProgress(0);
        }
        seekBarR.setProgress(0);
        seekBarB.setProgress(0);
        seekBarA.setProgress(0);


    }


    private void initData() {
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
        filterList = FilterInfo.getFilterInfos();


        pictureAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity, picList,
                R.layout.layout_item_camera) {
            @Override
            public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {
                if (newCurrentCameInfo.getPicIndex() == position) {
                    holder.setTextAndTextColor(R.id.item_name, itemData.name, ContextCompat.getColor(mActivity, R.color.red_ff52));
                } else {
                    holder.setTextAndTextColor(R.id.item_name, itemData.name, ContextCompat.getColor(mActivity, R.color.gray61));
                }
            }
        };

        preViewAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity,
                preList, R.layout.layout_item_camera) {
            @Override
            public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {
                if (newCurrentCameInfo.getPreIndex() == position) {
                    holder.setTextAndTextColor(R.id.item_name, itemData.name, ContextCompat.getColor(mActivity, R.color.red_ff52));
                } else {
                    holder.setTextAndTextColor(R.id.item_name, itemData.name, ContextCompat.getColor(mActivity, R.color.gray61));
                }

            }
        };
        filterAdapter = new RecycleCommonAdapter<FilterInfo>(mActivity,
                filterList, R.layout.layout_item_camera) {
            @Override
            public void bindItemData(SViewHolder holder, FilterInfo itemData, int position) {
                if (newCurrentCameInfo.getFilterIndex() == position) {
                    holder.setTextAndTextColor(R.id.item_name, itemData.getFilterName(), ContextCompat.getColor(mActivity, R.color.red_ff52));
                } else {
                    holder.setTextAndTextColor(R.id.item_name, itemData.getFilterName(), ContextCompat.getColor(mActivity, R.color.gray61));
                }
            }


        };
        pictureSizeRecyView.setAdapter(pictureAdapter);
        previewSizeRecyView.setAdapter(preViewAdapter);
        filterRecyView.setAdapter(filterAdapter);
        //移动到选择的地方，当然这个移动有点突然，不太好看，这个到时优化一下
        pictureSizeRecyView.smoothScrollToPosition(newCurrentCameInfo.getPicIndex());
        previewSizeRecyView.smoothScrollToPosition(newCurrentCameInfo.getPreIndex());

//		filterRecyView.smoothScrollToPosition(newCurrentCameInfo.getFilterIndex());
        cameraSettingInfos = null;
        bundle = null;
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



//		radioButton.setTextColor(ContextCompat.getColorStateList(mActivity,R.color.colorPrimary));
//		pictureSizeRecyView= (RecyclerView) dialog.findViewById(R.id.pictureSizeRecyView);
//        previewSizeRecyView = (RecyclerView) dialog.findViewById(R.id.previewSizeRecyView);
//        filterRecyView = (RecyclerView) dialog.findViewById(R.id.filterRecyView);
//		tvSaveSetting = (TextView) dialog.findViewById(R.id.tv_save_setting);
//		viewStubRGB = (ViewStub) dialog.findViewById(R.id.viewStubRGB);
//		 dialog.findViewById(R.id.changeShape).setOnClickListener(new OnClickListener() {
//			 @Override
//			 public void onClick(View v) {
//				 CameraSGLNative.onChangeShape(CameraSGLNative.CameraShape.Circle.getValue());
//			 }
//		 });
//		LinearLayoutManager pictureManager = new LinearLayoutManager(mActivity);
//		pictureManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//		LinearLayoutManager previewManager = new LinearLayoutManager(mActivity);
//		previewManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//		LinearLayoutManager filterManager = new LinearLayoutManager(mActivity);
//		filterManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//		pictureSizeRecyView.setLayoutManager(pictureManager);
//		previewSizeRecyView.setLayoutManager(previewManager);
//		filterRecyView.setLayoutManager(filterManager);
//
//		pictureSizeRecyView.setRecycledViewPool(mViewPool);
//		previewSizeRecyView.setRecycledViewPool(mViewPool);
//		filterRecyView.setRecycledViewPool(mViewPool);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View view) {


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
