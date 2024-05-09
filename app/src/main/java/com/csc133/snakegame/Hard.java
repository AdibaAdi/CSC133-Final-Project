package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hard implements DrawableMovable {
    private List<Bomb> bombs = new ArrayList<>();

    private int bombCount;

    private Point location = new Point();
    private int mSize;
    private Bitmap mBitmapBomb;

    private Point mSpawnRange; // Define spawn range for the Bomb

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }


    private Direction direction;

    Hard(Context context, Point sr, int s) {
        mSpawnRange = sr;
        mSize = s;
        location.x = -10;
        location.y = -10;
        int scaleFactor = 2;
        mBitmapBomb = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
        mBitmapBomb = Bitmap.createScaledBitmap(mBitmapBomb, (int) (mSize * scaleFactor), (int) (mSize * scaleFactor), false);


        // Initialize the direction randomly
        direction = Direction.values()[new Random().nextInt(Direction.values().length)];
    }
    public void spawn() {
        Random random = new Random();
        location.x = random.nextInt(mSpawnRange.x) + 1;
        location.y = random.nextInt(mSpawnRange.y - 1) + 1;
    }


    public Point getLocation() {
        return location;
    }


    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmapBomb, location.x * mSize, location.y * mSize, paint);
    }


    public RectF getHitbox() {
        int left = location.x * mSize;
        int top = location.y * mSize;
        int width = mBitmapBomb.getWidth();
        int height = mBitmapBomb.getHeight();
        return new RectF(left, top, left + width, top + height);
    }


    @Override
    public void move() {
        switch (direction) {
            case UP:
                location.y--;
                break;
            case DOWN:
                location.y++;
                break;
            case LEFT:
                location.x--;
                break;
            case RIGHT:
                location.x++;
                break;
        }
        if (location.x <= 0 || location.x >= mSpawnRange.x - mSize ||
                location.y <= 0 || location.y >= mSpawnRange.y - mSize) {
            changeDirectionRandomly();
        }


    }
    public int getBombCount() {
        return bombs.size(); // Assuming you want to return the number of bombs in the list
    }
    public void setBombCount(int count) {
        this.bombCount = count;
    }
    private void changeDirectionRandomly() {
        // Change direction randomly
        direction = Direction.values()[new Random().nextInt(Direction.values().length)];
    }


    @Override
    public void reset() {
        location.x = -10;
        location.y = -10;
    }
}
