package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.PastAppointment;


public interface PastAppointmentView extends MvpView {




    void setAppointmentList();


    void showAppointmentDetails2(PastAppointment appoint);

    void stopRefresh();

    void showError(String message);

    void showReturn(String message);

    void startLoading();

    void stopLoading();



}
