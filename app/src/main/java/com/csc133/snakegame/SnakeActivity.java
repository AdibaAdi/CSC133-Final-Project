package com.csc133.snakegame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SnakeActivity extends Activity implements GameOverListener {

    private SnakeGame mSnakeGame;
    private PauseButtonHandler pauseButtonHandler;
    private GameMenu gameMenu;

    private Difficulty mCurrentDifficulty;
    private Bitmap menuBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        menuBackground = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background);
        gameMenu = new GameMenu(this, menuBackground, size);

        mCurrentDifficulty = Difficulty.MEDIUM; // Default to MEDIUM difficulty
        if (savedInstanceState != null) {
            mCurrentDifficulty = (Difficulty) savedInstanceState.getSerializable("CurrentDifficulty");
        }

        // Initially show the game menu
        setContentView(gameMenu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("CurrentDifficulty", mCurrentDifficulty);
    }

    public void startGame(Difficulty difficulty) {
        mCurrentDifficulty = difficulty; // Set the current difficulty
        Point gameSize = getSize();

        mSnakeGame = new SnakeGame(this, gameSize, mCurrentDifficulty);
        mSnakeGame.setDifficulty(mCurrentDifficulty);

        pauseButtonHandler = new PauseButtonHandler(this, mSnakeGame);
        mSnakeGame.setPauseButtonHandler(pauseButtonHandler);

        FrameLayout gameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        buttonParams.bottomMargin = 50;

        gameLayout.addView(mSnakeGame);
        gameLayout.addView(pauseButtonHandler.getPauseButton(), buttonParams);

        setContentView(gameLayout);
        mSnakeGame.setGameOverListener(this);
        mSnakeGame.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSnakeGame != null) {
            mSnakeGame.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSnakeGame != null) {
            mSnakeGame.pause();
        }
    }

    public void onGameOver(int score) {
        runOnUiThread(() -> {
            LayoutInflater inflater = LayoutInflater.from(SnakeActivity.this);
            View gameOverView = inflater.inflate(R.layout.dialog_game_over, null);
            TextView tvScore = gameOverView.findViewById(R.id.tvScore);
            tvScore.setText(getResources().getString(R.string.score_text, score));

            new AlertDialog.Builder(SnakeActivity.this)
                    .setView(gameOverView)
                    .setPositiveButton("Play Again", (dialog, id) -> startGame(mCurrentDifficulty))
                    .setNegativeButton("Exit to Menu", (dialog, id) -> {
                        setContentView(gameMenu); // Exit to menu after game over
                    })
                    .setCancelable(false)
                    .show();
        });
    }

    public Difficulty getCurrentDifficulty() {
        return mCurrentDifficulty;
    }

    public Point getSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public Bitmap getBackgroundBitmap() {
        if (menuBackground == null) {
            menuBackground = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background);
        }
        return menuBackground;
    }
}
