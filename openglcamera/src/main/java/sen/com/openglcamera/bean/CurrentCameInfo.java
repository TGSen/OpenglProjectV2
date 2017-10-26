package sen.com.openglcamera.bean;

import java.io.Serializable;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/25 23:40
 * Des    : 当前的Camera 的设置
 */

public class CurrentCameInfo implements Serializable {
    private int picIndex;
    private int preIndex;
    private int filterIndex;
    private int picWith;
    private int picHeigth;
    private int preWith;
    private int preHeigth;

    public int getPicIndex() {
        return picIndex;
    }

    public void setPicIndex(int picIndex) {
        this.picIndex = picIndex;
    }

    public int getPreIndex() {
        return preIndex;
    }

    public void setPreIndex(int preIndex) {
        this.preIndex = preIndex;
    }

    public int getFilterIndex() {
        return filterIndex;
    }

    public void setFilterIndex(int filterIndex) {
        this.filterIndex = filterIndex;
    }

    public int getPicWith() {
        return picWith;
    }

    public void setPicWith(int picWith) {
        this.picWith = picWith;
    }

    public int getPicHeigth() {
        return picHeigth;
    }

    public void setPicHeigth(int picHeigth) {
        this.picHeigth = picHeigth;
    }

    public int getPreHeigth() {
        return preHeigth;
    }

    public void setPreHeigth(int preHeigth) {
        this.preHeigth = preHeigth;
    }

    public int getPreWith() {
        return preWith;
    }

    public void setPreWith(int preWith) {
        this.preWith = preWith;
    }


}
