package com.overflow.overlab.checkcalendar.Model;

import com.google.api.client.util.DateTime;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by over on 9/16/2016.
 */
public class GoalCalendarsDescriptionModel {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("startDate")
    @Expose
    private DateTime startDate;
    @SerializedName("endDate")
    @Expose
    private DateTime endDate;
    @SerializedName("colorId")
    @Expose
    private String colorId;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }
}
