package mychevroletconnect.com.chevroletapp.ui.profile;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;


import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfilePresenter extends MvpNullObjectBasePresenter<ProfileView> {
    private static final String TAG = ProfilePresenter.class.getSimpleName();
    User user2;
    void changePassword(String currPass, String newPass, String confirmNewPass) {
        final User user = App.getUser();

             if (newPass.equals(confirmNewPass)) {
                getView().showProgress();
                App.getInstance().getApiInterface().changePassword(Endpoints.UPDATEPASS,String.valueOf(user.getUserId()),currPass, newPass).enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, final Response<ResultResponse> response) {
                        getView().stopProgress();
                        if (response.isSuccessful()) {
                            if (response.body().getResult().equals(Constants.SUCCESS)) {


                                final Realm realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {



                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        realm.close();
                                        getView().onPasswordChanged();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        realm.close();
                                        Log.e(TAG, "onError: Unable to save USER", error);
                                        getView().showAlert("Error Saving API Response");
                                    }
                                });

                            } else if(response.body().getResult().equals("incorrect")) {
                                getView().showAlert("Wrong Old Password");
                            }else
                                getView().showAlert("Can't Connect to the Server");
                        } else {
                            getView().showAlert(response.message() != null ? response.message()
                                    : "Unknown Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        getView().stopProgress();
                        Log.e(TAG, "onFailure: Error calling login api", t);
                        getView().stopProgress();

                            getView().showAlert("Error Connecting to Server");
                    }
                });
            } else {
                getView().showAlert("New Password Mismatch");
            }

    }
}
