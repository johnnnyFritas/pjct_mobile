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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.tripleJTec.rotinaplus.R;

public class MainActivity extends AppCompatActivity {

    EditText edtTxtEmail, edtTxtPass;

    TextView txtPassForgotten, txtRegister;

    Button btnLogin;

    Boolean togglePassVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //iniciando views
        edtTxtEmail = findViewById(R.id.edtTxtEmailLogin);
        edtTxtPass = findViewById(R.id.edtTxtPassLogin);

        txtPassForgotten = findViewById(R.id.txtPassForgotten);
        txtRegister = findViewById(R.id.txtRegisterLogin);

        btnLogin = findViewById(R.id.btnLogin);

        //funções
        //define um listener de edição de texto para verificar se tem conteúdo no input de e-mail, se tiver adiciona drawable de icone à direita para apagar tudo com clique.
        setEdtTxtNameOnTextChangedListener(edtTxtEmail);

        //define um listener de clique no drawable de icone à direita no input de e-mail. Ao clicar apaga o conteúdo do input e-mail.
        setEmailIconClickListener(edtTxtEmail);

        //define um listener de clique no drawable de icone à direita no input de senha. Ao clicar ele mostra a senha e muda o icone. Ao clicar novamente, esconde a senha e muda o icone.
        setPassIconClickListener(edtTxtPass);

        //define um listener de clique na view. Ao clicar leva para a página de esqueci minha senha.
        setGoToForgottenPassClickListener(txtPassForgotten);

        //define um listener de clique na view. Ao clicar leva para a página de registro.
        setGoToRegisterClickListener(txtRegister);

        //define um listener de clique no botão. Ao clicar tenta autenticar com o BD. Se tiver sucesso leva o usuário para a página de home. Se não informa que não conseguiu.
        setBtnLoginListener(btnLogin);
    }

    protected void setEdtTxtNameOnTextChangedListener(EditText edtTxtEmail) {
        edtTxtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_erase_all_content, 0);
                } else {
                    edtTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void setEmailIconClickListener(EditText edtTxtEmail) {
        edtTxtEmail.setOnTouchListener( (view, motionEvent) -> {

            //verifica se foi toque na tela
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                //verifica se o drawable está na view
                if (edtTxtEmail.getCompoundDrawables()[2] != null) {

                    //transforma a unidade 'dp' do padding do edit text em integer
                    float dp = edtTxtEmail.getPaddingEnd();
                    int pixelsInteger = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            dp,
                            getResources().getDisplayMetrics()
                    );

                    //calcula a posição inicial do drawable
                    float initialCompoundWidth = view.getWidth() - edtTxtEmail.getCompoundDrawables()[2].getBounds().width() - pixelsInteger;


                    //verifica se o click foi no drawable (ou seja, entre a posição inicial do drawable e a largura total da view (final do drawable)).
                    if (motionEvent.getX() >= initialCompoundWidth) {

                        //limpando o edit text e removendo drawable
                        edtTxtEmail.setText("");
                        edtTxtEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                        return true;
                    }
                }
            }
            return false;
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

    protected void setGoToForgottenPassClickListener(TextView txtPassForgotten) {
        txtPassForgotten.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ForgottenPassActivity.class);
            startActivity(intent);
            finish();
        });
    }

    protected void setGoToRegisterClickListener(TextView txtRegister) {
        txtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    protected void setBtnLoginListener(Button btnLogin) {
        btnLogin.setOnClickListener(view -> {

        });
    }
}