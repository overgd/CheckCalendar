package com.overflow.overlab.checkcalendar.Check;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    Calendar calendar; // 다이얼로그의 날짜

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

        parent_id = args.getInt(PARENT_ID); // CalendarConstraintView ID

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(args.getLong(CALENDAR_LONG));

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

        builder.setView(checkDialogView)
                .setPositiveButton("OK", this)
                .setNegativeButton("CANCLE", this);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        final int OK = -1;
        final int CANCLE = -2;

        switch (which) {
            case OK : // 체크 표시 버튼을 눌렀을 때
                new AddCheckViewAsyncTask(getActivity(), calendar).execute();
                dialogListener.onDialogPositiveClick(this);
                break;
            case CANCLE :
                dialogListener.onDialogNegativeClick(this);
                break;
        }

    }
}
