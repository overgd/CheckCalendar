package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

import static android.view.Gravity.CENTER;

/**
 * Created by over on 2016-09-23.
 */

public class CalendarVerticalView extends RelativeLayout {

    Context context;
    TableLayout calendarMonthLayout;
    TableRow[] calendarMonthRow;
    LinearLayout[][] calendarMonthColumn;
    TextView[][] calendarDayNumberTextView;

    public CalendarVerticalView(Context context) {
        super(context);
        this.context = context;
        setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setId(R.id.calendarview_id);

        calendarMonthLayout = tableLayoutCalendarMonthUI(context);
        calendarMonthLayout.setPadding(
                getResources().getDimensionPixelSize(R.dimen.cardview_sidepadding),
                0,
                getResources().getDimensionPixelSize(R.dimen.cardview_sidepadding),
                0
        );
        addView(calendarMonthLayout);

    }

    public void setCalendar(int positionMonth) {

        Calendar mCalendar =
                CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(positionMonth);

        /** Clear TextView **/
        for (TextView[] i : calendarDayNumberTextView) {
            for (TextView j : i) {
                j.setText("");
            }
        }

        Log.d("remove row", calendarMonthRow.length+", "+CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(mCalendar));
        //빈 줄 삭제
        if(calendarMonthRow.length != CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(mCalendar)) {
            calendarMonthRow[calendarMonthRow.length-1].setVisibility(GONE);
        } else {
            calendarMonthRow[calendarMonthRow.length-1].setVisibility(VISIBLE);
        }

        /** Set Day TextView **/
        for (int total_day = 1; total_day < CalendarUtils.GET_NUMBER_DAY_OF_MONTH(mCalendar); ) {
            int total_row = CalendarUtils.GET_NUMBER_WEEK_OF_MONTH(mCalendar);

            for (int current_row = 0; current_row < total_row; current_row++) {
                for (int column = 0; column < calendarDayNumberTextView[current_row].length; column++) {
                    if (current_row == 0) {
                        int fdoim = CalendarUtils.GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(mCalendar);
                        if (fdoim + column < calendarDayNumberTextView[current_row].length) {
                            if(total_day == 1) {
                                calendarDayNumberTextView[current_row][column + fdoim]
                                        .setText((mCalendar.get(Calendar.MONTH)+1)
                                                +"/"+String.valueOf(total_day).toString());
                                calendarDayNumberTextView[current_row][column + fdoim]
                                        .setTextSize(
                                                TypedValue.COMPLEX_UNIT_PX,
                                                getResources().getDimension(R.dimen.calendarmonthcolumn_textsize)-2
                                        );
                            } else {
                                calendarDayNumberTextView[current_row][column + fdoim]
                                        .setText(String.valueOf(total_day).toString());
                            }
                            total_day++;
                        } else {
                            break;
                        }

                    } else {
                        calendarDayNumberTextView[current_row][column]
                                .setText(String.valueOf(total_day).toString());
                        if (total_day < CalendarUtils.GET_NUMBER_DAY_OF_MONTH(mCalendar)) {
                            total_day++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

    }

    /**
     * TableLayout Month Calendar Month UI
     **/
    public TableLayout tableLayoutCalendarMonthUI(Context context) {

        calendarMonthLayout = new TableLayout(context);
        calendarMonthLayout.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT));
//        calendarMonthLayout.setStretchAllColumns(true);

        calendarMonthRow = new TableRow[6]; // monthview row : 6
        calendarMonthColumn = new LinearLayout[6][7];
        calendarDayNumberTextView = new TextView[6][7]; // monthview day : 42  x:6 y:7

        for (int row = 0; row < 6; row++) {

            calendarMonthRow[row] = new TableRow(context);

            for (int day = 0; day < 7; day++) {

                calendarMonthColumn[row][day] = new LinearLayout(context);
                calendarMonthColumn[row][day].setLayoutParams(
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));

                calendarMonthColumn[row][day].setOrientation(LinearLayout.VERTICAL);
                calendarMonthColumn[row][day].setMinimumHeight(
                        context.getResources().getDimensionPixelSize(R.dimen.calendarmonthcolumn_minheight)
                );

                //in Text init
                calendarDayNumberTextView[row][day] = new TextView(context);
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                        getResources().getDimensionPixelSize(R.dimen.week_columnsize),
                        getResources().getDimensionPixelSize(R.dimen.week_columnsize)
                );
                mLayoutParams.setMargins(
                        0,
                        getResources().getDimensionPixelSize(R.dimen.size_3dp),
                        0,
                        getResources().getDimensionPixelSize(R.dimen.size_3dp));
                calendarDayNumberTextView[row][day].setLayoutParams(mLayoutParams);
                calendarDayNumberTextView[row][day].setGravity(CENTER);
                calendarDayNumberTextView[row][day].setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.calendarmonthcolumn_textsize)
                );
                if (Build.VERSION.SDK_INT >= 23) {
                    calendarDayNumberTextView[row][day].setTextColor(
                            context.getColor(R.color.fontColorPrimary)
                    );
                } else {
                    calendarDayNumberTextView[row][day].setTextColor(
                            context.getResources().getColor(R.color.fontColorPrimary)
                    );
                }

                calendarMonthColumn[row][day].addView(calendarDayNumberTextView[row][day]);
                calendarMonthRow[row].addView(calendarMonthColumn[row][day]);

            }

            calendarMonthRow[row].setBackgroundResource(R.drawable.calendar_column_shape);
            calendarMonthLayout.addView(calendarMonthRow[row]);
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
                (int) context.getResources().getDimension(R.dimen.week_padding),
                0,
                (int) context.getResources().getDimension(R.dimen.week_padding),
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
            layoutParams.setMargins((int) context.getResources().getDimension(R.dimen.week_left_padding),
                    0,
                    0,
                    (int) context.getResources().getDimension(R.dimen.week_left_padding));
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
