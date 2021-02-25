package app.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static app.quiz.MainActivity.catList;

public class CategoryActivity extends AppCompatActivity {
    private Switch aSwitch;
    private Button button;
    private GridView gridView;
    public static final String MyPREFERENCES = "nightNodePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //получение доступа к настройкам, MODE_PRIVATE - только приложение имеет доступ к настройкам
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        gridView = findViewById(R.id.cat_grid);
        CatBaseAdapter adapter = new CatBaseAdapter(catList);
        gridView.setAdapter(adapter);

        aSwitch = findViewById(R.id.switch_theme);
        //button = findViewById(R.id.button_back);
        checkNightModeActivated();

        //изменение состояния кнопки switch
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    recreate();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    recreate();
                }
            }
        });

        //Кнопка "назад" вернуться к уровням
        Button buttonBackUniversal = findViewById(R.id.button_back);

        buttonBackUniversal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Обрабатываем нажатие кнопки "назад"
                try {
                    //Вернуться назад к выбору уровня
                    Intent intent_universal = new Intent(CategoryActivity.this,
                            MainActivity.class); //Создали намерение для перехода
                    startActivity(intent_universal);
                    finish(); //Закрыть этот класс
                } catch (Exception ignored) {
                }
            }
        });
    }

    //сохранение состояния ночного режима
    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }

    //проверка активации ночного режима
    private void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)) {
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            aSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
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