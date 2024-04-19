package com.csc133.snakegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class GameEnd extends View {
    private Paint mPaint;
    private int mScore;
    private Typeface gameFont;
    private SnakeActivity mSnakeActivity;

    public GameEnd(Context context, int score) {
        super(context);
        if (context instanceof SnakeActivity) {
            mSnakeActivity = (SnakeActivity) context;
        }
        this.mScore = score;
        init();
    }

    private void init() {
        mPaint = new Paint();
        gameFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/press_start_2p.ttf");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background or overlay if needed
        canvas.drawColor(Color.argb(80, 0, 0, 0)); // Translucent overlay

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(100);
        mPaint.setTypeface(gameFont);
        mPaint.setTextAlign(Paint.Align.CENTER);

        // Draw the game over text
        canvas.drawText("Game Over!", canvas.getWidth() / 2, canvas.getHeight() / 4, mPaint);

        // Draw the score
        mPaint.setTextSize(50);
        canvas.drawText("Score: " + mScore, canvas.getWidth() / 2, canvas.getHeight() / 2, mPaint);

        // Draw "Tap to Play again"
        mPaint.setTextSize(80);
        canvas.drawText("Tap to Play again", canvas.getWidth() / 2, (float) (canvas.getHeight() / 1.5), mPaint);

        // Draw "Exit to menu" button
        mPaint.setTextSize(50);
        canvas.drawText("Exit to menu", canvas.getWidth() / 2, (float) (canvas.getHeight() / 1.2), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Handle "Tap to Play again" touch
            // Assuming "Tap to Play again" is in the bottom half of the screen
            if (y > getHeight() / 2) {
                mSnakeActivity.startGame(mSnakeActivity.getCurrentDifficulty());
            }

            // Handle "Exit to menu" touch
            // Assuming "Exit to menu" is in the lower part of the screen
            if (y > (float) (getHeight() / 1.2)) {
                mSnakeActivity.finish();
            }
        }
        return super.onTouchEvent(event);
    }

}
