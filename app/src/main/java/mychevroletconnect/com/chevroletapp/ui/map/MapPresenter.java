package mychevroletconnect.com.chevroletapp.ui.map;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;

import mychevroletconnect.com.chevroletapp.app.App;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.DealerContacts;
import mychevroletconnect.com.chevroletapp.model.data.NearDealer;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.model.response.DealerContactListResponse;
import mychevroletconnect.com.chevroletapp.model.response.DealerListResponse;
import mychevroletconnect.com.chevroletapp.util.DistanceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class MapPresenter extends MvpNullObjectBasePresenter<MapView> {
    private Realm realm;
    private User user;

    void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
      loadDealerList(0);


    }


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
                                        getView().updateMap();

                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        realm.close();
                                        error.printStackTrace();

                                            getView().showAlert("Loading Failed!");
                                    }
                                });
                            } else {
                                    getView().showAlert("Error Getting Data!");
                            }
                        }

                        @Override
                        public void onFailure(Call<DealerListResponse> call, Throwable t) {
                            t.printStackTrace();
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }


    public void loadContactList(int dealerID) {

        getView().startLoading();
        App.getInstance().getApiInterface().getDealerContactList(Endpoints.GET_DEALER_CONTACTS,String.valueOf(dealerID))
                .enqueue(new Callback<DealerContactListResponse>() {
                    @Override
                    public void onResponse(Call<DealerContactListResponse> call, final Response<DealerContactListResponse> response) {


                        getView().stopLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.delete(DealerContacts.class);
                                    realm.copyToRealmOrUpdate(response.body().getData());

                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().loadContacts();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    error.printStackTrace();

                                        getView().showAlert("Error to Retrieve Dealer Contacts");
                                }
                            });
                        } else {

                                getView().showAlert("Error to Retrieve Dealer Contacts");
                        }
                    }

                    @Override
                    public void onFailure(Call<DealerContactListResponse> call, Throwable t) {
                        t.printStackTrace();
                        getView().stopLoading();

                            getView().stopLoading();
                            getView().showAlert("Can't Connect to the Internet");

                    }
                });
    }

    public void onStop() {
        if(realm!=null)
        realm.close();
    }


    void getNearest(double latitude, double longitude, String filterMap) {
        final Realm realm = Realm.getDefaultInstance();
        getView().startLoading();
        List<Dealer> companys = realm.where(Dealer.class).findAll();
        if(!(filterMap.equals(""))) {
            companys = realm.where(Dealer.class)
                    .contains("dealerLocation", filterMap, Case.INSENSITIVE)
                    .findAll();

        }


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(NearDealer.class);
            }
        });
        for (Dealer company : companys) {
            float distance;
            try {
                 distance = DistanceUtil.getDistance(latitude, longitude, Double.parseDouble(company.getDealerLat()), Double.parseDouble(company.getDealerLong()));
            }catch (Exception e)
            {
               distance =  -1.0f;
            }
            final NearDealer nearest = new NearDealer();
            nearest.setDealerId(company.getDealerId());
            nearest.setDealerName(company.getDealerName());
            nearest.setDealerAddress(company.getDealerAddress());
            nearest.setDealerLocation(company.getDealerLocation());
            nearest.setDealerClosing(company.getDealerClosing());
            nearest.setDealerOpening(company.getDealerOpening());
            nearest.setDealerLat(company.getDealerLat());
            nearest.setDealerLong(company.getDealerLong());
            nearest.setDealerContact(company.getDealerContact());
            nearest.setDealerImage(company.getDealerImage());
            Double distanceKm = Double.parseDouble(String.format("%.2f", distance));
            nearest.setDistance(distanceKm);

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(nearest);
                }
            });
        }
        getView().stopLoading();
        realm.close();
    }




}
