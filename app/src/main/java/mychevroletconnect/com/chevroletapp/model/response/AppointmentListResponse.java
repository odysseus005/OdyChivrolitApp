package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;


/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class AppointmentListResponse {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }




    public List<Appointment> getData() {
        return data;
    }

    public void setData(List<Appointment> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<Appointment> data;


}
