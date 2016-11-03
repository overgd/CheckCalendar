package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.MainActivity;
import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 2016-09-23.
 */

public class CalendarVerticalView extends RelativeLayout
        implements View.OnClickListener, View.OnTouchListener {

    Context context;
    LinearLayout[] calendarMonthRowLayout;
    CalendarDayTextView[][] calendarDayTextView;

    public CalendarVerticalView(Context context) {
        super(context);
        this.context = context;
        setLayoutParams(new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        ));
        setId(R.id.calendarview_id);

        addView(linearLayoutCalendarMonthUI(context));
    }

    public void setCalendar(int positionMonth) {

        Calendar positionCalendar =
                CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(positionMonth);
        Calendar dayCalendar;

        /** Clear View **/
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 7; y++) {
                calendarDayTextView[x][y].setText("");
                calendarDayTextView[x][y].setOnClickListener(null);
            }
        }

        //빈 줄 삭제
        if (calendarMonthRowLayout.length != CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(positionCalendar)) {
            calendarMonthRowLayout[calendarMonthRowLayout.length - 1].setVisibility(GONE);
        } else {
            calendarMonthRowLayout[calendarMonthRowLayout.length - 1].setVisibility(VISIBLE);
        }

        /** Set Day TextView **/
        int total_day = 1;
        int first_day_num = CalendarUtils.GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(positionCalendar);
        int day_of_month = CalendarUtils.GET_NUMBER_DAY_OF_MONTH(positionCalendar);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {

                if (row == 0) {
                    if (col + first_day_num < 7) {
                        if (total_day == 1) {
                            calendarDayTextView[row][col + first_day_num]
                                    .setText((positionCalendar.get(Calendar.MONTH) + 1)
                                            + "/" + String.valueOf(total_day));
                        } else {
                            calendarDayTextView[row][col + first_day_num].setText(String.valueOf(total_day));
                        }
                        dayCalendar = Calendar.getInstance();
                        dayCalendar.setTimeInMillis(positionCalendar.getTimeInMillis());
                        dayCalendar.set(Calendar.DATE, total_day);
                        calendarDayTextView[row][col + first_day_num].setCalendar(dayCalendar);
                        calendarDayTextView[row][col + first_day_num].setOnClickListener((MainActivity) context);
                        total_day++;
                    }
                } else {
                    if (total_day <= day_of_month) {
                        calendarDayTextView[row][col].setText(String.valueOf(total_day));
                        dayCalendar = Calendar.getInstance();
                        dayCalendar.setTimeInMillis(positionCalendar.getTimeInMillis());
                        dayCalendar.set(Calendar.DATE, total_day);
                        calendarDayTextView[row][col].setCalendar(dayCalendar);
                        calendarDayTextView[row][col].setOnClickListener((MainActivity) context);
                        total_day++;
                    }
                }

            }
        }
    }

    public LinearLayout linearLayoutCalendarMonthUI(Context context) {

        LinearLayout calendarMonthLayout = new LinearLayout(context);
        calendarMonthLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        calendarMonthLayout.setOrientation(LinearLayout.VERTICAL);

        calendarMonthRowLayout = new LinearLayout[6];

        calendarDayTextView = new CalendarDayTextView[6][7];

        for (int row = 0; row < calendarMonthRowLayout.length; row++) {

            calendarMonthRowLayout[row] = new LinearLayout(context);
            calendarMonthRowLayout[row].setPadding(
                    getResources().getDimensionPixelSize(R.dimen.cardview_sidepadding),
                    0,
                    getResources().getDimensionPixelSize(R.dimen.cardview_sidepadding),
                    0
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                calendarMonthRowLayout[row].setBackground(
                        context.getDrawable(R.drawable.calendar_column_shape)
                );
            } else {
                calendarMonthRowLayout[row].setBackground(
                        context.getResources().getDrawable(R.drawable.calendar_column_shape)
                );
            }

            calendarMonthRowLayout[row].setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            calendarMonthRowLayout[row].setOrientation(LinearLayout.HORIZONTAL);
            for (int day = 0; day < 7; day++) {
                //in Text init
                calendarDayTextView[row][day] = new CalendarDayTextView(context);
                calendarDayTextView[row][day].setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                ));
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
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0,
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0);

        calendarWeekTextView = new TextView[7]; // days of week

        for (int i = 0; i < 7; i++) {

            calendarWeekTextView[i] = new TextView(context);

            calendarWeekTextView[i].setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
            ));

            calendarWeekTextView[i].setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.calendarmonth_column_textsize));
            calendarWeekTextView[i].setText(
                    context.getResources().getTextArray(R.array.calendar_week)[i]
            );
            calendarWeekTextView[i].setTypeface(null, Typeface.BOLD);

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

    @Override
    public void onClick(View v) {

//        int indexX = 0;
//        int indexY = 0;
//        for(LinearLayout[] columnX : calendarMonthColumn) {
//            for(LinearLayout column : columnX) {
//                if(v == column) {
//                    int[] viewPostion = new int[2];
//                    int[] tablePosition = new int[2];
//
//                    getLocationInWindow(tablePosition);
//                    v.getLocationInWindow(viewPostion);
//
//                    if(checkView[indexX][indexY] == null) {
//                        checkView[indexX][indexY] = new CheckView(context);
//                    }
//
//                    checkView[indexX][indexY].setX(viewPostion[0]-tablePosition[0]);
//                    checkView[indexX][indexY].setY(viewPostion[1]-tablePosition[1]);
//
//                    removeView(checkView[indexX][indexY]);
//                    addView(checkView[indexX][indexY]);
//
//                }
//                indexY++;
//            }
//            indexX++;
//            indexY = 0;
//        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;

    }
}
