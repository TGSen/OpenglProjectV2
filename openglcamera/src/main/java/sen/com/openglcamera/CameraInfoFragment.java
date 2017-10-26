package sen.com.openglcamera;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sen.com.openglcamera.bean.CameraSettingInfo;
import sen.com.openglcamera.bean.CurrentCameInfo;
import sen.com.openglcamera.bean.ItemCameraSetting;
import sen.com.openglcamera.commadapter.OnItemOnclickLinstener;
import sen.com.openglcamera.commadapter.RecycleCommonAdapter;
import sen.com.openglcamera.commadapter.SViewHolder;


public class CameraInfoFragment extends DialogFragment implements OnClickListener {
	private Activity mActivity;
	private RecyclerView pictureSizeRecyView ,previewSizeRecyView ,filterRecyView;
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
	private List<ItemCameraSetting> filterList;
	private TextView tvSaveSetting;
	private OnSettingChangeLinstener mLinstener;
	public interface OnSettingChangeLinstener{
		void onSettingChange(CurrentCameInfo currentCameInfo);
	}

	public void setOnSettingChangeLinstener(OnSettingChangeLinstener linstener){
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

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Light_NoTitleBar);
		initView();
		initData();
		initLinster();
		return dialog;
	}

	private void initLinster() {
		pictureAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
			@Override
			public void itemOnclickLinstener(int position) {
				if(newCurrentCameInfo.getPicIndex()==position){
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
				if(newCurrentCameInfo.getPreIndex()==position){
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
				if(newCurrentCameInfo.getFilterIndex()==position){
					return;
				}
				//改变样式
				filterAdapter.notifyItemChanged(position);
				filterAdapter.notifyItemChanged(newCurrentCameInfo.getFilterIndex());
				newCurrentCameInfo.setFilterIndex(position);
			}
		});

		tvSaveSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mLinstener!=null){
					mLinstener.onSettingChange(newCurrentCameInfo);
					//不能在这里dimiss 要不onSettingChange 会执行完毕
					dialog.dismiss();
				}
			}
		});
	}

	private void initData() {
		// // 获取数据
		 Bundle bundle = getArguments();
		CameraSettingInfo cameraSettingInfos= (CameraSettingInfo) bundle.getSerializable("CameraSettingInfo");
		oldCurrentCameInfo = (CurrentCameInfo) bundle.getSerializable("CurrentCameInfo");
		newCurrentCameInfo = (CurrentCameInfo) oldCurrentCameInfo.clone();
		Iterator<Camera.Size> picIterator = cameraSettingInfos.getSupportedPictureSize().iterator();
		picList = new ArrayList<>();
		int index =0;
		while (picIterator.hasNext()) {
			Camera.Size next = picIterator.next();
			ItemCameraSetting setting = new ItemCameraSetting(next.width,next.height);
			setting.name =next.width + "x" + next.height;
			setting.index =index;
			index++;
			picList.add(setting);
		}

		Iterator<Camera.Size> preIterator = cameraSettingInfos.getSupportedPreviewSizes().iterator();
		preList = new ArrayList<>();
		 index =0;
		while (preIterator.hasNext()) {
			Camera.Size next = preIterator.next();
			ItemCameraSetting setting = new ItemCameraSetting(next.width,next.height);
			setting.name =next.width + "x" + next.height;
			setting.index =index;
			index++;
			preList.add(setting);
		}
		filterList = new ArrayList<>();
		filterList.add(new ItemCameraSetting(0,"无滤镜"));
		filterList.add(new ItemCameraSetting(1,"黑白滤镜"));

		pictureAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity, picList,
				R.layout.layout_item_camera) {
			@Override
			public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {
				if(newCurrentCameInfo.getPicIndex()==position){
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.red_ff52));
				}else{
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.gray61));
				}
			}
		};

		preViewAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity,
				preList, R.layout.layout_item_camera) {
			@Override
			public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {
				if(newCurrentCameInfo.getPreIndex()==position){
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.red_ff52));
				}else{
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.gray61));
				}

			}
		};
		filterAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity,
				filterList, R.layout.layout_item_camera) {
			@Override
			public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {
				if(newCurrentCameInfo.getFilterIndex()==position){
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.red_ff52));
				}else{
					holder.setTextAndTextColor(R.id.item_name,itemData.name, ContextCompat.getColor(mActivity,R.color.gray61));
				}
			}
		};
		pictureSizeRecyView.setAdapter(pictureAdapter);
		previewSizeRecyView.setAdapter(preViewAdapter);
		filterRecyView.setAdapter(filterAdapter);
		//移动到选择的地方，当然这个移动有点突然，不太好看，这个到时优化一下
		pictureSizeRecyView.smoothScrollToPosition(newCurrentCameInfo.getPicIndex());
		previewSizeRecyView.smoothScrollToPosition(newCurrentCameInfo.getPreIndex());
		filterRecyView.smoothScrollToPosition(newCurrentCameInfo.getFilterIndex());
		cameraSettingInfos =null;
		bundle =null;
	}




	private void initView() {
		dialog = new Dialog(mActivity, R.style.Dialog);
		dialog.getWindow().setBackgroundDrawable(null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
		dialog.setContentView(R.layout.fragment_setting);
		dialog.setCanceledOnTouchOutside(false); // 外部点击取消
		// 设置宽度为屏宽, 靠近屏幕底部。
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.gravity = Gravity.BOTTOM; // 紧贴底部
		lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
		lp.height = getActivity().getWindowManager().getDefaultDisplay()
				.getHeight()/2;
		lp.dimAmount = 0.2f;
		window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		pictureSizeRecyView= (RecyclerView) dialog.findViewById(R.id.pictureSizeRecyView);
        previewSizeRecyView = (RecyclerView) dialog.findViewById(R.id.previewSizeRecyView);
        filterRecyView = (RecyclerView) dialog.findViewById(R.id.filterRecyView);
		tvSaveSetting = (TextView) dialog.findViewById(R.id.tv_save_setting);

		LinearLayoutManager pictureManager = new LinearLayoutManager(mActivity);
		pictureManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		LinearLayoutManager previewManager = new LinearLayoutManager(mActivity);
		previewManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		LinearLayoutManager filterManager = new LinearLayoutManager(mActivity);
		filterManager.setOrientation(LinearLayoutManager.HORIZONTAL);

		pictureSizeRecyView.setLayoutManager(pictureManager);
		previewSizeRecyView.setLayoutManager(previewManager);
		filterRecyView.setLayoutManager(filterManager);

		pictureSizeRecyView.setRecycledViewPool(mViewPool);
		previewSizeRecyView.setRecycledViewPool(mViewPool);
		filterRecyView.setRecycledViewPool(mViewPool);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();


	}

	@Override
	public void onClick(View view) {


	}


}
