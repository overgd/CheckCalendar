package com.overflow.overlab.checkcalendar.CalendarView;

import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 11/4/2016.
 */

public class CalendarTodayTextView extends TextView {

    Context context;

    public CalendarTodayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CalendarTodayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CalendarTodayTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void init() {

        setId(generateViewId());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(layoutParams);
        setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.calendar_today_fontsize)
        );
        setText(getResources().getText(R.string.today));

        if (Build.VERSION.SDK_INT >= 23) {
            setTextColor(context.getColor(R.color.colorWhite));
            setBackgroundColor(context.getColor(R.color.colorPrimary));
        } else {
            setTextColor(getResources().getColor(R.color.colorWhite));
            setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        setPadding(
                getResources().getDimensionPixelSize(R.dimen.calendar_text_startendpadding)
                ,0,0,0
        );
    }
}
