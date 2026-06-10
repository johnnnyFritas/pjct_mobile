package com.tripleJTec.rotinaplus.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.routines.CreateRoutineActivity;
import com.tripleJTec.rotinaplus.ui.routines.MyRoutinesActivity;
import com.tripleJTec.rotinaplus.ui.routines.Routine;
import com.tripleJTec.rotinaplus.ui.user.MyProfileActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    TextView txtTitle, txtNoRoutines, txtTitleCurrentRoutine, txtHourCurrentRoutine;
    Button btnCreateRoutine, btnMyRoutines;
    ImageView imgCreateRoutineBottomMenuIcon, imgProfileBottomMenuIcon;
    LinearLayout layoutRoutines;
    DataBase dbHelper;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Instancia o banco de dados passando o contexto atual (this)
        dbHelper = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        // Iniciando views
        txtTitle = findViewById(R.id.txtTitleHome);
        txtNoRoutines = findViewById(R.id.txtNoRoutinesHome);
        txtTitleCurrentRoutine = findViewById(R.id.txtTitleCurrentRoutine);
        txtHourCurrentRoutine = findViewById(R.id.txtHourCurrentRoutine);

        btnCreateRoutine = findViewById(R.id.btnCreateRoutineHome);
        btnMyRoutines = findViewById(R.id.btnMyRoutinesHome);

        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgProfileBottomMenuIcon = findViewById(R.id.imgProfileBottomMenuIcon);

        layoutRoutines = findViewById(R.id.layoutRoutinesHome);

        // Funções
        if (dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            String email = getEmailWithSharedPreferences(sharedPreferences);
            setBtnCreateRoutineListener();
            setBtnMyRoutinesListener();
            user = dbHelper.getUser(email);
            if (user != null) {
                setTxtTitleHome();
                getUserRoutine();
                setImgCreateRoutineBottomMenuIconListener();
                setImgMyProfileBottomMenuIconListener();
            } else {
                goBackToLoginWithLogout(sharedPreferences);
            }
        } else {
            goBackToLoginWithLogout(sharedPreferences);
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setBtnCreateRoutineListener() {
        btnCreateRoutine.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setBtnMyRoutinesListener() {
        btnMyRoutines.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MyRoutinesActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setTxtTitleHome() {
        String title = txtTitle.getResources().getString(R.string.title_home, user.getNome());
        txtTitle.setText(title);
    }

    private void getUserRoutine() {
        ArrayList<Routines> routinesArrayList = dbHelper.getAllRoutines(user);

        if (routinesArrayList == null || routinesArrayList.isEmpty()) {
            txtNoRoutines.setVisibility(View.VISIBLE);
            txtNoRoutines.setText(getString(R.string.no_routines));
        } else {
            layoutRoutines.setVisibility(LinearLayout.VISIBLE);
            txtTitleCurrentRoutine.setText(routinesArrayList.get(routinesArrayList.size() - 1).getName());
            txtHourCurrentRoutine.setText(routinesArrayList.get(routinesArrayList.size() - 1).getHour());

            layoutRoutines.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, Routine.class);
                intent.putExtra("name", routinesArrayList.get(routinesArrayList.size() - 1).getName());
                intent.putExtra("daysOfWeek", routinesArrayList.get(routinesArrayList.size() - 1).getDaysOfWeek());
                intent.putExtra("hour", routinesArrayList.get(routinesArrayList.size() - 1).getHour());
                startActivity(intent);
                finish();
            });
        }
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgMyProfileBottomMenuIconListener() {
        imgProfileBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void goBackToLoginWithLogout(SharedPreferences sharedPreferences) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        sharedPreferences.edit().clear().apply();
        startActivity(intent);
        finish();
    }
}
