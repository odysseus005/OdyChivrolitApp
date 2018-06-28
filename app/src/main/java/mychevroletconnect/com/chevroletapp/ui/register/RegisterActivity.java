package mychevroletconnect.com.chevroletapp.ui.register;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityRegisterBinding;
import mychevroletconnect.com.chevroletapp.ui.login.LoginActivity;


public class RegisterActivity extends MvpViewStateActivity<RegisterView, RegisterPresenter> implements RegisterView, TextWatcher {
    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;
    private String etAddress;
    private String car_id;
    private ArrayList<String> gender;
    private ArrayList<String> civil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setView(getMvpView());

        populateGenderAndCivil();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @NonNull
    @Override
    public ViewState<RegisterView> createViewState() {
        setRetainInstance(true);
        return new RegisterViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        initializeViewStateValues();

    }


    /***
     * End of MvpViewStateActivity
     ***/

    @Override
    public void onNext()
    {
        binding.regCargroup.setVisibility(View.VISIBLE);
        binding.regUsergroup.setVisibility(View.GONE);

        presenter.registerCar(binding.etCarModel.getText().toString(),
                binding.etCarChassis.getText().toString(),
                binding.etCarPlate.getText().toString(),
                binding.etCarYearModel.getText().toString(),
                binding.etCarDop.getText().toString(),
              car_id);
    }


    @Override
    public void onSubmit() {





        presenter.registerUser(
                binding.etEmail.getText().toString(),
                binding.etPassword.getText().toString(),
                binding.etRepeatPassword.getText().toString(),
                binding.etFirstName.getText().toString(),
                binding.etMiddleName.getText().toString(),
                binding.etLastName.getText().toString(),
                binding.etBirthday.getText().toString(),
                binding.etMobileNumber.getText().toString(),
                binding.etAddress.getText().toString(),
                binding.etCitizenship.getText().toString(),
                binding.etOccupation.getText().toString(),
                binding.spGender.getSelectedItem().toString(),
                binding.spCivil.getSelectedItem().toString()
               );


    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Signing up...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }


    @Override
    public void onRegistrationSuccess() {
        new AlertDialog.Builder(this)
                .setTitle("Register Successful")
                .setMessage("Go Back to Login Page Thank you!")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RegisterActivity.this.finish();
                       // Toast.makeText(UserRegisterActivity.this, "An email has been sent to your email for verification!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void onUserRegistrationSuccess(String cid) {

        binding.regCargroup.setVisibility(View.VISIBLE);
        binding.regUsergroup.setVisibility(View.GONE);

        if(!(cid==null)) {
            binding.regCargroup.setVisibility(View.VISIBLE);
            binding.regUsergroup.setVisibility(View.GONE);
            car_id = cid;
        }
        else
        {
            showAlert("Error on Adding Car!");
        }


    }




    @Override
    public void onBirthdayClicked() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.etBirthday.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }



    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        initializeViewStateValues();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }



    private void initializeViewStateValues() {
        RegisterViewState registerViewState = (RegisterViewState) getViewState();

    }


    private void populateGenderAndCivil() {



        gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item, gender);
        binding.spGender.setAdapter(arrayAdapter);


        /**
         * Triggers on click of the spinner
         */
        binding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       civil = new ArrayList<>();
       civil.add("Single");
       civil.add("Married");
       civil.add("Widowed");
       civil.add("Seperated");



        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(RegisterActivity.this, R.layout.spinner_custom_item,civil);
        binding.spCivil.setAdapter(arrayAdapter2);


        /**
         * Triggers on click of the spinner
         */
        binding.spCivil.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
