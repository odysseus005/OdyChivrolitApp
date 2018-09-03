package mychevroletconnect.com.chevroletapp.ui.inquiries.parts;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.response.CarListResponse;
import mychevroletconnect.com.chevroletapp.model.response.DealerListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PartsPresenter extends MvpNullObjectBasePresenter<PartsView> {

    private static final String TAG = PartsPresenter.class.getSimpleName();




    public void loadDealerList(int userID) {
        getView().startLoading();
        App.getInstance().getApiInterface().getDealerList(Endpoints.GET_DEALER,String.valueOf(userID))
                .enqueue(new Callback<DealerListResponse>() {
                    @Override
                    public void onResponse(Call<DealerListResponse> call, final Response<DealerListResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Dealer.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadDealer();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                        getView().showAlert("Error Loading Dealer List");
                                }
                            });
                        } else {
                                getView().showAlert("Error Loading Dealer List");
                        }
                    }

                    @Override
                    public void onFailure(Call<DealerListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        getView().showAlert("Can't Connect to Server");
                    }
                });
    }

    public void loadCarList(int userID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getCarList(Endpoints.GET_CAR,String.valueOf(userID))
                .enqueue(new Callback<CarListResponse>() {
                    @Override
                    public void onResponse(Call<CarListResponse> call, final Response<CarListResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Service.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadCar();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    getView().showAlert("Error Loading Car List");
                                }
                            });
                        } else {
                            getView().showAlert("Error Loading Car List");
                        }
                    }

                    @Override
                    public void onFailure(Call<CarListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        getView().showAlert("Can't Connect to Server");
                    }
                });
    }


    public void sendParts(
                          String contact_method,
                        String car_model,
                        String year_model,
                         String plate_num,
                         String chassis_num,
                        String dealer_name,
                        String firstName,
                        String lname,
                          String email,
                          String contact,
                         String concern) {


        getView().startLoading();
        App.getInstance().getApiInterface().partsInquire(Endpoints.PARTS, contact_method, car_model, year_model, plate_num, chassis_num, dealer_name, firstName, lname, email, contact, concern)
                .enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            switch (response.body().getResult()) {
                                case Constants.SUCCESS:
                                    getView().showReturn("");
                                    break;
                                default:
                                    getView().showAlert("Error Sending Parts Inquiries");
                                    break;
                            }
                        } else {
                            getView().showAlert("Error Sending Parts Inquiries");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultResponse> call, Throwable t) {
                        //Log.e(TAG, "onFailure: Error calling register api", t);
                        getView().stopLoading();
                        getView().showAlert("Error Connecting to Server");
                    }
                });

      }
    }
