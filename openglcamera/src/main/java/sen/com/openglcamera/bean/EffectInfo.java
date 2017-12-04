package sen.com.openglcamera.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/12/2 19:40
 * Des    :
 */

public class EffectInfo {

    //一般vs 不变
    public static final String NORMAL_VS = "resource/camera/picture_normal.vs";
    public static final String NORMAL_FS = "resource/camera/picture_normal.fs";

    /**
     * Normal 正常效果
     * Gaussion 高斯模糊
     * Anime 手绘，目前构思：这个是灰色+边缘检测结合,不过目前还不算好看，得借鉴别人的
     */
    public enum EffectType {
        Normal(0), Gaussion(1), Anime(2), Eclosion(3), DrarkGreen(4), DarkBrown(5), BlueCold(6);
        private int value;

        EffectType(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }

    public static String getType(EffectType effectType) {
        String res = "";
        switch (effectType) {
            case Normal:
                res = "resource/camera/picture_normal.fs";
                break;
            case Gaussion:
                res = "resource/effects/fullsrceenfbo_gaussion.fs";
                break;
            case Anime:
                res = "resource/effects/effect_anime_hander.fs";
                break;
            case Eclosion:
                res = "resource/effects/effect_eclosion.fs";
                break;
            case DrarkGreen:
                res = "resource/effects/effect_dark_greens.fs";
                break;
            case DarkBrown:
                res = "resource/effects/effect_brown.fs";
                break;
            case BlueCold:
                res = "resource/effects/effect_blue_cold.fs";
                break;
        }
        return res;
    }


    private String vsPath;
    private String fsPath;
    private String filterName;

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

    public static List<EffectInfo> getEffectInfos() {
        List<EffectInfo> filterInfos = new ArrayList<>();
        EffectInfo info0 = new EffectInfo();
        info0.setFilterName("正常");
        info0.setVsPath(NORMAL_VS);
        info0.setFsPath(NORMAL_FS);
        filterInfos.add(info0);
        EffectInfo info1 = new EffectInfo();
        info1.setFilterName("高斯模糊");
        info1.setVsPath(NORMAL_VS);
        info1.setFsPath("resource/effects/fullsrceenfbo_gaussion.fs");
        filterInfos.add(info1);

        EffectInfo info2 = new EffectInfo();
        info2.setFilterName("手绘");
        info2.setVsPath(NORMAL_VS);
        info2.setFsPath("resource/effects/effect_anime_hander.fs");
        filterInfos.add(info2);

        EffectInfo info3 = new EffectInfo();
        info3.setFilterName("羽化");
        info3.setVsPath(NORMAL_VS);
        info3.setFsPath("resource/effects/effect_eclosion.fs");
        filterInfos.add(info3);
        EffectInfo info4 = new EffectInfo();
        info4.setFilterName("怀旧");
        info4.setVsPath(NORMAL_VS);
        info4.setFsPath("resource/effects/effect_brown.fs");
        filterInfos.add(info4);
        return filterInfos;
    }
}
