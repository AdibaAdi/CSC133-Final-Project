package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;
import android.graphics.RectF;

class Apple implements DrawableMovable {

    // The location of the apple on the grid
    // Not in pixels
    private Point location = new Point();

    // The range of values we can choose from
    // to spawn an apple
    private Point mSpawnRange;
    private int mSize;

    // An image to represent the apple
    private Bitmap mBitmapApple;

    private int additionalBombsToCreate = 0;
    /// Set up the apple in the constructor
    Apple(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        mSpawnRange = sr;
        // Make a note of the size of an apple
        mSize = s;
        // Hide the apple off-screen until the game starts
        location.x = -10;
        location.y = -10;
        int scaleFactor = 2;

        // Load the image to the bitmap
        mBitmapApple = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);

        // Resize the bitmap
        //mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, s, s, false);
        mBitmapApple = Bitmap.createScaledBitmap(mBitmapApple, (int)(mSize * scaleFactor), (int)(mSize * scaleFactor), false);
    }
    public void setAdditionalBombsToCreate(int count) {
        additionalBombsToCreate = count;
    }



    public int getAdditionalBombsToCreate() {
        // Logic to determine the number of additional bombs to create
        // For example, you can return a constant value or calculate it based on certain conditions
        return 0; // Replace 0 with your logic
    }
    // This is called every time an apple is eaten
    public void spawn(){
        // Choose two random values and place the apple
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }

    // Let SnakeGame know where the apple is
    // SnakeGame can share this with the snake
    public Point getLocation(){
        return location;
    }

    // Draw the apple
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmapApple,
                location.x * mSize, location.y * mSize, paint);

    }

    // Create hitbox of the apple
    public RectF getHitbox() {
        // Top left corner for reference
        int left = location.x * mSize;
        int top = location.y * mSize;

        // Get the width and height of the apple bitmap
        int width = mBitmapApple.getWidth();
        int height = mBitmapApple.getHeight();

        // Create a rectangle hitbox of the apple
        return new RectF(left, top, left + width, top + height);
    }

    // Since the Apple doesn't move by itself, this method can be left empty
    @Override
    public void move() {
        // Apple doesn't move so this can remain empty
    }

    // Resets the apple's position off-screen
    @Override
    public void reset() {
        location.x = -10; // Move off-screen
        location.y = -10; // Move off-screen
    }

}