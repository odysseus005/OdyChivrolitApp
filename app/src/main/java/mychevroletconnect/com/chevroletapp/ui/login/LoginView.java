package mychevroletconnect.com.chevroletapp.ui.login;


import com.hannesdorfmann.mosby.mvp.MvpView;

import mychevroletconnect.com.chevroletapp.model.data.User;


public interface LoginView extends MvpView {

    void onLoginButtonClicked();


    void onRegisterButtonClicked();

    void showAlert(String message);

    void setEditTextValue(String username, String password);

    void startLoading();

    void stopLoading();

    void onLoginSuccess(User user);

    void onForgotPasswordButtonClicked();


}
