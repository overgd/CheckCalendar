package com.overflow.overlab.checkcalendar.CalendarView;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.overflow.overlab.checkcalendar.R;

import java.util.List;

/**
 * Created by over on 2016-09-24.
 */

public class CalendarVerticalViewAdapter extends RecyclerView.Adapter<CalendarVerticalViewAdapter.ViewHolder> {

    private List mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

    public CalendarVerticalViewAdapter(List mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_vertical, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
