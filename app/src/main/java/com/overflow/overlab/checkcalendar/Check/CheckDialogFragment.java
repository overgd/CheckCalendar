package com.overflow.overlab.checkcalendar.Check;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.overflow.overlab.checkcalendar.Goal.GoalUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by over on 10/22/2016.
 */

public class CheckDialogFragment extends DialogFragment
implements DialogInterface.OnClickListener{

    static final String CALENDAR_LONG = "calendar_long";
    static final String GOAL_ID = "goal_id";
    static final String GOAL_SUMMARY = "goal_summary";
    Intent intent;

    public interface DialogListener {

    }

    DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String goal_id = args.getString(GOAL_ID);
        String goal_summary = args.getString(GOAL_SUMMARY);
        String goal_description = GoalUtils.getGoalDescription(goal_id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(args.getLong(CALENDAR_LONG));
        Log.d("calendar_long", String.valueOf(calendar.get(Calendar.DATE)));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CheckView checkView = new CheckView(getActivity());
        checkView.setGoalDateText(
                calendar.get(Calendar.YEAR) +"/" +
                        calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                        +"/"+ calendar.get(Calendar.DATE)
        );
        checkView.setGoalSummaryText(goal_summary);
        if(! goal_description.isEmpty()) {
            checkView.goal_description_textview.setVisibility(View.VISIBLE);
            checkView.setGoalDescriptionText(goal_description);
        } else {
            checkView.goal_description_textview.setVisibility(View.GONE);
        }

        builder.setView(checkView)
                .setPositiveButton("OK", this)
                .setNegativeButton("CANCLE", this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        final int OK = -1;
        final int CANCLE = -2;

        switch (which) {
            case OK :
                //모르겠따
                break;
            case CANCLE :
                break;
        }

    }
}
