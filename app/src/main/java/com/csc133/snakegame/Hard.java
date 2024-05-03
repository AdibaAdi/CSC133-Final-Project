package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

public class Hard extends RectangleView {
    private int moveSpace = 10; // Example movement speed



    public Hard(Context context, int x, int y, int width, int height) {
            this(context, null, 0, x, y, width, height);
        }

    public Hard(Context context, AttributeSet attrs, int defStyleAttr, int x, int y, int width, int height) {
        super(context, attrs, defStyleAttr);
        initialize(x, y, width, height);
    }
    public Hard(Context context, AttributeSet attrs, int x, int y, int width, int height) {
        super(context, attrs);
        initialize(x, y, width, height);
    }
    private void initialize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        // Initialize any variables here
        max_x = getWidth(); // Set max_x to the width of the view
        max_y = getHeight(); // Set max_y to the height of the view
    }

    //@Override
    protected void draw(Canvas mCanvas, Paint mPaint) {
        super.draw(mCanvas);

        // Draw the rectangle on the canvas
        //Paint paint = new Paint();
        mPaint.setColor(Color.WHITE);
        mCanvas.drawRect(x, y, x + width, y + height, mPaint);

        // Update the position of the rectangle
        if (x + width > max_x || x < 0) {
            moveSpace = -moveSpace;
        }
        x += moveSpace;

        // Invalidate the view to trigger a redraw
        invalidate();
    }

    //public void draw(Canvas mCanvas, Paint mPaint) {
    //}
}
