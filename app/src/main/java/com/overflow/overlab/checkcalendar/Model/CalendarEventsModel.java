package com.overflow.overlab.checkcalendar.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarEventsModel {

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("timeZone")
    @Expose
    private String timeZone;

    @SerializedName("nextSyncToken")
    @Expose
    private String nextSyncToken;

    @SerializedName("colorId")
    @Expose
    private String colorId;

    @SerializedName("items")
    @Expose
    private List<CalendarEventsItemsModel> items;

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    /**
     * @return The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone The timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return The nextSyncToken
     */
    public String getNextSyncToken() {
        return nextSyncToken;
    }

    /**
     * @param nextSyncToken The nextSyncToken
     */
    public void setNextSyncToken(String nextSyncToken) {
        this.nextSyncToken = nextSyncToken;
    }

    /**
     * @return The items
     */
    public List<CalendarEventsItemsModel> getItems() {
        return items;
    }

    /**
     * @param items The items
     */
    public void setItems(List<CalendarEventsItemsModel> items) {
        this.items = items;
    }



}




