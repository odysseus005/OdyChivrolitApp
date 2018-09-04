package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DealerContacts extends RealmObject {


    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactDealerId() {
        return contactDealerId;
    }

    public void setContactDealerId(String contactDealerId) {
        this.contactDealerId = contactDealerId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactRole() {
        return contactRole;
    }

    public void setContactRole(String contactRole) {
        this.contactRole = contactRole;
    }

    @PrimaryKey
    @SerializedName("contact_id")
    @Expose
    private int contactId;

    @SerializedName("dealer_id")
    @Expose
    private String contactDealerId;

    @SerializedName("contact_number")
    @Expose
    private String contactNumber;

    @SerializedName("contact_role")
    @Expose
    private String contactRole;






}
