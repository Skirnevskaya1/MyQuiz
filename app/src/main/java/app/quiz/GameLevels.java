package app.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameLevels extends AppCompatActivity {
    private Button buttonBack;
    private GridView gridView;
    private FirebaseFirestore firestore;
    public static int category_id;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_levels);
        gridView = findViewById(R.id.cat_grid);

       // String title = getIntent().getIntExtra("CATEGORY");
        category_id = getIntent().getIntExtra("CATEGORY_ID", 1);
       // getSupportActionBar().setTitle(title);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //Подключение к прогрессбар
        loadingDialog = new Dialog(GameLevels.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        firestore = FirebaseFirestore.getInstance();
        loadSets();

        //Кнопка "Назад" в меню
        buttonBack = (Button) findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intentBack = new Intent(GameLevels.this, CategoryActivity.class);
                    startActivity(intentBack);
                    finish();
                } catch (Exception e) {

                }
            }
        });

//        //Кнопка для перехода на первый уровень
//        TextView textView = (TextView) findViewById(R.id.level_number);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intentLevel1 = new Intent(GameLevels.this, Level1.class);
//                    startActivity(intentLevel1);
//                    finish();
//                } catch (Exception e) {
//
//                }
//            }
//        });
    }

    private void loadSets() {
        firestore.collection("QUIZ").document("Categories" + String.valueOf(category_id))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        long sets = (long) snapshot.get("SETS");
                        SetsAdapter setsAdapter = new SetsAdapter((int) sets);
                        gridView.setAdapter(setsAdapter);
                    } else {
                        Toast.makeText(GameLevels.this, "No Categories document Exists!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    Toast.makeText(GameLevels.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.cancel();
            }
        });
    }

    //Системная кнопка "Назад"
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(GameLevels.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
        }
    }
}
