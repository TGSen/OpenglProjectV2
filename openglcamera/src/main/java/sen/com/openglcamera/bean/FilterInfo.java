package sen.com.openglcamera.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/28 18:31
 * Des    : 滤镜
 */

public class FilterInfo {
    private int filterId;
    private String vsPath;
    private String fsPath;
    private String filterName;
    private boolean isShowSeekbar; //是否显示seekbar来控制，目前只有在自定义的滤镜才有
    private boolean seekbarHalf; // 有些需要是 -1 ，0,1 这样的数，中间为0，如果低于50% 是负数，高于为正数
    private int countSeekBar;
    private String info;
//    public FilterInfo(int filterId,String filterName, String vsPath, String fsPath, boolean isShowSeekbar) {
//        this.filterId = filterId;
//        this.vsPath = vsPath;
//        this.fsPath = fsPath;
//        this.filterName = filterName;
//        this.isShowSeekbar = isShowSeekbar;
//    }

    public boolean isSeekbarHalf() {
        return seekbarHalf;
    }

    public void setSeekbarHalf(boolean seekbarHalf) {
        this.seekbarHalf = seekbarHalf;
    }

    public int getCountSeekBar() {
        return countSeekBar;
    }

    public void setCountSeekBar(int countSeekBar) {
        this.countSeekBar = countSeekBar;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public String getVsPath() {
        return vsPath;
    }

    public void setVsPath(String vsPath) {
        this.vsPath = vsPath;
    }

    public String getFsPath() {
        return fsPath;
    }

    public void setFsPath(String fsPath) {
        this.fsPath = fsPath;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public boolean isShowSeekbar() {
        return isShowSeekbar;
    }

    public void setShowSeekbar(boolean showSeekbar) {
        isShowSeekbar = showSeekbar;
    }

    //先产生固定的数据先

    /**
     * camera_normal.vs 使用同一份
     * fs 就不是
     */
    public static List<FilterInfo> getFilterInfos (){
        List<FilterInfo> filterInfos = new ArrayList<>();
        FilterInfo info0= new FilterInfo();
        info0.setFilterId(0);
        info0.setFilterName("无滤镜");
        info0.setVsPath("resource/camera/camera_normal.vs");
        info0.setFsPath("resource/camera/camera_normal.fs");
        info0.setShowSeekbar(false);
        filterInfos.add(info0);


        FilterInfo info1= new FilterInfo();
        info1.setFilterId(1);
        info1.setFilterName("灰色");
        info1.setVsPath("resource/camera/camera_normal.vs");
        info1.setFsPath("resource/camera/camera_filter_gray.fs");
        info1.setShowSeekbar(false);
        filterInfos.add(info1);

        FilterInfo info2= new FilterInfo();
        info2.setFilterId(1);
        info2.setFilterName("美白");
        info2.setVsPath("resource/camera/camera_normal.vs");
        info2.setFsPath("resource/camera/camera_filter_skinwhile_v2.fs");
        info2.setShowSeekbar(true);
        info2.setSeekbarHalf(true);
        info2.setCountSeekBar(2);
        info2.setInfo("分别调节亮度,对比度");
        filterInfos.add(info2);



        FilterInfo info3= new FilterInfo();
        info3.setFilterId(3);
        info3.setFilterName("自定义");
        info3.setVsPath("resource/camera/camera_normal.vs");
        info3.setFsPath("resource/camera/camera_filter_customer.fs");
        info3.setShowSeekbar(true);
        info3.setCountSeekBar(4);
        info3.setInfo("分别调节RGBA");
        filterInfos.add(info3);

        return filterInfos;
    }
}
