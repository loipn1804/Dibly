package com.dibs.dibly.view;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Flake
{


    float x, y;
    float rotation;
    float speed;
    float rotationSpeed;
    int width, height;
    Bitmap bitmap;


    static HashMap<Integer, Bitmap> bitmapMap = new HashMap<Integer, Bitmap>();


    static Flake createFlake(float xRange, Bitmap originalBitmap, int screenHeigh, int pos)
    {
        List<Float> listRandom = new ArrayList<>();
        listRandom.add(0.2f);
        listRandom.add(0.25f);
        listRandom.add(0.3f);
        listRandom.add(0.35f);
        listRandom.add(0.5f);
        listRandom.add(0.4f);
        listRandom.add(0.7f);
        listRandom.add(0.8f);
        listRandom.add(0.9f);
        listRandom.add(0.85f);
        listRandom.add(0.55f);

        Flake flake = new Flake();
        // Size each flake with a width between 5 and 55 and a proportional
        // height
        flake.width = originalBitmap.getWidth();//(int) (5 + (float) Math.random() * 50);

        float hwRatio = originalBitmap.getHeight() / originalBitmap.getWidth();
        if (hwRatio == 0)
        {
            hwRatio = 2;
        }

        if (hwRatio > 1)
        {
            hwRatio = 2;
        }

        flake.width = (int)(originalBitmap.getWidth()/hwRatio);

        flake.height = (int)(originalBitmap.getHeight()/hwRatio);//(int) (flake.width * hwRatio);

        // Position the flake horizontally between the left and right
        flake.x = (float) Math.random() * (xRange);
        // Position the flake vertically slightly off the top of the display
        flake.y = (float) (screenHeigh * (listRandom.get(pos - 1)));//(screenHeigh + (flake.height + (float) Math.random() * flake.height))*(50/100);

        // Each flake travels at 50-200 pixels per second
        flake.speed = 10 + (float) Math.random() * 150;

        // Flakes start at -90 to 90 degrees rotation, and rotate between -45
        // and 45
        // degrees per second
        flake.rotation = (float) Math.random() * 180 - 90;
        flake.rotationSpeed = (float) Math.random() * 90 - 45;

        // Get the cached bitmap for this size if it exists
        flake.bitmap = bitmapMap.get(flake.width);
        if (flake.bitmap == null)
        {
            flake.bitmap = Bitmap.createScaledBitmap(originalBitmap, flake.width, flake.height, false);
            bitmapMap.put(flake.width, flake.bitmap);
        }

//        flake.bitmap = originalBitmap;

        return flake;
    }
}
