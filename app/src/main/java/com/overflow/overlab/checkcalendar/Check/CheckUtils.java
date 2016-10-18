package com.overflow.overlab.checkcalendar.Check;

import android.content.Context;

import com.overflow.overlab.checkcalendar.CheckCalendarApplication;
import com.overflow.overlab.checkcalendar.Goal.GoalUtils;
import com.overflow.overlab.checkcalendar.Model.CalendarEventsItemsModel;
import com.overflow.overlab.checkcalendar.Model.GoalCalendarsDescriptionModel;

/**
 * Created by over on 10/18/2016.
 */

public class CheckUtils {

    CheckCalendarApplication applicationClass;

    Context context;

    public CheckUtils(Context context) {
        super();
        this.context = context;
        applicationClass = (CheckCalendarApplication) context.getApplicationContext();
    }

    public CalendarEventsItemsModel setCheckEventsItemsModel(String description) {

        String[] goalIdSummary = applicationClass.getCurrentGoal();

        CalendarEventsItemsModel checkEventsItemsModel = new CalendarEventsItemsModel();

        for(GoalCalendarsDescriptionModel goalCalendarsDescriptionModel :
                GoalUtils.getGoalListGCMfromFile().getDescription()) {
            if(goalCalendarsDescriptionModel.getId().equals(goalIdSummary[0])) {
                checkEventsItemsModel.setSummary(goalCalendarsDescriptionModel.getSummary());
            }
        }

        checkEventsItemsModel.setDescription(description);

        return checkEventsItemsModel;
    }
}
