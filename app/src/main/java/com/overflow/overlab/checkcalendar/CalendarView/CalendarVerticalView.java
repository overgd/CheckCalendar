package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.Check.CheckView;
import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 2016-09-23.
 */

public class CalendarVerticalView extends RelativeLayout
        implements View.OnClickListener, View.OnTouchListener {

    Context context;
    TableLayout calendarMonthLayout;
    TableRow[] calendarMonthRow;
    LinearLayout[][] calendarMonthColumn;
    TextView[][] calendarDayNumberTextView;
    CheckView[][] checkView;

    public CalendarVerticalView(Context context) {
        super(context);
        this.context = context;
        setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        setId(R.id.calendarview_id);

        checkView = new CheckView[6][7];

        calendarMonthLayout = tableLayoutCalendarMonthUI(context);
        addView(calendarMonthLayout);
    }

    public void setCalendar(int positionMonth) {

        Calendar mCalendar =
                CalendarUtils.CONVERT_MONTH_POSITION_NUMBER_TO_CALENDAR(positionMonth);

        /** Clear View **/
        for(int x = 0; x < 6; x++) {
            for(int y = 0; y < 7; y++) {
                calendarDayNumberTextView[x][y].setText("");
                calendarMonthColumn[x][y].setOnClickListener(null);
                removeView(checkView[x][y]);
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
        int total_day = 1;
        int first_day_num = CalendarUtils.GET_NUMBER_FIRST_DAY_OF_WEEK_IN_MONTH(mCalendar);
        int day_of_month = CalendarUtils.GET_NUMBER_DAY_OF_MONTH(mCalendar);

        for(int row = 0; row < 6; row++) {
            for(int col = 0; col < 7; col++) {
                if(row == 0) {
                    if(col + first_day_num < 7) {
                        if(total_day == 1)  {
                            calendarDayNumberTextView[row][col + first_day_num].setText(
                                    (mCalendar.get(Calendar.MONTH)+1)
                                                +"/"+String.valueOf(total_day));
                        } else {
                            calendarDayNumberTextView[row][col + first_day_num].setText(String.valueOf(total_day));
                        }
                        calendarMonthColumn[row][col + first_day_num].setOnClickListener(this);
                        total_day++;
                    }
                } else {
                    if(total_day <= day_of_month) {
                        calendarDayNumberTextView[row][col].setText(String.valueOf(total_day));
                        calendarMonthColumn[row][col].setOnClickListener(this);
                        total_day++;
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
        calendarMonthLayout.setPadding(
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0,
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0);

        calendarMonthRow = new TableRow[6]; // monthview row : 6
        calendarMonthColumn = new LinearLayout[6][7];
        calendarDayNumberTextView = new TextView[6][7]; // monthview day : 42  x:6 y:7

        for (int row = 0; row < 6; row++) {

            calendarMonthRow[row] = new TableRow(context);

            for (int day = 0; day < 7; day++) {

                calendarMonthColumn[row][day] = new LinearLayout(context);

                calendarMonthColumn[row][day].setLayoutParams(
                        new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.MATCH_PARENT,
                                1f
                        ));

                calendarMonthColumn[row][day].setOrientation(LinearLayout.VERTICAL);
                calendarMonthColumn[row][day].setMinimumHeight(
                        context.getResources().getDimensionPixelSize(R.dimen.calendarmonthcolumn_minheight)
                );

                //in Text init
                calendarDayNumberTextView[row][day] = new TextView(context);
                calendarDayNumberTextView[row][day].setLayoutParams(
                        new ViewGroup.LayoutParams(
                                (int) context.getResources().getDimension(R.dimen.monthnum_size),
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
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
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0,
                (int) context.getResources().getDimension(R.dimen.cardview_sidepadding),
                0);

        calendarWeekTextView = new TextView[7]; // days of week

        for (int i = 0; i < 7; i++) {

            calendarWeekTextView[i] = new TextView(context);

            calendarWeekTextView[i].setLayoutParams(new TableLayout.LayoutParams(
                    (int) context.getResources().getDimension(R.dimen.monthnum_size),
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1f
            ));

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

    @Override
    public void onClick(View v) {
        int indexX = 0;
        int indexY = 0;
        for(LinearLayout[] columnX : calendarMonthColumn) {
            for(LinearLayout column : columnX) {
                if(v == column) {
                    int[] viewPostion = new int[2];
                    int[] tablePosition = new int[2];

                    getLocationInWindow(tablePosition);
                    v.getLocationInWindow(viewPostion);

                    if(checkView[indexX][indexY] == null) {
                        checkView[indexX][indexY] = new CheckView(context);
                    }

                    checkView[indexX][indexY].setX(viewPostion[0]-tablePosition[0]);
                    checkView[indexX][indexY].setY(viewPostion[1]-tablePosition[1]);

                    removeView(checkView[indexX][indexY]);
                    addView(checkView[indexX][indexY]);
                }
                indexY++;
            }
            indexX++;
            indexY = 0;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;

    }
}
