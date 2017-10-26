package sen.com.openglcamera.bean;

import android.hardware.Camera;

import java.io.Serializable;
import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/10/25 21:59
 * Des    :
 */

public class CameraSettingInfo implements Serializable {
    private List<Camera.Size> supportedPictureSize;
    private List<Camera.Size> supportedPreviewSizes;

    public List<Camera.Size> getSupportedPictureSize() {
        return supportedPictureSize;
    }

    public void setSupportedPictureSize(List<Camera.Size> supportedPictureSize) {
        this.supportedPictureSize = supportedPictureSize;
    }

    public List<Camera.Size> getSupportedPreviewSizes() {
        return supportedPreviewSizes;
    }

    public void setSupportedPreviewSizes(List<Camera.Size> supportedPreviewSizes) {
        this.supportedPreviewSizes = supportedPreviewSizes;
    }
}
