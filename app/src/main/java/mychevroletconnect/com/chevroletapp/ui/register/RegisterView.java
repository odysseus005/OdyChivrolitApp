package mychevroletconnect.com.chevroletapp.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface RegisterView extends MvpView {

    void onSubmit();

    void onNext();

    void showAlert(String message);


    void startLoading();

    void stopLoading();

    void onRegistrationSuccess();

    void onUserRegistrationSuccess(String bussinessID);

    void onBirthdayClicked();

    void onPurchasedClicked();
}
