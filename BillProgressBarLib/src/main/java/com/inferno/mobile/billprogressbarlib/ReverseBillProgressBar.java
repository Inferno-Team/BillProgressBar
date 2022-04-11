package com.inferno.mobile.billprogressbarlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

public class ReverseBillProgressBar extends BillProgressBar {

    public ReverseBillProgressBar(Context context) {
        super(context);
    }

    public ReverseBillProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        super.init(attrs);
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        matrix.postTranslate(logo.getWidth(), 0);

        logo = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(),
                logo.getHeight(), matrix, true);

        smoke = Bitmap.createBitmap(smoke, 0, 0, smoke.getWidth(),
                smoke.getHeight(), matrix, true);
        bigSmoke = Bitmap.createBitmap(bigSmoke, 0, 0, bigSmoke.getWidth(),
                bigSmoke.getHeight(), matrix, true);

    }

    @Override
    protected void initValues() {

        logoCenterX = (getWidth() - logo.getWidth()) / 2;
        logoCenterY = (getHeight() - logo.getHeight()) / 2;

        wheelCenterX = logoCenterX + wheelDim / 2;


        oldWheelCenterX = logoCenterX + wheelDim / 2;
        wheelCenterY = (int) (logoCenterY + wheelDim * 2.70);

        bigSmokeCenterY = (int) ((logoCenterY + wheelCenterY) / 2.05);
        bigSmokeCenterX = (int) (logoCenterX * 1.5 + (wheelCenterX / 6));

        smallSmokeCenterX = (logoCenterX * 2);
        smallSmokeCenterY = (int) ((logoCenterY + wheelCenterY) / 1.92);
        isStopping = false;
        isBigSmoke = true;
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

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        int animate = value / 4;
        System.out.println(value);
        if (animation.equals(stopAnimator)) {
            wheelCenterX -= animate;
            logoCenterX -= animate;
            bigSmokeCenterX -= animate;
            smallSmokeCenterX -= animate;
            isStopping = true;
            if (animate % 7 == 0)
                isBigSmoke = !isBigSmoke;
            if (value == 280) {
                this.setVisibility(GONE);
                isRunning = false;
            }
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            wheel1 = Bitmap.createBitmap(wheel1, 0, 0, wheel1.getWidth(), wheel1.getHeight(), matrix, true);
            wheel2 = Bitmap.createBitmap(wheel2, 0, 0, wheel2.getWidth(), wheel2.getHeight(), matrix, true);

        }
        invalidate();
    }
}
