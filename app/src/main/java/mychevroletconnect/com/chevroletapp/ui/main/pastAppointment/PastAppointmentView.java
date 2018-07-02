package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import com.hannesdorfmann.mosby.mvp.MvpView;

import mychevroletconnect.com.chevroletapp.model.data.Appointment;


public interface PastAppointmentView extends MvpView {




    void setAppointmentList();


    void showAppointmentDetails(Appointment appoint);

    void stopRefresh();

    void showError(String message);

    void showReturn(String message);




}
