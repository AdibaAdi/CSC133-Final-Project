package com.csc133.snakegame;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;


public abstract class Rectangle implements DrawableMovable{


    public int x, y;
    public int width, height;
    private int color; // Changed from Color to int
    private RectF rectF;
    private Paint mPaint;


    private boolean visible;


    public Rectangle(int x, int y, int width, int height, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.rectF = new RectF(x, y, x + width, y + height);
        this.mPaint = new Paint();
        this.mPaint.setColor(color);
    }


    // Getters and setters for private fields if needed


    @Override
    public Point getLocation() {
        return null;
    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible){
        this.visible = visible;
    }
    // Example: Set color of the object
    public void setColor(int color) {
        this.color = color;
        this.mPaint.setColor(color);
    }


    // Example: Get the RectF representing the boundaries
    public RectF getRectF() {
        return rectF;
    }
    @Override
    public void reset() {

    }

    // Example: Draw the rectangle on a canvas
    public void draw(Canvas canvas, Paint mPaint) {
        canvas.drawRect(rectF, mPaint);
    }
}
