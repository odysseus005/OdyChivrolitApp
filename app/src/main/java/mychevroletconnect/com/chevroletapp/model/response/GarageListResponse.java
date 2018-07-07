package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.model.data.Garage;


/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class GarageListResponse {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }




    public List<Garage> getData() {
        return data;
    }

    public void setData(List<Garage> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<Garage> data;


}
