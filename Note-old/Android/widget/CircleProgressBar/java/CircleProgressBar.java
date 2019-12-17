package com.yu.hu.circleprogressbar.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.yu.hu.circleprogressbar.R;

/**
 * 文件名：CircleProgressBar
 * 创建者：HY
 * 创建时间：2019/6/3 17:19
 * 描述：  圆形进度条
 */
public class CircleProgressBar extends View {

    /**
     * 默认边框宽度 10
     */
    private static final int DEFAULT_BORDER_WIDTH = 10;

    /**
     * 默认边框填充色 0xFF000000
     */
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    /**
     * 默认动画时间 2
     * 单位：秒
     */
    private static final int DEFAULT_DURATION = 2;

    /**
     * 默认背景颜色 0x32000000
     */
    private static final int DEFAULT_FILL_COLOR = Color.argb(32, 0, 0, 0);

    /**
     * 当前进度
     */
    private float mAnimatorValue;

    /**
     * 填充/背景色
     * 默认值为{@link #DEFAULT_FILL_COLOR}
     * 同时可由自定义属性{@code fill_color}设置
     * {@code R.styleable.CircleProgressBar}
     */
    private int mFillColor = DEFAULT_FILL_COLOR;

    /**
     * 边框颜色
     * 默认值为{@value #DEFAULT_BORDER_COLOR}
     * 同时可由自定义属性{@code border_color}设置
     * {@code R.styleable.CircleProgressBar}
     */
    private int mBorderColor = DEFAULT_BORDER_COLOR;

    /**
     * 边框宽度
     * 默认值为{@value #DEFAULT_BORDER_WIDTH}
     * 同时可由自定义属性{@code border_width}设置
     * {@code R.styleable.CircleProgressBar}
     */
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    /**
     * 动画时间
     * 默认值为{@value #DEFAULT_DURATION}
     * 同时可由自定义属性{@code duration_second}设置
     * {@code R.styleable.CircleProgressBar}
     */
    private int mDuration = DEFAULT_DURATION;

    /**
     * 绘制外边的画笔
     */
    private Paint mPaint;

    /**
     * 绘制背景的画笔
     */
    private Paint mBgPaint;

    private Path mPath;
    private Path mDst;
    private PathMeasure mPathMeasure;
    private float mLength;
    private ValueAnimator mValueAnimator;


    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleProgressBar_border_color, DEFAULT_BORDER_COLOR);
        mDuration = a.getInt(R.styleable.CircleProgressBar_duration_second, DEFAULT_DURATION);
        mFillColor = a.getColor(R.styleable.CircleProgressBar_fill_color, DEFAULT_FILL_COLOR);
        a.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔样式   STROKE--描边
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        //设置画笔末端样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mDst = new Path();

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.setDuration(mDuration * 1000);
        //        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getHeight() != 0 && getWidth() != 0) {
            //Path.Direction.CW 顺时针绘制  CCW为逆时针绘制
            mPath.addCircle(getWidth() >> 1, getHeight() >> 1, (Math.min(getWidth(), getHeight()) >> 1) - mBorderWidth, Path.Direction.CW);
            mPathMeasure.setPath(mPath, true);
            mLength = mPathMeasure.getLength();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBgPaint == null) {
            mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            //设置背景画笔样式 FILL--填充
            mBgPaint.setStyle(Paint.Style.FILL);
            mBgPaint.setColor(mFillColor);
        }
        //绘制背景（默认灰色的那个圆形）
        //  >> 1  右移1位  等同于  / 2
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, (Math.min(getWidth(), getHeight()) >> 1) - mBorderWidth / 1.8f, mBgPaint);

        mDst.reset();
        // 硬件加速的BUG
        mDst.lineTo(0, 0);
        float stop = mLength * mAnimatorValue;
        //截取path片段并包并添加到 mDst 中
        mPathMeasure.getSegment(0, stop, mDst, true);
        //绘制边 （border）细边
        canvas.drawPath(mDst, mPaint);
    }

    /**
     * 停止动画
     */
    public void stop() {
        //取消动画
        mValueAnimator.cancel();
    }

}
