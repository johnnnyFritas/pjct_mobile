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
import android.widget.Toast; // Adicionado para exibir as mensagens na tela

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.tripleJTec.rotinaplus.R;

import android.util.Log;
import com.tripleJTec.rotinaplus.activities.database.BancoDeDados;

public class MainActivity extends AppCompatActivity {

    EditText edtTxtEmail, edtTxtPass;
    TextView txtPassForgotten, txtRegister;
    Button btnLogin;
    Boolean togglePassVisibility = false;

    // Declarando o banco de dados no escopo da classe para poder acessá-lo nos listeners
    BancoDeDados dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Instancia o banco de dados passando o contexto atual (this)
        dbHelper = new BancoDeDados(this);

        dbHelper.imprimirTodosOsUsuarios();
        edtTxtEmail = findViewById(R.id.edtTxtEmailLogin);
        // Iniciando views
        edtTxtEmail = findViewById(R.id.edtTxtEmailLogin);
        edtTxtPass = findViewById(R.id.edtTxtPassLogin);

        txtPassForgotten = findViewById(R.id.txtPassForgotten);
        txtRegister = findViewById(R.id.txtRegisterLogin);

        btnLogin = findViewById(R.id.btnLogin);

        // Funções
        setEdtTxtNameOnTextChangedListener(edtTxtEmail);
        setEmailIconClickListener(edtTxtEmail);
        setPassIconClickListener(edtTxtPass);
        setGoToForgottenPassClickListener(txtPassForgotten);
        setGoToRegisterClickListener(txtRegister);
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
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (edtTxtEmail.getCompoundDrawables()[2] != null) {
                    float dp = edtTxtEmail.getPaddingEnd();
                    int pixelsInteger = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            dp,
                            getResources().getDisplayMetrics()
                    );
                    float initialCompoundWidth = view.getWidth() - edtTxtEmail.getCompoundDrawables()[2].getBounds().width() - pixelsInteger;

                    if (motionEvent.getX() >= initialCompoundWidth) {
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
                    if (!togglePassVisibility) {
                        edtTxtPass.setInputType(145);
                        Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.poppins_medium);
                        edtTxtPass.setTypeface(typeface);
                        edtTxtPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_not_show_password, 0);
                        togglePassVisibility = true;
                    } else {
                        edtTxtPass.setInputType(129);
                        Typeface typeface = ResourcesCompat.getFont(view.getContext(), R.font.poppins_medium);
                        edtTxtPass.setTypeface(typeface);
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
            // Captura o que o usuário digitou
            String email = edtTxtEmail.getText().toString().trim();
            String senha = edtTxtPass.getText().toString().trim();

            // Validação simples
            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Checa as credenciais no banco de dados
            boolean loginValido = dbHelper.checarLogon(email, senha);

            if (loginValido) {
                Toast.makeText(MainActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                // TODO: Quando você criar a tela principal da rotina, descomente e adapte as linhas abaixo:
                // Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                // startActivity(intent);
                // finish();
            } else {
                Toast.makeText(MainActivity.this, "E-mail ou senha incorretos.", Toast.LENGTH_LONG).show();
            }
        });
    }
}