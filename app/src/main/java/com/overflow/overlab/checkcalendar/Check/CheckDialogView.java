package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 10/18/2016.
 */

public class CheckDialogView extends ConstraintLayout {

    Context context;
    public TextView goal_summary_textview;
    public TextView goal_description_textview;
    public TextView goal_date_textview;

    public CheckDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public CheckDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CheckDialogView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public void init() {
        LayoutInflater li = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.check_dialog_view, this, true);

        goal_date_textview = (TextView) view.findViewById(R.id.check_date_textview);
        goal_summary_textview = (TextView) view.findViewById(R.id.check_goal_textview);
        goal_description_textview = (TextView) view.findViewById(R.id.check_description_textview);

    }

    public void setGoalDateText(String goalDate) {
        goal_date_textview.setText(goalDate);
    }

    public void setGoalSummaryText(String goalSummary) {
        goal_summary_textview.setText(goalSummary);
    }

    public void setGoalDescriptionText(String goalDescription) {
        goal_description_textview.setText(goalDescription);
    }

}
