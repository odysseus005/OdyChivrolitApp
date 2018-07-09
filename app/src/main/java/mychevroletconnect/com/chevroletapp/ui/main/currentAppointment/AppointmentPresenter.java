package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.response.DealerListResponse;
import mychevroletconnect.com.chevroletapp.model.response.GarageListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ServiceListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ConstantConditions")
public class AppointmentPresenter extends MvpBasePresenter<AppointmentView> {

    private Realm realm;

    public void onStart() {
        realm = Realm.getDefaultInstance();
     //   user = App.getUser();
    }

    public void loadAppointmentList(String eventID) {
//
//        App.getInstance().getApiInterface().getAttendeeList(eventID,Constants.BEARER+apiToken, Constants.APPJSON)
//                .enqueue(new Callback<AttendeeListResponse>() {
//                    @Override
//                    public void onResponse(Call<AttendeeListResponse> call, final Response<AttendeeListResponse> response) {
//                        if (isViewAttached()) {
//                            getView().stopRefresh();
//                        }
//                        if (response.isSuccessful()) {
//                            final Realm realm = Realm.getDefaultInstance();
//                            realm.executeTransactionAsync(new Realm.Transaction() {
//                                @Override
//                                public void execute(Realm realm) {
//                                    realm.delete(Attendee.class);
//                                    realm.copyToRealmOrUpdate(response.body().getData());
//
//
////                                    List<Attendee> event = response.body().getData();
////                                    Attendee prize1 = new Attendee();
////                                    prize1.setFirstName("Sasas");
////                                    prize1.setAddress("sasasas");
////                                    prize1.setBirthday("2121");
////                                    prize1.setCpNumber("0909");
////                                    prize1.setEmailAddress("Sasas");
////                                    prize1.setStatus("1");
////                                   event.add(prize1);
////                                    realm.copyToRealmOrUpdate(response.body().getData());
//
//                                }
//                            }, new Realm.Transaction.OnSuccess() {
//                                @Override
//                                public void onSuccess() {
//                                    realm.close();
//                                    getView().setAttendeeList();
//                                }
//                            }, new Realm.Transaction.OnError() {
//                                @Override
//                                public void onError(Throwable error) {
//                                    realm.close();
//                                    error.printStackTrace();
//                                    if (isViewAttached())
//                                        getView().showError(error.getLocalizedMessage());
//                                }
//                            });
//                        } else {
//                            if (isViewAttached())
//                                getView().showError(response.errorBody().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<AttendeeListResponse> call, Throwable t) {
//                        t.printStackTrace();
//                        if (isViewAttached()) {
//                            getView().stopRefresh();
//                            getView().showError(t.getLocalizedMessage());
//                        }
//                    }
//                });
    }

    public void loadGarageList(int userID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getGarageList(Endpoints.GET_GARAGE,String.valueOf(userID))
                .enqueue(new Callback<GarageListResponse>() {
                    @Override
                    public void onResponse(Call<GarageListResponse> call, final Response<GarageListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Garage.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadGarage();
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
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }


    public void loadDealerList(int userID) {

           getView().startLoading();
        App.getInstance().getApiInterface().getDealerList(Endpoints.GET_DEALER,String.valueOf(userID))
                .enqueue(new Callback<DealerListResponse>() {
                    @Override
                    public void onResponse(Call<DealerListResponse> call, final Response<DealerListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
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
                    public void onFailure(Call<DealerListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }


    public void loadServiceList(int userID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getServiceList(Endpoints.GET_SERVICE,String.valueOf(userID))
                .enqueue(new Callback<ServiceListResponse>() {
                    @Override
                    public void onResponse(Call<ServiceListResponse> call, final Response<ServiceListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
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
                                    getView().loadService();
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
                    public void onFailure(Call<ServiceListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }




    public void onStop() {
        realm.close();
    }
}
