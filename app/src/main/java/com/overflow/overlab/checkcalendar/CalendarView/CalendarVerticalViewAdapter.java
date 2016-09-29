package com.overflow.overlab.checkcalendar.CalendarView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 2016-09-24.
 */

public class CalendarVerticalViewAdapter extends RecyclerView.Adapter<CalendarVerticalViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout view;
        public ViewHolder(RelativeLayout itemView) {
            super(itemView);
            view = itemView;
        }

    }

    public CalendarVerticalViewAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_vertical_cardview, parent, false);

        CardView cardView =
                (CardView) view.findViewById(R.id.calendar_vertical_recyclerview_cardview);

        CalendarVerticalView calendarView = new CalendarVerticalView(parent.getContext());
        cardView.addView(calendarView);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CardView cardView =
                (CardView) holder.view.findViewById(R.id.calendar_vertical_recyclerview_cardview);
        CalendarVerticalView calendarVerticalView =
                (CalendarVerticalView) holder.view.findViewById(R.id.calendarview_id);
        calendarVerticalView.setCalendar(position);

        TextView textView = (TextView) holder.view.findViewById(R.id.status_textview);
        textView.setText(position+"");

    }

    @Override
    public int getItemCount() {
        return 4400; //1900 ~ 2100 까지 월 갯수
    }
}
