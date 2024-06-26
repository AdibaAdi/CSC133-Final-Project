package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.graphics.RectF;
import java.util.ArrayList;

class Snake implements DrawableMovable{

    // The location in the grid of all the segments
    private ArrayList<Point> segmentLocations;

    // How big is each segment of the snake?
    private int mSegmentSize;

    // How big is the entire grid
    private Point mMoveRange;
    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;

    private static final int scaleFactor = 3; // The scale factor to increase the size by 3

    Snake(Context context, Point mr, int ss) {
        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

        // Initialize the segment size and movement range from the passed in parameters
        mSegmentSize = ss;
        mMoveRange = mr;

        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        ss * scaleFactor, ss * scaleFactor, false);
        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss * scaleFactor, ss * scaleFactor, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss * scaleFactor, ss * scaleFactor, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss * scaleFactor, ss * scaleFactor, matrix, true);

        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);

        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        ss, ss, false);

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x * ss / 2;
    }

    @Override
    public void reset() {
        // Resetting the snake's state for a new game
        segmentLocations.clear(); // Clear old segments
        segmentLocations.add(new Point(mMoveRange.x / 2, mMoveRange.y / 2)); // Start in middle
        heading = Heading.RIGHT; // Default direction
    }

    @Override
    public void spawn() {

    }


    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;

        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(w / 2, h / 2));
    }


    public void move() {
        if (segmentLocations.isEmpty()) {
            // Nothing to move if there are no segments
            return;
        }
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {

            // Make it the same value as the next segment
            // going forwards towards the head
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        Point p = segmentLocations.get(0);

        // Move it appropriately
        switch (heading) {
            case UP:
                p.y--;
                break;

            case RIGHT:
                p.x++;
                break;

            case DOWN:
                p.y++;
                break;

            case LEFT:
                p.x--;
                break;
        }


    }

    boolean detectDeath() {
        if (segmentLocations.isEmpty()) {
            // Can't die if there are no segments
            return false;
        }
        // Has the snake died?
        boolean dead = false;

        // Hit any of the screen edges
        if (segmentLocations.get(0).x == -1 ||
                segmentLocations.get(0).x > mMoveRange.x ||
                segmentLocations.get(0).y == -1 ||
                segmentLocations.get(0).y > mMoveRange.y) {

            dead = true;
        }

        // Eaten itself?
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            // Have any of the sections collided with the head
            if (segmentLocations.get(0).x == segmentLocations.get(i).x &&
                    segmentLocations.get(0).y == segmentLocations.get(i).y) {

                dead = true;
            }
        }
        return dead;
    }

    boolean checkDinner(RectF objectHitbox, int num) {
        // Get the head location of the snake
        Point headLocation = segmentLocations.get(0);

        // Calculate the hitbox of the snake's head
        int headLeft = headLocation.x * mSegmentSize;
        int headTop = headLocation.y * mSegmentSize;
        int headRight = headLeft + mSegmentSize;
        int headBottom = headTop + mSegmentSize;

        // Create a RectF representing the snake's head hitbox
        RectF headHitbox = new RectF(headLeft, headTop, headRight, headBottom);

        // Check if the snake's head hitbox collides with the apple hitbox
        if (RectF.intersects(headHitbox, objectHitbox)) {
            // Add or remove segments based on the value of num
            if (num > 0) {
                for (int i = 0; i < num; i++) {
                    segmentLocations.add(new Point(-10, -10));
                }
            } else if (num < 0 && segmentLocations.size() + num > 0) {
                for (int i = 0; i < -num; i++) {
                    segmentLocations.remove(segmentLocations.size() - 1);
                }
            }
            return true;
        }

        return false;
    }

    public void draw(Canvas canvas, Paint paint) {
        // Check if there are any segments to draw
        if (!segmentLocations.isEmpty()) {
            // Existing drawing code...
            // Draw the head

            // Calculate the offset to center the bitmap around the head location
            int offsetX = (mBitmapHeadRight.getWidth() - mSegmentSize) / 2;
            int offsetY = (mBitmapHeadRight.getHeight() - mSegmentSize) / 2;

            Point headLocation = segmentLocations.get(0);
            float headX = headLocation.x * mSegmentSize - offsetX;
            float headY = headLocation.y * mSegmentSize - offsetY;

            switch (heading) {
                case RIGHT:
                canvas.drawBitmap(mBitmapHeadRight, headX, headY, paint);
                break;

                case LEFT:
                    canvas.drawBitmap(mBitmapHeadLeft, headX, headY, paint);
                    break;

                case UP:
                    canvas.drawBitmap(mBitmapHeadUp, headX, headY, paint);
                    break;

                case DOWN:
                    canvas.drawBitmap(mBitmapHeadDown, headX, headY, paint);
                    break;
            }

            // Draw the snake body one block at a time
            for (int i = 1; i < segmentLocations.size(); i++) {
                canvas.drawBitmap(mBitmapBody,
                        segmentLocations.get(i).x
                                * mSegmentSize,
                        segmentLocations.get(i).y
                                * mSegmentSize, paint);

            }
        }
    }


    @Override
    public Point getLocation() {
        // Return the location of the snake's head
        return segmentLocations.get(0);
    }

    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfWayPoint) {
            switch (heading) {
                // Rotate right
                case UP:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.UP;
                    break;

            }
        } else {
            // Rotate left
            switch (heading) {
                case UP:
                    heading = Heading.LEFT;
                    break;
                case LEFT:
                    heading = Heading.DOWN;
                    break;
                case DOWN:
                    heading = Heading.RIGHT;
                    break;
                case RIGHT:
                    heading = Heading.UP;
                    break;
            }
        }
    }
}