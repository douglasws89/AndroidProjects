
package com.example.cmps121.slugsocial.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultList {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("event_image")
    @Expose
    private String eventImage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The eventImage
     */
    public String getEventImage() {
        return eventImage;
    }

    /**
     * 
     * @param eventImage
     *     The event_image
     */
    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 
     * @param eventId
     *     The event_id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @return
     *     The place
     */
    public String getPlace() {
        return place;
    }

    /**
     * 
     * @param place
     *     The place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return
     *     The event
     */
    public String getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
