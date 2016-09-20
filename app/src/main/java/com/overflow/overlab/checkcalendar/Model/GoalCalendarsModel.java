package com.overflow.overlab.checkcalendar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by over on 9/16/2016.
 */
public class GoalCalendarsModel {

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("description")
    @Expose
    private List<GoalCalendarsDescriptionModel> description;

    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    @SerializedName("id")
    @Expose
    private String id;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<GoalCalendarsDescriptionModel> getDescription() {
        return description;
    }

    public void setDescription(List<GoalCalendarsDescriptionModel> description) {
        this.description = description;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
