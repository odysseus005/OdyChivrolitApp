package mychevroletconnect.com.chevroletapp.model.response;


import com.google.gson.annotations.SerializedName;

import mychevroletconnect.com.chevroletapp.model.data.User;


public class ResultResponse {

    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }




    @SerializedName("client_id")
    private String client_id;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @SerializedName("data")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


}
