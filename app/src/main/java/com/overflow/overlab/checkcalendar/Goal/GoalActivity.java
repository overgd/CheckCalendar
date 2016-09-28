package com.overflow.overlab.checkcalendar.Goal;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.overflow.overlab.checkcalendar.R;

import java.lang.reflect.Method;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by over on 2016-09-21.
 * 시작시간과 종료시간 설정 시에 종료시간이 시작시간을 넘지않게
 *
 */

public class GoalActivity extends AppCompatActivity {

    @BindView(R.id.goal_input_form_startdate_edittext)
    EditText startDateEditText;
    @BindView(R.id.goal_input_form_enddate_edittext)
    EditText endDateEditText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int start_hour;
    int strat_min;
    int end_hour;
    int end_min;

    Calendar currentCalendar = Calendar.getInstance();
    int hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
    int min = currentCalendar.get(Calendar.MINUTE);

    final static int START_DATE = 0;
    final static int END_DATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_goal);
        ButterKnife.bind(this);

        initMainUi();
        setDateEditText(startDateEditText, START_DATE);
        setDateEditText(endDateEditText, END_DATE);

        super.onCreate(savedInstanceState);
    }

    void initMainUi() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setDateEditText(EditText dateEditText, int startOrEnd) {

        dateEditText.setText((hour + startOrEnd) + " : " + min);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Don't show Keyboard
            dateEditText.setShowSoftInputOnFocus(false);
        } else {
            try {
                final Method method = EditText.class.getMethod(
                        "setShowSoftInputOnFocus"
                        , new Class[]{boolean.class});
                method.setAccessible(true);
                method.invoke(dateEditText, false);
            } catch (Exception e) {
                // ignore
            }
        }

        dateEditText.setCursorVisible(false);
        dateEditText.setClickable(false);
        dateEditText.setKeyListener(null);
    }

    public void onClickEditTextTimePicker(View view) {

        TimePickerDialog timePickerDialog;

        if (view == endDateEditText) {

            timePickerDialog = new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            end_hour = hourOfDay;
                            end_min = minute;
                            endDateEditText.setText(end_hour + " : " + end_min);
                        }
                    }, hour + 1, min, true
            );

            timePickerDialog.show();

        } else if (view == startDateEditText) {

            timePickerDialog = new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            start_hour = hourOfDay;
                            strat_min = minute;
                            startDateEditText.setText(start_hour + " : " + strat_min);
                        }
                    }, hour, min, true
            );
            timePickerDialog.show();
        }


    }

}
