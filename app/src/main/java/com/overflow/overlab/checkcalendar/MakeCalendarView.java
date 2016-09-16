package com.overflow.overlab.checkcalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by over on 6/30/2016.
 */
public class MakeCalendarView implements View.OnClickListener {

    private static final int NO_TOUCH = 110;
    private static final int SINGLE_TOUCH = 111;
    private static final int DOUBLE_TOUCH = 112;

    private LinearLayout calendar_main;
    private Context context;

    private TableLayout calendarMonthLayout;
    private TableRow[] calendarMonthRow;

    private LinearLayout[][] calendarMonthColumn;
    private TextView[][] calendarDayNumberTextView;

    private TextView[][] calendarDayEventTextView_1;
    private TextView[][] calendarDayEventTextView_2;
    private TextView[][] calendarDayEventTextView_3;

    private int[][] calendarMonthColumnClickCounter;

    private CheckCalendarApplication applicationClass;

    Calendar selectedCalendar;

    /**
     * Make CalendarView
     **/
    public MakeCalendarView(LinearLayout calendar_main, Context context, Calendar selectedCalendar) {

        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
        this.calendar_main = calendar_main;
        this.context = context.getApplicationContext();
        this.selectedCalendar = selectedCalendar;

        addCalendarViewInActivity(selectedCalendar);

    }

    public MakeCalendarView(LinearLayout calendar_main, Context context) {

        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
        selectedCalendar = Calendar.getInstance();
        this.calendar_main = calendar_main;
        this.context = context.getApplicationContext();

        addCalendarViewInActivity(getTodayCalendar());

    }

    /**
     * Add Month Calendar Week UI
     **/
    private LinearLayout addCalendarWeekUI(Context context) {

        LinearLayout calendarWeekLayout;
        TextView[] calendarWeekTextView;

        calendarWeekLayout = new LinearLayout(context);
        calendarWeekLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        calendarWeekLayout.setOrientation(LinearLayout.HORIZONTAL);

        calendarWeekTextView = new TextView[7]; // days of week

        for (int i = 0; i < 7; i++) {

            calendarWeekTextView[i] = new TextView(context);

            calendarWeekTextView[i].setLayoutParams(new TableLayout.LayoutParams(
                    (int) context.getResources().getDimension(R.dimen.week_leftpadding),
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
                    calendarWeekTextView[i].setTextColor(context.getColor(R.color.colorPrimaryDark));
                }
            } else {
                if (i == 0 | i == 6) {
                    calendarWeekTextView[i].setTextColor(context.getResources().getColor(R.color.colorAccent));
                } else {
                    calendarWeekTextView[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            calendarWeekLayout.addView(calendarWeekTextView[i]);

        }

        return calendarWeekLayout;
    }

    /**
     * Add Month Calendar Month UI
     **/
    private TableLayout addCalendarMonthUI(Context context, Calendar calendar) {

        calendarMonthLayout = new TableLayout(context);
        calendarMonthLayout.setLayoutParams(new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        calendarMonthLayout.setStretchAllColumns(true);

        calendarMonthRow = new TableRow[6]; // monthview row : 6
        calendarMonthColumn = new LinearLayout[6][7];
        calendarDayNumberTextView = new TextView[6][7]; // monthview day : 42  x:6 y:7

        calendarDayEventTextView_1 = new TextView[6][7]; //column event textview_1
        calendarDayEventTextView_2 = new TextView[6][7]; //column event textview_2
        calendarDayEventTextView_3 = new TextView[6][7]; //column event textview_3

        calendarMonthColumnClickCounter = new int[6][7];

        for (int row = 0; row < 6; row++) {

            calendarMonthRow[row] = new TableRow(context);
            calendarMonthRow[row].setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1f
            ));

            for (int day = 0; day < 7; day++) {

                calendarMonthColumnClickCounter[row][day] = NO_TOUCH;

                calendarMonthColumn[row][day] = (LinearLayout) View.inflate(context, R.layout.calendar_column, null);
                calendarMonthColumn[row][day].setOnClickListener(this);

                calendarDayNumberTextView[row][day] = (TextView) calendarMonthColumn[row][day].findViewById(R.id.calendar_column_day);

                calendarDayEventTextView_1[row][day] = (TextView) calendarMonthColumn[row][day].findViewById(R.id.calendar_column_event_1);
                calendarDayEventTextView_2[row][day] = (TextView) calendarMonthColumn[row][day].findViewById(R.id.calendar_column_event_2);
                calendarDayEventTextView_3[row][day] = (TextView) calendarMonthColumn[row][day].findViewById(R.id.calendar_column_event_3);

                //in Text init
                calendarDayNumberTextView[row][day].setText("");
                calendarDayEventTextView_1[row][day].setText("");
                calendarDayEventTextView_2[row][day].setText("");
                calendarDayEventTextView_3[row][day].setText("");

                calendarMonthRow[row].addView(calendarMonthColumn[row][day]);
            }
            calendarMonthRow[row].setBackgroundResource(R.drawable.calendar_column_shape);
            calendarMonthLayout.addView(calendarMonthRow[row]);
        }

        /** Set Day TextView **/
        for (int day = 1; day < GET_NUMBER_OF_DAY_IN_MONTH(calendar); ) {
            int dayrow = GET_NUMBER_OF_WEEK_IN_MONTH(calendar);
            for (int row = 0; row < dayrow; row++) {
                for (int column = 0; column < calendarDayNumberTextView[row].length; column++) {
                    if (row == 0) {
                        int fdoim = GET_FIRST_DAY_OF_WEEK_IN_MONTH(calendar);
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
                        if (day < GET_NUMBER_OF_DAY_IN_MONTH(calendar)) {
                            day++;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        selectedCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        return calendarMonthLayout;
    }


    @Override
    public void onClick(View v) {

        for (int row = 0; row < 6; row++) {
            for (int day = 0; day < 7; day++) {
                if (calendarMonthColumnClickCounter[row][day] == NO_TOUCH) {
                    singleTouchEvent(v, row, day);
                } else if (calendarMonthColumnClickCounter[row][day] == SINGLE_TOUCH) {
//                    doubleTouchEvent(v, row, day);
                } else {

                }
            }
        }

    }

    /**
     * Date Single Touch Event
     **/
    private void singleTouchEvent(View v, int row, int day) {
        if (v == calendarMonthColumn[row][day]) {

            if (calendarDayNumberTextView[row][day].getText() == "") {

            } else {
                offPositionColumn(selectedCalendar);
                selectedCalendar.set(Calendar.DATE, Integer.valueOf(String.valueOf(calendarDayNumberTextView[row][day].getText())));
                onPositionColumn(selectedCalendar);
                calendarMonthColumnClickCounter[row][day] = SINGLE_TOUCH;
            }
        }
    }

    /**
     * Date Double Touch Event
     **/
    private void doubleTouchEvent(View v, int row, int day) {
        if (v == calendarMonthColumn[row][day]) {

            if (calendarDayNumberTextView[row][day].getText() == "") {

            } else {
                calendarMonthColumnClickCounter[row][day] = DOUBLE_TOUCH;

                Intent dayIntent = new Intent(context, DayActivity.class);
                dayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dayIntent.putExtra("SelectedCalendarTimeInMillis",
                        selectedCalendar.getTimeInMillis());
                context.startActivity(dayIntent);

                calendarMonthColumnClickCounter[row][day] = SINGLE_TOUCH;
            }
        }
    }

    /**
     * Get Current Calendar
     **/
    private Calendar getTodayCalendar() {

        Calendar todayCalendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        todayCalendar.setTime(date);

        return todayCalendar;

    }

    /**
     * 1월 = 0 ~ 12월 = 11
     **/
    private int GET_MONTH(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 월 일 갯수
     **/
    private int GET_NUMBER_OF_DAY_IN_MONTH(Calendar calendar) {  //월 일 갯수
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 월 주 갯수
     **/
    private int GET_NUMBER_OF_WEEK_IN_MONTH(Calendar calendar) { //월 주 갯수
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 일요일 = 0 ~ 토요일 = 6
     **/
    private int GET_FIRST_DAY_OF_WEEK_IN_MONTH(Calendar calendar) {

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());
        mCalendar.set(Calendar.DATE, 1);
        return mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 지정된 Calendar의 row, day값
     * position[0] = y (row)
     * position[1] = x (day)
     **/
    private int[] GET_DAY_POSITION_IN_MONTH(Calendar calendar) {

        int position[] = new int[2];

        position[0] = calendar.get(Calendar.WEEK_OF_MONTH) - 1;
        position[1] = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        return position;
    }

    /**
     * Turn On Position
     **/
    public void onPositionColumn(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());

        calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                .setBackgroundResource(R.drawable.calendar_acitive_number);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                    [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                    .setTextColor(context.getColor(R.color.colorWhite));
        } else {
            calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                    [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                    .setTextColor(context.getResources().getColor(R.color.colorWhite));
        }

    }

    /**
     * Turn Off Position
     **/
    public void offPositionColumn(Calendar calendar) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());

        calendarMonthColumnClickCounter[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]] = NO_TOUCH;

        calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                .setBackgroundResource(R.drawable.empty);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                    [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                    .setTextColor(context.getColor(R.color.fontColorPrimary));
        } else {

            calendarDayNumberTextView[GET_DAY_POSITION_IN_MONTH(mCalendar)[0]]
                    [GET_DAY_POSITION_IN_MONTH(mCalendar)[1]]
                    .setTextColor(context.getResources().getColor(R.color.fontColorPrimary));
        }

    }

    /**
     * Calendar View Add
     **/
    public void addCalendarViewInActivity(Calendar calendar) {

        calendar_main.addView(addCalendarWeekUI(context));
        calendar_main.addView(addCalendarMonthUI(context, calendar));

        if (calendar.get(Calendar.DATE) <= GET_NUMBER_OF_DAY_IN_MONTH(calendar)) {
            onPositionColumn(calendar);
        }

        if (applicationClass.isDeviceOnline()) {
            initCalendarEvent(calendar);
        }


    }

    /**
     * Init Calendar Event
     **/
    public void initCalendarEvent(Calendar calendar) {

        List<TextView[][]> eventViewList = new ArrayList<TextView[][]>();
        eventViewList.add(calendarDayEventTextView_1);
        eventViewList.add(calendarDayEventTextView_2);
        eventViewList.add(calendarDayEventTextView_3);

        new GoogleCalendarMakeRequestTask(context, eventViewList).execute(calendar);

    }

}
