package com.overflow.overlab.checkcalendar.Goal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.overflow.overlab.checkcalendar.MainActivity;
import com.overflow.overlab.checkcalendar.R;

/**
 * Created by over on 10/26/2016.
 */

public class GoalSheetView extends RelativeLayout {

    Context context;
    private RelativeLayout goalSheetRelativeLayout;
    private CircularImageView goalIconCircularImageView;
    private TextView goalTextView;
    private String goalId;

    public GoalSheetView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GoalSheetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GoalSheetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.goal_sheet, this, true);

        this.goalSheetRelativeLayout =
                (RelativeLayout) findViewById(R.id.goal_fab_sheet_layout);
        this.goalIconCircularImageView =
                (CircularImageView) findViewById(R.id.goal_fab_sheet_list_icon);
        this.goalTextView = (TextView) view.findViewById(R.id.goal_fab_sheet_list_text);

        goalSheetRelativeLayout.setOnClickListener((MainActivity) context);
    }

    public void setGoalIcon() {

    }

    public void setGoalText(String listText) {

        goalTextView.setText(listText);
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getGoalId() {
        return goalId;
    }

}
