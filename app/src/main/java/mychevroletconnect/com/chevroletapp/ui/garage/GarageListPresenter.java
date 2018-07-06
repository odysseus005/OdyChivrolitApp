package mychevroletconnect.com.chevroletapp.ui.garage;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.response.GarageListResponse;
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




}
