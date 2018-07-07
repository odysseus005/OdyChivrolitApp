package mychevroletconnect.com.chevroletapp.ui.garage;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;



class GarageViewState implements RestorableViewState<GarageListView> {
    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<GarageListView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(GarageListView view, boolean retained) {

    }
}
