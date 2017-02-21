package com.overflow.overlab.checkcalendar.Check;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.overflow.overlab.checkcalendar.Goal.GoalUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by over on 10/22/2016.
 * 체크 다이얼로그
 *
 */

public class CheckDialogFragment extends DialogFragment
implements DialogInterface.OnClickListener {

    static final String CALENDAR_LONG = "calendar_long";
    static final String GOAL_ID = "goal_id";
    static final String GOAL_SUMMARY = "goal_summary";
    static final String PARENT_ID = "parent_id";

    public int parent_id;

    DialogListener dialogListener;

    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String goal_id = args.getString(GOAL_ID); // Goal id
        String goal_summary = args.getString(GOAL_SUMMARY); // Goal Summary
        String goal_description = new GoalUtils(getActivity()).getGoalDescription(goal_id); // Goal Description
        // 현재 목표의 id, 제목, 내용

        parent_id = args.getInt(PARENT_ID); // Calendar Text View ID

        Calendar calendar = Calendar.getInstance();  // 다이얼로그에 표시되는 날짜
        calendar.setTimeInMillis(args.getLong(CALENDAR_LONG));
        Log.d("calendar_long", String.valueOf(calendar.get(Calendar.DATE)));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CheckDialogView checkDialogView = new CheckDialogView(getActivity());
        checkDialogView.setGoalDateText(calendar.get(Calendar.YEAR) +"/" +
                        calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                        +"/"+calendar.get(Calendar.DATE)
        ); // Ex) 2017/Feb/12

        checkDialogView.setGoalSummaryText(goal_summary); // 목표 제목 표시

        if(! goal_description.isEmpty()) { //목표 내용이 있으면 표시, 없으면 표시 안함
            checkDialogView.goal_description_textview.setVisibility(View.VISIBLE);
            checkDialogView.setGoalDescriptionText(goal_description);
        } else {
            checkDialogView.goal_description_textview.setVisibility(View.GONE);
        }

        String checkCalendarModel = new CheckSetup(getActivity()).initCheckCalendar(calendar);

        Log.d("checkcalendarmodel", checkCalendarModel);

        builder.setView(checkDialogView)
                .setPositiveButton("OK", this)
                .setNegativeButton("CANCLE", this);

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        final int OK = -1;
        final int CANCLE = -2;

        switch (which) {
            case OK :
                dialogListener.onDialogPositiveClick(this);
                break;
            case CANCLE :
                dialogListener.onDialogNegativeClick(this);
                break;
        }

    }
}
