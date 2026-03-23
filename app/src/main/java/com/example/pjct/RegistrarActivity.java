package com.example.pjct;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistrarActivity extends AppCompatActivity {
    EditText editTextNameActivityRegistrar, editTextTextEmailAddressActivityRegistrar, editTextTextPasswordActivityRegistrar;
    TextView textViewAcitivityRegistrar1, textViewAcitivityRegistrar2, textViewAcitivityRegistrar3;
    Button buttonActivityRegistrar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Pegar inputs
        editTextNameActivityRegistrar = findViewById(R.id.EditTextNameActivityRegistrar);
        editTextTextEmailAddressActivityRegistrar = findViewById(R.id.EditTextTextEmailAddressActivityRegistrar);
        editTextTextPasswordActivityRegistrar = findViewById(R.id.EditTextTextPasswordActivityRegistrar);
        textViewAcitivityRegistrar1 = findViewById(R.id.TextViewActivityRegistrar1);
        textViewAcitivityRegistrar2 = findViewById(R.id.TextViewActivityRegistrar2);
        textViewAcitivityRegistrar3 = findViewById(R.id.TextViewActivityRegistrar3);

        //Retirar e acrescentar icone ao digitar
        editTextNameActivityRegistrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() > 0)
                    editTextNameActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else
                    editTextNameActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_nome_registrar, 0);
            }
        });

        editTextTextEmailAddressActivityRegistrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() > 0)
                    editTextTextEmailAddressActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else
                    editTextTextEmailAddressActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_email_registrar, 0);
            }
        });

        editTextTextPasswordActivityRegistrar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(s.length() > 0)
                    editTextTextPasswordActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                else
                    editTextTextPasswordActivityRegistrar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.logo_senha_registrar, 0);
            }
        });

        //Iniciar checador de campos vazios
        ArrayList<Boolean> checks = new ArrayList<>();

        //checar preenchimento ao entrar e sair do modo de digitação
        editTextNameActivityRegistrar.setOnFocusChangeListener((v, hasFocus) -> {
            String nome = editTextNameActivityRegistrar.getText().toString();
            if(!hasFocus) {
                if (nome.isBlank() || nome.length() < 3) {
                    textViewAcitivityRegistrar1.setText("Por favor, preencha um nome válido.");
                    checks.add(0, true);
                }else {
                    textViewAcitivityRegistrar1.setText("");
                    checks.add(0, false);
                }
            }
        });

        editTextTextEmailAddressActivityRegistrar.setOnFocusChangeListener((v, hasFocus) -> {
            String email = editTextTextEmailAddressActivityRegistrar.getText().toString();
            if(!hasFocus) {
                if (email.isBlank() || email.length() < 5 || !email.contains("@")) {
                    textViewAcitivityRegistrar2.setText("Por favor, preencha um e-mail válido.");
                    checks.add(1, true);
                }else {
                    textViewAcitivityRegistrar2.setText("");
                    checks.add(1, false);
                }
            }
        });

        editTextTextPasswordActivityRegistrar.setOnFocusChangeListener((v, hasFocus) -> {
            String password = editTextTextPasswordActivityRegistrar.getText().toString();
            if(!hasFocus) {
                if (password.isBlank() || password.length() < 8) {
                    textViewAcitivityRegistrar3.setText("Por favor, preencha uma senha válida.");
                    checks.add(2, true);
                }else {
                    textViewAcitivityRegistrar3.setText("");
                    checks.add(2, false);
                }
            }
        });

        //Buscar cliente no banco de dados firebase
        buttonActivityRegistrar.setOnClickListener(v -> {
            if(!checks.contains(true)) {

            }
        });

    }

}
