package mychevroletconnect.com.chevroletapp.ui.profile.edit;

import com.hannesdorfmann.mosby.mvp.MvpView;


public interface EditProfileView extends MvpView{


    void showAlert(String message);

    void onEdit();

    void startLoading();

    void stopLoading();


    void finishAct();

    void onBirthdayClicked();

    void finish();

    void startupLoading();

    void stopupLoading();

}
