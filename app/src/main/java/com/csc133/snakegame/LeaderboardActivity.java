package com.csc133.snakegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LeaderboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        SharedPreferences prefs = getSharedPreferences("SnakeGamePrefs", MODE_PRIVATE);
        TextView easyScoreView = findViewById(R.id.easyHighScore);
        TextView mediumScoreView = findViewById(R.id.mediumHighScore);
        TextView hardScoreView = findViewById(R.id.hardHighScore);

        easyScoreView.setText("Easy: " + prefs.getInt("HighScore_EASY", 0));
        mediumScoreView.setText("Medium: " + prefs.getInt("HighScore_MEDIUM", 0));
        hardScoreView.setText("Hard: " + prefs.getInt("HighScore_HARD", 0));

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

}
