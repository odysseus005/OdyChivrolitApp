package mychevroletconnect.com.chevroletapp.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;


import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {
    private int login_counter = 0;
    private static final String TAG = LoginPresenter.class.getSimpleName();
    User user;

    public void login(final String email, final String password) {
        if (email.isEmpty() || email.equals("")) {
            getView().showAlert("Please enter email");
        } else if (password.isEmpty() || password.equals("")) {
            getView().showAlert("Please enter Password");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().login(Endpoints.LOGIN,email, password)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               final Response<LoginResponse> response) {
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
                                                    getView().onLoginSuccess(user);
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
                                        case Constants.NOT_EXIST:
                                            getView().showAlert("Email does not exist");
                                            break;
                                        case Constants.WRONG_PASSWORD:
                                            getView().showAlert("Wrong Password or Email");
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


    public void firstLogin(String userId) {
        getView().startLoading();
        App.getInstance().getApiInterface().updateUserCode(Endpoints.FIRSTLOGIN,userId).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals(Constants.SUCCESS)) {

                        final Realm realm = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                user = response.body().getUser();
                                realm.copyToRealmOrUpdate(user);


                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                realm.close();

                                getView().showAlert("Verification Successful!");
                                getView().onLoginSuccess(user);

                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                realm.close();
                                Log.e(TAG, "onError: Unable to save USER", error);
                                getView().showAlert("Error Saving API Response");
                            }
                        });
                    } else {
                        getView().showAlert(String.valueOf(R.string.cantConnect));
                    }
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                getView().stopLoading();
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });

    }


    public void resendLogin(String userId,String email) {
        getView().startLoading();
        App.getInstance().getApiInterface().resendUserCode(Endpoints.RESENDCODE,email,userId).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                getView().stopLoading();
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

                                getView().showAlert("Code Successfully Sent!");
                                getView().onLoginSuccess(user);

                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                realm.close();
                                Log.e(TAG, "onError: Unable to save USER", error);
                                getView().showAlert("Error Saving API Response");
                            }
                        });
                    } else {
                        getView().showAlert(String.valueOf(R.string.cantConnect));
                    }
                } else {
                    getView().showAlert(response.message() != null ? response.message()
                            : "Unknown Error");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                getView().stopLoading();
                Log.e(TAG, "onFailure: Error calling login api", t);
                getView().stopLoading();
                getView().showAlert("Error Connecting to Server");
            }
        });

    }





}
