package mychevroletconnect.com.chevroletapp.ui.inquiries.testdrive;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface TestView extends MvpView {

    void onSubmit();

    void loadDealer();

    void loadCar();

    void showAlert(String message);

    void showReturn(String message);

    void startLoading();

    void stopLoading();


}
