package com.tripleJTec.rotinaplus.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.tripleJTec.rotinaplus.R;

public class RegisterActivity extends AppCompatActivity {

    EditText edtTxtName, edtTxtEmail, edtTxtPass;

    TextView txtNameWarning, txtEmailWarning, txtPassWarning, txtBackToLogin;

    Button btnRegister;

    Boolean togglePassVisibility = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //iniciando views
        edtTxtName = findViewById(R.id.edtTxtNameRegister);
        edtTxtEmail = findViewById(R.id.edtTxtEmailRegister);
        edtTxtPass = findViewById(R.id.edtTxtPassRegister);

        txtNameWarning = findViewById(R.id.txtNameWarning);
        txtEmailWarning = findViewById(R.id.txtEmailWarning);
        txtPassWarning = findViewById(R.id.txtPassWarning);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);

        btnRegister = findViewById(R.id.btnRegister);

        //funções
        //define um listener de edição de texto para verificar se tem conteúdo no input de nome, se tiver remove o drawable de icone à direita.
        setEdtTxtNameOnTextChangedListener(edtTxtName);

        //define um listener de edição de texto para verificar se tem conteúdo no input de e-mail, se tiver remove o drawable de icone à direita.
        setEdtTxtEmailOnTextChangedListener(edtTxtEmail);

        //define um listener de clique no drawable de icone à direita no input de senha. Ao clicar ele mostra a senha e muda o icone. Ao clicar novamente, esconde a senha e muda o icone.
        setPassIconClickListener(edtTxtPass);

        //define um listener de clique na view. Ao clicar leva o usuário para a página de login.
        setGoToLoginClickListener(txtBackToLogin);
    }

    protected void setEdtTxtNameOnTextChangedListener(EditText edtTxtName) {
        edtTxtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    edtTxtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_profile, 0);
                }
            }
        });
    }

    protected void setEdtTxtEmailOnTextChangedListener(EditText edtTxtEmail) {
        edtTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    edtTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_email, 0);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void setPassIconClickListener(EditText edtTxtPass) {
        edtTxtPass.setOnTouchListener( (view, motionEvent) -> {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                float dp = edtTxtPass.getPaddingEnd();
                int pixelsInteger = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        getResources().getDisplayMetrics()
                );

                float initialCompoundWidth = view.getWidth() - edtTxtPass.getCompoundDrawables()[2].getBounds().width() - pixelsInteger;

                if (motionEvent.getX() >= initialCompoundWidth) {

                    //verifica se o toggle está true ou false.
                    if (!togglePassVisibility) {

                        //define o input como text password visible
                        edtTxtPass.setInputType(145);

                        //ao alterar o input type na linha anterior, a fonte reseta para a fonte padrão (não foi identificado o motivo). Para isso o código abaixo garante que a view tenha a fonte original do projeto.
                        Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.poppins_medium);
                        edtTxtPass.setTypeface(typeface);

                        //define o icone de senha visivel
                        edtTxtPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_not_show_password, 0);

                        //alterna toggle
                        togglePassVisibility = true;
                    } else {

                        //define o input como text password invisible
                        edtTxtPass.setInputType(129);

                        Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.poppins_medium);
                        edtTxtPass.setTypeface(typeface);

                        //define o icone de senha invisivel
                        edtTxtPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_show_password, 0);

                        togglePassVisibility = false;
                    }
                    return true;
                }
            }
            return false;
        });
    }

    protected void setGoToLoginClickListener(TextView txtBackToLogin) {
        txtBackToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
