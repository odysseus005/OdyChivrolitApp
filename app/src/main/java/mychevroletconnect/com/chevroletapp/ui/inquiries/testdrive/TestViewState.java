package mychevroletconnect.com.chevroletapp.ui.inquiries.testdrive;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;


public class TestViewState implements RestorableViewState<TestView> {

    @Override
    public void saveInstanceState(@NonNull Bundle out) {

    }

    @Override
    public RestorableViewState<TestView> restoreInstanceState(Bundle in) {

        return this;
    }

    @Override
    public void apply(TestView view, boolean retained) {

    }


}
