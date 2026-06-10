package com.tripleJTec.rotinaplus.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.auth.RegisterActivity;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;
import com.tripleJTec.rotinaplus.ui.routines.CreateRoutineActivity;
import com.tripleJTec.rotinaplus.ui.routines.MyRoutinesActivity;

public class MyProfileActivity extends AppCompatActivity {

    DataBase dbHelper;
    TextView txtNameMyProfile;
    EditText edtTxtNameMyProfile;
    Button btnNameEditMyProfile;
    ImageView imgCreateRoutineBottomMenuIcon, imgBottomMenuIcon;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        dbHelper = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        //iniciando views
        txtNameMyProfile = findViewById(R.id.txtNameMyProfile);

        edtTxtNameMyProfile = findViewById(R.id.edtTxtNameMyProfile);

        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgBottomMenuIcon = findViewById(R.id.imgBottomMenuIcon);

        btnNameEditMyProfile = findViewById(R.id.btnNameEditMyProfile);

        //funções
        setImgCreateRoutineBottomMenuIconListener();
        setImgBottomMenuIconListener();
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        user = dbHelper.getUser(email);
        setTxtTitleMyProfile(user);
        setBtnNameEditMyProfileListener();
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgBottomMenuIconListener() {
        imgBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setTxtTitleMyProfile(User user) {
        txtNameMyProfile.setText(user.getNome());
    }

    private void setBtnNameEditMyProfileListener() {
        btnNameEditMyProfile.setOnClickListener(v -> {
            String newName = edtTxtNameMyProfile.getText().toString();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Preisa inserir um nome para alterá-lo", Toast.LENGTH_LONG).show();
            } else {
                boolean sucesso = dbHelper.updateUser(new User(user.getId(), newName, user.getEmail(), user.getSenha()));

                if (sucesso) {
                    setLog(1, this.getLocalClassName(), "Usuário atualizado");

                    //Vai para a home page
                    Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setLog(Integer idLogType, String className, String message) {
        switch (idLogType) {
            case 1:
                Log.d(className, message);
                break;
            case 2:
                Log.e(className, message);
                break;
            default:
                Log.e(className, "Só são permitidos 1 ou 2 como id para o log");
        }
    }
}
