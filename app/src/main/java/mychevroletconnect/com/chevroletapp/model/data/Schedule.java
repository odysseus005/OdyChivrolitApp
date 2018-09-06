package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Schedule extends RealmObject {



    @PrimaryKey
    @SerializedName("schedule_id")
    @Expose
    private int scheduleId;

    @SerializedName("schedule_time")
    @Expose
    private String scheduleTime;

    @SerializedName("schedule_limit")
    @Expose
    private String scheduleLimit;

    @SerializedName("schedule_reserve")
    @Expose
    private String scheduleReserve;


    @SerializedName("schedule_status")
    @Expose
    private String scheduleStatus;


    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleLimit() {
        return scheduleLimit;
    }

    public void setScheduleLimit(String scheduleLimit) {
        this.scheduleLimit = scheduleLimit;
    }

    public String getScheduleReserve() {
        return scheduleReserve;
    }

    public void setScheduleReserve(String scheduleReserve) {
        this.scheduleReserve = scheduleReserve;
    }


}
