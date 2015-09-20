package com.siger.example.progresspie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhangwen on 15/9/20.
 */

public class ProgressPie extends View{

    private final int PROGRESS_STYLE_DEFAULT = 0;
    private  final int PROGRESS_STYLE_IOS_LIKE = 1;
    private int mProgressStyle;

    private Context mContext;
    private boolean mNeedCircleRing;//是否需要外部圆环
    private RectF mCircleRingRect;
    private Paint mCircleRingPaint;
    private int mCircleRingColor;//外部圆环颜色
    private float mCircleRingWidth;//外部圆环宽度

    private RectF mArcRec;
    private Paint mArcPaint;
    private int mProgressColor;//进度的颜色
    private float mProgressWidth;//进度的宽度

    private int mProgressBgColor;//进度背景颜色

    private RectF mCenterSquareRect;
    private float mCenterSquareWidth;

    private boolean mNeedProgressText;
    private int mProgressTextColor;
    private float mProgressTextSize;
    private Paint mProgressTextPaint;

    private float mProgress;
    public ProgressPie(Context context){
        super(context);
        mContext = context;
    }

    public ProgressPie(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressPie);
        mProgressStyle = ta.getInt(R.styleable.ProgressPie_style,PROGRESS_STYLE_DEFAULT);
        initWithStyle();

        mNeedCircleRing = ta.getBoolean(R.styleable.ProgressPie_needCircleRing, mNeedCircleRing);
        mCircleRingColor = ta.getColor(R.styleable.ProgressPie_circleRingColor, mCircleRingColor);
        mCircleRingWidth = ta.getDimension(R.styleable.ProgressPie_circleRingWidth, mCircleRingWidth);

        mProgressColor = ta.getColor(R.styleable.ProgressPie_progressColor, mProgressColor);
        mProgressWidth = ta.getDimension(R.styleable.ProgressPie_progressWidth, mProgressWidth);
        mProgressBgColor = ta.getColor(R.styleable.ProgressPie_progressBgColor,mProgressBgColor);
        mCenterSquareWidth = ta.getDimension(R.styleable.ProgressPie_centerSquareWidth, mCenterSquareWidth);

        mNeedProgressText = ta.getBoolean(R.styleable.ProgressPie_needProgressText, mNeedProgressText);
        mProgressTextColor = ta.getColor(R.styleable.ProgressPie_progressTextColor, mProgressTextColor);
        mProgressTextSize = ta.getDimension(R.styleable.ProgressPie_progressTextSize, mProgressTextSize);
        ta.recycle();

        mCircleRingPaint = new Paint();
        mCircleRingPaint.setColor(mCircleRingColor);
        mCircleRingPaint.setAntiAlias(true);
        mCircleRingPaint.setStyle(Style.STROKE);
        mCircleRingPaint.setStrokeWidth(mCircleRingWidth);

        mCircleRingRect = new RectF();
        mArcRec = new RectF();
        mArcPaint = new Paint();
        mArcPaint.setColor(mProgressColor);
        mArcPaint.setStrokeWidth(mProgressWidth);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Style.STROKE);

        mCenterSquareRect = new RectF();

        mProgressTextPaint = new Paint();
        mProgressTextPaint.setColor(mProgressTextColor);
        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void initWithStyle() {
        switch (mProgressStyle){
            case PROGRESS_STYLE_IOS_LIKE:

                mNeedCircleRing = true;
                mCircleRingColor = Color.WHITE;
                mCircleRingWidth = -1;

                mProgressColor = Color.WHITE;
                mProgressWidth = -1;
                mCenterSquareWidth = -1;
                mNeedProgressText = false;
                break;

            case PROGRESS_STYLE_DEFAULT:
                mNeedCircleRing = false;
                mProgressBgColor = Color.parseColor("#999999");
                mProgressColor = Color.WHITE;
                mProgressWidth = -1;
                mNeedProgressText = true;
                mProgressTextSize = -1;
                mProgressTextColor = Color.WHITE;
                break;

        }
    }

    private void initSizeContainerSize(float w,float h){

        switch (mProgressStyle){
            case PROGRESS_STYLE_IOS_LIKE:

                if(mProgressWidth==-1){
                    mProgressWidth = w/10.0f;
                    mArcPaint.setStrokeWidth(mProgressWidth);
                }
                if(mCircleRingWidth==-1){
                    mCircleRingWidth = w/40.0f;
                    mCircleRingPaint.setStrokeWidth(mCircleRingWidth);
                }
                if(mCenterSquareWidth==-1){
                    mCenterSquareWidth = w/3.5f;
                }
                break;
            case PROGRESS_STYLE_DEFAULT:
                if(mProgressWidth==-1){
                    mProgressWidth = w/30.0f;
                    mArcPaint.setStrokeWidth(mProgressWidth);
                }
                if(mProgressTextSize==-1){
                    mProgressTextSize = w/3.0f;
                    mProgressTextPaint.setTextSize(mProgressTextSize);
                }
                break;

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v("ProgressPie", "onDraw");
        if(mNeedCircleRing){
            canvas.drawOval(mCircleRingRect, mCircleRingPaint);
        }
        mArcPaint.setColor(mProgressBgColor);
        mArcPaint.setStyle(Style.STROKE);
        canvas.drawCircle(mArcRec.centerX(), mArcRec.centerY(), mArcRec.width() / 2, mArcPaint);

        mArcPaint.setColor(mProgressColor);
        canvas.drawArc(mArcRec, -90, 360 * mProgress, false, mArcPaint);
        mArcPaint.setStyle(Style.FILL);
        canvas.drawRect(mCenterSquareRect, mArcPaint);

        if(mNeedProgressText){
            float textYOffset = mProgressTextSize/2;
            canvas.drawText((int)(mProgress*100)+"%",mArcRec.centerX(), mArcRec.centerY()+textYOffset,mProgressTextPaint);
        }
    }



    @Override
    protected void onFinishInflate() {
        Log.v("ProgressPie", "onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("ProgressPie ", "onMeasure widthMeasureSpec:" + widthMeasureSpec + " heightMeasureSpec:" + heightMeasureSpec);

        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.v("MyView ", "dispatchTouchEvent" + event.getAction());
        return true;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,

                            int bottom) {
        Log.v("ProgressPie", "onLayout changed:" + changed + " left:" + left + " top:" + top + " right:" + right + " buttom:" + bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        Log.v("ProgressPie", "onSizeChanged width:"+ w+"height:"+ h+"old width:"+oldw+"old height:" +oldh);
        initSizeContainerSize(w,h);
        int padLeft =  getPaddingLeft();
        int padRight = getPaddingRight();
        int padTop = getPaddingTop();
        int padBottom = getPaddingBottom();

        float xpad = (float) (padLeft + padRight);
        float ypad = (float) (padTop + padBottom);

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;
        float circOffset = mCircleRingWidth/2;
        float progressOffset = mProgressWidth/2;
        mCircleRingRect.set(padLeft+circOffset, padTop+circOffset, ww -circOffset, hh -circOffset);
        mArcRec.set(padLeft+mCircleRingWidth+progressOffset, padRight+mCircleRingWidth+progressOffset, ww -mCircleRingWidth-progressOffset, hh -mCircleRingWidth-progressOffset);
        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
        float squareLeft = (w-mCenterSquareWidth)/2;
        float squareTop = (h-mCenterSquareWidth)/2;
        mCenterSquareRect.set(squareLeft, squareTop, squareLeft + mCenterSquareWidth, squareTop+mCenterSquareWidth);



    }

    /**
     * 设置比例  progress=0~1
     * @param progress 比例
     */
    public void setProgress(float progress){
        mProgress = progress;
        invalidate();
    }

    private int dp2Px(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
