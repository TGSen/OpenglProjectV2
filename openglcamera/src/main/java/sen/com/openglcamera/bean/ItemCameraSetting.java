package sen.com.openglcamera.bean;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/25 22:41
 * Des    :
 */

public class ItemCameraSetting {
    private int with ;
    private int heigth;
    public int index; //索引或者是路径的
    public String name ;

    public ItemCameraSetting(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public ItemCameraSetting(int with, int heigth) {
        this.with = with;
        this.heigth = heigth;
    }

    public int getWith() {
        return with;
    }

    public void setWith(int with) {
        this.with = with;
    }

    public int getHeigth() {
        return heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }
}
