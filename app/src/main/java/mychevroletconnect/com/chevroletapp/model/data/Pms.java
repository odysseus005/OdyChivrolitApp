package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Pms extends RealmObject {



    @PrimaryKey
    @SerializedName("pms_id")
    @Expose
    private int pmsId;

    @SerializedName("mileage")
    @Expose
    private String pmsMileage;

    @SerializedName("month_period")
    @Expose
    private String pmsMonth;

    @SerializedName("pms_service_name")
    @Expose
    private String pmsName;


    public int getPmsId() {
        return pmsId;
    }

    public void setPmsId(int pmsId) {
        this.pmsId = pmsId;
    }

    public String getPmsMileage() {
        return pmsMileage;
    }

    public void setPmsMileage(String pmsMileage) {
        this.pmsMileage = pmsMileage;
    }

    public String getPmsMonth() {
        return pmsMonth;
    }

    public void setPmsMonth(String pmsMonth) {
        this.pmsMonth = pmsMonth;
    }

    public String getPmsName() {
        return pmsName;
    }

    public void setPmsName(String pmsName) {
        this.pmsName = pmsName;
    }



}
