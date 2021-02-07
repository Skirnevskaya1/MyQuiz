package app.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<String> catList = new ArrayList<>();
    private FirebaseFirestore firestore;
    private long backPressedTime;
    private Toast backToast;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //анимация для кнопки "старт"
        Animation startButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start_button);
        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.startAnimation(startButton);

        firestore = FirebaseFirestore.getInstance();

        //нажитие кнопки старт в меню категории
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadData();
                } catch (Exception ignored) {
                }
            }
        });
    }

    // Загрузка категорий из firebase
    private void loadData() {
        catList.clear();
        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        long count = (long) snapshot.get("COUNT");
                        for (int i = 1; i <= count; i++) {
                            String catName = snapshot.getString("Categories" + i);
                            catList.add(catName);
                        }
                        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    } else {
                        Toast.makeText(MainActivity.this, "No Category document exists!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Log.d("LOGG", "aaaaaaaaaaa");
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Ctrl+O Системная кнопка "Назад"
    @Override
    public void onBackPressed() {
        //Время нажатия кнопки "Назад"
        if ((backPressedTime + 2000) > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(),
                    "Нажмите еще раз, чтобы выйти", Toast.LENGTH_LONG);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}