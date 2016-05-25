package com.dibs.dibly.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.dibs.dibly.R;

import java.util.ArrayList;


public class FlakeView extends View {

    private Bitmap droid; // The bitmap that all flakes use
    private Bitmap droid1;
    private Bitmap droid2;
    private Bitmap droid3;
    private Bitmap droid4;
    private Bitmap droid5;
    private Bitmap droid6;
    private Bitmap droid7;
    private Bitmap droid8;
    private Bitmap droid9;
    private Bitmap droid10;

    private int numFlakes; // Current number of flakes
    private ArrayList<Flake> flakes; // List of current flakes


    private ValueAnimator animator;
    private long startTime, prevTime; // Used to track elapsed time for animations and
    // fps
    private int frames; // Used to track frames per second
    private Paint textPaint; // Used for rendering fps text
    private float fps; // frames per second
    private Matrix m; // Matrix used to translate/rotate each flake
    // during rendering
    private String fpsString = "";
    private String numFlakesString = "";

    public FlakeView(Context context) {
        super(context);
        animator = ValueAnimator.ofFloat(0, 1);
        m = new Matrix();

        flakes = new ArrayList<Flake>();
        numFlakes = 0;

        frames = 0;
        fps = 0;

        // textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // textPaint.setColor(Color.WHITE);
        // textPaint.setTextSize(24);


        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float) (nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y -= (flake.speed * secs);
                    if (flake.y < 0 - flake.height) {
                        // If a flake falls off the bottom, send it back to the
                        // top
                        flake.y = getHeight() + flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                // Force a redraw to see the flakes in their new positions and
                // orientations
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
        animator.start();
    }

    int getNumFlakes() {
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    void addFlakes() {

        droid = BitmapFactory.decodeResource(getResources(), R.drawable.fine);
        droid1 = BitmapFactory.decodeResource(getResources(), R.drawable.fine4);
        droid2 = BitmapFactory.decodeResource(getResources(), R.drawable.fine2);
        droid3 = BitmapFactory.decodeResource(getResources(), R.drawable.fine3);
        droid4 = BitmapFactory.decodeResource(getResources(), R.drawable.fine1);
        droid5 = BitmapFactory.decodeResource(getResources(), R.drawable.fine5);
        droid6 = BitmapFactory.decodeResource(getResources(), R.drawable.fine6);
        droid7 = BitmapFactory.decodeResource(getResources(), R.drawable.fine7);
        droid8 = BitmapFactory.decodeResource(getResources(), R.drawable.fine10);
        droid9 = BitmapFactory.decodeResource(getResources(), R.drawable.fine9);
        droid10 = BitmapFactory.decodeResource(getResources(), R.drawable.fine8);
        for (int i = 0; i < 3; ++i) {
            flakes.add(Flake.createFlake(getWidth(), droid, getHeight(), 1));
            flakes.add(Flake.createFlake(getWidth(), droid1, getHeight(), 2));
            flakes.add(Flake.createFlake(getWidth(), droid2, getHeight(), 3));
            flakes.add(Flake.createFlake(getWidth(), droid3, getHeight(), 4));
            flakes.add(Flake.createFlake(getWidth(), droid4, getHeight(), 5));
            flakes.add(Flake.createFlake(getWidth(), droid5, getHeight(), 6));
            flakes.add(Flake.createFlake(getWidth(), droid6, getHeight(), 7));
            flakes.add(Flake.createFlake(getWidth(), droid7, getHeight(), 8));
            flakes.add(Flake.createFlake(getWidth(), droid8, getHeight(), 9));
            flakes.add(Flake.createFlake(getWidth(), droid9, getHeight(), 10));
            flakes.add(Flake.createFlake(getWidth(), droid10, getHeight(), 11));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine), getHeight(), 1));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine4), getHeight(), 2));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine2), getHeight(), 3));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine3), getHeight(), 4));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine1), getHeight(), 5));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine5), getHeight(), 6));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine6), getHeight(), 7));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine7), getHeight(), 8));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine10), getHeight(), 9));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine9), getHeight(), 10));
//            flakes.add(Flake.createFlake(getWidth(), BitmapFactory.decodeResource(getResources(), R.drawable.fine8), getHeight(), 11));
        }
        setNumFlakes(33);
    }


    void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        flakes.clear();
        numFlakes = 0;
        addFlakes();
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to
        // rotate around its center),
        // rotate by its current rotation, translate by its location, then draw
        // its bitmap
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width / 2, -flake.height / 2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }

        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = (float) deltaTime / 1000f;
            fps = (float) frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
        // canvas.drawText(numFlakesString, getWidth() - 200, 20, textPaint);
        // canvas.drawText(fpsString, getWidth() - 200, 50, textPaint);
    }

    public void pause() {

        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

    public void destroy() {
//        droid.recycle();
//        droid1.recycle();
//        droid2.recycle();
//        droid3.recycle();
//        droid4.recycle();
//        droid5.recycle();
//        droid6.recycle();
//        droid7.recycle();
//        droid8.recycle();
//        droid9.recycle();
//        droid10.recycle();
        droid = null;
        droid1 = null;
        droid2 = null;
        droid3 = null;
        droid4 = null;
        droid5 = null;
        droid6 = null;
        droid7 = null;
        droid8 = null;
        droid9 = null;
        droid10 = null;

        for (Flake flake : flakes) {
            flake.bitmap = null;
        }

        animator.removeAllUpdateListeners();
        animator.end();
        flakes.clear();
    }
}
