package com.overflow.overlab.checkcalendar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarEventsItemsModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("colorId")
    @Expose
    private String colorId;
    @SerializedName("start")
    @Expose
    private CalendarEventsTimeModel start;
    @SerializedName("end")
    @Expose
    private CalendarEventsTimeModel end;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return The colorId
     */
    public String getColorId() {
        return colorId;
    }

    /**
     * @param colorId The colorId
     */
    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    /**
     * @return The start
     */
    public CalendarEventsTimeModel getStart() {
        return start;
    }

    /**
     * @param start The start
     */
    public void setStart(CalendarEventsTimeModel start) {
        this.start = start;
    }

    /**
     * @return The end
     */
    public CalendarEventsTimeModel getEnd() {
        return end;
    }

    /**
     * @param end The end
     */
    public void setEnd(CalendarEventsTimeModel end) {
        this.end = end;
    }

}
