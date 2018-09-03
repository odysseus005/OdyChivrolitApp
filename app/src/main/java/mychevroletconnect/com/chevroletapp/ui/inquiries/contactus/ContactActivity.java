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



//        String emailbody = "Name: "+ binding.etFirstName.getText().toString()+", "+binding.etLastName.getText().toString()+"\n"
//                +"Requested Car Model: "+ carSelect.getCarModel()+"\n\n"
//                +"Preferred Contact Method: "+ binding.spContact.getSelectedItem().toString()+"\n\n"
//                + "Email: "+binding.etEmail.getText().toString() +"\n"
//                + "Contact: "+binding.etMobileNumber.getText().toString()+"\n\n"
//                + "Message:\n"+binding.etRemars.getText().toString()+"\n";
//
//        //   dealer.getSelectedItem().toString();
//
//
//        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{dealerSelect.getDealerEmailAddress()});
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Concerns and Feedback");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, emailbody);
//
//
//        emailIntent.setType("message/rfc822");
//
//        try {
//            startActivity(Intent.createChooser(emailIntent,
//                    "Send email using..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this,
//                    "No email clients installed.",
//                    Toast.LENGTH_SHORT).show();
//        }


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
