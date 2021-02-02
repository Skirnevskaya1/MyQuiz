package app.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static app.quiz.MainActivity.catList;

public class CategoryActivity extends AppCompatActivity {
    private Button buttonBackUniversal;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        gridView = findViewById(R.id.cat_grid);
//        List<String> categoryList = new ArrayList<>();
//        categoryList.add("Category 1");
//        categoryList.add("Category 2");
//        categoryList.add("Category 3");
//        categoryList.add("Category 4");
//        categoryList.add("Category 5");
//        categoryList.add("Category 6");
//        categoryList.add("Category 7");
//        categoryList.add("Category 8");

        CatBaseAdapter adapter = new CatBaseAdapter(catList);
        gridView.setAdapter(adapter);


        //Кнопка "назад" вернуться к уровням
        buttonBackUniversal = (Button) findViewById(R.id.button_back);

        buttonBackUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Обрабатываем нажатие кнопки "назад"
                try {
                    //Вернуться назад к выбору уровня
                    Intent intent_universal = new Intent(CategoryActivity.this, MainActivity.class); //Создали намерение для перехода
                    startActivity(intent_universal); // Старт намерения
                    finish(); //Закрыть этот класс
                } catch (Exception e) {
                }
            }
        });

//        //нажитие кнопки категория в меню уровней
//        button_category = (TextView) findViewById(R.id.textView_category_1);
//        button_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(CategoryActivity.this, GameLevels.class);
//                    startActivity(intent);
//                    finish();
//                } catch (Exception e) {
//
//                }
//            }
//        });
    }

    //Системная кнопка "назад"
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {

        }
    }
}