package mychevroletconnect.com.chevroletapp.ui.inquiries.parts;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class PartsViewState implements RestorableViewState<PartsView> {

    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<PartsView> restoreInstanceState(Bundle in) {

        return this;
    }

    @Override
    public void apply(PartsView view, boolean retained) {

    }


}
