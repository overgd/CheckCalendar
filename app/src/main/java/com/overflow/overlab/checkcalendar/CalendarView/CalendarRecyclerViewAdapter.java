package com.overflow.overlab.checkcalendar.CalendarView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 2016-09-24.
 */

public class CalendarRecyclerViewAdapter
        extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CalendarConstraintView view;

        public ViewHolder(CalendarConstraintView itemView) {
            super(itemView);
            view = itemView;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CalendarConstraintView calendarConstraintView = (CalendarConstraintView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(calendarConstraintView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CalendarConstraintView calendarConstraintView =
                (CalendarConstraintView) holder.view.findViewById(R.id.calendar_vertical_recyclerview_calendarview);
        calendarConstraintView.setCalendar(position);

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