package com.overflow.overlab.checkcalendar.CalendarView;

import android.support.v7.widget.CardView;
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

        public CardView view;

        public ViewHolder(CardView itemView) {
            super(itemView);
            view = itemView;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_cardview, parent, false);
//        ConstraintLayout monthLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.calendar_month, null);
//        view.addView(monthLayout);
        view.addView(new CalendarVerticalView(parent.getContext()));
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CalendarVerticalView calendarVerticalView =
                (CalendarVerticalView) holder.view.findViewById(R.id.calendarview_id);
        calendarVerticalView.setCalendar(position);
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
