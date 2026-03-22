package com.example.pjct;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText editTextTextEmailAddressActivityMain, editTextTextPasswordActivityMain;
    TextView textViewActivityMain, textViewActivityMain1;
    Button buttonActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextTextEmailAddressActivityMain = findViewById(R.id.EditTextTextEmailAddressActivityMain);
        editTextTextPasswordActivityMain = findViewById(R.id.EditTextTextPasswordActivityMain);

        editTextTextEmailAddressActivityMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                    editTextTextEmailAddressActivityMain.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.excluir_logo, 0);
                else
                    editTextTextEmailAddressActivityMain.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        editTextTextPasswordActivityMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                    editTextTextPasswordActivityMain.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_olho_password, 0);
                else
                    editTextTextPasswordActivityMain.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        });

        textViewActivityMain = findViewById(R.id.TextViewActivityMain);
        textViewActivityMain1 = findViewById(R.id.TextViewActivityMain1);

        textViewActivityMain.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EsqueceuSenhaActivity.class);
            intent.putExtra("email", editTextTextEmailAddressActivityMain.getText().toString().isEmpty() || editTextTextEmailAddressActivityMain.getText().toString().isBlank() ?
                    null :
                    editTextTextEmailAddressActivityMain.getText().toString());
            startActivity(intent);
        });

        textViewActivityMain1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
            intent.putExtra("email", editTextTextEmailAddressActivityMain.getText().toString().isEmpty() || editTextTextEmailAddressActivityMain.getText().toString().isBlank() ?
                    null :
                    editTextTextEmailAddressActivityMain.getText().toString());
            startActivity(intent);
        });

        buttonActivityMain = findViewById(R.id.ButtonActivityMain);



    }
}