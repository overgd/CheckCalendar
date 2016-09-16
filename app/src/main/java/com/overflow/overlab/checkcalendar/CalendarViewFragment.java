package com.overflow.overlab.checkcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Calendar;

/**
 * Created by over on 7/1/2016.
 */
public class CalendarViewFragment extends Fragment {

    public static final String CALENDAR_TIMEINMILLIS = "TimeInMillis";

    LinearLayout calendar_main;
    View rootView;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.

        rootView = inflater.inflate(
                R.layout.calendar_main, container, false);

        calendar_main = (LinearLayout) rootView.findViewById(R.id.calendar_main);

        args = getArguments();

        new MakeCalendarView(calendar_main, getContext()) {

            @Override
            public void addCalendarViewInActivity(Calendar calendar) {

                calendar.setTimeInMillis(args.getLong("TimeInMillis"));

                super.addCalendarViewInActivity(calendar);
            }

        };

        return calendar_main;
    }


}
