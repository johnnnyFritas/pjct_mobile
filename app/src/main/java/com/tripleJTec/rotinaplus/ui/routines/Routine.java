package com.tripleJTec.rotinaplus.ui.routines;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;
import com.tripleJTec.rotinaplus.ui.home.HomeActivity;
import com.tripleJTec.rotinaplus.ui.user.MyProfileActivity;

public class Routine extends AppCompatActivity {
    Routines routine;
    User user;
    DataBase dbHelper;
    TextView txtTitleRoutine, txtDescriptionRoutine, txtHourRoutine, txtRoutineHourWarning, txtSundayRoutine, txtMondayRoutine, txtTuesdayRoutine, txtWednesdayRoutine, txtThursdayRoutine, txtFridayRoutine, txtSaturdayRoutine;
    EditText edtTxtDescriptionRoutine, edtTxtHourRoutine;
    Button btnDesriptionEditRoutine, btnHourEditRoutine, btnFinishRoutine;
    ImageView imgCreateRoutineBottomMenuIcon, imgBottomMenuIcon, imgProfileBottomMenuIcon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        dbHelper = new DataBase(this);

        String routineName = getIntent().getStringExtra("name");
        String routineDaysOfWeekNormalString = getIntent().getStringExtra("daysOfWeek");
        String routineHour = getIntent().getStringExtra("hour");

        if (routineDaysOfWeekNormalString == null) {
            Intent intent = new Intent(Routine.this, HomeActivity.class);
            Toast.makeText(this, "Erro! Rotina Incompleta no sistema! Por favor escolha outra.", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

        assert routineDaysOfWeekNormalString != null;
        String[] routineDaysOfWeek = routineDaysOfWeekNormalString.split(", ");

        //iniciando views
        txtTitleRoutine = findViewById(R.id.txtTitleRoutine);
        txtDescriptionRoutine = findViewById(R.id.txtDescriptionRoutine);
        txtHourRoutine = findViewById(R.id.txtHourRoutine);
        txtRoutineHourWarning = findViewById(R.id.txtRoutineHourWarning);
        txtSundayRoutine = findViewById(R.id.txtSundayRoutine);
        txtMondayRoutine = findViewById(R.id.txtMondayRoutine);
        txtTuesdayRoutine = findViewById(R.id.txtTuesdayRoutine);
        txtWednesdayRoutine = findViewById(R.id.txtWednesdayRoutine);
        txtThursdayRoutine = findViewById(R.id.txtThursdayRoutine);
        txtFridayRoutine = findViewById(R.id.txtFridayRoutine);
        txtSaturdayRoutine = findViewById(R.id.txtSaturdayRoutine);

        edtTxtDescriptionRoutine = findViewById(R.id.edtTxtDescriptionRoutine);
        edtTxtHourRoutine = findViewById(R.id.edtTxtHourRoutine);

        btnDesriptionEditRoutine = findViewById(R.id.btnDesriptionEditRoutine);
        btnHourEditRoutine = findViewById(R.id.btnHourEditRoutine);
        btnFinishRoutine = findViewById(R.id.btnFinishRoutine);

        imgCreateRoutineBottomMenuIcon = findViewById(R.id.imgCreateRoutineBottomMenuIcon);
        imgBottomMenuIcon = findViewById(R.id.imgBottomMenuIcon);
        imgProfileBottomMenuIcon = findViewById(R.id.imgProfileBottomMenuIcon);

        //funções
        setImgCreateRoutineBottomMenuIconListener();
        setImgBottomMenuIconListener();
        setImgMyProfileBottomMenuIconListener();
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        user = dbHelper.getUser(email);
        routine = dbHelper.getRoutine(routineName, routineHour, routineDaysOfWeek);
        setTxtTitleRoutine();
        setTxtDescription();
        setEdtTxtDescriptionRoutineOnTextChangedListener();
        setDescriptionIconClickListener();
        setTxtHour();
        setDaysOfWeek();
        setBtnEditDescriptionListener();
        setBtnEditHourListener();
        setBtnFinishRoutineListener();
    }

    private void setImgCreateRoutineBottomMenuIconListener() {
        imgCreateRoutineBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, CreateRoutineActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgBottomMenuIconListener() {
        imgBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setImgMyProfileBottomMenuIconListener() {
        imgProfileBottomMenuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Routine.this, MyProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(Routine.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setTxtTitleRoutine() {
        txtTitleRoutine.setText(routine.getName());
    }

    private void setTxtDescription() {
        txtDescriptionRoutine.setText(routine.getDescricao());
    }

    private void setEdtTxtDescriptionRoutineOnTextChangedListener() {
        edtTxtDescriptionRoutine.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtTxtDescriptionRoutine.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_erase_all_content, 0);
                } else {
                    edtTxtDescriptionRoutine.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDescriptionIconClickListener() {
        edtTxtDescriptionRoutine.setOnTouchListener(((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float viewWidth = view.getWidth();
                float iconWidth = edtTxtDescriptionRoutine.getCompoundDrawables()[2].getBounds().width();
                int dp = view.getPaddingEnd();
                int pixelsInteger = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        getResources().getDisplayMetrics()
                );

                float initialCompoundWidth = viewWidth - iconWidth - pixelsInteger;

                if (motionEvent.getX() >= initialCompoundWidth) {
                    edtTxtDescriptionRoutine.setText("");
                    return true;
                }
            }

            return false;
        }));
    }

    private void setTxtHour() {
        txtHourRoutine.setText(routine.getHour());
    }

    private void setDaysOfWeek() {
        String[] daysOfWeek = routine.getDaysOfWeek().split(", ");

        for (String day : daysOfWeek) {
            if (day.contains("0")) {
                txtSundayRoutine.setVisibility(TextView.VISIBLE);
                txtSundayRoutine.setText(getString(R.string.sunday));
            } else if (day.contains("1")) {
                txtMondayRoutine.setVisibility(TextView.VISIBLE);
                txtMondayRoutine.setText(getString(R.string.monday));
            } else if (day.contains("2")) {
                txtTuesdayRoutine.setVisibility(TextView.VISIBLE);
                txtTuesdayRoutine.setText(getString(R.string.tuesday));
            } else if (day.contains("3")) {
                txtWednesdayRoutine.setVisibility(TextView.VISIBLE);
                txtWednesdayRoutine.setText(getString(R.string.wednesday));
            } else if (day.contains("4")) {
                txtThursdayRoutine.setVisibility(TextView.VISIBLE);
                txtThursdayRoutine.setText(getString(R.string.thursday));
            } else if (day.contains("5")) {
                txtFridayRoutine.setVisibility(TextView.VISIBLE);
                txtFridayRoutine.setText(getString(R.string.friday));
            } else if (day.contains("6")) {
                txtSaturdayRoutine.setVisibility(TextView.VISIBLE);
                txtSaturdayRoutine.setText(getString(R.string.saturday));
            }
        }
    }

    private void setBtnEditDescriptionListener() {
        btnDesriptionEditRoutine.setOnClickListener(v -> {
            String newDescription = edtTxtDescriptionRoutine.getText().toString();

            if (newDescription.isEmpty()) {
                Toast.makeText(this, "Precisa inserir uma descrição para altera-la", Toast.LENGTH_LONG).show();
            } else if (newDescription.length() > 75) {
                Toast.makeText(this, "A descrição deve ter 75 carácteres ou menos", Toast.LENGTH_LONG).show();
            } else {
                String[] daysOfWeek = routine.getDaysOfWeek().split(", ");

                boolean sucesso = dbHelper.updateRoutine(new Routines(null, routine.getName(), newDescription, daysOfWeek, routine.getHour(), routine.getRepeatable(), routine.getFinished(), routine.getUser_id()));

                if (sucesso) {
                    setLog(1, this.getLocalClassName(), "Rotina atualizada");
                    Toast.makeText(getApplicationContext(), "Rotina atualizada!", Toast.LENGTH_LONG).show();

                    //Vai para a home page
                    Intent intent = new Intent(Routine.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setBtnEditHourListener() {
        btnHourEditRoutine.setOnClickListener(v -> {
            if (validateHour()) {
                txtRoutineHourWarning.setVisibility(TextView.INVISIBLE);

                String[] daysOfWeek = routine.getDaysOfWeek().split(", ");

                boolean sucesso = dbHelper.updateRoutine(new Routines(null, routine.getName(), routine.getDescricao(), daysOfWeek, edtTxtHourRoutine.getText().toString(), routine.getRepeatable(), routine.getFinished(), routine.getUser_id()));

                if (sucesso) {
                    Toast.makeText(getApplicationContext(), "Rotina atualizada!", Toast.LENGTH_LONG).show();

                    //Vai para a home page
                    Intent intent = new Intent(Routine.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                txtRoutineHourWarning.setVisibility(TextView.VISIBLE);
                txtRoutineHourWarning.setText(getString(R.string.invalid_hour));
            }
        });
    }

    private Boolean validateHour() {
        String regex = "(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9])";
        return edtTxtHourRoutine.getText().toString().trim().matches(regex);
    }

    private void setBtnFinishRoutineListener() {
        btnFinishRoutine.setOnClickListener(v -> {
            boolean sucesso = dbHelper.deleteRoutine(routine);

            if (sucesso) {
                Toast.makeText(getApplicationContext(), "Rotina Deletada!", Toast.LENGTH_LONG).show();

                //Vai para a home page
                Intent intent = new Intent(Routine.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
