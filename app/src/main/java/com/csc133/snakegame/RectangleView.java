package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;


public class RectangleView extends SurfaceView {
    public int x = 0;
    public int y = 100;
    public int width = 100;
    public int height = 50;
    public int moveSpace = 10;
    public int max_x, max_y;

    public RectangleView(Context context) {
        super(context);

    }

    public RectangleView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public RectangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init() {
        // Initialize any variables here
        max_x = getWidth(); // Set max_x to the width of the view
        max_y = getHeight(); // Set max_y to the height of the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the rectangle on the canvas
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(x, y, x + width, y + height, paint);

        // Update the position of the rectangle
        if (x + width > max_x || x < 0)
            moveSpace =- moveSpace;
        x += moveSpace;

        // Invalidate the view to trigger a redraw
        invalidate();
    }

}

