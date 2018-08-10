package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class NearDealer extends RealmObject {

    @PrimaryKey
    @SerializedName("dealer_id")
    @Expose
    private int dealerId;
    @SerializedName("dealer_name")
    @Expose
    private String dealerName;
    @SerializedName("dealer_address")
    @Expose
    private String dealerAddress;
    @SerializedName("dealer_location")
    @Expose
    private String dealerLocation;
    @SerializedName("dealer_lat")
    @Expose
    private String dealerLat;
    @SerializedName("dealer_long")
    @Expose
    private String dealerLong;
    @SerializedName("dealer_contact_number")
    @Expose
    private String dealerContact;
    @SerializedName("dealer_opening")
    @Expose
    private String dealerOpening;
    @SerializedName("dealer_closing")
    @Expose
    private String dealerClosing;
    @SerializedName("profile_pic")
    @Expose
    private String dealerImage;

    private double distance;


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getDealerLocation() {
        return dealerLocation;
    }

    public void setDealerLocation(String dealerLocation) {
        this.dealerLocation = dealerLocation;
    }

    public String getDealerLat() {
        return dealerLat;
    }

    public void setDealerLat(String dealerLat) {
        this.dealerLat = dealerLat;
    }

    public String getDealerLong() {
        return dealerLong;
    }

    public void setDealerLong(String dealerLong) {
        this.dealerLong = dealerLong;
    }

    public String getDealerContact() {
        return dealerContact;
    }

    public void setDealerContact(String dealerContact) {
        this.dealerContact = dealerContact;
    }

    public String getDealerOpening() {
        return dealerOpening;
    }

    public void setDealerOpening(String dealerOpening) {
        this.dealerOpening = dealerOpening;
    }

    public String getDealerClosing() {
        return dealerClosing;
    }

    public void setDealerClosing(String dealerClosing) {
        this.dealerClosing = dealerClosing;
    }

    public String getDealerImage() {
        return dealerImage;
    }

    public void setDealerImage(String dealerImage) {
        this.dealerImage = dealerImage;
    }

}
