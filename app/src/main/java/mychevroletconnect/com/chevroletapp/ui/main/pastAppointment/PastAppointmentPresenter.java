package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.response.AppointmentListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@SuppressWarnings("ConstantConditions")
public class PastAppointmentPresenter extends MvpBasePresenter<PastAppointmentView> {

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
                        }
                        getView().stopLoading();
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
                                        getView().showError(error.getLocalizedMessage());
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
                            getView().showError(t.getLocalizedMessage());
                        }
                    }
                });
    }





    public void onStop() {
        realm.close();
    }
}
