package com.csc133.snakegame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.Random;

public class Hard {
    private Point boomLocation = new Point();
    private Point mboomSpawnRange;
    private boolean boomEncountered;
    private int boomX;
    private int boomY;

    public Hard(int initialSpeed, int initialEnemyCount, int initialResourceCount) {
        // Existing constructor code...

        this.boomEncountered = false;
        spawn();
    }
    public Obstacle(int x, int y, int size) {
        super(x, y, size, ObstacleType.BOOM);
    }

    private void spawn() {
        // Generate coordinates for the boom obstacle randomly
        Random rand = new Random();
        this.boomX = rand.nextInt(mboomSpawnRange.x); // Assuming screenWidth is defined elsewhere
        this.boomY = rand.nextInt(mboomSpawnRange.y); // Assuming screenHeight is defined elsewhere
    }

    public boolean isBoomEncountered() {
        return boomEncountered;
    }
    @Override
    public void draw(Canvas canvas, Paint paint) {
        // Customize appearance of boom obstacles
        paint.setColor(Color.RED); // Set color for boom obstacles
        canvas.drawRect(getX(), getY(), getX() + getSize(), getY() + getSize(), paint);

        // Draw a symbol or pattern on the boom obstacle
        paint.setColor(Color.BLACK);
        paint.setTextSize(getSize() * 0.7f); // Adjust text size relative to obstacle size
        canvas.drawText("BOOM", getX() + getSize() * 0.1f, getY() + getSize() * 0.7f, paint);
    }
    public void checkCollisionWithBoom(int playerX, int playerY) {
        // Check if the player's position collides with the boom obstacle
        if (playerX == boomX && playerY == boomY) {
            boomEncountered = true;
            // Handle the effect of encountering the boom (e.g., decrease resources, end game, etc.)
            // Example: decrease player resources
            decreasePlayerResources();
        }
    }

    private void decreasePlayerResources() {
        // Decrease player resources upon encountering the boom obstacle
        // Example: reduce the length of the snake, reduce score, etc.
    }

}
