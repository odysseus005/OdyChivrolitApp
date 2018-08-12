package mychevroletconnect.com.chevroletapp.ui.inquiries.parts;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface PartsView extends MvpView {

    void onSubmit();

    void loadDealer();

    void loadCar();

    void showAlert(String message);


    void startLoading();

    void stopLoading();


}
