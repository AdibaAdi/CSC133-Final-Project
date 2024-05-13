package com.csc133.snakegame;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.app.Activity;
import android.media.MediaPlayer;

import android.util.Log;  // Import Log class


class SnakeGame extends SurfaceView implements Runnable, GameControls {


    private Activity mActivity;

    private Typeface gameFont;
    private Bitmap mBackgroundBitmap;
    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;
    private volatile boolean mPaused = true;
    private PauseButtonHandler pauseButtonHandler;
    private Difficulty mCurrentDifficulty; // To hold the current difficulty level


    // for playing sound effects
    private SoundPool mSP;
    private int mEat_Apple_ID = -1;
    private int mEat_Coin_ID = -1;
    private int mEat_Sword_ID = -1;

    private int mEat_Hard_ID = -1;
    private int mCrashID = -1;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 40;
    private int mNumBlocksHigh;
    private GameOverListener gameOverListener;
    // How many points does the player have
    private int mScore;

    // Objects for drawing
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private MediaPlayer mediaPlayer;


    // A snake ssss
//    private Snake mSnake;
////    // And an apple
//    private Apple mApple;


    // DrawableMovable interfaces for the snake and apple
    private DrawableMovable mSnake;
    private DrawableMovable mApple;
    private DrawableMovable mCoin;
    private DrawableMovable mSword1;
    private DrawableMovable mSword2;
    private DrawableMovable mSword3;
    private DrawableMovable mSword4;
    private DrawableMovable mSword5;
    private DrawableMovable[] mSwords;
    private DrawableMovable mHard;

    public void setGameOverListener(GameOverListener listener) {
        gameOverListener = listener;
    }


    public void setDifficulty(Difficulty difficulty) {
        // Adjust game parameters based on difficulty
        switch (difficulty) {
            case EASY:
                // Set parameters for easy difficulty
                break;
            case MEDIUM:
                // Set parameters for medium difficulty
                break;
            case HARD:
                // Set parameters for hard difficulty
                break;
        }
    }

    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size, Difficulty difficulty) {
        super(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }

        int blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_Apple_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("get_coin.ogg");
            mEat_Coin_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("dragon_hurt.ogg");
            mEat_Sword_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("get_bomb.ogg");
            mEat_Hard_ID = mSP.load(descriptor, 0);


            // MediaPlayer for background song
            descriptor = assetManager.openFd("ali_rap.ogg");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();

            // Start playing the background song
            mediaPlayer.setVolume(1.0f, 1.0f);
            mediaPlayer.setLooping(true); // Optional: set the song to loop
            mediaPlayer.start();

        } catch (IOException e) {
            // Error handling
        }


        mSurfaceHolder = getHolder();
        mPaint = new Paint();

        mBackgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_background);
        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, size.x, size.y, false);

        gameFont = Typeface.createFromAsset(context.getAssets(), "fonts/press_start_2p.ttf");

        mApple = new Apple(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSnake = new Snake(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mCoin = new Coin(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSword1 = new Sword(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSword2 = new Sword(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSword3 = new Sword(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSword4 = new Sword(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
        mSword5 = new Sword(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);

        mSwords = new DrawableMovable[]{mSword1, mSword2, mSword3, mSword4, mSword5};
        mCurrentDifficulty = difficulty;
        mHard = new Hard(context, new Point(NUM_BLOCKS_WIDE, mNumBlocksHigh), blockSize);
    }


    public void setPauseButtonHandler(PauseButtonHandler handler) {
        this.pauseButtonHandler = handler;
    }

    // Called to start a new game
    public void newGame() {
        // Reset the game objects
        mSnake.reset();
        mApple.reset();
        // Always spawn the apple
        ((Apple) mApple).spawn(); // Respawn the apple

        // Check if the difficulty is medium
        if (mCurrentDifficulty == Difficulty.MEDIUM) {
            // Prepare the game objects for a new game
            mCoin.reset(); // Reset the coin object
            ((Coin) mCoin).spawn(); // Respawn the coin
            for (DrawableMovable sword : mSwords) {
                sword.reset();
                ((Sword) sword).spawn(); // Respawn the swords
            }
        }
        if (mCurrentDifficulty == Difficulty.HARD) {
            // Prepare the game objects for a new game
            mCoin.reset(); // Reset the coin object
            ((Coin) mCoin).spawn(); // Respawn the coin
            mHard.reset();
            ((Hard) mHard).spawn();

        }

        // Reset the score
        mScore = 0;

        // Setup mNextFrameTime so an update can be triggered
        mNextFrameTime = System.currentTimeMillis();

        // Reset the pause button if the handler is set
        if (pauseButtonHandler != null) {
            pauseButtonHandler.resetPauseButton();
        }

    }

    // Handles the game loop
    @Override
    public void run() {
        // Ensure newGame is called before game loop starts
        newGame();
        while (mPlaying) {
            if (!mPaused) {
                // Update 10 times a second
                if (updateRequired()) {
                    update();
                }
            }

            draw();
        }
    }

    // Implement the GameControls interface methods
    @Override
    public void pauseGame() {
        mPaused = true;
    }



    @Override
    public void resumeGame() {
        mPaused = false;
    }


    // Check to see if it is time for an update
    public boolean updateRequired() {

        // Run at 10 frames per second
        long TARGET_FPS = 10;
        final long MILLIS_PER_SECOND = 1000;
        switch (mCurrentDifficulty) {
            case EASY: // Easy level
                TARGET_FPS = 10; // 10 FPS
                break;
            case MEDIUM: // Medium level
                TARGET_FPS = 20; // 20 FPS
                break;
            case HARD: // Hard level
                //TARGET_FPS = 30; // 30 FPS
                //break;
            default:
                TARGET_FPS = 10; // Default to 10 FPS for unknown levels
                break;
        }

        // Are we due to update the frame
        if (mNextFrameTime <= System.currentTimeMillis()) {
            // Tenth of a second has passed

            // Setup when the next update will be triggered
            mNextFrameTime = System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }

        return false;
    }

    // Method to create additional bombs
    public void createAdditionalBombs(int count) {
        for (int i = 0; i < count; i++) {
            mHard.reset(); // Reset the bomb object
            mHard.spawn(); // Respawn the bomb
        }
    }

    // Update all the game objects
    public void update() {
        mHard.move();
        mSnake.move(); // Move the snake

        // Check if the snake has eaten an apple
        if (((Snake) mSnake).checkDinner(((Apple) mApple).getHitbox(), 1)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // UI updates here
                    ((Apple) mApple).spawn(); // Respawn the apple
                    mScore += 1; // Increase the score
                    multiplyBombCount();
                    multiplyBombCount();
                    int additionalBombsToCreate = ((Apple) mApple).getAdditionalBombsToCreate();
                    createAdditionalBombs(additionalBombsToCreate);
                }
            });
            mSP.play(mEat_Apple_ID, 1.0F, 1.0F, 0, 0, 1); // Play eating sound


        }

        if (((Snake) mSnake).checkDinner(((Coin) mCoin).getHitbox(), 2)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // UI updates here
                    ((Coin) mCoin).spawn(); // Respawn the apple
                    mScore += 2; // Increase the score
                    multiplyBombCount();
                }
            });
            mSP.play(mEat_Coin_ID, 0.6F, 0.6F, 0, 0, 1); // Play eating sound
        }
        if (((Snake) mSnake).checkDinner(((Hard) mHard).getHitbox(), -2)) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // UI updates here
                    //((Hard) mHard).spawn(); // Respawn the Bomb
                    mPaused = true;
                }
            });
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            // Trigger game over actions, such as showing a message or resetting the game
            if (gameOverListener != null) {
                gameOverListener.onGameOver(mScore);
            }
            mSP.play(mCrashID, 0.4F, 0.4F, 0, 0, 1); // Play death sound
            mSP.play(mEat_Hard_ID, 1F, 1F, 0, 0, 1); // Play eating sound
        }

        for (DrawableMovable sword : mSwords) {
            if (((Snake) mSnake).checkDinner(((Sword) sword).getHitbox(), -2)) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // UI updates here
                        ((Sword) sword).spawn(); // Respawn the Sword
                        mScore += -1; // Increase the score
                    }
                });
                mSP.play(mEat_Sword_ID, 0.6F, 0.6F, 0, 0, 1); // Play eating sound
            }
        }

        // Check if the snake has died
        if (((Snake) mSnake).detectDeath()) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Don't automatically start a new game. Just pause and show "Tap to Play".
                    mPaused = true;

                }
            });

            // Stop the background song when the snake dies
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            mSP.play(mCrashID, 1, 1, 0, 0, 1); // Play death sound
            if (gameOverListener != null) {
                gameOverListener.onGameOver(mScore);
            }

            mSP.play(mCrashID, 0.4F, 0.4F, 0, 0, 1); // Play death sound
        }

        saveHighScore(mScore);
    }

    private void multiplyBombCount() {
            int currentBombCount = ((Hard) mHard).getBombCount();
            currentBombCount++;
            ((Hard) mHard).setBombCount(currentBombCount);
    }
    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();

            // Check if the mCanvas is null
            if (mCanvas == null) {
                return; // If mCanvas is null, exit the method to avoid NullPointerException
            }

            // Draw the background first
            mCanvas.drawBitmap(mBackgroundBitmap, 0, 0, null);

            // Draw the names in the top right corner
            mPaint.setTextSize(40); // Smaller size for better screen fit
            mPaint.setColor(Color.WHITE);
            mPaint.setTypeface(gameFont);

            // Draw the difficulty level below the score
            String difficultyText = "Level: " + mCurrentDifficulty.name();
            // Use the same text size as the score or adjust as needed
            mPaint.setTextSize(60);
            mCanvas.drawText(difficultyText, 20, 190, mPaint); // Position it below the score

            // Calculate the position for "Jacob & Adiba" to appear in the top right corner
            String names = "Group 5";
            float textWidth = mPaint.measureText(names);
            float xPositionNames = mCanvas.getWidth() - textWidth - 20; // 20 pixels from the right edge
            float yPositionNames = 60; // 60 pixels from the top
            mCanvas.drawText(names, xPositionNames, yPositionNames, mPaint);

            // Draw the score in the top left corner
            mPaint.setTextSize(60); // Adjust text size for the score
            mCanvas.drawText("Score: " + mScore, 20, 120, mPaint); // Adjust y-position to avoid overlap with names

            // Draw the apple and the snake
            mApple.draw(mCanvas, mPaint);
            mHard.draw(mCanvas, mPaint);
            mCoin.draw(mCanvas, mPaint);
            for (DrawableMovable sword : mSwords) {
                sword.draw(mCanvas, mPaint);
            }
            mSnake.draw(mCanvas, mPaint);

            // If the game is paused, draw the "Tap to Play" message centered
            if (mPaused) {
                mPaint.setTextSize(90); // Larger text size for "Tap to Play"
                float tapToPlayWidth = mPaint.measureText("Tap to Play");
                float xPositionTapToPlay = (mCanvas.getWidth() - tapToPlayWidth) / 2;
                float yPositionTapToPlay = mCanvas.getHeight() / 2;
                mCanvas.drawText("Tap to Play", xPositionTapToPlay, yPositionTapToPlay, mPaint);
            }

            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (mPaused) {
                    // Check if the game was paused due to the snake's death and waiting for a restart
                    newGame(); // Start a new game
                    mPaused = false; // Unpause the game
                    // No need to check mPlaying or start a new thread here as newGame() and resume() should handle it
                    resume(); // Ensure the game resumes correctly if it was not already playing
                    return true;
                } else {
                    // If the game is already playing, handle snake direction changes
                    ((Snake) mSnake).switchHeading(motionEvent);
                }
                break;
            default:
                break;
        }
        return true;
    }


    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        newGame(); // Set up the initial game state
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

    // Call this when the game ends
    public void onGameOver() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                saveHighScore(mScore);
                if (gameOverListener != null) {
                    gameOverListener.onGameOver(mScore);
                }
            }
        });
    }

    // Save high score


    private void saveHighScore(int newScore) {
        SharedPreferences prefs = getContext().getSharedPreferences("SnakeGamePrefs", MODE_PRIVATE);
        String key = "HighScore_" + mCurrentDifficulty.name();
        int highScore = prefs.getInt(key, 0);
        Log.d("SaveHighScore", "Current high score for " + key + ": " + highScore + ", New score: " + newScore);
        if (newScore > highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(key, newScore);
            editor.apply();
            Log.d("SaveHighScore", "New high score saved for " + key + ": " + newScore);
        }
    }



}