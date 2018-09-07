package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;


import mychevroletconnect.com.chevroletapp.model.data.Holiday2;



/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class ScheduleListResponse2 {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }






    public List<Holiday2> getData2() {
        return data2;
    }

    public void setData2(List<Holiday2> data2) {
        this.data2 = data2;
    }

    @SerializedName("data")
    private List<Holiday2> data2;


}
