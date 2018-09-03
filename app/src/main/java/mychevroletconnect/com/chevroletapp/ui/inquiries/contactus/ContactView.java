package mychevroletconnect.com.chevroletapp.ui.inquiries.contactus;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface ContactView extends MvpView {

    void onSubmit();

    void showAlert(String message);
    void showReturn(String message);

    void startLoading();

    void stopLoading();


}
