package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
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

    private void init() {
        setMinHeight((int) getResources().getDimension(R.dimen.calendarmonth_column_minheight));
        setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.calendarmonth_column_textsize)
        );
        setText("num");
        if (Build.VERSION.SDK_INT >= 23) {
            setTextColor(context.getColor(R.color.fontColorPrimary));
        } else {
            setTextColor(context.getResources().getColor(R.color.fontColorPrimary));
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

}
