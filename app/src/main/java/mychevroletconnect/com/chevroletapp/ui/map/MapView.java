package mychevroletconnect.com.chevroletapp.ui.map;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.NearDealer;


public interface MapView extends MvpView {
    void showNearest();

    void setNearestCompany(List<NearDealer> companyList);

    void OnItemClicked(NearDealer companyList);

    void startLoading();

    void stopLoading();

    void showAlert(String s);

    void updateMap();
}
