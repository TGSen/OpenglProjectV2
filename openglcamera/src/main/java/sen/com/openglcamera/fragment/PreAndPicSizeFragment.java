package sen.com.openglcamera.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.ItemCameraSetting;
import sen.com.openglcamera.commadapter.OnItemOnclickLinstener;
import sen.com.openglcamera.commadapter.RecycleCommonAdapter;
import sen.com.openglcamera.commadapter.SViewHolder;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/01 16:33
 * Des    : 这个Fragment 是可以修改，预览分辨率
 *
 */

public class PreAndPicSizeFragment extends BaseFragment {
    private int currentIndex;
    private RecyclerView pictureSizeRecyView;
    private RecycleCommonAdapter pictureAdapter;
    public RecyclerView.RecycledViewPool pool ;



    @Override
    protected void onLoadData() {
        //可见时，回到已选择的那个地方
        if(isVisible && pictureSizeRecyView!=null){
            pictureSizeRecyView.smoothScrollToPosition(currentIndex);
        }
    }

    @Override
    public View initParentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_change_preview, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pictureSizeRecyView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager previewManager = new LinearLayoutManager(mActivity);
        previewManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictureSizeRecyView.setLayoutManager(previewManager);
        Bundle bundle = getArguments();
        List<ItemCameraSetting> preList = (List<ItemCameraSetting>) bundle.getParcelableArrayList("PreViewInfo").get(0);
        currentIndex = bundle.getInt("CurrentCameInfo");
        pictureAdapter = new RecycleCommonAdapter<ItemCameraSetting>(mActivity, preList,
                R.layout.layout_item_camera) {
            @Override
            public void bindItemData(SViewHolder holder, ItemCameraSetting itemData, int position) {

                if (currentIndex == position) {
                    holder.setTextAndTextSeleted(R.id.item_name, itemData.name,true);
                } else {
                    holder.setTextAndTextSeleted(R.id.item_name, itemData.name,false);
                }
            }
        };

        pictureSizeRecyView.setAdapter(pictureAdapter);
        //移动到选择的地方，当然这个移动有点突然，不太好看，这个到时优化一下
        pictureSizeRecyView.smoothScrollToPosition(currentIndex);
        pictureSizeRecyView.setRecycledViewPool(pool);
        pictureAdapter.setOnItemClickLinstener(new OnItemOnclickLinstener() {
            @Override
            public void itemOnclickLinstener(int position) {
                if (currentIndex == position) {
                    return;
                }
                //改变样式
                pictureAdapter.notifyItemChanged(position);
                pictureAdapter.notifyItemChanged(currentIndex);
                //然后改变currentCameInfo数据
                currentIndex =position;
//                newCurrentCameInfo.setPicWith(picList.get(position).getWith());
//                newCurrentCameInfo.setPicHeigth(picList.get(position).getHeigth());

            }
        });
    }
}
