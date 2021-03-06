package mychevroletconnect.com.chevroletapp.ui.inquiries.contactus;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;

import mychevroletconnect.com.chevroletapp.databinding.ActivityContactusBinding;
import mychevroletconnect.com.chevroletapp.model.data.User;


public class ContactActivity extends MvpViewStateActivity<ContactView, ContactPresenter> implements ContactView, TextWatcher {
    private ActivityContactusBinding binding;
    private ProgressDialog progressDialog;
    private ArrayList<String> contact;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contactus);
        binding.setView(getMvpView());


        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        populateContact();
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
    public ContactPresenter createPresenter() {
        return new ContactPresenter();
    }

    @NonNull
    @Override
    public ViewState<ContactView> createViewState() {
        setRetainInstance(true);
        return new ContactViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        initializeViewStateValues();

    }


    /***
     * End of MvpViewStateActivity
     ***/


    @Override
    public void onSubmit() {



        if(binding.etFirstName.getText().toString().equals("")||binding.etLastName.getText().toString().equals("")
                ||binding.spContact.getSelectedItem().toString().equals("")||binding.etEmail.getText().toString().equals("")||binding.etMobileNumber.getText().toString().equals("")
                ||binding.etRemars.getText().toString().equals(""))
        {
            showAlert("Please Fill up All Fields");
        }
        else
            presenter.sendContactis(binding.spContact.getSelectedItem().toString(),
                    binding.etFirstName.getText().toString(),binding.etLastName.getText().toString(),binding.etEmail.getText().toString(),binding.etMobileNumber.getText().toString(),
                    binding.etRemars.getText().toString());




    }

    @Override
    public void showReturn(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        binding.spContact.setSelection(0);
        binding.etFirstName.setText("");
        binding.etLastName.setText("");
        binding.etEmail.setText("");
        binding.etMobileNumber.setText("");
        binding.etRemars.setText("");

    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ContactActivity.this);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
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



    private void initializeViewStateValues() {
        ContactViewState contactViewState = (ContactViewState) getViewState();

    }


    private void populateContact() {



        user = realm.where(User.class).findFirst();

        if (user != null) {
            binding.etEmail.setText(user.getEmail());
            binding.etMobileNumber.setText(user.getContact());
            binding.etFirstName.setText(user.getFirstname());
            binding.etLastName.setText(user.getLastname());
        }

        contact = new ArrayList<>();
        contact.add("Email");
        contact.add("Telephone");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ContactActivity.this, R.layout.spinner_custom_item, contact);
        binding.spContact.setAdapter(arrayAdapter);


        /**
         * Triggers on click of the spinner
         */
        binding.spContact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



}
