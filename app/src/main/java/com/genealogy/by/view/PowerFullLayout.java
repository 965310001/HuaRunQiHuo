package com.genealogy.by.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

public class PowerFullLayout extends FrameLayout {
    // 屏幕宽高
    private int screenHeight;
    private int screenWidth;
    private ViewDragHelper mDragHelper;
    private long lastMultiTouchTime;// 记录多点触控缩放后的时间
    private ScaleGestureDetector mScaleGestureDetector = null;

    public  boolean isScale = false;
    private float scale;
    private float preScale = 1;// 默认前一次缩放比例为1

    public PowerFullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PowerFullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PowerFullLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mDragHelper = ViewDragHelper.create(this,1.0f, callback);
        mScaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleGestureListener());

        // view.post(new Runnable() {
        //
        // @Override
        // public void run() {
        // left = view.getLeft();
        // top = view.getTop();
        // right = view.getRight();
        // bottom = view.getBottom();
        // originalWidth = view.getWidth();
        // originalHeight = view.getHeight();
        // }
        // });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);

        return isScale;
    }


    private boolean needToHandle=true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerCount = event.getPointerCount(); // 获得多少点
        if (pointerCount > 1) {// 多点触控，
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    needToHandle=true;
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_POINTER_2_UP://第二个手指抬起的时候
                    needToHandle=true;
                    break;

                default:
                    break;
            }
            return mScaleGestureDetector.onTouchEvent(event);//让mScaleGestureDetector处理触摸事件
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastMultiTouchTime > 200 && needToHandle) {
//                  多点触控全部手指抬起后要等待200毫秒才能执行单指触控的操作，避免多点触控后出现颤抖的情况
                try {
                    Log.d("getPointerId",event.getPointerId(0)+"");
                    //Log.d("getX",""+event.getX(event.getPointerId(0)));
                    //单点时会出错 所以暂时取消单点
                    if(event.getPointerId(0) != 1){
                        mDragHelper.processTouchEvent(event);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
//            }
            //mScaleGestureDetector.onTouchEvent(event);
        }
        return false;
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        /**
         * 用于判断是否捕获当前child的触摸事件
         *
         * @param child
         *            当前触摸的子view
         * @param pointerId
         * @return true就捕获并解析；false不捕获
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
//            if (preScale > 1)
//                return true;
            return true;
        }

        /**
         * 控制水平方向上的位置
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d("left:",left+"");
            //默认比例边界控制
            if (preScale == 1){
                if (left >= 450){
                    return Math.max(450, 450);
                }else if(left <= -450){
                    return Math.max(-450, -450);
                }
            }
            // 拖动限制（大于左边界）
            return Math.max(left, left);
        }

        public int clampViewPositionVertical(View child, int top, int dy) {

            if (top < (screenHeight - screenHeight * preScale) / 2) {
                top = (int) (screenHeight - screenHeight * preScale) / 2;// 限制mainView可向上移动到的位置
            }
            if (top > (screenHeight * preScale - screenHeight) / 2) {
                top = (int) (screenHeight * preScale - screenHeight) / 2;// 限制mainView可向上移动到的位置
            }
            return top;
        }

    };

    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float previousSpan = detector.getPreviousSpan();//缩放发生前的两点距离
            float currentSpan = detector.getCurrentSpan();//缩放发生时的两点距离
            if (previousSpan < currentSpan)//放大
            {
                scale = preScale + (currentSpan - previousSpan) / previousSpan;
            } else {
                scale = preScale - (previousSpan - currentSpan) / previousSpan;
            }
            //确保放大最多为2倍，最少不能小于原图
            if (scale > 2) {
                scale = 2;
            } else if (scale < 0.5) {
                scale = 0.5f;
            }
            setScaleX(scale);
            setScaleY(scale);
            //这里调用的是本自定义View的方法，是对本自定义view进行的缩放
            /*在这里调用getChildView（index）的进行缩放，虽然控件显示大小改变了，但是在ViewDragHelper的回调方法中获得的View child的getWidth（）和getHeigit（）是原来的大小，不会发生改变*/
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;// 记录本次缩放比例
            lastMultiTouchTime = System.currentTimeMillis();// 记录双指缩放后的时间
        }
    }
}