package com.tripleJTec.rotinaplus.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    TextView txtTitle, txtNoRoutines;
    Button btnCreateRoutine, btnMyRoutines;
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

        btnCreateRoutine = findViewById(R.id.btnCreateRoutineHome);
        btnMyRoutines = findViewById(R.id.btnMyRoutinesHome);

        // Funções
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        setTxtTitleHome(txtTitle, dbHelper.getUser(email));
        setBtnCreateRoutineListener(btnCreateRoutine);
        setBtnMyRoutinesListener(btnMyRoutines);
        User user = dbHelper.getUser(email);
        getUserRoutines(user);
    }

    private void setTxtTitleHome(TextView txtTitle, User user) {
        String title = txtTitle.getResources().getString(R.string.title_home, user.getNome());
        txtTitle.setText(title);
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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

    private void getUserRoutines(User user) {
        ArrayList<Routines> routinesArrayList = dbHelper.getAllRoutines(user);

        if (routinesArrayList == null || routinesArrayList.isEmpty()) {
            txtNoRoutines.setVisibility(View.VISIBLE);
            txtNoRoutines.setText(getString(R.string.no_routines));
        }


    }
}
