package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


class AppointmentViewState implements RestorableViewState<AppointmentView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<AppointmentView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(AppointmentView view, boolean retained) {

    }
}
