package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mychevroletconnect.com.chevroletapp.model.data.Schedule;


/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class ScheduleListResponse {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }




    public List<Schedule> getData() {
        return data;
    }

    public void setData(List<Schedule> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<Schedule> data;


}
