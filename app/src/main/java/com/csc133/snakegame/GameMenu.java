package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Point;


enum Difficulty {
    EASY, MEDIUM, HARD
}

class GameMenu extends View {
    private Bitmap mBackgroundBitmap;
    private Difficulty selectedDifficulty = Difficulty.MEDIUM; // Default difficulty
    private Paint mPaint;
    private Rect mEasyButton, mMediumButton, mHardButton;
    private SnakeActivity mSnakeActivity;

        public GameMenu(Context context, Bitmap background, Point size) {
            super(context);
            mSnakeActivity = (SnakeActivity) context;
            // Scale the background bitmap to fit the screen
            mBackgroundBitmap = Bitmap.createScaledBitmap(background, size.x, size.y, false);
            mPaint = new Paint();

        // Define button areas for difficulty selection
        // For simplicity, these are just example rects, you should position and size them appropriately.
        mEasyButton = new Rect(50, 100, 250, 200);
        mMediumButton = new Rect(50, 300, 250, 400);
        mHardButton = new Rect(50, 500, 250, 600);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(50);

        // Draw buttons for difficulty levels
        canvas.drawText("Easy", mEasyButton.left, mEasyButton.bottom, mPaint);
        canvas.drawText("Medium", mMediumButton.left, mMediumButton.bottom, mPaint);
        canvas.drawText("Hard", mHardButton.left, mHardButton.bottom, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Check if a difficulty button was pressed
                if (mEasyButton.contains(x, y)) {
                    selectedDifficulty = Difficulty.EASY;
                } else if (mMediumButton.contains(x, y)) {
                    selectedDifficulty = Difficulty.MEDIUM;
                } else if (mHardButton.contains(x, y)) {
                    selectedDifficulty = Difficulty.HARD;
                }
                // Once a difficulty is selected, start the game
                mSnakeActivity.startGame(selectedDifficulty);
                return true;
        }

        return super.onTouchEvent(event);
    }
}

