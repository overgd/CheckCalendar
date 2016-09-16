package com.overflow.overlab.checkcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by over on 7/7/2016.
 */
public class DayActivity extends AppCompatActivity implements View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private Intent getIntent;
    private Calendar getCalendar;
    private long calendarTimeInMillis = 0;

    private CoordinatorLayout dayActivity;
    private TextView yearMonthTextView;
    private View plusFab;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_goal_check);
        initDayActivityUI();


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(verticalOffset)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void initDayActivityUI () {

        getIntent = getIntent();

        calendarTimeInMillis = getIntent.getLongExtra("SelectedCalendarTimeInMillis", 0);

        if(calendarTimeInMillis == 0) {
            getCalendar = Calendar.getInstance();
        } else {
            getCalendar = Calendar.getInstance();
            getCalendar.setTimeInMillis(calendarTimeInMillis);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.day_collapsing);
        collapsingToolbarLayout.setTitle(String.valueOf(getCalendar.get(Calendar.DATE)));

        yearMonthTextView = (TextView) findViewById(R.id.day_yearmonth_textview);
        yearMonthTextView.setText(String.valueOf(getCalendar.get(Calendar.YEAR))+" "+
                getResources().getStringArray(R.array.calendar_month)[getCalendar.get(Calendar.MONTH)]);

        appBarLayout = (AppBarLayout) findViewById(R.id.day_appbar);
        appBarLayout.addOnOffsetChangedListener(this);

    }

}
