package sen.com.openglcamera.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
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

public class VideoRecoder implements BaseRecoder{
    private static final String Tag = "sen_";
    //用来标志，编码器是否初始成功
    private boolean isReadyEncoder = false;
    private final VideoParms videoParms;
    private MediaCodec mediaCodec;
    private byte[] mI420Data;
    private final long timeoutUs = 10000;//微秒
    private long mStartTime = 0;
    private MediaCodec.BufferInfo mBufferInfo;
    private int mTrackIndex = -1;
    private MediaMuxer mediaMuxer;
    private boolean mMuxerStarted;


    public VideoRecoder(VideoParms videoParms) {
        this.videoParms = videoParms;
        //4个y对应一个U 和一个V
        mStartTime = System.nanoTime(); //纳秒级别
        mBufferInfo = new MediaCodec.BufferInfo();

    }

    private BaseRecoder.OnRecoderListener listener;
    private static final int ERROR_CODER_INIT = 0; //initRecoder() 初始化时出错

    public void setVideoRecoderError(BaseRecoder.OnRecoderListener listener){
        this.listener = listener;
    }
    private void showError(int code,String msg){
        if(listener !=null){
            listener.onRecoderError(code,msg);
        }
    }
    @Override
    public void initRecoder() {
        try {
            //create对应格式的编码器
            mediaCodec = MediaCodec.createEncoderByType(videoParms.getMimeType());
            //create format 来设置编码器的格式
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(videoParms.getMimeType(),
                    videoParms.getWidth(),videoParms.getHeight());
            //------------设置
            //设置帧数
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE,videoParms.getFrameRate());
            //设置码流
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE,videoParms.getBitRate());
            //设置关键帧时间
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,videoParms.getIframeInterval());
            //设置颜色格式
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,videoParms.getColorFormat());
            //设置给mediaCodec
            mediaCodec.configure(mediaFormat,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();
            isReadyEncoder =true;

            mediaMuxer = new MediaMuxer(videoParms.getOutFilePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            Log.e(Tag,"video encoder init finish");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Tag,"video encoder init error\n"+e.getMessage());
            showError(ERROR_CODER_INIT,e.getMessage());
        }
    }

    @Override
    public void encodeData(byte[] data) {

        if(mediaCodec==null){
            Log.e(Tag,"encodeData but mediaCodec is init error");
            return;
        }

        Log.e(Tag,"encodeData 1");
        //先转格式
        NV21toI420SemiPlanar(data,mI420Data,videoParms.getWidth(),videoParms.getHeight());
        //获得一个ByteBuffer的数组，(准备处理的数据)
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
//        dequeueInputBuffer方法提取出要处理的部分（也就是一个ByteBuffer数据流）把这一部分放到缓存区。
        //返回要用有效数据填充的输入缓冲区的索引。
       int inputBufferIndex = mediaCodec.dequeueInputBuffer(timeoutUs);
        if(inputBufferIndex >=0){
            Log.e(Tag,"encodeData 2");
            long endTime = System.nanoTime();
            long ptsTime = (endTime -mStartTime)/1000;
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(mI420Data);
            //调用queueInputBuffer把这个ByteBuffer放回到队列中，这样才能正确释放缓存区
            mediaCodec.queueInputBuffer(inputBufferIndex,0,mI420Data.length,System.nanoTime()/1000,0);
        }else{
            Log.e(Tag,"input buffer not available");
        }
        //返回已成功输出的缓冲区的索引,mBufferInfo 信息将充满缓存元数据。
        int encoderStatus = mediaCodec.dequeueOutputBuffer(mBufferInfo,timeoutUs);
        while(encoderStatus >=0){
            Log.e(Tag,"encodeData 3");
            if(encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER){
                Log.e(Tag,"out buffer not available");
            }else if(encoderStatus == INFO_OUTPUT_BUFFERS_CHANGED){
                outputBuffers = mediaCodec.getOutputBuffers();
                Log.d(TAG, "encoder output buffers changed");
            }else if(encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
               //
                MediaFormat outputFormat = mediaCodec.getOutputFormat();
                mTrackIndex = mediaMuxer.addTrack(outputFormat);
                mediaMuxer.start();
                mMuxerStarted = true;
            }else if(encoderStatus<0){
                Log.d(TAG, "outputBufferIndex");
            }else{
                //encoding
                ByteBuffer outPutBuffer = outputBuffers[encoderStatus];
                if(outPutBuffer ==null){
                    Log.d(TAG, "outPutBuffer is null");
                }else{
                    if((mBufferInfo.flags &MediaCodec.BUFFER_FLAG_CODEC_CONFIG)!=0){
                        // The codec config data was pulled out and fed to the muxer when we got
                        // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                        mBufferInfo.size =0;
                    }
                    if(mBufferInfo.size!=0){
                        if(!mMuxerStarted){
                            MediaFormat newFormat = mediaCodec.getOutputFormat();
                            mTrackIndex = mediaMuxer.addTrack(newFormat);
                            mediaMuxer.start();
                            mMuxerStarted = true;
                        }
                        outPutBuffer.position(mBufferInfo.offset);
                        outPutBuffer.limit(mBufferInfo.offset+mBufferInfo.size);
                        mediaMuxer.writeSampleData(mTrackIndex,outPutBuffer,mBufferInfo);
                        Log.e(Tag,"mediaMuxer.writeSampleData");
                    }
                }
                //完成了缓冲区，请使用此调用将缓冲区返回到编解码器
                mediaCodec.releaseOutputBuffer(encoderStatus,false);
            }
            encoderStatus = mediaCodec.dequeueOutputBuffer(mBufferInfo, timeoutUs);

        }

    }

    public void NV21toI420SemiPlanar(byte[] nv21,byte[] i420,int width,int height){
        int length = width* height;
        //复制y
        System.arraycopy(nv21,0,i420,0,length);
        //交换UV
        int size = nv21.length ;
        for (int i = length;i<nv21.length;i+=2){
            i420[i] =nv21[i+1];
            i420[i+1]= nv21[i];
        }

    }


    @Override
    public void stopRecoder() {

    }

    @Override
    public void releseRecoder() {
        if(mediaCodec !=null){
            mediaCodec.stop();
            mediaCodec.release();
            mediaCodec = null;
        }

        if(mediaMuxer!=null){
            mediaMuxer.stop();
            mediaMuxer.release();
            mediaMuxer = null;
        }
    }

    public void setFrameBuffer(byte[] callbackBuffer) {
        mI420Data = callbackBuffer;
    }
}
