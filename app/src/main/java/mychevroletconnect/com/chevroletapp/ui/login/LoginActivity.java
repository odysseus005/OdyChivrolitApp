package mychevroletconnect.com.chevroletapp.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;


import io.realm.Realm;
import mychevroletconnect.com.chevroletapp.R;

import mychevroletconnect.com.chevroletapp.databinding.ActivityLoginBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogVerificationBinding;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.ui.forgot.ForgotPasswordActivity;
import mychevroletconnect.com.chevroletapp.ui.inquiries.parts.PartsActivity;
import mychevroletconnect.com.chevroletapp.ui.inquiries.testdrive.TestActivity;
import mychevroletconnect.com.chevroletapp.ui.main.MainActivity;
import mychevroletconnect.com.chevroletapp.ui.map.MapActivity;
import mychevroletconnect.com.chevroletapp.ui.register.RegisterActivity;
import mychevroletconnect.com.chevroletapp.util.CircleTransform;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MvpViewStateActivity<LoginView, LoginPresenter>
        implements LoginView, TextWatcher,NavigationView.OnNavigationItemSelectedListener {


    // UI references.
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;
    private Realm realm;
    User user;
    Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();



        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.appBarLogin.setView(getMvpView());
        setSupportActionBar(binding.appBarLogin.toolbar);
        getSupportActionBar().setTitle("Chevrolet App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.appBarLogin.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        dialog = new Dialog(LoginActivity.this);
        user = realm.where(User.class).findFirst();
        if (user != null)
            onLoginSuccess(user);
        Menu nav_Menu =  binding.navView.getMenu();
        nav_Menu.findItem(R.id.nav_appointment).setVisible(false);
        nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        nav_Menu.findItem(R.id.nav_garage).setVisible(false);
        nav_Menu.findItem(R.id.nav_logout).setVisible(false);


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
                binding.appBarLogin.username.getText().toString(),
                binding.appBarLogin.password.getText().toString()
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
        binding.appBarLogin.username.setText(username);
        binding.appBarLogin.password.setText(password);
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

            dialogBinding.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialogBinding.etCode.getText().toString().equalsIgnoreCase(user.getCode()))
                        presenter.firstLogin(String.valueOf(user.getUserId()));
                    else
                        showAlert("Invalid Code");

                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog)
                {

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
            dialog.setCancelable(true);
            dialog.show();



        }else {


            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // startActivity(new Intent(this, GarageListActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        }else if (id == R.id.nav_dealer) {
            startActivity(new Intent(this, MapActivity.class));

        } else if (id == R.id.nav_promo) {

        } else if (id == R.id.nav_showroom) {

        } else if (id == R.id.nav_parts) {
            startActivity(new Intent(this, PartsActivity.class));
        } else if (id == R.id.nav_testdrive) {
            startActivity(new Intent(this, TestActivity.class));

        } else if (id == R.id.nav_roadside) {

        }else if (id == R.id.nav_pms) {

        }else if (id == R.id.nav_warranty) {

        }else if (id == R.id.nav_care) {

        }
        else if (id == R.id.nav_logout) {

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

