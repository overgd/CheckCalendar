package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 2016-09-23.
 */

public class CalendarVerticalView extends RelativeLayout {

    Context context;
    TableLayout calendatMonthLayout;

    public CalendarVerticalView(Context context) {
        super(context);
        this.context = context;
        setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setId(R.id.calendarview_id);

    }

    public void setCalendar(final int positionMonth) {
        this.removeAllViews();
        calendatMonthLayout = tableLayoutCalendarMonthUI(context, positionMonth);
        addView(calendatMonthLayout);
    }

    /**
     * TableLayout Month Calendar Month UI
     **/
    public TableLayout tableLayoutCalendarMonthUI(Context context, int positionMonth) {

        TableLayout calendarMonthLayout;
        TableRow[] calendarMonthRow;
        LinearLayout[][] calendarMonthColumn;
        TextView[][] calendarDayNumberTextView;

        int year = (positionMonth / 12) + 1900;
        int month = (positionMonth % 12);
        if(month == 0) {  // 나머지가 0일 경우 12월이며, 년도가 +1되는 걸 막아주어야 한다.
            year = year - 1;
            month = 12;
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month-1, mCalendar.get(Calendar.DATE));

        calendarMonthLayout = new TableLayout(context);
        calendarMonthLayout.setLayoutParams(new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        calendarMonthLayout.setStretchAllColumns(true);

        calendarMonthRow = new TableRow[6]; // monthview row : 6
        calendarMonthColumn = new LinearLayout[6][7];
        calendarDayNumberTextView = new TextView[6][7]; // monthview day : 42  x:6 y:7

        for (int row = 0; row < 6; row++) {

            calendarMonthRow[row] = new TableRow(context);
            calendarMonthRow[row].setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1f
            ));

            for (int day = 0; day < 7; day++) {

                calendarMonthColumn[row][day] = (LinearLayout) View.inflate(context, R.layout.calendar_column, null);
//                calendarMonthColumn[row][day].setOnClickListener(this);

                calendarDayNumberTextView[row][day] = (TextView) calendarMonthColumn[row][day].findViewById(R.id.calendar_column_day);

                //in Text init
                calendarDayNumberTextView[row][day].setText("");

                calendarMonthRow[row].addView(calendarMonthColumn[row][day]);

            }
            calendarMonthRow[row].setBackgroundResource(R.drawable.calendar_column_shape);
            calendarMonthLayout.addView(calendarMonthRow[row]);
        }

        /** Set Day TextView **/
        for (int day = 1; day < CalendarUtils.GET_NUMBER_DAY_OF_MONTH(mCalendar); ) {
            int dayrow = CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(mCalendar);
            for (int row = 0; row < dayrow; row++) {
                for (int column = 0; column < calendarDayNumberTextView[row].length; column++) {
                    if (row == 0) {
                        int fdoim = CalendarUtils.GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(mCalendar);
                        if (fdoim + column < calendarDayNumberTextView[row].length) {
                            calendarDayNumberTextView[row][column + fdoim]
                                    .setText(String.valueOf(day).toString());
                            day++;
                        } else {
                            break;
                        }

                    } else {
                        calendarDayNumberTextView[row][column]
                                .setText(String.valueOf(day).toString());
                        if (day < CalendarUtils.GET_NUMBER_DAY_OF_MONTH(mCalendar)) {
                            day++;
                        } else {
                            break;
                        }
                    }
                }
            }
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
        calendarWeekLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        calendarWeekLayout.setOrientation(LinearLayout.HORIZONTAL);
        calendarWeekLayout.setPadding(
                (int) context.getResources().getDimension(R.dimen.size_16dp),
                0,
                (int) context.getResources().getDimension(R.dimen.size_16dp),
                0);

        calendarWeekTextView = new TextView[7]; // days of week

        for (int i = 0; i < 7; i++) {

            calendarWeekTextView[i] = new TextView(context);

            calendarWeekTextView[i].setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            ));

            TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) calendarWeekTextView[i].getLayoutParams();
            layoutParams.setMargins((int) context.getResources().getDimension(R.dimen.week_leftpadding),
                    0,
                    0,
                    (int) context.getResources().getDimension(R.dimen.week_leftpadding));
            calendarWeekTextView[i].setLayoutParams(layoutParams);

            calendarWeekTextView[i].setTextSize(10f);
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
}
