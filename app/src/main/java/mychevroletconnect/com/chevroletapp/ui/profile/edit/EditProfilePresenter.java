package mychevroletconnect.com.chevroletapp.ui.profile.edit;

import android.util.Log;
import android.util.Patterns;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;


import java.io.File;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.LoginResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfilePresenter extends MvpNullObjectBasePresenter<EditProfileView> {

    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    private Realm realm;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = App.getUser();
    }


    public void updateUser(String userId,
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

        if (firstName.equals("") || lastName.equals("") || birthday.equals("") ||
                contact.equals("") || middleName.equals("") ||  address.equals("")) {
            getView().showAlert("Fill-up all fields");
        } else if (!Patterns.PHONE.matcher(contact).matches()) { // check if mobile number is valid
            getView().showAlert("Invalid mobile number.");
        }
        else {
            getView().startLoading();
            App.getInstance().getApiInterface().updateUser(Endpoints.UPDATEUSER,userId, firstName, middleName, lastName, birthday, contact, address, citizenship, occupation, gender, civil)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                try {
                                    switch (response.body().getResult()) {
                                        case Constants.SUCCESS:
                                            final Realm realm = Realm.getDefaultInstance();
                                            realm.executeTransactionAsync(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {

                                                    user = response.body().getUser();
                                                    Log.e(">>>>>", "sasa"+user.getFullName());
                                                    realm.copyToRealmOrUpdate(user);


                                                }
                                            }, new Realm.Transaction.OnSuccess() {
                                                @Override
                                                public void onSuccess() {
                                                    realm.close();
                                                   getView().finishAct();
                                                }
                                            }, new Realm.Transaction.OnError() {
                                                @Override
                                                public void onError(Throwable error) {
                                                    realm.close();
                                                    Log.e(TAG, "onError: Unable to save USER", error);
                                                    getView().showAlert("Error Saving API Response");
                                                }
                                            });
                                            break;
                                        case "failed":
                                            getView().showAlert("Can't Connect to the Server!");

                                            break;
                                        default:
                                            getView().showAlert(String.valueOf(R.string.cantConnect));
                                            break;

                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    getView().showAlert("Oops");
                                }
                            } else {
                                getView().showAlert("Wrong Password or Email!");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling login api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }


   public void upload(String fname, final File imageFile) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fname, requestFile);
       RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), fname);
            getView().startupLoading();
       App.getInstance().uploadImage().uploadFile(body,filename)
                .enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, final Response<ResultResponse> response) {
                        getView().stopupLoading();
                        if (response.isSuccessful()) {
                            if (response.body().getResult().equals("true")) {
                                final Realm realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        User user = realm.where(User.class).findFirst();
                                       // user.setImage(response.body().getImage());
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        getView().showAlert("Uploading Success");
                                        realm.close();
                                        //getView().finishAct();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        realm.close();
                                        error.printStackTrace();
                                        getView().showAlert(error.getLocalizedMessage());
                                    }
                                });
                            } else {
                                getView().showAlert(response.body().getResult());
                            }
                        } else {
                            getView().showAlert("Error Server Connection");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        getView().stopupLoading();
                        t.printStackTrace();
                        getView().showAlert("Error Server Connection");
                    }
                });
    }



    public void onStop() {
        realm.close();
    }


}
