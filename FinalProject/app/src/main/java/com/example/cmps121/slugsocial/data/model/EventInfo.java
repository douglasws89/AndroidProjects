
package com.example.cmps121.slugsocial.data.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventInfo {

    @SerializedName("result_list")
    @Expose
    private List<ResultList> resultList = new ArrayList<ResultList>();
    @SerializedName("response")
    @Expose
    private String response;

    /**
     * 
     * @return
     *     The resultList
     */
    public List<ResultList> getResultList() {
        return resultList;
    }

    /**
     * 
     * @param resultList
     *     The result_list
     */
    public void setResultList(List<ResultList> resultList) {
        this.resultList = resultList;
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
