package com.tripleJTec.rotinaplus.ui.routines;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;
import com.tripleJTec.rotinaplus.ui.user.MyProfileActivity;

public class Routine extends AppCompatActivity {
    Routines routine;
    User user;
    DataBase dbHelper;
    ImageView imgCreateRoutineBottomMenuIcon, imgBottomMenuIcon, imgProfileBottomMenuIcon;
    TextView txtTitleRoutine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        dbHelper = new DataBase(this);

        String routineName = getIntent().getStringExtra("name");
        String routineDaysOfWeekNormalString = getIntent().getStringExtra("daysOfWeek");
        String routineHour = getIntent().getStringExtra("hour");

        if (routineDaysOfWeekNormalString == null) {
            Intent intent = new Intent(Routine.this, HomeActivity.class);
            Toast.makeText(this, "Erro! Rotina Incompleta no sistema! Por favor escolha outra.", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

        assert routineDaysOfWeekNormalString != null;
        String[] routineDaysOfWeek = routineDaysOfWeekNormalString.split(", ");

        //iniciando views
        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgBottomMenuIcon = findViewById(R.id.imgBottomMenuIcon);
        imgProfileBottomMenuIcon = findViewById(R.id.imgProfileBottomMenuIcon);

        //funções
        setImgCreateRoutineBottomMenuIconListener();
        setImgBottomMenuIconListener();
        setImgMyProfileBottomMenuIconListener();
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        user = dbHelper.getUser(email);
        routine = dbHelper.getRoutine(routineName, routineHour, routineDaysOfWeek);
        setTxtTitleRoutine(routine);
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgBottomMenuIconListener() {
        imgBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgMyProfileBottomMenuIconListener() {
        imgProfileBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, MyProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(Routine.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setTxtTitleRoutine(Routines routine) {
        txtTitleRoutine.setText(routine.getName());
    }
}
