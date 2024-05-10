package com.csc133.snakegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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

    private int headingTop;
    private Rect mLeaderboardButton;

    public GameMenu(Context context, Bitmap background, Point size) {
        super(context);
        mSnakeActivity = (SnakeActivity) context;
        // Scale the background bitmap to fit the screen
        mBackgroundBitmap = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        mPaint = new Paint();


        // Calculate the position of the difficulty level heading and buttons
        int left = size.x / 2; // Center of the screen horizontally
        int top = size.y / 2; // Center of the screen vertically or wherever the buttons should start

        // These values may need to be adjusted to fit the content and aesthetics
        int buttonWidth = 200; // Width of each button
        int buttonHeight = 100; // Height of each button
        // Initialize headingTop here based on the provided size
        headingTop = size.y / 2 - 150;
        mPaint.setTextAlign(Paint.Align.CENTER); // Center text horizontally

        // Define the button rectangles centered horizontally
        mEasyButton = new Rect(left - buttonWidth / 2, top, left + buttonWidth / 2, top + buttonHeight);
        mMediumButton = new Rect(left - buttonWidth / 2, top + buttonHeight + 20, left + buttonWidth / 2, top + buttonHeight * 2 + 20);
        mHardButton = new Rect(left - buttonWidth / 2, top + buttonHeight * 2 + 40, left + buttonWidth / 2, top + buttonHeight * 3 + 40);
        //leaderboard button
        int buttonTop = mHardButton.bottom + 20;
        mLeaderboardButton = new Rect(left - buttonWidth / 2, buttonTop, left + buttonWidth / 2, buttonTop + buttonHeight);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Get the current width and height of the view
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        // Draw the background
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(60); // Adjust text size if necessary
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); // Set text to bold

        // Adjust your drawing coordinates using viewWidth and viewHeight
        // Draw the difficulty level heading
        canvas.drawText("Difficulty Level", viewWidth / 2, headingTop, mPaint);

        // Draw buttons for difficulty levels
        // Align the text to the center of the button
        canvas.drawText("Easy", mEasyButton.centerX(), mEasyButton.centerY() + mPaint.getTextSize() / 3, mPaint);
        canvas.drawText("Medium", mMediumButton.centerX(), mMediumButton.centerY() + mPaint.getTextSize() / 3, mPaint);
        canvas.drawText("Hard", mHardButton.centerX(), mHardButton.centerY() + mPaint.getTextSize() / 3, mPaint);
        canvas.drawText("Leaderboard", mLeaderboardButton.centerX(), mLeaderboardButton.centerY() + mPaint.getTextSize() / 3, mPaint);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mLeaderboardButton.contains(x, y)) {
                Intent intent = new Intent(getContext(), LeaderboardActivity.class);
                getContext().startActivity(intent);
                return true;
            } else if (mEasyButton.contains(x, y)) {
                selectedDifficulty = Difficulty.EASY;
                mSnakeActivity.startGame(selectedDifficulty);
                return true;
            } else if (mMediumButton.contains(x, y)) {
                selectedDifficulty = Difficulty.MEDIUM;
                mSnakeActivity.startGame(selectedDifficulty);
                return true;
            } else if (mHardButton.contains(x, y)) {
                selectedDifficulty = Difficulty.HARD;
                mSnakeActivity.startGame(selectedDifficulty);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

}