package com.csc133.snakegame;

public class Obstacle {
        private int x, y; // Position of the obstacle
        private int size; // Size of the obstacle

        public Obstacle(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }

        // Getters and setters for position and size

        public boolean collidesWith(Rect rect) {
            // Check if the obstacle collides with a given rectangle (e.g., snake's bounding box)
            return Rect.intersects(new Rect(x, y, x + size, y + size), rect);
        }

        public void draw(Canvas canvas, Paint paint) {
            // Draw the obstacle on the canvas
            paint.setColor(Color.RED); // Set color for obstacles (adjust as needed)
            canvas.drawRect(x, y, x + size, y + size, paint);
        }
    }


