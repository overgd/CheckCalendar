package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

import java.util.Calendar;

/**
 * Created by over on 10/28/2016.
 */

public class CalendarDayTextView extends TextView {

    Context context;
    Calendar calendar;

    public CalendarDayTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CalendarDayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CalendarDayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private void init() {
        setId(generateViewId());

        setLayoutParams(new LinearLayout.LayoutParams(
                0,
                getResources().getDimensionPixelSize(R.dimen.calendarmonth_column_minheight),
                1f));
        setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.calendarmonth_column_fontsize)
        );
        setText("num");
        if (Build.VERSION.SDK_INT >= 23) {
            setTextColor(context.getColor(R.color.fontColorPrimary));
        } else {
            setTextColor(getResources().getColor(R.color.fontColorPrimary));
        }
        setPadding(
                getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding),
                getResources().getDimensionPixelSize(R.dimen.calendar_text_topbottompadding),
                getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding),
                0
        );
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {

        this.calendar = calendar;

        setText(String.valueOf(calendar.get(Calendar.DATE)));

        //Today!
//        if(calendar.get(Calendar.YEAR) == CalendarUtils.getNowCalendar().get(Calendar.YEAR)
//                && calendar.get(Calendar.MONTH) == CalendarUtils.getNowCalendar().get(Calendar.MONTH)
//                && calendar.get(Calendar.DATE) == CalendarUtils.getNowCalendar().get(Calendar.DATE)) {
//            setBackgroundResource(R.drawable.calendar_active_number_wrapped);
//            setTextColor(getResources().getColor(R.color.colorWhite));
//        }
    }

    public void setDayText(String day) {
        setText(day);
    }

}
