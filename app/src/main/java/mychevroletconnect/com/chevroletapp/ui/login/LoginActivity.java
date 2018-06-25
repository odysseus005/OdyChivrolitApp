package mychevroletconnect.com.chevroletapp.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;


import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityLoginBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogVerificationBinding;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.ui.forgot.ForgotPasswordActivity;
import mychevroletconnect.com.chevroletapp.ui.register.RegisterActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MvpViewStateActivity<LoginView, LoginPresenter>
        implements LoginView, TextWatcher {


    // UI references.
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private Realm realm;
    User user;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();



        dialog = new Dialog(LoginActivity.this);
         user = realm.where(User.class).findFirst();
        if (user != null) {
            onLoginSuccess(user);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setView(getMvpView());
//        binding.username.addTextChangedListener(this);
//        binding.password.addTextChangedListener(this);





    }



    @Override
    protected  void onResume()
    {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
    @Override
    protected void onStop() {
        super.onStop();
        //Facebook login

    }
    /***
     * Start of LoginView
     ***/

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @NonNull
    @Override
    public ViewState<LoginView> createViewState() {
        setRetainInstance(true);
        return new LoginViewState();
    }

    /***
     * End of MvpViewStateActivity
     ***/

    @Override
    public void onNewViewStateInstance() {
        saveValues();
    }

    @Override
    public void onLoginButtonClicked() {


       /* startActivity(new Intent(this, GuestActivity.class));
        finish();*/

        presenter.login(
                binding.username.getText().toString(),
                binding.password.getText().toString()
        );
    }

    @Override
    public void onRegisterButtonClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEditTextValue(String username, String password) {
        binding.username.setText(username);
        binding.password.setText(password);
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }


    @Override
    public void onLoginSuccess(final User user) {


        if(!(user.getFirstlogin().equalsIgnoreCase("APPROVED")))
        {

            dialog = new Dialog(LoginActivity.this);
            final DialogVerificationBinding dialogBinding = DataBindingUtil.inflate(
                    getLayoutInflater(),
                    R.layout.dialog_verification,
                    null,
                    false);
            dialogBinding.close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.deleteAll();
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();

                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                            realm.close();
                            Toast.makeText(LoginActivity.this, "Realm Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            dialog.setContentView(dialogBinding.getRoot());
            dialog.setCancelable(false);
            dialog.show();



        }else {


            //startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onBackPressed() {

      finish();
    }



    @Override
    public void onForgotPasswordButtonClicked() {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        saveValues();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void saveValues() {
        LoginViewState loginViewState = (LoginViewState) getViewState();
//        loginViewState.setUsername(binding.etEmail.getText().toString());
  //      loginViewState.setPassword(binding.etPassword.getText().toString());
    }


}

