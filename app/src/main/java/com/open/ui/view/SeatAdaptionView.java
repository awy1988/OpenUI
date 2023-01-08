package com.open.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SeatAdaptionView extends View {

    Paint linePaint = new Paint();
    Paint trianglePaint = new Paint();
    Paint circlePaint1 = new Paint();
    Paint circlePaint2 = new Paint();

    Path upTrianglePath = new Path();
    Path downTrianglePath = new Path();
    Path leftTrianglePath = new Path();
    Path rightTrianglePath = new Path();
    private float touchX;
    private float touchY;
    private float lastX;
    private float lastY;
    private float mDeltaX;
    private float mDeltaY;
    private int moveDirection = -1; // 手势移动方向，默认为-1，0为水平方向，1为垂直方向

    public SeatAdaptionView(Context context) {
        super(context);
    }

    public SeatAdaptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        setClickable(true);
    }

    public SeatAdaptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        setClickable(true);
    }

    public SeatAdaptionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
        setClickable(true);
    }

    private void initPaint() {
        PathEffect pathEffect = new DashPathEffect(new float[]{10f,10f}, 1);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5f);
        linePaint.setColor(Color.GRAY);
        linePaint.setPathEffect(pathEffect);

        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setColor(Color.GRAY);
        float radius = 10f;
        CornerPathEffect corEffect = new CornerPathEffect(radius);
        trianglePaint.setPathEffect(corEffect);

        circlePaint1.setStyle(Paint.Style.FILL);
        circlePaint1.setColor(Color.GRAY);
        circlePaint2.setStyle(Paint.Style.STROKE);
        circlePaint2.setStrokeWidth(14);
        circlePaint2.setColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(30,getHeight()/2, getWidth()-30,getHeight()/2, linePaint);
        canvas.drawLine(getWidth()/2, 30, getWidth()/2, getHeight()-30, linePaint);
        canvas.drawPath(getTrianglePath(0), trianglePaint);
        canvas.drawPath(getTrianglePath(1), trianglePaint);
        canvas.drawPath(getTrianglePath(2), trianglePaint);
        canvas.drawPath(getTrianglePath(3), trianglePaint);
        if (moveDirection != -1) {
            circlePaint1.setAlpha(100);
            circlePaint2.setAlpha(100);
        } else {
            circlePaint1.setAlpha(255);
            circlePaint2.setAlpha(255);
        }
        canvas.drawCircle(getCircleX(), getCircleY(), 20, circlePaint1);
        canvas.drawCircle(getCircleX(), getCircleY(), 48, circlePaint2);

    }

    private float getCircleY() {
        float y = getHeight() / 2 - mDeltaY;
        if (y < 120) {
            y = 120;
            return y;
        }
        if (y > getHeight() - 120) {
            y = getHeight() - 120;
            return y;
        }
        return y;
    }

    private float getCircleX() {
        float x = getWidth()/2 - mDeltaX;
        if (x < 120) {
            x = 120;
            return x;
        }
        if (x > getWidth() - 120) {
            x = getWidth() - 120;
            return x;
        }

        return x;
    }


    private void initTrianglePath() {
        upTrianglePath.moveTo(getWidth()/2, 0);
        upTrianglePath.lineTo(getWidth()/2 - 30, 52);
        upTrianglePath.lineTo(getWidth()/2 + 30, 52);
        upTrianglePath.close();

        downTrianglePath.moveTo(getWidth()/2, getHeight());
        downTrianglePath.lineTo(getWidth()/2 - 30, getHeight() - 52);
        downTrianglePath.lineTo(getWidth()/2 + 30, getHeight() - 52);
        downTrianglePath.close();

        leftTrianglePath.moveTo(0, getHeight()/2);
        leftTrianglePath.lineTo(52, getHeight()/2 + 30);
        leftTrianglePath.lineTo(52, getHeight()/2 - 30);
        leftTrianglePath.close();

        rightTrianglePath.moveTo(getWidth(), getHeight()/2);
        rightTrianglePath.lineTo(getWidth() - 52, getHeight()/2 + 30);
        rightTrianglePath.lineTo(getWidth() - 52, getHeight()/2 - 30);
        rightTrianglePath.close();
    }

    private Path getTrianglePath(int direction) {
        initTrianglePath();
        switch (direction) {
            case 0:// 上
                return upTrianglePath;
            case 1: // 下
                return downTrianglePath;
            case 2: // 左
                return leftTrianglePath;
            case 3: // 右
                return rightTrianglePath;
            default:
                break;
        }
        return upTrianglePath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                touchY = event.getY();
                float deltaX = lastX - touchX;
                float deltaY = lastY - touchY;
                if (moveDirection == -1) {
                    if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                        moveDirection = 0;
                    } else {
                        moveDirection = 1;
                    }
                } else if (moveDirection == 0) {
                    // 水平方向移动
                    mDeltaX = deltaX;
                    invalidate();
                } else {
                    // 垂直方向移动
                    mDeltaY = deltaY;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                touchX = -1;
                touchY = -1;
                moveDirection = -1;
                mDeltaX = 0;
                mDeltaY = 0;
                invalidate();
                break;
            default:
                break;
        }

        Log.d("SeatAdaptionView", "onTouchEvent: Action = " + event.getAction() + " touchX = " + touchX + " touchY = " + touchY);

        return super.onTouchEvent(event);
    }
}
