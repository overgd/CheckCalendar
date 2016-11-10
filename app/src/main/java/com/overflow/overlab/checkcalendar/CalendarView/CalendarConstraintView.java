package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.MainActivity;
import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 2016-09-23.
 */

public class CalendarConstraintView extends ConstraintLayout {

    static final int ROW = 6;
    static final int COL = 7;

    Context context;

    public Calendar positionCalendar;

    LinearLayout[] calendarMonthRowLayout;
    CalendarDayTextView[][] calendarDayTextView;
    CalendarDayTextView todayTextView;
    CalendarTodayTextView todayView;

    public CalendarConstraintView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CalendarConstraintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CalendarConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        addView(linearLayoutCalendarMonthUI(context));
    }

    public void setCalendar(int positionMonth) {

        positionCalendar =
                CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(positionMonth);
        Calendar dayCalendar;

        /** Clear View **/
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                calendarDayTextView[row][col].clearView();
            }
        }
        removeView(todayView);
        calendarMonthRowLayout[calendarMonthRowLayout.length - 1].setVisibility(VISIBLE);

        /**빈 줄 삭제*/
        if (calendarMonthRowLayout.length != CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(positionCalendar)) {
            calendarMonthRowLayout[calendarMonthRowLayout.length - 1].setVisibility(GONE);
        }

        /** Set Day TextView **/
        int total_day = 1;
        final int first_day_num = CalendarUtils.GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(positionCalendar);
        final int day_of_month = CalendarUtils.GET_NUMBER_DAY_OF_MONTH(positionCalendar);

        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {

                int day = col;

                if(row == 0) {
                    day = col+first_day_num;
                    if(day == 7) break;
                } else if (total_day > day_of_month) {
                    break;
                }

                dayCalendar = Calendar.getInstance();
                dayCalendar.setTimeInMillis(positionCalendar.getTimeInMillis());
                dayCalendar.set(Calendar.DATE, total_day);

                calendarDayTextView[row][day].calendar = dayCalendar;
                calendarDayTextView[row][day].setText(String.valueOf(dayCalendar.get(Calendar.DATE)));
                calendarDayTextView[row][day].setOnClickListener((MainActivity) context);

                //Today!
                if(positionCalendar.get(Calendar.YEAR) == CalendarUtils.getNowCalendar().get(Calendar.YEAR)
                        && positionCalendar.get(Calendar.MONTH) == CalendarUtils.getNowCalendar().get(Calendar.MONTH)
                        && Integer.valueOf(CalendarUtils.getNowCalendar().get(Calendar.DATE)) == total_day) {
                    setTodayView(calendarDayTextView[row][day]);
                }

                total_day++;
            }
        }
    }

    public LinearLayout linearLayoutCalendarMonthUI(Context context) {

        LinearLayout calendarMonthLayout = new LinearLayout(context);
        calendarMonthLayout.setLayoutParams(new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));
        calendarMonthLayout.setOrientation(LinearLayout.VERTICAL);
        calendarMonthLayout.setId(generateViewId());

        calendarMonthRowLayout = new LinearLayout[ROW];
        calendarDayTextView = new CalendarDayTextView[ROW][COL];

        ViewGroup.LayoutParams rowLayoutParams = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        for (int row = 0; row < ROW; row++) {

            calendarMonthRowLayout[row] = new LinearLayout(context);
            calendarMonthRowLayout[row].setPadding(
                    getResources().getDimensionPixelSize(R.dimen.calendarmonth_row_sidepadding),
                    0,
                    getResources().getDimensionPixelSize(R.dimen.calendarmonth_row_sidepadding),
                    0
            );
            calendarMonthRowLayout[row].setBackgroundResource(R.drawable.calendar_column_shape);
            calendarMonthRowLayout[row].setLayoutParams(rowLayoutParams);
            calendarMonthRowLayout[row].setOrientation(LinearLayout.HORIZONTAL);

            for (int day = 0; day < COL; day++) {
                //in Text init
                calendarDayTextView[row][day] = new CalendarDayTextView(context);
                calendarMonthRowLayout[row].addView(calendarDayTextView[row][day]);
            }

            calendarMonthLayout.addView(calendarMonthRowLayout[row]);

        }

        return calendarMonthLayout;
    }

    /**
     * LinearLayout Month Calendar Week UI
     **/
    static public LinearLayout linearLayoutCalendarWeekUI(Context context) {

        LinearLayout calendarWeekLayout;
        TextView[] calendarWeekTextView;

        calendarWeekLayout = new LinearLayout(context);
        calendarWeekLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        calendarWeekLayout.setWeightSum(7f);
        calendarWeekLayout.setOrientation(LinearLayout.HORIZONTAL);
        calendarWeekLayout.setPadding(
                context.getResources().getDimensionPixelSize(R.dimen.calendarview_sidepadding),
                0,
                context.getResources().getDimensionPixelSize(R.dimen.calendarview_sidepadding),
                0);

        calendarWeekTextView = new TextView[7]; // days of week

        for (int i = 0; i < 7; i++) {

            calendarWeekTextView[i] = new TextView(context);

            calendarWeekTextView[i].setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            calendarWeekTextView[i].setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.calendarmonth_column_fontsize));
            calendarWeekTextView[i].setText(
                    context.getResources().getTextArray(R.array.calendar_week)[i]
            );
            calendarWeekTextView[i].setTypeface(null, Typeface.BOLD);
            calendarWeekTextView[i].setPadding(
                    context.getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding),
                    context.getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding),
                    context.getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding),
                    context.getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding)
            );

            if (Build.VERSION.SDK_INT >= 23) {
                if (i == 0 | i == 6) {
                    calendarWeekTextView[i].setTextColor(context.getColor(R.color.colorAccent));
                } else {
                    calendarWeekTextView[i].setTextColor(context.getColor(R.color.colorWhite));
                }
            } else {
                if (i == 0 | i == 6) {
                    calendarWeekTextView[i].setTextColor(context.getResources().getColor(R.color.colorAccent));
                } else {
                    calendarWeekTextView[i].setTextColor(context.getResources().getColor(R.color.colorWhite));
                }
            }
            calendarWeekLayout.addView(calendarWeekTextView[i]);

        }
        calendarWeekLayout.setId(generateViewId());

        return calendarWeekLayout;
    }

    public void setTodayView(CalendarDayTextView tv) {
        todayTextView = tv;
        todayView = new CalendarTodayTextView(context);
        addView(todayView);

        todayView.setWidth(todayTextView.getWidth());
    }

    public void addCheckActiveView() {
        ImageView view = new ImageView(context);
        view.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(todayView != null) {
            int[] tvPostion = new int[2];
            int[] position = new int[2];

            todayTextView.getLocationInWindow(tvPostion);
            getLocationInWindow(position);

            todayView.setX(tvPostion[0]-position[0]);
            todayView.setY(tvPostion[1]-position[1]);

            todayView.setWidth(todayTextView.getWidth());
        }

    }

}
