package mychevroletconnect.com.chevroletapp.model.response;


import com.google.gson.annotations.SerializedName;

import mychevroletconnect.com.chevroletapp.model.data.User;


public class LoginResponse {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }



    public void setUser(User user) {
        this.user = user;
    }


    private String result;

    public String getResult() {
        return result;
    }


    public void setResult(String result) {
        this.result = result;
    }



}
