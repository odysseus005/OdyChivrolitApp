package mychevroletconnect.com.chevroletapp.ui.register;

import android.util.Log;
import android.util.Patterns;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;


import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    private static final String TAG = RegisterPresenter.class.getSimpleName();

    public void registerUser(String email,
                         String password,
                         String confirmPassword,
                         String firstName,
                         String middleName,
                         String lastName,
                         String birthday,
                         String contact,
                         String address,
                         String citizenship,
                         String occupation,
                         String gender,
                         String civil
                         ) {

        if (email.equals("") || password.equals("") || confirmPassword.equals("") || firstName.equals("") || lastName.equals("") || birthday.equals("") ||
                contact.equals("") || middleName.equals("") ||  address.equals("")) {
            getView().showAlert("Please Fill up All Account Information Fields");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { //check if email is valid
            getView().showAlert("Invalid email address.");
        } else if (!Patterns.PHONE.matcher(contact).matches()) { // check if mobile number is valid
            getView().showAlert("Invalid mobile number.");
        }
         else if (password.length() < 8) {
            getView().showAlert("Password must be atleast 8 characters");
        } /*else if (password.matches("[A-Za-z0-9 ]*")) {
            getView().showAlert("Password must have at least 1 numeric and special character");
        } */else if (!password.contentEquals(confirmPassword)) {
            getView().showAlert("Password does not match");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().register(Endpoints.REGISTER,email, password, firstName, middleName, lastName, birthday, contact, address, citizenship, occupation, gender, civil)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:

                                        getView().onUserRegistrationSuccess(response.body().getClient_id());
                                        break;
                                    case Constants.EMAIL_EXIST:
                                        getView().showAlert("Email already exists");
                                        break;
                                    default:
                                        getView().showAlert("Can't Register Account");
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showAlert(errorBody);
                                } catch (IOException e) {
                                    Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showAlert("Can't Connect to Server");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showAlert("No Internet Connection!");
                        }
                    });
        }

    }


    public void registerCar(String model,
                            String chasis,
                            String plate,
                            String year,
                            String dop,String cid,String carname) {

        if (model.equals("")  || plate.equals("") || year.equals("")||dop.equals("")||carname.equals("")) {
            getView().showAlert("Fill-up all fields");
        }else {

            if(chasis.equalsIgnoreCase(""))
                chasis = " ";

            getView().startLoading();
            App.getInstance().getApiInterface().registerCar(Endpoints.ADD_GARAGE,chasis,model,year,plate,dop,cid,carname)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().onRegistrationSuccess();

                                        break;
                                    case Constants.EMAIL_EXIST:
                                        getView().showAlert("Car already exists");
                                        break;
                                    default:
                                        getView().showAlert("Can't Connect to Server");
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showAlert(errorBody);
                                } catch (IOException e) {
                                    Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showAlert(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }

    }


}
