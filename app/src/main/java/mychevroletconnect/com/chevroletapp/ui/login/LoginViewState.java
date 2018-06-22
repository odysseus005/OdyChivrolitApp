package mychevroletconnect.com.chevroletapp.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

public class LoginViewState implements RestorableViewState<LoginView> {
    private String email;
    private String password;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
    }

    @Override
    public RestorableViewState<LoginView> restoreInstanceState(Bundle in) {
        return this;
    }

    @Override
    public void apply(LoginView view, boolean retained) {
        view.setEditTextValue(email, password);
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
