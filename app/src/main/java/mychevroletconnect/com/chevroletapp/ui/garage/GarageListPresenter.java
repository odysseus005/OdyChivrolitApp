package mychevroletconnect.com.chevroletapp.ui.garage;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.GarageListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



@SuppressWarnings("ConstantConditions")
public class GarageListPresenter extends MvpBasePresenter<GarageListView> {

    public void loadGarageList(int userID) {

        App.getInstance().getApiInterface().getGarageList(Endpoints.GET_GARAGE,String.valueOf(userID))
                .enqueue(new Callback<GarageListResponse>() {
                    @Override
                    public void onResponse(Call<GarageListResponse> call, final Response<GarageListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Garage.class);
                                    Log.d(">>>>>>>",response.body().getData()+"");
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().setGarageList();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    if (isViewAttached())
                                        getView().showError(error.getLocalizedMessage());
                                }
                            });
                        } else {
                            if (isViewAttached())
                                getView().showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<GarageListResponse> call, Throwable t) {
                        t.printStackTrace();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }


    public void registerCar(String model,
                            String chasis,
                            String plate,
                            String year,
                            String dop,String cid,String carname) {

        if (model.equals("")  || plate.equals("") || year.equals("")||dop.equals("")||carname.equals("")) {
            getView().showError("Fill-up all fields");
        }else {
            getView().startLoading();
            App.getInstance().getApiInterface().registerCar(Endpoints.ADD_GARAGE,chasis,model,year,plate,dop,cid,carname)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().closeDialog("Car Registration Successful!");

                                        break;
                                    case Constants.EMAIL_EXIST:
                                        getView().showError("Car already exists");
                                        break;
                                    default:
                                        getView().showError("Can't Connect to Server");
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showError(errorBody);
                                } catch (IOException e) {
                                    //Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showError(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            //Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showError("Error Connecting to Server");
                        }
                    });
        }

    }

    public void editCar(String id,String model,
                            String chasis,
                            String plate,
                            String year,
                            String dop,String carname) {

        if (model.equals("")  || plate.equals("") || year.equals("")||dop.equals("")||carname.equals("")) {
            getView().showError("Fill-up all fields");
        }else {
            getView().startLoading();
            App.getInstance().getApiInterface().editCar(Endpoints.UPDATE_GARAGE,chasis,model,year,plate,dop,id,carname)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                             getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().closeDialog("Update Successful!");

                                        break;
                                    case "no_change":
                                        getView().closeDialog("No Changes Made!");

                                        break;
                                    default:
                                        getView().showError("Can't Connect to Server");
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showError(errorBody);
                                } catch (IOException e) {
                                    //Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showError(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            //Log.e(TAG, "onFailure: Error calling register api", t);
                              getView().stopLoading();
                            getView().showError("Error Connecting to Server");
                        }
                    });
        }

    }

    public void deleteCar(String id,String cid) {


            getView().startLoading();
            App.getInstance().getApiInterface().deleteCar(Endpoints.DELETE_GARAGE,id,cid)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                             getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().showError("Delete Successful!");
                                        getView().onRefresh();
                                        break;
                                    default:
                                        getView().showError("Can't Connect to Server");
                                        break;
                                }
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showError(errorBody);
                                } catch (IOException e) {
                                    //Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showError(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultResponse> call, Throwable t) {
                            //Log.e(TAG, "onFailure: Error calling register api", t);
                              getView().stopLoading();
                            getView().showError("Error Connecting to Server");
                        }
                    });

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
                            if (response.body().getResult().equals("success")) {
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
                                        getView().showError("Uploading Success");
                                        realm.close();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        realm.close();
                                        error.printStackTrace();
                                        getView().showError(error.getLocalizedMessage());
                                    }
                                });
                            } else {
                                getView().showError(response.body().getResult());
                            }
                        } else {
                            getView().showError("Error Server Connection");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        getView().stopupLoading();
                        t.printStackTrace();
                        getView().showError("Error Server Connection");
                    }
                });
    }

}
