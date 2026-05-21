package com.tripleJTec.rotinaplus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;

public class ForgottenPassActivity extends AppCompatActivity {

    ImageView logoMaintenance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_pass);

        //iniciando views
        logoMaintenance = findViewById(R.id.logoMaintenance);

        //funções
        //define um listener de clique na view. Ao clicar leva o usuário para a página de login.
        setGoToLoginClickListener(logoMaintenance);
    }

    protected void setGoToLoginClickListener(ImageView logoMaintenance) {
        logoMaintenance.setOnClickListener(view -> {
            Intent intent = new Intent(ForgottenPassActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
