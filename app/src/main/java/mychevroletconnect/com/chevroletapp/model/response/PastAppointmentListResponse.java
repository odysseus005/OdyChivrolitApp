package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.PastAppointment;


/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class PastAppointmentListResponse {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }




    public List<PastAppointment> getData() {
        return data;
    }

    public void setData(List<PastAppointment> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<PastAppointment> data;


}
