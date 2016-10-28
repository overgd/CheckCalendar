package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 10/28/2016.
 */

public class CalendarMonthRowView extends LinearLayout {

    CalendarDayTextView[] textView;
    Context context;

    public CalendarMonthRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CalendarMonthRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CalendarMonthRowView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    private void init() {
        LayoutInflater li = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.calendar_month_row, this, true);

        this.textView = new CalendarDayTextView[7];
        for(int i = 0; i < textView.length; i++) {
            String textId = "calendarmonthday_"+i;
            int resId = getResources().getIdentifier(textId, "id", context.getPackageName());
            textView[i] = (CalendarDayTextView) view.findViewById(resId);
        }

    }

    public CalendarDayTextView[] getTextView() {
        return textView;
    }

    public void setTextView(CalendarDayTextView[] textView) {
        this.textView = textView;
    }
}
