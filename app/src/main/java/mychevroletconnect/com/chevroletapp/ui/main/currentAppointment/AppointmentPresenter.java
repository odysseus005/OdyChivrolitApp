package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Constants;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Advisor;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Holiday;
import mychevroletconnect.com.chevroletapp.model.data.Holiday2;
import mychevroletconnect.com.chevroletapp.model.data.Pms;
import mychevroletconnect.com.chevroletapp.model.data.Schedule;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.response.AdvisorListResponse;
import mychevroletconnect.com.chevroletapp.model.response.AppointmentListResponse;
import mychevroletconnect.com.chevroletapp.model.response.DealerListResponse;
import mychevroletconnect.com.chevroletapp.model.response.GarageListResponse;
import mychevroletconnect.com.chevroletapp.model.response.PmsListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ResultResponse;
import mychevroletconnect.com.chevroletapp.model.response.ScheduleListResponse;
import mychevroletconnect.com.chevroletapp.model.response.ScheduleListResponse2;
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

    public void loadAppointmentList(String userID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getAppointmentList(Endpoints.GET_APPOINTMENT,String.valueOf(userID))
                .enqueue(new Callback<AppointmentListResponse>() {
                    @Override
                    public void onResponse(Call<AppointmentListResponse> call, final Response<AppointmentListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().stopLoading();
                        }

                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Appointment.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().setAppointmentList();

                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    if (isViewAttached())
                                        getView().showError("Loading Appointment Error");
                                }
                            });
                        } else {
                            if (isViewAttached())
                                getView().showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppointmentListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError("Can't Connect to the Internet");
                        }
                    }
                });
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
                                        getView().showError("Loading Garage Error");
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
                            getView().showError("Can't Connect to the Internet");
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
                                        getView().showError("Loading Dealer Error");
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
                            getView().showError("Can't Connect to the Internet");
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
                            getView().stopLoading();
                        }

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
                                        getView().showError("Loading Service Error");
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
                            getView().showError("Can't Connect to the Internet");
                        }
                    }
                });
    }


    public void loadPMSList(int userID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getPMSList(Endpoints.GET_PMS,String.valueOf(userID))
                .enqueue(new Callback<PmsListResponse>() {
                    @Override
                    public void onResponse(Call<PmsListResponse> call, final Response<PmsListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().stopLoading();
                        }

                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Pms.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    if (isViewAttached())
                                        getView().showError("Loading Services Error");
                                }
                            });
                        } else {
                            if (isViewAttached())
                                getView().showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<PmsListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError("Can't Connect to the Internet");
                        }
                    }
                });
    }


    public void loadHolidaysList(int dealerID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getHoliday(Endpoints.GET_HOLIDAYS,String.valueOf(dealerID))
                .enqueue(new Callback<ScheduleListResponse2>() {
                    @Override
                    public void onResponse(Call<ScheduleListResponse2> call, final Response<ScheduleListResponse2> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Holiday2.class);
                                    realm.copyToRealmOrUpdate(response.body().getData2());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadHolidays();
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
                    public void onFailure(Call<ScheduleListResponse2> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }

    public void loadAdvisorList(int dealerID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getAdvisorList(Endpoints.GET_ADVISOR,String.valueOf(dealerID))
                .enqueue(new Callback<AdvisorListResponse>() {
                    @Override
                    public void onResponse(Call<AdvisorListResponse> call, final Response<AdvisorListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(Advisor.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadAdvisor();
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
                    public void onFailure(Call<AdvisorListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }


    public void loadTimeslotList(int userid,String dealerid, String date) {

        getView().startLoading();
        App.getInstance().getApiInterface().getTimeslot(Endpoints.GET_TIMESLOT,String.valueOf(userid),dealerid,date)
                .enqueue(new Callback<ScheduleListResponse>() {
                    @Override
                    public void onResponse(Call<ScheduleListResponse> call, final Response<ScheduleListResponse> response) {
                        if (isViewAttached()) {
                            getView().stopRefresh();
                        }
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Log.d(">>>>>>",response.body().getChecker());
                                    if(response.body().getChecker().equals("2")) {
                                        realm.delete(Schedule.class);
                                        realm.copyToRealmOrUpdate(response.body().getData());
                                    }else
                                    {
                                        realm.delete(Holiday.class);
                                        realm.copyToRealmOrUpdate(response.body().getData2());
                                    }
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    if(response.body().getChecker().equals("2")){
                                        if(!(response.body().getData().equals(null)))
                                        getView().loadTimeslot();
                                         else
                                            getView().showError("Error to Retrieve Schedule");
                                    }else
                                    {
                                        if(!(response.body().getData2().equals(null)))
                                            getView().loadTimeslot2();
                                        else
                                            getView().showError("Error to Retrieve Schedule");
                                    }




                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();
                                    if (isViewAttached())
                                        getView().showError("Loading Schedule Error");
                                }
                            });
                        } else {
                            if (isViewAttached())
                                getView().showError(response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ScheduleListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();
                        if (isViewAttached()) {
                            getView().stopRefresh();
                            getView().showError("Can't Connect to the Internet");
                        }
                    }
                });
    }

    public void reserveSched(
                             String userID,
                            String garID,
                             String schedID,
                             String specialid,
                             String dealerID,
                             String advisorID,
                             String serviceID,
                             String pmsID,
                             String date,
                             String remark) {


            getView().startLoading();
            App.getInstance().getApiInterface().registerReservation(Endpoints.RESERVE_TIMESLOT,userID,garID,schedID,specialid,dealerID,advisorID,serviceID,pmsID,date,remark)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().showReturn("Reservation Successful!");

                                        break;
                                    case Constants.EMAIL_EXIST:
                                        getView().appointmentExist("");

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


    public void cancelReservation(String id,String remarks) {


            getView().startLoading();
            App.getInstance().getApiInterface().cancelReservation(Endpoints.CANCEL_APPOINTMENT,id,remarks)
                    .enqueue(new Callback<ResultResponse>() {
                        @Override
                        public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                switch (response.body().getResult()) {
                                    case Constants.SUCCESS:
                                        getView().closeCancel("Appointment Cancelled!");

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


    public void reSchedReservation(String id,String schedid,String specialid,String date,String gid) {


        getView().startLoading();
        App.getInstance().getApiInterface().reschedReservation(Endpoints.RESCHED_APPOINTMENT,id,schedid,specialid,date,gid)
                .enqueue(new Callback<ResultResponse>() {
                    @Override
                    public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            switch (response.body().getResult()) {
                                case Constants.SUCCESS:
                                    getView().closeDialog("Appointment Rescheduled!");

                                    break;
                                case Constants.EMAIL_EXIST:
                                    getView().appointmentExist("");

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


    Service getService(String id){
        return realm.where(Service.class)
                .equalTo("serviceId", id)
                .findFirst();
    }

    public void onStop() {
        realm.close();
    }
}
