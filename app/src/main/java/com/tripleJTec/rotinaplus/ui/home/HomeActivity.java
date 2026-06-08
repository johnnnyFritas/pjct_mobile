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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    TextView txtTitle, txtNoRoutines, txtTitleCurrentRoutine, txtHourCurrentRoutine;
    Button btnCreateRoutine, btnMyRoutines;
    ImageView imgCreateRoutineBottomMenuIcon;
    LinearLayout layoutRoutines;
    DataBase dbHelper;

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

        layoutRoutines = findViewById(R.id.layoutRoutinesHome);

        // Funções
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        setBtnCreateRoutineListener(btnCreateRoutine);
        setBtnMyRoutinesListener(btnMyRoutines);
        User user = dbHelper.getUser(email);
        setTxtTitleHome(txtTitle, user);
        getUserRoutines(user);
        setImgCreateRoutineBottomMenuIconListener();
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setBtnCreateRoutineListener(Button btnCreateRoutine) {
        btnCreateRoutine.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setBtnMyRoutinesListener(Button btnMyRoutines) {
        btnMyRoutines.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, MyRoutinesActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setTxtTitleHome(TextView txtTitle, User user) {
        String title = txtTitle.getResources().getString(R.string.title_home, user.getNome());
        txtTitle.setText(title);
    }

    private void getUserRoutines(User user) {
        ArrayList<Routines> routinesArrayList = dbHelper.getAllRoutines(user);

        if (routinesArrayList == null || routinesArrayList.isEmpty()) {
            txtNoRoutines.setVisibility(View.VISIBLE);
            txtNoRoutines.setText(getString(R.string.no_routines));
        } else {
            layoutRoutines.setVisibility(LinearLayout.VISIBLE);
            txtTitleCurrentRoutine.setText(routinesArrayList.get(routinesArrayList.size() - 1).getName());
            txtHourCurrentRoutine.setText(routinesArrayList.get(routinesArrayList.size() - 1).getHour());
        }
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
