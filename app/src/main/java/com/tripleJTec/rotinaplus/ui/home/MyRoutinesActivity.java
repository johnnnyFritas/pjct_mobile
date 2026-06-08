package com.tripleJTec.rotinaplus.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;

import java.util.ArrayList;

public class MyRoutinesActivity extends AppCompatActivity {
    User user;
    DataBase dbHelper;
    TextView txtNoRoutines;
    ImageView imgCreateRoutineBottomMenuIcon, imgBottomMenuIcon;
    LinearLayout layoutMyRoutines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);

        // Instancia o banco de dados passando o contexto atual (this)
        dbHelper = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        //iniciando views
        txtNoRoutines = findViewById(R.id.txtNoRoutinesMyRoutines);
        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgBottomMenuIcon = findViewById(R.id.imgBottomMenuIcon);
        layoutMyRoutines = findViewById(R.id.layoutMyRoutines);

        //funções
        setImgCreateRoutineBottomMenuIconListener();
        setImgBottomMenuIconListener();
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        user = dbHelper.getUser(email);
        setRoutines();
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutinesActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgBottomMenuIconListener() {
        imgBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutinesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(MyRoutinesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setRoutines() {
        ArrayList<Routines> routines = dbHelper.getAllRoutines(user);

        if (routines.isEmpty()) {
            txtNoRoutines.setVisibility(View.VISIBLE);
            txtNoRoutines.setText(getString(R.string.no_routines));
        } else {
            for (Routines routine : routines) {
                String name = routine.getName();
                String hour = routine.getHour();

                //define o layout principal
                LinearLayout layout = new LinearLayout(this);
                layout.setId(View.generateViewId());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 50, 0, 0);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setBackgroundResource(R.drawable.shape_home_1);
                layout.setPadding(5, 5, 5, 5);
                layout.setGravity(Gravity.CENTER);
                layoutMyRoutines.addView(layout);

                //define o layout secundario e terciario
                LinearLayout layoutSecond = new LinearLayout(this);
                layoutSecond.setId(View.generateViewId());
                LinearLayout.LayoutParams paramsSecond = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutSecond.setLayoutParams(paramsSecond);
                layoutSecond.setOrientation(LinearLayout.HORIZONTAL);
                layoutSecond.setGravity(Gravity.START);

                LinearLayout layoutThird = new LinearLayout(this);
                layoutThird.setId(View.generateViewId());
                layoutThird.setLayoutParams(paramsSecond);
                layoutThird.setOrientation(LinearLayout.HORIZONTAL);
                layoutThird.setGravity(Gravity.START);

                layout.addView(layoutSecond);
                layout.addView(layoutThird);

                //define textos layout secundario
                TextView labelTitleRoutine = new TextView(this);
                labelTitleRoutine.setId(View.generateViewId());
                labelTitleRoutine.setLayoutParams(paramsSecond);
                labelTitleRoutine.setTextSize(20);
                labelTitleRoutine.setTextColor(getResources().getColor(R.color.black));
                Typeface typeface = ResourcesCompat.getFont(this, R.font.poppins_bold);
                labelTitleRoutine.setTypeface(typeface);
                labelTitleRoutine.setPadding(15, 0, 0, 0);
                labelTitleRoutine.setLetterSpacing(.1F);
                labelTitleRoutine.setText(getResources().getString(R.string.label_name_current_routine));

                TextView titleRoutine = new TextView(this);
                titleRoutine.setId(View.generateViewId());
                titleRoutine.setLayoutParams(paramsSecond);
                titleRoutine.setTextSize(18);
                titleRoutine.setTextColor(getResources().getColor(R.color.blue_light_2));
                Typeface typefaceSecond = ResourcesCompat.getFont(this, R.font.poppins_medium);
                titleRoutine.setTypeface(typefaceSecond);
                titleRoutine.setPadding(15, 0, 0, 0);
                titleRoutine.setLetterSpacing(.1F);
                titleRoutine.setText(name);

                //define textos layout terciario
                TextView labelHourRoutine = new TextView(this);
                labelHourRoutine.setId(View.generateViewId());
                labelHourRoutine.setLayoutParams(paramsSecond);
                labelHourRoutine.setTextSize(20);
                labelHourRoutine.setTextColor(getResources().getColor(R.color.black));
                labelHourRoutine.setTypeface(typeface);
                labelHourRoutine.setPadding(15, 0, 0, 0);
                labelHourRoutine.setLetterSpacing(.1F);
                labelHourRoutine.setText(getResources().getString(R.string.label_hour_current_routine));

                TextView hourRoutine = new TextView(this);
                hourRoutine.setId(View.generateViewId());
                hourRoutine.setLayoutParams(paramsSecond);
                hourRoutine.setTextSize(18);
                hourRoutine.setTextColor(getResources().getColor(R.color.blue_light_2));
                hourRoutine.setTypeface(typefaceSecond);
                hourRoutine.setPadding(15, 0, 0, 0);
                hourRoutine.setLetterSpacing(.1F);
                hourRoutine.setText(hour);

                layoutSecond.addView(labelTitleRoutine);
                layoutSecond.addView(titleRoutine);
                layoutThird.addView(labelHourRoutine);
                layoutThird.addView(hourRoutine);
            }
        }
    }
}
