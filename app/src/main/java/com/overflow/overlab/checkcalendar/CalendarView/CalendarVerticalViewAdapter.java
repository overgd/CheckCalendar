package com.overflow.overlab.checkcalendar.CalendarView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 2016-09-24.
 */

public class CalendarVerticalViewAdapter
        extends RecyclerView.Adapter<CalendarVerticalViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CalendarView view;

        public ViewHolder(CalendarView itemView) {
            super(itemView);
            view = itemView;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CalendarView calendarView = (CalendarView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(calendarView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CalendarView calendarView =
                (CalendarView) holder.view.findViewById(R.id.calendar_vertical_recyclerview_calendarview);
        calendarView.setCalendar(position);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 4400; //1900 ~ 2100 까지 월 갯수
    }


}