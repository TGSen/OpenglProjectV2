package sen.com.openglcamera.fragment.picture;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import sen.com.openglcamera.R;
import sen.com.openglcamera.bean.EffectInfo;
import sen.com.openglcamera.commadapter.OnItemOnclickLinstener;
import sen.com.openglcamera.commadapter.RecycleCommonAdapter;
import sen.com.openglcamera.commadapter.SViewHolder;
import sen.com.openglcamera.fragment.BaseFragment;
import sen.com.openglcamera.natives.BaseGLNative;


/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/27 22:22
 * Des    : 这个Fragment 是特效
 *         目前有文字贴图
 */

public class EffectsFragment extends BaseFragment  {

    private int currentIndex;
    private RecyclerView recyView;
    private RecycleCommonAdapter pictureAdapter;
    public RecyclerView.RecycledViewPool pool ;

    private OnAddObjecteDrawFrameLisenter lisenter;

    public interface OnAddObjecteDrawFrameLisenter {
        void add(Object object);
    }

    public void setOnAddObjecteDrawFrameLisenter(OnAddObjecteDrawFrameLisenter lisenter) {
        this.lisenter = lisenter;
    }

    @Override
    protected void onLoadData() {
        //可见时，回到已选择的那个地方
        if(isVisible && recyView!=null){
            recyView.smoothScrollToPosition(currentIndex);
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
        recyView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager previewManager = new LinearLayoutManager(mActivity);
        previewManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyView.setLayoutManager(previewManager);
        currentIndex = 0;
        final List<EffectInfo> list = EffectInfo.getEffectInfos();
        pictureAdapter = new RecycleCommonAdapter<EffectInfo>(mActivity, list,
                R.layout.layout_item_camera) {
            @Override
            public void bindItemData(SViewHolder holder, EffectInfo itemData, int position) {

                if (currentIndex == position) {
                    holder.setTextAndTextSeleted(R.id.item_name, itemData.getFilterName(),true);
                } else {
                    holder.setTextAndTextSeleted(R.id.item_name, itemData.getFilterName(),false);
                }
            }
        };

        recyView.setAdapter(pictureAdapter);
        //移动到选择的地方，当然这个移动有点突然，不太好看，这个到时优化一下
        recyView.smoothScrollToPosition(currentIndex);
        // recyView.setRecycledViewPool(pool);
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
                BaseGLNative.onChangeVSFS(list.get(position).getVsPath(),list.get(position).getFsPath());

            }
        });
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
//                Bitmap bitmap = BitmapUtils.creatBitmapFromText("我是唐家森",mActivity);
//                if (lisenter != null)
//                    lisenter.add(bitmap);
                //
            }
        });
        builder.setView(dialog);
        builder.show();
    }


}
