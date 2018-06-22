package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Garage extends RealmObject {

    @PrimaryKey
    @SerializedName("g_id")
    @Expose
    private int garageId;
    @SerializedName("client_id")
    @Expose
    private int clientId;
    @SerializedName("g_chassis")
    @Expose
    private String garageChassis;


    @SerializedName("g_model")
    @Expose
    private String garageModel;

    @SerializedName("g_yearModel")
    @Expose
    private String garageYear;

    @SerializedName("g_plateNum")
    @Expose
    private String garagePlate;



    @SerializedName("g_purchaseDate")
    @Expose
    private String garagePurchase;

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getGarageChassis() {
        return garageChassis;
    }

    public void setGarageChassis(String garageChassis) {
        this.garageChassis = garageChassis;
    }

    public String getGarageModel() {
        return garageModel;
    }

    public void setGarageModel(String garageModel) {
        this.garageModel = garageModel;
    }

    public String getGarageYear() {
        return garageYear;
    }

    public void setGarageYear(String garageYear) {
        this.garageYear = garageYear;
    }

    public String getGaragePlate() {
        return garagePlate;
    }

    public void setGaragePlate(String garagePlate) {
        this.garagePlate = garagePlate;
    }

    public String getGaragePurchase() {
        return garagePurchase;
    }

    public void setGaragePurchase(String garagePurchase) {
        this.garagePurchase = garagePurchase;
    }





}
