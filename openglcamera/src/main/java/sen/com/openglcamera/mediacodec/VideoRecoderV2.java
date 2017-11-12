package sen.com.openglcamera.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

import static android.content.ContentValues.TAG;
import static android.media.MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/11/6 17:05
 * Des    : 视频录制
 */

public class VideoRecoderV2 implements BaseRecoder {
    private static final String Tag = "sen_";
    //用来标志，编码器是否初始成功
    private boolean isReadyEncoder = false;
    private final VideoParms videoParms;
    private MediaCodec mediaCodec;
    private byte[] mI420Data;
    private final long timeoutUs = 10000;//微秒
    private MediaCodec.BufferInfo mBufferInfo;
    private int mTrackIndex = -1;
    private MediaMuxer mediaMuxer;
    private boolean mMuxerStarted;
    private MediaFormat mediaFormat;


    public VideoRecoderV2(VideoParms videoParms) {
        this.videoParms = videoParms;
        //4个y对应一个U 和一个V
        mBufferInfo = new MediaCodec.BufferInfo();

    }

    private OnRecoderListener listener;
    private static final int ERROR_CODER_INIT = 0; //initRecoder() 初始化时出错
    private static final int ERROR_MEDIAMUXER_INIT =1 ; //MEDIAMUXER初始化时出错

    public void setVideoRecoderError(OnRecoderListener listener) {
        this.listener = listener;
    }

    private void showError(int code, String msg) {
        if (listener != null) {
            listener.onRecoderError(code, msg);
        }
    }

    @Override
    public void initRecoder() {
        try {
            //create对应格式的编码器
            mediaCodec = MediaCodec.createEncoderByType(videoParms.getMimeType());
            //create format 来设置编码器的格式
            mediaFormat = MediaFormat.createVideoFormat(videoParms.getMimeType(),
                    videoParms.getWidth(), videoParms.getHeight());
            //------------设置
            //设置帧数
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, videoParms.getFrameRate());
            //设置码流
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, videoParms.getBitRate());
            //设置关键帧时间
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, videoParms.getIframeInterval());
            //从网上找来，修改视频颜色不对的bug
//            MediaCodecInfo codecInfo =selectCodec(videoParms.getMimeType());
//            int COLOR_FORMAT =selectColorFormat(codecInfo,videoParms.getMimeType());
            //设置颜色格式
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, videoParms.getColorFormat());
            //设置给mediaCodec

            isReadyEncoder = true;


            mI420Data = new byte[videoParms.getWidth() * videoParms.getHeight() * 3 / 2];
            Log.e(Tag, "video encoder init finish");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Tag, "video encoder init error\n" + e.getMessage());
            showError(ERROR_CODER_INIT, e.getMessage());
        }
    }

    /**
     * 小于0表示MediaCodec没有成功decode，大于等于0表示成功decode。
     * 编码器正常的流程是输入一次数据输出一次数据，但少数时候，编码器并不会严格按照这个流程执行，
     * 有可能往编码器输入了几次数据，编码器都没有准备好输出数据，那么编码器将不会输出数据，
     * 有的时候编码器把之前没有处理好的数据，在这一次输出数据的时候，
     * 又会输出多次数据，所以这里用了while，在结束的时候去查询是否编码器的还有数据没有输出来。
     * @param data
     */

    @Override
    public void encodeData(byte[] data) {

        if (mediaCodec == null || mediaMuxer ==null) {
            Log.e(Tag, "encodeData but mediaCodec is init error");
            return;
        }
        //先转格式
      mI420Data = NativeSwapYUV.nV21ToI420(data,videoParms.getWidth(),videoParms.getHeight(),data.length);
//        NV21toI420SemiPlanar(data, mI420Data, videoParms.getWidth(), videoParms.getHeight());
        // dequeueInputBuffer方法提取出要处理的部分（也就是一个ByteBuffer数据流）把这一部分放到缓存区。
        //返回要用有效数据填充的输入缓冲区的索引。
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(timeoutUs);
        //获得一个ByteBuffer的数组，(准备处理的数据)
        ByteBuffer inputBuffers;
        ByteBuffer outputBuffers;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            inputBuffers = mediaCodec.getInputBuffer(inputBufferIndex);
        } else {
            inputBuffers = mediaCodec.getInputBuffers()[inputBufferIndex];
        }
        if (inputBufferIndex >= 0) {
            inputBuffers.clear();
            inputBuffers.put(mI420Data);
            //调用queueInputBuffer把这个ByteBuffer放回到队列中，这样才能正确释放缓存区
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, mI420Data.length, System.nanoTime() / 1000, 0);
        } else {
        }
        //返回已成功输出的缓冲区的索引,mBufferInfo 信息将充满缓存元数据。
        int encoderStatus = mediaCodec.dequeueOutputBuffer(mBufferInfo, timeoutUs);
        while (encoderStatus >= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.e(Tag, "Build.VERSION.SDK_INT");
                outputBuffers = mediaCodec.getOutputBuffer(encoderStatus);
            } else {
                outputBuffers = mediaCodec.getOutputBuffers()[encoderStatus];
            }
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                Log.e(Tag, "out buffer not available");
            } else if (encoderStatus == INFO_OUTPUT_BUFFERS_CHANGED) {
                Log.d(TAG, "encoder output buffers changed");
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                //
                MediaFormat outputFormat = mediaCodec.getOutputFormat();
                mTrackIndex = mediaMuxer.addTrack(outputFormat);
                mediaMuxer.start();
                mMuxerStarted = true;
            } else if (encoderStatus < 0) {
                Log.d(TAG, "encoderStatus < 0 忽略");
            } else {
                //encoding
                if (outputBuffers == null) {
                    Log.d(TAG, "outPutBuffer is null");
                } else {
                    if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                        // The codec config data was pulled out and fed to the muxer when we got
                        // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                        mBufferInfo.size = 0;
                    }
                    if (mBufferInfo.size != 0) {
                        if (!mMuxerStarted) {
                            MediaFormat newFormat = mediaCodec.getOutputFormat();
                            mTrackIndex = mediaMuxer.addTrack(newFormat);
                            mediaMuxer.start();
                            mMuxerStarted = true;
                        }

//                       可以写到文件 也可以rtmp 发送
//					byte[] outData = new byte[bufferInfo.size];
//					outputBuffer.get(outData);
//					outputBuffer.position(bufferInfo.offset);

//
                        outputBuffers.position(mBufferInfo.offset);
                        outputBuffers.limit(mBufferInfo.offset + mBufferInfo.size);
                        mediaMuxer.writeSampleData(mTrackIndex, outputBuffers, mBufferInfo);
                    }
                }
                //完成了缓冲区，请使用此调用将缓冲区返回到编解码器
                mediaCodec.releaseOutputBuffer(encoderStatus, false);
            }
            encoderStatus = mediaCodec.dequeueOutputBuffer(mBufferInfo, timeoutUs);

        }

    }

    //该方法执行时间大概在20-毫秒到60之间，得优化
    public void NV21toI420SemiPlanar(byte[] nv21, byte[] i420, int width, int height) {
        int length = width * height;
        //复制y
        System.arraycopy(nv21, 0, i420, 0, length);
        //交换UV
        int size = nv21.length;
        for (int i = length; i < size; i += 2) {
            i420[i] = nv21[i + 1];
            i420[i + 1] = nv21[i];
        }

    }

    //该方法执行时间大概在0-2毫秒，在了解两者之间 UV 排列，yv12->I420 更有优势，目前在我的手机有花屏情况：这算法有错吗
    // I420: YYYYYYYY UU VV
   // YV12: YYYYYYYY VV UU    =>YUV420P
    public void yV12toI420SemiPlanar(byte[] yv21, byte[] i420, int width, int height) {
        int length = width * height;
        int uSize = length/4;
        //复制y
        System.arraycopy(yv21, 0, i420, 0, length);
        //复制U
        System.arraycopy(yv21, length+uSize, i420, length,uSize);
        System.arraycopy(yv21,length, i420, length+uSize,uSize);
    }


    @Override
    public void stopRecoder() {
        if (mediaCodec != null) {
            mediaCodec.stop();
        }

        if (mediaMuxer != null) {
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaMuxer = null;
        }
    }

    @Override
    public void releseRecoder() {
        if (mediaCodec != null) {
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }

        if (mediaMuxer != null) {
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaMuxer = null;
        }
    }

    @Override
    public void startRecoder() {
        try {

            if (mediaCodec!=null && isReadyEncoder){
                //每次录制都要重新设置，可以看官方的流程图，stop 状态->Uninitialzed状态  然后在configure函数后->Configured状态，才可以start
                mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                mediaCodec.start();
                //重新录制mp4
                mediaMuxer = new MediaMuxer(videoParms.getRootPath()+System.currentTimeMillis()+".mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                mTrackIndex = -1;
                mMuxerStarted = false;

            }
        } catch (IOException e) {
            e.printStackTrace();
            showError(ERROR_MEDIAMUXER_INIT,"startRecoder new MediaMuxer error");
        }
    }


}



