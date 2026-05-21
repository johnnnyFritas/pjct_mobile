package com.tripleJTec.rotinaplus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtTxtEmail, edtTxtPass;

    TextView txtPassForgotten, txtRegister;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //inicializando widgets
        edtTxtEmail = findViewById(R.id.edtTxtEmailLogin);
        edtTxtPass = findViewById(R.id.edtTxtPassLogin);

        txtPassForgotten = findViewById(R.id.txtPassForgotten);
        txtRegister = findViewById(R.id.txtRegisterLogin);

        btnLogin = findViewById(R.id.btnLogin);

        //funções
        edtTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                verificarTextoEmail(edtTxtEmail, charSequence);
            }
        });


    }

    protected void verificarTextoEmail(EditText edtTxt, CharSequence charSequence) {
        if(charSequence.length() > 0) {
            edtTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_erase_all_content, 0);
        }else {
            edtTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }
}