package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Holiday2 extends RealmObject {



    @PrimaryKey
    @SerializedName("special_id")
    @Expose
    private int specialId;

    @SerializedName("time")
    @Expose
    private String specialTime;

    @SerializedName("limit_per_hour")
    @Expose
    private String specialLimit;

    @SerializedName("date")
    @Expose
    private String specialDate;

    @SerializedName("all_day")
    @Expose
    private String specialAllDat;


    @SerializedName("special_reserve")
    @Expose
    private String specialReserve;


    @SerializedName("status")
    @Expose
    private String specialStatus;


    public String getSpecialReserve() {
        return specialReserve;
    }

    public void setSpecialReserve(String specialReserve) {
        this.specialReserve = specialReserve;
    }

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    public String getSpecialTime() {
        return specialTime;
    }

    public void setSpecialTime(String specialTime) {
        this.specialTime = specialTime;
    }

    public String getSpecialLimit() {
        return specialLimit;
    }

    public void setSpecialLimit(String specialLimit) {
        this.specialLimit = specialLimit;
    }

    public String getSpecialDate() {
        return specialDate;
    }

    public void setSpecialDate(String specialDate) {
        this.specialDate = specialDate;
    }

    public String getSpecialAllDat() {
        return specialAllDat;
    }

    public void setSpecialAllDat(String specialAllDat) {
        this.specialAllDat = specialAllDat;
    }

    public String getSpecialStatus() {
        return specialStatus;
    }

    public void setSpecialStatus(String specialStatus) {
        this.specialStatus = specialStatus;
    }



}
