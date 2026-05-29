package com.tripleJTec.rotinaplus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.local.dataBase;
import com.tripleJTec.rotinaplus.domain.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;

public class HomeActivity extends AppCompatActivity {

    TextView txtTitleHome;

    dataBase dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //buscando views
        txtTitleHome = findViewById(R.id.txtTitleHome);

        //Buscando user no intent
        User user = dbHelper.getUser(getIntent().getStringExtra("email"));
        //se não achou não pode estar na home, volta ao login.
        if (user == null) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //confirma não nulo para user
        assert user != null : "Usuário nulo";

        //funções
        setTxtTitleHome(txtTitleHome, user);
    }

    protected void setTxtTitleHome(TextView txtTitleHome, User user) {
        String title = txtTitleHome.getResources().getString(R.string.title_home, user.getNome());
        txtTitleHome.setText(title);
    }
}
