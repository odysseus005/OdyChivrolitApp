package mychevroletconnect.com.chevroletapp.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class Car extends RealmObject {

    @PrimaryKey
    @SerializedName("car_id")
    @Expose
    private int carId;
    @SerializedName("car_model")
    @Expose
    private String carModel;
    @SerializedName("car_model_year")
    @Expose
    private String carModelYear;
    @SerializedName("car_description")
    @Expose
    private String carDescription;
    @SerializedName("car_images")
    @Expose
    private String carImages;


    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarModelYear() {
        return carModelYear;
    }

    public void setCarModelYear(String carModelYear) {
        this.carModelYear = carModelYear;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    public String getCarImages() {
        return carImages;
    }

    public void setCarImages(String carImages) {
        this.carImages = carImages;
    }
}
