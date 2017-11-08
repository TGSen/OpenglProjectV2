package sen.com.openglcamera.mediacodec;

import android.media.MediaCodecInfo;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/6 17:50
 * Des    :VideoParms 的一些参数，到时在应用时可以提供修改
 */

public class VideoParms {
    private String mimeType = "video/avc"; // H.264 Advanced Video Coding ，编码格式
    private int bitRate = 512 * 1024; // 512 kbps maybe better，码流
    private int frameRate = 32; // 32帧率
    private int iframeInterval = 1; // 1s 时间间隔的关键帧，google/grafika IFRAME_INTERVAL =5
    // API 21 新加入了MediaCodec的所有硬件解码都支持的 COLOR_FormatYUV420Flexible 格式。它并不是一种确定的 YUV420 格式
    private int colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible;
    private int width = 720;
    private int height = 1280;
    private String rootPath ;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getIframeInterval() {
        return iframeInterval;
    }

    public void setIframeInterval(int iframeInterval) {
        this.iframeInterval = iframeInterval;
    }

    public int getColorFormat() {
        return colorFormat;
    }

    public void setColorFormat(int colorFormat) {
        this.colorFormat = colorFormat;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
