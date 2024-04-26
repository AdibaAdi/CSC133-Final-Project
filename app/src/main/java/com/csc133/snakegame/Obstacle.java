package com.csc133.snakegame;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle {


    public enum ObstacleType {
        REGULAR, BOOM
    }
    private int x, y; // Position of the obstacle
    private int size; // Size of the obstacle
    private ObstacleType type;
    public Obstacle(int x, int y, int size, ObstacleType type) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.type = type;
    }

        // Getters and setters for position and size

    public boolean collidesWith(Rect rect) {
            // Check if the obstacle collides with a given rectangle (e.g., snake's bounding box)
            return Rect.intersects(new Rect(x, y, x + size, y + size), rect);
    }
    public void draw(Canvas canvas, Paint paint) {
            // Draw the obstacle on the canvas
            paint.setColor(Color.BLUE); // Set color for obstacles (adjust as needed)
            canvas.drawRect(x, y, x + size, y + size, paint);
    }
}


