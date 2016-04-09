
package com.example.cmps121.slugsocial.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultMessage {

    @SerializedName("event_id")
    @Expose
    private String eventId;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("message_id")
    @Expose
    private String messageId;

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
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

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
     *     The messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 
     * @param messageId
     *     The message_id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
