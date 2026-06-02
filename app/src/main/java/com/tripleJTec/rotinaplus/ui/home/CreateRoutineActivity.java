package com.tripleJTec.rotinaplus.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripleJTec.rotinaplus.R;
import com.tripleJTec.rotinaplus.data.DataBase;

public class CreateRoutineActivity extends AppCompatActivity {

    EditText edtTxtName, edtTxtDescription, edtTxtHour;
    TextView txtHourWarning, txtBackToHome;
    CheckBox checkBoxSunday, checkBoxMonday, checkBoxTuesday, checkBoxWednesday, checkBoxThursday, checkBoxFriday, checkBoxSaturday;
    Button btnCreateRoutine;
    DataBase dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        dbHelper = new DataBase(this);

        //iniciando views
        edtTxtName = findViewById(R.id.edtTxtCreateRoutineName);
        edtTxtDescription = findViewById(R.id.edtTxtCreateRoutineDescription);
        edtTxtHour = findViewById(R.id.edtTxtCreateRoutineHour);

        txtHourWarning = findViewById(R.id.txtCreateRoutineHourWarning);
        txtBackToHome = findViewById(R.id.txtCreateRoutineBackToHome);

        checkBoxSunday = findViewById(R.id.checkboxCreateRoutineSunday);
        checkBoxMonday = findViewById(R.id.checkboxCreateRoutineMonday);
        checkBoxTuesday = findViewById(R.id.checkboxCreateRoutineTuesday);
        checkBoxWednesday = findViewById(R.id.checkboxCreateRoutineWednesday);
        checkBoxThursday = findViewById(R.id.checkboxCreateRoutineThursday);
        checkBoxFriday = findViewById(R.id.checkboxCreateRoutineFriday);
        checkBoxSaturday = findViewById(R.id.checkboxCreateRoutineSaturday);

        btnCreateRoutine = findViewById(R.id.btnCreateRoutine);

        //funções
        setEdtTxtDescriptionOnTextChangedListener(edtTxtDescription);
        setDescriptionIconClickListener(edtTxtDescription);
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


}
