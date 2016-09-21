package com.overflow.overlab.checkcalendar.Goal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.R;

import butterknife.ButterKnife;

/**
 * Created by amelie2134 on 2016-09-21.
 */

public class GoalActivity extends AppCompatActivity {

    CheckCalendarApplication applicationClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_goal);
        ButterKnife.bind(this);
        applicationClass = (CheckCalendarApplication) getApplicationContext();

        super.onCreate(savedInstanceState);
    }
}
