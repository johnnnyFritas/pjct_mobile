package com.tripleJTec.rotinaplus.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;
import com.tripleJTec.rotinaplus.model.Routines;
import com.tripleJTec.rotinaplus.model.User;
import com.tripleJTec.rotinaplus.ui.auth.MainActivity;

import java.io.IOException;

public class CreateRoutineActivity extends AppCompatActivity {

    EditText edtTxtName, edtTxtDescription, edtTxtHour;
    TextView txtHourWarning, txtBackToHome, txtAudioWarning;
    CheckBox checkBoxSunday, checkBoxMonday, checkBoxTuesday, checkBoxWednesday, checkBoxThursday, checkBoxFriday, checkBoxSaturday;
    Button btnCreateRoutine;
    ImageButton imgPlay, imgRecord;
    Integer click = 0;
    DataBase dbHelper;

    //audio
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFile;
    private boolean isRecording = false;
    private boolean hasRecording = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        dbHelper = new DataBase(this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences_name), MODE_PRIVATE);

        //iniciando views
        edtTxtName = findViewById(R.id.edtTxtCreateRoutineName);
        edtTxtDescription = findViewById(R.id.edtTxtCreateRoutineDescription);
        edtTxtHour = findViewById(R.id.edtTxtCreateRoutineHour);

        txtHourWarning = findViewById(R.id.txtCreateRoutineHourWarning);
        txtBackToHome = findViewById(R.id.txtCreateRoutineBackToHome);
        txtAudioWarning = findViewById(R.id.txtAudioWarning);

        checkBoxSunday = findViewById(R.id.checkboxCreateRoutineSunday);
        checkBoxMonday = findViewById(R.id.checkboxCreateRoutineMonday);
        checkBoxTuesday = findViewById(R.id.checkboxCreateRoutineTuesday);
        checkBoxWednesday = findViewById(R.id.checkboxCreateRoutineWednesday);
        checkBoxThursday = findViewById(R.id.checkboxCreateRoutineThursday);
        checkBoxFriday = findViewById(R.id.checkboxCreateRoutineFriday);
        checkBoxSaturday = findViewById(R.id.checkboxCreateRoutineSaturday);

        //audio
        audioFile = getExternalFilesDir(null)+ "/rotina_audio.3gp";
        imgRecord = findViewById(R.id.imgRecord);
        imgPlay = findViewById(R.id.imgPlay);
        btnCreateRoutine = findViewById(R.id.btnCreateRoutine);

        //funções
        checkUserAuthenticationWithSharedPreferences(sharedPreferences);
        String email = getEmailWithSharedPreferences(sharedPreferences);
        User user = dbHelper.getUser(email);
        setEdtTxtDescriptionOnTextChangedListener(edtTxtDescription);
        setDescriptionIconClickListener(edtTxtDescription);
        setBtnCreateRoutineListener(btnCreateRoutine, txtHourWarning, user);
        setGoToHomeClickListener(txtBackToHome);

        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    1
            );
        }

        setImgRecordListener();
        setImgPlayListener();
    }

    private void checkUserAuthenticationWithSharedPreferences(SharedPreferences sharedPreferences) {
        if (!dbHelper.checkUserAuthenticationWithSharedPreferences(sharedPreferences)) {
            Intent intent = new Intent(CreateRoutineActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getEmailWithSharedPreferences(SharedPreferences sharedPreferences) {
        String email = sharedPreferences.getString("email", "E-mail não salvo");
        return email.equals("E-mail não salvo") ? "" : email;
    }

    private void setEdtTxtDescriptionOnTextChangedListener(EditText edtTxtDescription) {
        edtTxtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    edtTxtDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_erase_all_content, 0);
                } else {
                    edtTxtDescription.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });
    }

    protected void onStop(){
        super.onStop();
        if(mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDescriptionIconClickListener(EditText edtTxtDescription) {
        edtTxtDescription.setOnTouchListener(((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float viewWidth = view.getWidth();
                float iconWidth = edtTxtDescription.getCompoundDrawables()[2].getBounds().width();
                int dp = view.getPaddingEnd();
                int pixelsInteger = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dp,
                        getResources().getDisplayMetrics()
                );

                float initialCompoundWidth = viewWidth - iconWidth - pixelsInteger;

                float clickable = view.getHeight() - view.getPaddingTop() - view.getPaddingBottom();
                //area que não é drawable
                float blankSpace = clickable - edtTxtDescription.getCompoundDrawables()[2].getBounds().height();
                //da area vazia que sobra, metade dela está acima do drawable e metade está abaixo. Pois o drawable está sempre exatamente no meio da veio (verticalmente).
                float part = blankSpace/2;
                float initialCompoundHeight =  edtTxtDescription.getPaddingTop() + part;
                float finalCompoundHeight = initialCompoundHeight + edtTxtDescription.getCompoundDrawables()[2].getBounds().height();

                if (motionEvent.getX() >= initialCompoundWidth && (motionEvent.getY() >= initialCompoundHeight && motionEvent.getY() <= finalCompoundHeight)) {
                    edtTxtDescription.setText("");
                    return true;
                }
            }

            return false;
        }));
    }

    private void setBtnCreateRoutineListener(Button btnCreateRoutine, TextView txtHourWarning, User user) {
        btnCreateRoutine.setOnClickListener(v -> {
            if (validateHour()) {
                txtHourWarning.setVisibility(TextView.VISIBLE);

                String sunday = checkBoxSunday.isChecked() ? "0" : null;
                String monday = checkBoxMonday.isChecked() ? "1" : null;
                String tuesday = checkBoxTuesday.isChecked() ? "2" : null;
                String wednesday = checkBoxWednesday.isChecked() ? "3" : null;
                String thursday = checkBoxThursday.isChecked() ? "4" : null;
                String friday = checkBoxFriday.isChecked() ? "5" : null;
                String saturday = checkBoxSaturday.isChecked() ? "6" : null;

                String[] daysOfWeek = new String[7];
                daysOfWeek[0] = sunday;
                daysOfWeek[1] = monday;
                daysOfWeek[2] = tuesday;
                daysOfWeek[3] = wednesday;
                daysOfWeek[4] = thursday;
                daysOfWeek[5] = friday;
                daysOfWeek[6] = saturday;

                boolean repeatable = false;

                for (String day : daysOfWeek) {
                    if (day == null) {
                        repeatable = true;
                        break;
                    }
                }

                boolean createRoutineBool = dbHelper.insertRoutine(new Routines(null, edtTxtName.getText().toString(), edtTxtDescription.getText().toString(), daysOfWeek, edtTxtHour.getText().toString(), repeatable, false, user.getId()));

                if (createRoutineBool) {
                    Toast.makeText(getApplicationContext(), "Rotina criada!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(CreateRoutineActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                txtHourWarning.setText(getString(R.string.invalid_hour));
            }
        });
    }

    private Boolean validateHour() {
        String regex = "(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|1[0-9]|2[0-9]|3[0-9]|4[0-9]|5[0-9])";
        return edtTxtHour.getText().toString().trim().matches(regex);
    }

    private void setGoToHomeClickListener(TextView txtBackToHome) {
        txtBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(CreateRoutineActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setImgRecordListener() {
        final long[] pressStartTime = {0};

        imgRecord.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pressStartTime[0] = System.currentTimeMillis();
                startRecording();

                return true;
            } else {
                long pressDuration = System.currentTimeMillis() - pressStartTime[0];

                if (pressDuration < 500) {
                    // tempo mínimo que não grava
                    if (mediaRecorder != null) {
                        try {
                            mediaRecorder.stop();
                        } catch (Exception ignored) {}
                        mediaRecorder.release();
                        mediaRecorder = null;
                        isRecording = false;

                        txtAudioWarning.setText(getString(R.string.audio_warning));
                        imgRecord.setImageResource(android.R.drawable.ic_btn_speak_now);

                        return true;
                    }
                    Toast.makeText(this, "Segure para gravar!", Toast.LENGTH_SHORT).show();
                } else {
                    // Gravação válida
                    if (isRecording) {
                        txtAudioWarning.setVisibility(TextView.GONE);

                        stopRecording();

                        return true;
                    }
                }
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setImgPlayListener() {
        imgPlay.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (hasRecording && audioFile !=null) {
                    playRecording();
                } else {
                    Toast.makeText(this, "Nenhum áudio gravado ainda", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        });
    }

    private void startRecording(){
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFile);
            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;

            imgRecord.setImageResource(android.R.drawable.ic_media_pause);

            Toast.makeText(this, "Gravando....", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro ao gravar áudio", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording(){
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            isRecording = false;
            hasRecording = true;

            imgRecord.setImageResource(android.R.drawable.ic_btn_speak_now);
            imgPlay.setEnabled(true);

            Toast.makeText(this,"Áudio salvo", Toast.LENGTH_SHORT).show();
        }
    }

    private void playRecording() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Reproduzindo...", Toast.LENGTH_SHORT).show();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

        } catch (IOException e) {
            Toast.makeText(this, "Erro ao reproduzir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
