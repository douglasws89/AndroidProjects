
package com.example.cmps121.slugsocial.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MessageInfo {

    @SerializedName("result_messages")
    @Expose
    private List<ResultMessage> resultMessages = new ArrayList<ResultMessage>();
    @SerializedName("response")
    @Expose
    private String response;

    /**
     * 
     * @return
     *     The resultMessages
     */
    public List<ResultMessage> getResultMessages() {
        return resultMessages;
    }

    /**
     * 
     * @param resultMessages
     *     The result_messages
     */
    public void setResultMessages(List<ResultMessage> resultMessages) {
        this.resultMessages = resultMessages;
    }

    /**
     * 
     * @return
     *     The response
     */
    public String getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(String response) {
        this.response = response;
    }

}
