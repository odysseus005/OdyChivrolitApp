package mychevroletconnect.com.chevroletapp.ui.inquiries.contactus;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class ContactViewState implements RestorableViewState<ContactView> {

    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<ContactView> restoreInstanceState(Bundle in) {

        return this;
    }

    @Override
    public void apply(ContactView view, boolean retained) {

    }


}
