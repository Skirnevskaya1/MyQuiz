package app.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {
    private Animation score_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView score = findViewById(R.id.score);
        Button done = findViewById(R.id.buttonDone);

        Animation score_tv = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.score);
        LinearLayout score_broad = findViewById(R.id.container1);
        score_broad.startAnimation(score_tv);

        score_button = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.score_button);
        done.startAnimation(score_button);

        String str_score = getIntent().getStringExtra("SCORE");
        score.setText(str_score);

        //Кнопка для перехода в макет с уровнями
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intentLevels = new Intent(ScoreActivity.this, GameLevels.class);
                    startActivity(intentLevels);
                    finish();
                } catch (Exception ignored) {

                }
            }
        });
    }
}
