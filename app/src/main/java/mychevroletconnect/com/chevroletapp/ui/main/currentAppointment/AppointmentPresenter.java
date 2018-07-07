package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
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





    public void onStop() {
        realm.close();
    }
}