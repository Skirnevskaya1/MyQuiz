package app.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import static app.quiz.MainActivity.catList;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        GridView gridView = findViewById(R.id.cat_grid);
        CatBaseAdapter adapter = new CatBaseAdapter(catList);
        gridView.setAdapter(adapter);

        //Кнопка "назад" вернуться к уровням
        Button buttonBackUniversal = findViewById(R.id.button_back);

        buttonBackUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Обрабатываем нажатие кнопки "назад"
                try {
                    //Вернуться назад к выбору уровня
                    Intent intent_universal = new Intent(CategoryActivity.this, MainActivity.class); //Создали намерение для перехода
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
            Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception ignored) {

        }
    }
}