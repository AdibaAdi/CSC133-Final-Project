package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Coin implements DrawableMovable {

    private Point coinLocation = new Point();
    private Point mCoinSpawnRange;
    private int mSize;
    private Bitmap mBitmapCoin;

    Coin(Context context, Point sr, int s){
        mCoinSpawnRange = sr;
        mSize = s;
        coinLocation.x = -10;
        coinLocation.y = -10;
        int scaleFactor = 2;

        // Load the image to the bitmap
        mBitmapCoin = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin);
        mBitmapCoin = Bitmap.createScaledBitmap(mBitmapCoin, (int)(mSize * scaleFactor), (int)(mSize * scaleFactor), false);
    }

    public void spawn(){
        Random random = new Random();
        coinLocation.x = random.nextInt(mCoinSpawnRange.x) + 1;
        coinLocation.y = random.nextInt(mCoinSpawnRange.y - 1) + 1;
    }

    @Override
    public Point getLocation(){
        return coinLocation;
    }

    @Override
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapCoin, coinLocation.x * mSize, coinLocation.y * mSize, paint);
    }

    @Override
    public void move() {
    }

    @Override
    public void reset() {
        coinLocation.x = -10; // Move off-screen
        coinLocation.y = -10; // Move off-screen
    }
}

