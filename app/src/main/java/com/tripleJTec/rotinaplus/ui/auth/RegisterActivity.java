package com.tripleJTec.rotinaplus.ui.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtTxtName, edtTxtEmail, edtTxtPass;
    TextView txtNameWarning, txtEmailWarning, txtPassWarning, txtBackToLogin;
    Button btnRegister;
    Boolean togglePassVisibility = false;
    DataBase dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializa o banco de dados
        dbHelper = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        // Iniciando views
        edtTxtName = findViewById(R.id.edtTxtNameRegister);
        edtTxtEmail = findViewById(R.id.edtTxtEmailRegister);
        edtTxtPass = findViewById(R.id.edtTxtPassRegister);

        txtNameWarning = findViewById(R.id.txtNameWarning);
        txtEmailWarning = findViewById(R.id.txtEmailWarning);
        txtPassWarning = findViewById(R.id.txtPassWarning);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);

        btnRegister = findViewById(R.id.btnRegister);

        // Funções
        setEdtTxtNameOnTextChangedListener(edtTxtName);
        setEdtTxtEmailOnTextChangedListener(edtTxtEmail);
        setPassIconClickListener(edtTxtPass);
        setGoToLoginClickListener(txtBackToLogin);
        setBtnRegisterOnClickListener(btnRegister, sharedPreferences);
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

    protected void setGoToLoginClickListener(TextView txtBackToLogin) {
        txtBackToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    protected void setBtnRegisterOnClickListener(Button btnRegister, SharedPreferences sharedPreferences) {
        btnRegister.setOnClickListener(view -> {
            // Captura e limpa os textos digitados
            String nome = edtTxtName.getText().toString().trim();
            String email = edtTxtEmail.getText().toString().trim();
            String senha = edtTxtPass.getText().toString().trim();

            // Validação simples
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            //Cria um objeto do tipo user para inserir no bd
            //id null pois o banco de dados cria o ID sozinho
            User user = new User(null, nome, email, senha);

            //verifica se o usuario ja esta registrado
            User userCheck = dbHelper.getUser(email);

            if (userCheck == null) {
                // Tenta salvar no banco de dados
                boolean sucesso = dbHelper.insertUser(user);

                if (sucesso) {
                    setLog(1, this.getLocalClassName(), "Usuário cadastrado");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.apply();

                    //Vai para a home page
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                setLog(2, this.getLocalClassName(), "Usuário já cadastrado");
            }
        });
    }

    protected void setLog(Integer idLogType, String className, String message) {
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
