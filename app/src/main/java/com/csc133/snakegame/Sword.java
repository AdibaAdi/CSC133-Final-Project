package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.Random;

class Sword implements DrawableMovable {

    private Point swordLocation = new Point();
    private Point mSwordSpawnRange;
    private int mSize;
    private Bitmap mBitmapSword;

    public Sword(Context context, Point sr, int s) {
        mSwordSpawnRange = sr;
        mSize = s;
        swordLocation.x = -10;
        swordLocation.y = -10;
        int scaleFactor = 2;

        // Load the image to the bitmap
        mBitmapSword = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword);
        mBitmapSword = Bitmap.createScaledBitmap(mBitmapSword, (int) (mSize * scaleFactor), (int) (mSize * scaleFactor), false);
    }

    public void spawn() {
        Random random = new Random();
        swordLocation.x = random.nextInt(mSwordSpawnRange.x);
        swordLocation.y = random.nextInt(mSwordSpawnRange.y);
    }

    @Override
    public Point getLocation() {
        return swordLocation;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapSword, swordLocation.x * mSize, swordLocation.y * mSize, paint);
    }
    public RectF getHitbox() {
        // Top left corner for reference
        int left = swordLocation.x * mSize;
        int top = swordLocation.y * mSize;

        // Get the width and height of the apple bitmap
        int width = mBitmapSword.getWidth();
        int height = mBitmapSword.getHeight();

        // Create a rectangle hitbox of the apple
        return new RectF(left, top, left + width, top + height);
    }
    @Override
    public void move() {
        // Swords do not move, so this method is empty
    }

    @Override
    public void reset() {
        swordLocation.x = -10; // Move off-screen
        swordLocation.y = -10; // Move off-screen
    }
}