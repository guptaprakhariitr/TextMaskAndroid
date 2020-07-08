package com.example.firstlibrary;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;

public class TextBackImage extends androidx.appcompat.widget.AppCompatTextView {
    private Context contextSensor;
    private Bitmap mMaskBitmap;
    private Canvas mMaskCanvas;
    private Paint mPaint;
    private Drawable mBackground;
    private Bitmap mBackgroundBitmap;
    private Canvas mBackgroundCanvas;
    public TextBackImage(final Context context) {
        super(context);
        contextSensor=context;
        init();
    }
    public TextBackImage(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        contextSensor=context;
        init();
    }
    private void init() {
        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
       // super.setBackgroundColor(Color.BLACK);
        super.setTextColor(Color.BLACK);
        super.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }
    @Override
    public void setBackground(final Drawable bg) {
        if (mBackground == bg) {
            return;
        }
        mBackground = bg;
        int w = getWidth();
        int h = getHeight();
        if (mBackground != null && w != 0 && h != 0) {
            mBackground.setBounds(0, 0, w, h);
        }
        requestLayout();
        invalidate();
    }
    @Override
    public void setBackgroundColor(final int color) {
        setBackground(new ColorDrawable(color));
    }
    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == 0 || h == 0) {
            freeBitmaps();
            return;
        }
        createBitmaps(w, h);
        if (mBackground != null) {
            mBackground.setBounds(0, 0, w, h);
        }
    }
    private void createBitmaps(int w, int h) {
        mBackgroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBackgroundCanvas = new Canvas(mBackgroundBitmap);
        mMaskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);
        mMaskCanvas = new Canvas(mMaskBitmap);
    }
    private void freeBitmaps() {
        mBackgroundBitmap = null;
        mBackgroundCanvas = null;
        mMaskBitmap = null;
        mMaskCanvas = null;
    }
    @Override
    protected void onDraw(final Canvas canvas) {
        if (isNothingToDraw()) {
            return;
        }
        drawMask();
        drawBackground();
        canvas.drawBitmap(mBackgroundBitmap, 0.f, 0.f, null);
    }
    private boolean isNothingToDraw() {
        return mBackground == null
                || getWidth() == 0
                || getHeight() == 0;
    }
    @SuppressLint("WrongCall")
    private void drawMask() {
        clear(mMaskCanvas);
        super.onDraw(mMaskCanvas);
    }
    private void drawBackground() {
        clear(mBackgroundCanvas);
        mBackground.draw(mBackgroundCanvas);
        mBackgroundCanvas.drawBitmap(mMaskBitmap, 0.f, 0.f, mPaint);
    }
    private static void clear(Canvas canvas) {
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
    }
    double previousAzi,previousPic,previousRoll;
    public void setOrientation(final ImageView imageView)
    {
        SensorManager sensorManager =
                (SensorManager) this.contextSensor.getSystemService(SENSOR_SERVICE);
        Sensor magnetometer=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SensorEventListener magnetoSensorListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                double mAzimuth =Math.abs((Math.pow(event.values[0],1.0)+47)%100);
                double mPitch = Math.abs((Math.pow(event.values[1],1)+47)%100);
                double mRoll = Math.abs((Math.pow(event.values[2],1)+47)%100);
                double left=(mAzimuth*mPitch*mRoll)%100;
                Log.i("tag",mAzimuth + "   "+mRoll+"   "+mPitch+ "     "+left+"   "+Math.abs((int)((mAzimuth+mPitch+mRoll)-(previousRoll+previousPic+previousAzi))));
                if(Math.abs((int)((mAzimuth+mPitch+mRoll)-(previousRoll+previousPic+previousAzi)))>2){
                imageView.setPadding((int)left,(int)mPitch,(int)mRoll,(int)mAzimuth);}
            previousAzi=mAzimuth;
            previousPic=mPitch;
            previousRoll=mRoll;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        // Register the listener
        sensorManager.registerListener(magnetoSensorListener,
                magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}