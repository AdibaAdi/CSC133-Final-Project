package com.csc133.snakegame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;

public class SnakeActivity extends Activity {

    SnakeGame mSnakeGame;
    PauseButtonHandler pauseButtonHandler;
    GameMenu gameMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Bitmap menuBackground = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background);
        gameMenu = new GameMenu(this, menuBackground, size); // Pass size to properly scale the background

        setContentView(gameMenu); // Set the game menu as the initial view

    }


    public void startGame(Difficulty difficulty) {
        FrameLayout gameLayout = new FrameLayout(this);
        mSnakeGame = new SnakeGame(this, new Point(gameMenu.getWidth(), gameMenu.getHeight()));
        mSnakeGame.setDifficulty(difficulty);

        pauseButtonHandler = new PauseButtonHandler(this, mSnakeGame);
        mSnakeGame.setPauseButtonHandler(pauseButtonHandler);

        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        buttonParams.bottomMargin = 50;

        gameLayout.addView(mSnakeGame);
        gameLayout.addView(pauseButtonHandler.getPauseButton(), buttonParams);

        setContentView(gameLayout); // Switch view to the game layout

        mSnakeGame.resume(); // Start/resume the game
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Only call resume if mSnakeGame is not null and the game has been initialized
        if (mSnakeGame != null) {
            mSnakeGame.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Only call pause if mSnakeGame is not null and the game has been initialized
        if (mSnakeGame != null) {
            mSnakeGame.pause();
        }
    }
}
