package com.overflow.overlab.checkcalendar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by over on 9/16/2016.
 */
public class CalendarsModel {

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("description")
    @Expose
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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
