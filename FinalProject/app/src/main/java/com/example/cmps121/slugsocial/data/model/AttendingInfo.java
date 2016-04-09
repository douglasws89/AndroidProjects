
package com.example.cmps121.slugsocial.data.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendingInfo {

    @SerializedName("result_attending")
    @Expose
    private List<ResultAttending> resultAttending = new ArrayList<ResultAttending>();
    @SerializedName("response")
    @Expose
    private String response;

    /**
     * 
     * @return
     *     The resultAttending
     */
    public List<ResultAttending> getResultAttending() {
        return resultAttending;
    }

    /**
     * 
     * @param resultAttending
     *     The result_attending
     */
    public void setResultAttending(List<ResultAttending> resultAttending) {
        this.resultAttending = resultAttending;
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
