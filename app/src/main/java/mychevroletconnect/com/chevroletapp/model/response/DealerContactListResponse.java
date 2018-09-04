package mychevroletconnect.com.chevroletapp.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.DealerContacts;

/**
 * @author mudeleon
 * @since 06/04/2018
 */

public class DealerContactListResponse {


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }


    public List<DealerContacts> getData() {
        return data;
    }

    public void setData(List<DealerContacts> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<DealerContacts> data;


}
