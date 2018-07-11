package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Appointment extends RealmObject {

    @PrimaryKey
    @SerializedName("reserve_id")
    @Expose
    private int appointId;



    @SerializedName("dealer_id")
    @Expose
    private int appointdealerId;


    @SerializedName("dealer_name")
    @Expose
    private String appointdealerName;


    @SerializedName("dealer_address")
    @Expose
    private String appointdealerLocation;


    @SerializedName("g_id")
    @Expose
    private int appointgaragerId;

    @SerializedName("g_name")
    @Expose
    private String appointgaragerName;


    @SerializedName("g_plateNum")
    @Expose
    private String appointgaragePlate;


    @SerializedName("schedule_id")
    @Expose
    private int appointschedId;


    @SerializedName("schedule_time")
    @Expose
    private String appointschedTime;


    @SerializedName("advisor_id")
    @Expose
    private String appointsAdvisorId;


    @SerializedName("advisor_first_name")
    @Expose
    private String firstname;

    @SerializedName("advisor_last_name")
    @Expose
    private String lastname;



    @SerializedName("service_id")
    @Expose
    private String appointServicesId;

    @SerializedName("service_name")
    @Expose
    private String appointServicesName;

    @SerializedName("pms_service_id")
    @Expose
    private String appointPMSId;

    @SerializedName("mileage")
    @Expose
    private String appointPMSMil;

    @SerializedName("months")
    @Expose
    private String appointPMSMonth;

    @SerializedName("service")
    @Expose
    private String appointPMSService;


    @SerializedName("reserve_date")
    @Expose
    private String appointDate;


    @SerializedName("reserve_status")
    @Expose
    private String appointStatus;



    @SerializedName("reserve_remarks")
    @Expose
    private String appointRemarks;

    private String fullName;

    public int getAppointId() {
        return appointId;
    }

    public void setAppointId(int appointId) {
        this.appointId = appointId;
    }

    public int getAppointdealerId() {
        return appointdealerId;
    }

    public void setAppointdealerId(int appointdealerId) {
        this.appointdealerId = appointdealerId;
    }



    public int getAppointschedId() {
        return appointschedId;
    }

    public void setAppointschedId(int appointschedId) {
        this.appointschedId = appointschedId;
    }



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

    public String getAppointServicesId() {
        return appointServicesId;
    }

    public void setAppointServicesId(String appointServicesId) {
        this.appointServicesId = appointServicesId;
    }

    public String getAppointServicesName() {
        return appointServicesName;
    }

    public void setAppointServicesName(String appointServicesName) {
        this.appointServicesName = appointServicesName;
    }

    public String getAppointPMSId() {
        return appointPMSId;
    }

    public void setAppointPMSId(String appointPMSId) {
        this.appointPMSId = appointPMSId;
    }

    public String getAppointPMSMil() {
        return appointPMSMil;
    }

    public void setAppointPMSMil(String appointPMSMil) {
        this.appointPMSMil = appointPMSMil;
    }

    public String getAppointPMSMonth() {
        return appointPMSMonth;
    }

    public void setAppointPMSMonth(String appointPMSMonth) {
        this.appointPMSMonth = appointPMSMonth;
    }

    public String getAppointPMSService() {
        return appointPMSService;
    }

    public void setAppointPMSService(String appointPMSService) {
        this.appointPMSService = appointPMSService;
    }

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public String getAppointStatus() {
        return appointStatus;
    }

    public void setAppointStatus(String appointStatus) {
        this.appointStatus = appointStatus;
    }

    public String getAppointRemarks() {
        return appointRemarks;
    }

    public void setAppointRemarks(String appointRemarks) {
        this.appointRemarks = appointRemarks;
    }

    public String getAppointdealerName() {
        return appointdealerName;
    }

    public void setAppointdealerName(String appointdealerName) {
        this.appointdealerName = appointdealerName;
    }

    public String getAppointdealerLocation() {
        return appointdealerLocation;
    }

    public void setAppointdealerLocation(String appointdealerLocation) {
        this.appointdealerLocation = appointdealerLocation;
    }

    public int getAppointgaragerId() {
        return appointgaragerId;
    }

    public void setAppointgaragerId(int appointgaragerId) {
        this.appointgaragerId = appointgaragerId;
    }

    public String getAppointgaragerName() {
        return appointgaragerName;
    }

    public void setAppointgaragerName(String appointgaragerName) {
        this.appointgaragerName = appointgaragerName;
    }

    public String getAppointgaragePlate() {
        return appointgaragePlate;
    }

    public void setAppointgaragePlate(String appointgaragePlate) {
        this.appointgaragePlate = appointgaragePlate;
    }

    public String getAppointschedTime() {
        return appointschedTime;
    }

    public void setAppointschedTime(String appointschedTime) {
        this.appointschedTime = appointschedTime;
    }

    public String getAppointsAdvisorId() {
        return appointsAdvisorId;
    }

    public void setAppointsAdvisorId(String appointsAdvisorId) {
        this.appointsAdvisorId = appointsAdvisorId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAdvisorFullName() {
        return firstname+" "+lastname;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
