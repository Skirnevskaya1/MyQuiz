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
    private LinearLayout score_broad;
    private TextView score;
    private Button done;
    private Animation score_tv, score_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        score = findViewById(R.id.score);
        done = findViewById(R.id.buttonDone);

        score_tv = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.score);
        score_broad = findViewById(R.id.container1);
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
                    Intent intentLevel1 = new Intent(ScoreActivity.this, GameLevels.class);

                    startActivity(intentLevel1);
                    finish();
                } catch (Exception e) {

                }
            }
        });
    }
}
