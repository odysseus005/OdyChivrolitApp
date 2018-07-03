package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


class PastAppointmentViewState implements RestorableViewState<PastAppointmentView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<PastAppointmentView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(PastAppointmentView view, boolean retained) {

    }
}
