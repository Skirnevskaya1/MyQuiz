package app.quiz;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static app.quiz.GameLevels.category_id;

public class Levels extends AppCompatActivity implements View.OnClickListener {
    private TextView question;
    private TextView qCount;
    private Button option1, option2, option3, option4;
    private ImageView imageView;
    private int questionNum, score;
    private List<Question> questionList;
    private List<String> imgUrlList = new ArrayList<>();

    private FirebaseFirestore firestore;
    private int setNumber;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        setupUI();
        getQuestionsList();
    }

    private void getQuestionsList() {
        questionList = new ArrayList<>();

        firestore.collection("QUIZ").document("Categories" + category_id)
                .collection("SET" + setNumber)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot questions = task.getResult();
                    Log.d("looooooooog", String.valueOf(questions.size()));
                    for (QueryDocumentSnapshot doc : questions) {

                        String urlString = doc.getString("IMAGE");
                        imgUrlList.add(urlString);

                        questionList.add(new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                doc.getString("IMAGE"),
                                Integer.valueOf(doc.getString("ANSWER"))
                        ));
                    }
                    setQuestion();
                } else {
                    Toast.makeText(Levels.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.cancel();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setQuestion() {
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());
        Glide.with(this).load(imgUrlList.get(0)).into(imageView);
        questionNum = 0;
        qCount.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));
    }

    @SuppressLint("CutPasteId")
    private void setupUI() {
        question = findViewById(R.id.text_question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        imageView = findViewById(R.id.image_centre);
        qCount = findViewById(R.id.text_count);

        //Код который скругляет углы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setClipToOutline(true);
        }

        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        setNumber = getIntent().getIntExtra("SETNUMBER", 1);
        score = 0;

        loadingDialog = new Dialog(Levels.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore = FirebaseFirestore.getInstance();

        //Создаем переменную text_levels и устанавливаем текст для данного view
        TextView textLevels = findViewById(R.id.text_levels);
        textLevels.setText(R.string.level1); //установили текст

        //Кнопка "назад" вернуться к уровням
        Button buttonBackUniversal = findViewById(R.id.button_back);
        buttonBackUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Обрабатываем нажатие кнопки "назад"
                try {
                    //Вернуться назад к выбору уровня
                    Intent intent_universal = new Intent(Levels.this, GameLevels.class); //Создали намерение для перехода
                    startActivity(intent_universal); // Старт намерения
                    finish(); //Закрыть этот класс
                } catch (Exception ignored) {
                }
            }
        });
    }

    //Системная кнопка "назад"
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(Levels.this, GameLevels.class);
            startActivity(intent);
            finish();
        } catch (Exception ignored) {

        }
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int selectedOption = 0;
        switch (v.getId()) {
            case R.id.option1:
                selectedOption = 1;
                break;
            case R.id.option2:
                selectedOption = 2;
                break;
            case R.id.option3:
                selectedOption = 3;
                break;
            case R.id.option4:
                selectedOption = 4;
                break;
            default:
        }
        checkAnswer(selectedOption, v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(int selectedOption, View view) {
        if (selectedOption == questionList.get(questionNum).getCorrectAns()) {
            //правильный ответ загорается зеленым
            view.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        } else {
            //неверный ответ красным
            view.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(questionNum).getCorrectAns()) {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }

        Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        }, 2000);
    }

    @SuppressLint("SetTextI18n")
    private void changeQuestion() {
        if (questionNum < questionList.size() - 1) {
            questionNum++;
            playAnim(question, 0, 0);
            playAnim(option1, 0, 1);
            playAnim(option2, 0, 2);
            playAnim(option3, 0, 3);
            playAnim(option4, 0, 4);
            playAnim(imageView, 0, 5);

            qCount.setText((questionNum + 1) + "/" + questionList.size());
            String str = imgUrlList.get(questionNum);
            Glide.with(getApplicationContext()).
                    load(str).
                    into(imageView);
        } else {
            // перейти к активности ScoreActivity
            Intent intent = new Intent(Levels.this, ScoreActivity.class);
            intent.putExtra("SCORE", score + "/" + questionList.size());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //Level1.this.finish();
//
//            //Сохранение последних действий  в игре
//            SharedPreferences preferences = getSharedPreferences("Save", MODE_PRIVATE);
//            final int level = preferences.getInt("Level", 1);
//            if (level > 1){
//
//            }else {
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putInt("Level", 2);
//                editor.commit();
//            }
        }
    }

    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView) view).setText(questionList.get(questionNum).getQuestion());
                                    break;
                                case 1:
                                    ((Button) view).setText(questionList.get(questionNum).getOptionA());
                                    break;
                                case 2:
                                    ((Button) view).setText(questionList.get(questionNum).getOptionB());
                                    break;
                                case 3:
                                    ((Button) view).setText(questionList.get(questionNum).getOptionC());
                                    break;
                                case 4:
                                    ((Button) view).setText(questionList.get(questionNum).getOptionD());
                                    break;
                                case 5:
                                    // ((ImageView) view).set...(questionList.get(questionNum).getImageView());
                                    break;
                            }
                            if (viewNum != 0 && viewNum != 5) {
                                view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E4E4E4")));
                            }
                            playAnim(view, 1, viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}
