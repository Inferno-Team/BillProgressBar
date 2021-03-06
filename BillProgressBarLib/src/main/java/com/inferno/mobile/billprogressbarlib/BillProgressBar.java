package com.inferno.mobile.billprogressbarlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BillProgressBar extends View implements ValueAnimator.AnimatorUpdateListener {

    public static long SLOW = 2000;
    public static long NORMAL = 1000;
    public static long FAST = 500;

    protected boolean isRunning = false;
    protected int logoHeight;

    protected int logoWidth;

    protected int wheelDim;

    protected long delay;

    protected Paint progressPaint, smokePaint;
    protected Bitmap logo, wheel1, wheel2, smoke, bigSmoke;
    protected Resources res;
    protected ValueAnimator mAnimator, stopAnimator;

    protected int wheelCenterX, wheelCenterY, oldWheelCenterX;
    protected int logoCenterX, logoCenterY;
    protected int bigSmokeCenterX, bigSmokeCenterY, smallSmokeCenterX, smallSmokeCenterY;
    protected boolean isStopping = false, isBigSmoke = true;


    public float getLogoHeight() {
        return logoHeight;
    }

    public void setLogoHeight(int logoHeight) {
        this.logoHeight = logoHeight;
    }

    public int getLogoWidth() {
        return logoWidth;
    }

    public void setLogoWidth(int logoWidth) {
        this.logoWidth = logoWidth;
    }

    public int getWheelDim() {
        return wheelDim;
    }

    public void setWheelDim(int wheelDim) {
        this.wheelDim = wheelDim;
    }

    public BillProgressBar(Context context) {
        super(context);
    }

    public BillProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        this.delay = NORMAL;
        progressPaint = new Paint();
        smokePaint = new Paint();
        progressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        smokePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        smokePaint.setAlpha(80);
        res = getResources();

        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.BillProgressBar,
                        0, 0);

        try {
            setLogoWidth(typedArray.getInteger
                    (R.styleable.BillProgressBar_logoWidth, 480));
            setLogoHeight(typedArray.getInteger
                    (R.styleable.BillProgressBar_logoHeight, 480));
            setWheelDim(typedArray.getInteger
                    (R.styleable.BillProgressBar_wheelDim, 128));

        } finally {
            typedArray.recycle();
        }
        logo = BitmapFactory.decodeResource(res, R.drawable.logo2);
        wheel1 = BitmapFactory.decodeResource(res, R.drawable.animate_wheel);
        bigSmoke = BitmapFactory.decodeResource(res, R.drawable.small_smoke);
        smoke = BitmapFactory.decodeResource(res, R.drawable.small_smoke);
        wheel2 = BitmapFactory.decodeResource(res, R.drawable.animate_wheel);
        logo = Bitmap.createScaledBitmap(logo, logoWidth, logoHeight, true);
        wheel1 = Bitmap.createScaledBitmap(wheel1, wheelDim, wheelDim, true);
        wheel2 = Bitmap.createScaledBitmap(wheel2, wheelDim, wheelDim, true);
        bigSmoke = Bitmap.createScaledBitmap(bigSmoke, wheelDim * 2, wheelDim * 2, true);
        smoke = Bitmap.createScaledBitmap(smoke, wheelDim, wheelDim, true);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initValues();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int dis = wheelCenterX + (logoWidth / 3);
        canvas.drawBitmap(logo, logoCenterX, logoCenterY, progressPaint);
        canvas.drawBitmap(wheel1, wheelCenterX, wheelCenterY, progressPaint);
        canvas.drawBitmap(wheel2, dis, wheelCenterY, progressPaint);
        if (isStopping)
            if (isBigSmoke)
                canvas.drawBitmap(bigSmoke, bigSmokeCenterX, bigSmokeCenterY, smokePaint);
            else canvas.drawBitmap(smoke, smallSmokeCenterX, smallSmokeCenterY, smokePaint);


    }

    protected void initValues() {

        logoCenterX = (getWidth() - logo.getWidth()) / 2;
        logoCenterY = (getHeight() - logo.getHeight()) / 2;

        wheelCenterX = logoCenterX + wheelDim;
        oldWheelCenterX = logoCenterX + wheelDim;
        wheelCenterY = (int) (logoCenterY + wheelDim * 2.70);

        bigSmokeCenterY = (int) ((logoCenterY + wheelCenterY) / 2.05);
        bigSmokeCenterX = logoCenterX - (wheelCenterX / 6);

        smallSmokeCenterX = logoCenterX;
        smallSmokeCenterY = (int) ((logoCenterY + wheelCenterY) / 1.92);
        isStopping = false;
        isBigSmoke = true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        int animate = value / 4;

        if (animation.equals(stopAnimator)) {
            wheelCenterX += animate;
            logoCenterX += animate;
            bigSmokeCenterX += animate;
            smallSmokeCenterX += animate;
            isStopping = true;
            if (animate % 7 == 0)
                isBigSmoke = !isBigSmoke;
            if (value == 280) {
                this.setVisibility(GONE);
                isRunning = false;
            }
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            wheel1 = Bitmap.createBitmap(wheel1, 0, 0, wheel1.getWidth(), wheel1.getHeight(), matrix, true);
            wheel2 = Bitmap.createBitmap(wheel2, 0, 0, wheel2.getWidth(), wheel2.getHeight(), matrix, true);

        }
        invalidate();
    }

    public void startAnimation() {
        startAnimation(delay);
    }

    public void startAnimation(long delay) {
        System.out.println(isRunning);
        if(isRunning)return;
        this.setVisibility(VISIBLE);
        isRunning=true;
        initValues();
        mAnimator = ValueAnimator.ofInt(4, 280);
        mAnimator.setDuration(delay);
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    public void stopAnimation() {
        wheelCenterX = oldWheelCenterX;
        stopAnimator = ValueAnimator.ofInt(4, 280);
        stopAnimator.setDuration(delay);
        stopAnimator.addUpdateListener(this);
        stopAnimator.start();
    }
}
