package mychevroletconnect.com.chevroletapp.ui.garage;

import com.hannesdorfmann.mosby.mvp.MvpView;

import mychevroletconnect.com.chevroletapp.model.data.Garage;


public interface GarageListView extends MvpView {








    void setGarageList();

    void showGarageListDetails(Garage garage);

    void stopRefresh();

    void showError(String message);




}
