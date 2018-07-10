package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.SupportMapFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityAppointmentCurrentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAddAppointmentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogChooseDealerBinding;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.data.User;


import static android.app.Activity.RESULT_OK;


public class AppointmentActivity
        extends MvpViewStateFragment<AppointmentView, AppointmentPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, AppointmentView {


    private ProgressDialog progressDialog;
    private static final String TAG = AppointmentActivity.class.getSimpleName();
    private ActivityAppointmentCurrentBinding binding;
    private Realm realm;
    private User user;
    private RealmResults<Appointment> appointmentlmResults;
    private RealmResults<Garage> garageRealmResults;
    private RealmResults<Dealer> dealerRealmResults;
    private RealmResults<Service> servicesRealmResults;
    private String searchText;
    public String id;
    private GarageAdapter garageListAdapter;
    private ServiceAdapter serviceListAdapter;
    private DealerAdapter dealerListAdapter;
    private AppointmentAdapter appointmentListAdapter;
    private DialogAddAppointmentBinding dialogBinding;
    private DialogChooseDealerBinding dealerBinding;
    private Dialog dialog,dialog2;
    private ArrayList<String> civil;
    private String selectedDealerId;

    public AppointmentActivity(){

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }




    @NonNull
    @Override
    public ViewState<AppointmentView> createViewState() {
        setRetainInstance(true);
        return new AppointmentViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_appointment_current, container, false);
        return binding.getRoot();
    }



    @Override
    public void onStart() {
        super.onStart();
        searchText = "";

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        if (user == null) {
            Log.d(TAG, "No User found");
            //  finish();
        }

        presenter.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Appointment");

        garageListAdapter = new GarageAdapter(getActivity(), getMvpView());
        dealerListAdapter = new DealerAdapter(getActivity(), getMvpView());
        serviceListAdapter = new ServiceAdapter(getActivity(),getMvpView());

        presenter.loadAppointmentList(String.valueOf(user.getUserId()));
        appointmentListAdapter = new AppointmentAdapter(getActivity(), getMvpView());
        binding.recyclerView.setAdapter(appointmentListAdapter);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        // binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });


        binding.attendeeScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                presenter.loadGarageList(user.getUserId());
                presenter.loadServiceList(user.getUserId());

            }
        });


    }

    @NonNull
    @Override
    public AppointmentPresenter createPresenter() {
        return new AppointmentPresenter();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                //prepareList();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }


    @Override
    public void onDestroy() {
        presenter.onStop();
        appointmentlmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
            presenter.loadAppointmentList(String.valueOf(user.getUserId()));
    }


    public void loadData()
    {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        appointmentlmResults = realm.where(Appointment.class).findAll();
            if (appointmentlmResults.isLoaded() && appointmentlmResults.isValid()) {
                getMvpView().setAppointmentList();
            }else
            {
                presenter.loadAppointmentList(String.valueOf(user.getUserId()));
            }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setAppointmentList(){

        appointmentlmResults = realm.where(Appointment.class).findAllAsync();
       appointmentListAdapter.setAppointmentResult(realm.copyToRealmOrUpdate(appointmentlmResults.where()
               .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        appointmentListAdapter.notifyDataSetChanged();


        if(appointmentListAdapter.getItemCount()==0)
        {
            binding.appointmentcurrentNoRecyclerview.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }



    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    @Override
    public void stopRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showReturn(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        loadData();
    }






    @Override
    public void showAppointmentDetails(final Appointment attendee) {


    }


    @Override
    public void selectDealer(Dealer dealer) {

        dialogBinding.etDealer.setText(dealer.getDealerName());
        selectedDealerId = String.valueOf(dealer.getDealerId());
        dialogBinding.layoutAdvisor.setVisibility(View.VISIBLE);
        dialog2.dismiss();
    }


        @Override
        public void loadGarage()
        {
            garageRealmResults = realm.where(Garage.class).findAll();
            garageListAdapter.setGarageResult(realm.copyToRealmOrUpdate(garageRealmResults.where()
                    .findAll()));
            garageListAdapter.notifyDataSetChanged();


            if(garageListAdapter.getItemCount()==0)
            {
               showError("No Available Cars");
            }else
            {
                setAppointment();
            }


        }


    @Override
    public void loadDealer()
    {
        dealerRealmResults = realm.where(Dealer.class).findAll();
        dealerListAdapter.setDealerResult(realm.copyToRealmOrUpdate(dealerRealmResults.where()
                .findAll()));
        dealerListAdapter.notifyDataSetChanged();


        if(dealerListAdapter.getItemCount()==0)
        {
            showError("Can't Connect to Server");
        }else
        {
            chooseDelear();
        }


    }


    @Override
    public void loadService()
    {
       servicesRealmResults = realm.where(Service.class).findAll();
        serviceListAdapter.setServiceResult(realm.copyToRealmOrUpdate(servicesRealmResults.where()
                .findAll()));
        serviceListAdapter.notifyDataSetChanged();
        if(serviceListAdapter.getItemCount()==0)
        {
            showError("Can't Connect to Server");
        }


    }


    @Override
    public void loadKms()
    {

           // showError("Load KMS");
        showError(serviceListAdapter.getSelectedService());

    }


    @Override
    public void setAppointment(){



        dialog = new Dialog(getContext());

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_add_appointment,
                null,
                false);


        dialogBinding.setView(getMvpView());


        dialogBinding.recyclerView.setAdapter(garageListAdapter);


        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
        dialogBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());




        dialogBinding.recyclerView2.setAdapter(serviceListAdapter);


        dialogBinding.recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        dialogBinding.recyclerView2.setItemAnimator(new DefaultItemAnimator());


        dialogBinding.layoutDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              presenter.loadDealerList(user.getUserId());
            }
        });

        dialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();


    }




    public void chooseDelear()
    {


        dialog2 = new Dialog(getContext());

        dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dealerBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_choose_dealer,
                null,
                false);


        dealerBinding.setView(getMvpView());


        dealerBinding.recyclerView.setAdapter(dealerListAdapter);


        dealerBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dealerBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());



        dealerBinding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {

                searchText = query;
               searchDealer();

                return true;

            }
        });

        dealerBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        dialog2.setContentView(dealerBinding.getRoot());
        dialog2.setCancelable(false);
        dialog2.show();
    }



    private void searchDealer() {
        if (dealerRealmResults.isLoaded() && dealerRealmResults.isValid()) {
            if (searchText.isEmpty()) {


                dealerRealmResults = realm.where(Dealer.class).findAllAsync();
                dealerListAdapter.setDealerResult(realm.copyToRealmOrUpdate(dealerRealmResults.where()
                        .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
                dealerListAdapter.notifyDataSetChanged();

            } else {

                dealerRealmResults = realm.where(Dealer.class).findAllAsync();
                dealerListAdapter.setDealerResult(realm.copyToRealmOrUpdate(dealerRealmResults.where()
                        .contains("dealerName",searchText, Case.INSENSITIVE)
                        .or()
                        .contains("dealerAddress",searchText, Case.INSENSITIVE)
                        .or()
                        .contains("dealerLocation",searchText, Case.INSENSITIVE)
                        .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
                dealerListAdapter.notifyDataSetChanged();
            }
        }
    }





}
