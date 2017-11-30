package sen.com.openglcamera.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import sen.com.openglcamera.R;

/**
 * Created by John on 2017/5/24.
 */

public class LoadingView extends View {
    private static final int DEFUAL_RADIUS = 60;
    private static final int DEFUAL_DURATION = 1500;
    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;
    private ValueAnimator valueAnimator;
    ValueAnimator.AnimatorUpdateListener updateListener= new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mAnimatorValue = (float) valueAnimator.getAnimatedValue();
            invalidate();
        }
    };
    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPathMeasure = new PathMeasure();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(DEFUAL_DURATION);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPath = new Path();
        int center = w/2;
        int radius = Math.min(DEFUAL_RADIUS,center);
        mPath.addCircle(center, h/2, radius, Path.Direction.CW);
        mPathMeasure.setPath(mPath, true);
        mLength = mPathMeasure.getLength();
        mDst = new Path();
    }

    public void startAnimator(){
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.start();
    }

    public void stopAnimator(){
        valueAnimator.end();
        valueAnimator.removeAllListeners();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDst.reset();
        // 硬件加速的BUG
        mDst.lineTo(0,0);
        float stop = mLength * mAnimatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
        mPathMeasure.getSegment(start, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
    }
}
