package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Advisor extends RealmObject {




    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public String getAdvisorDealerId() {
        return advisorDealerId;
    }

    public void setAdvisorDealerId(String advisorDealerId) {
        this.advisorDealerId = advisorDealerId;
    }

    @PrimaryKey
    @SerializedName("advisor_id")
    @Expose
    private int advisorId;

    @SerializedName("dealer_id")
    @Expose
    private String advisorDealerId;

    @SerializedName("advisor_first_name")
    @Expose
    private String firstname;

    @SerializedName("advisor_last_name")
    @Expose
    private String lastname;




    private String fullName;


    public String getFullName() {
        return firstname+" "+lastname;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
