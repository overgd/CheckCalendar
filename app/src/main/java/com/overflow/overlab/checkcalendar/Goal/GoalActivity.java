package com.overflow.overlab.checkcalendar.Goal;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.overflow.overlab.checkcalendar.BaseActivity;
import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsDescriptionModel;
import com.overflow.overlab.checkcalendar.R;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by over on 2016-09-21.
 * 시작시간과 종료시간 설정 시에 종료시간이 시작시간을 넘지않게
 *
 */

public class GoalActivity extends BaseActivity {

    @BindView(R.id.goal_input_form_startdate_edittext)
    EditText goalStartDateEditText;
    @BindView(R.id.goal_input_form_enddate_edittext)
    EditText goalEndDateEditText;
    @BindView(R.id.goal_input_form_goal_title)
    EditText goalTitleEditText;
    @BindView(R.id.goal_input_form_description)
    EditText goalDescriptionEditText;

    @BindView(R.id.confirm_fab)
    FloatingActionButton confirm_Fab;

    Calendar startCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    Calendar currentCalendar = Calendar.getInstance();

    CheckCalendarApplication applicationClass;

    final static int START_DATE = 0;
    final static int END_DATE = 1;

    int requestCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_goal);
        super.onCreate(savedInstanceState);
        applicationClass = (CheckCalendarApplication) getApplicationContext();
        requestCode = getIntent().getIntExtra(REQUEST_CODE, 0);
        initMainUi();
        setDateEditText(goalStartDateEditText, START_DATE);
        setDateEditText(goalEndDateEditText, END_DATE);

    }

    @Override
    protected void initMainUi() {
        super.initMainUi();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if(requestCode == ADD_GOAL) {
            getSupportActionBar().setTitle("Add Goal");
        } else if (requestCode == EDIT_GOAL) {
            getSupportActionBar().setTitle("Edit Goal");
        }
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

//        dateEditText.setText((hour + startOrEnd) + " : " + min);
        if(startOrEnd == START_DATE) {
            dateEditText.setText(GoalUtils.convertCalendarStringTime(currentCalendar));
        }
        else if(startOrEnd == END_DATE) {
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(currentCalendar.getTimeInMillis());
            mCalendar.set(Calendar.HOUR, mCalendar.get(Calendar.HOUR) + END_DATE);
            dateEditText.setText(GoalUtils.convertCalendarStringTime(mCalendar));
        }

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

        if (view == goalEndDateEditText) {

            timePickerDialog = new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            endCalendar.set(Calendar.MINUTE, minute);
                            goalEndDateEditText
                                    .setText(GoalUtils.convertCalendarStringTime(endCalendar));
                        }
                    },
                    currentCalendar.get(Calendar.HOUR_OF_DAY) + END_DATE,
                    currentCalendar.get(Calendar.MINUTE), true
            );

            timePickerDialog.show();

        } else if (view == goalStartDateEditText) {

            timePickerDialog = new TimePickerDialog(
                    this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startCalendar.set(Calendar.MINUTE, minute);
                            goalStartDateEditText
                                    .setText(GoalUtils.convertCalendarStringTime(startCalendar));
                        }
                    },
                    currentCalendar.get(Calendar.HOUR_OF_DAY),
                    currentCalendar.get(Calendar.MINUTE), true
            );
            timePickerDialog.show();
        }
    }

    @OnClick(R.id.confirm_fab)
    void OnClickConfirmFab() {
        if(requestCode == ADD_GOAL) {
            GoalCalendarsDescriptionModel goalCalendarsDescriptionModel
                    = GoalUtils.setGoalDescriptionModel(
                    goalTitleEditText.getText().toString(),
                    goalDescriptionEditText.getText().toString(),
                    GoalUtils.convertDateTimeToCalendar(startCalendar),
                    GoalUtils.convertDateTimeToCalendar(endCalendar));

            String result = GoalUtils.addGoal(goalCalendarsDescriptionModel);
            if(Objects.equals(result, GoalUtils.CONFIRM)) {
                this.setResult(RESULT_OK);
                applicationClass.setCurrentGoal(
                        goalCalendarsDescriptionModel.getId(),
                        goalCalendarsDescriptionModel.getSummary()
                );
                this.finish();
            } else {
                Log.d("ERROR", result);
            }
        }
    }

}
