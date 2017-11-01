package sen.com.openglcamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sen.com.openglcamera.R;

/**
 * Created by Administrator on 2017/7/24.
 */

public class CameraButtonView extends View {

    private Paint mPaint;
    private Path bigCirclePath;
    private Path smollCirclePath;
    private final static int DEF_STATE = 0;
    private final static int DOWN_STATE = 1;
    private int currentState = DEF_STATE;
    private int centerX;
    private int centerY;
    private int changeOffset = 0;
    private int ruduis;


    public CameraButtonView(Context context) {
        this(context, null);
    }

    public CameraButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bigCirclePath = new Path();
        centerX = w / 2;
        centerY = h / 2;
        ruduis = w/2-16;

        smollCirclePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        bigCirclePath.reset();
        smollCirclePath.reset();
        bigCirclePath.addCircle(centerX, centerY, ruduis+changeOffset, Path.Direction.CW);
        canvas.drawPath(bigCirclePath, mPaint);
        smollCirclePath.addCircle(centerX,centerY,ruduis-12+changeOffset,Path.Direction.CW);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(smollCirclePath, mPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() ){
            case  MotionEvent.ACTION_DOWN:
                //currentState = DOWN_STATE;
                changeOffset =-4;
                invalidate();

               break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
              //  currentState = DEF_STATE;
                changeOffset =0;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }




}
