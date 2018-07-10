package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.SupportMapFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityAppointmentCurrentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAddAppointmentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogChooseDateBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogChooseDealerBinding;
import mychevroletconnect.com.chevroletapp.model.data.Advisor;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Pms;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.ui.register.RegisterActivity;


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
    private List<Advisor> advisorRealmResults;
    private List<Pms> pmsRealmResults;
    private String searchText;
    public String id;
    private GarageAdapter garageListAdapter;
    private ServiceAdapter serviceListAdapter;
    private DealerAdapter dealerListAdapter;
    private AppointmentAdapter appointmentListAdapter;
    private DialogAddAppointmentBinding dialogBinding;
    private DialogChooseDealerBinding dealerBinding;
    private DialogChooseDateBinding dateBinding;
    private Dialog dialog,dialog2,dialog3;
    private ArrayList<String> civil;
    private String selectedDealerId,advisorPosition,pmsPosition;

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
                presenter.loadPMSList(user.getUserId());

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
        presenter.loadAdvisorList(dealer.getDealerId());
        //dialogBinding.layoutAdvisor.setVisibility(View.VISIBLE);
        dialog2.dismiss();
    }

    @Override
    public  void loadAdvisor()
    {


        List<String> advisor = new ArrayList<>();
        advisorRealmResults = realm.where(Advisor.class).findAll();

        if(!advisorRealmResults.isEmpty()) {
            for (Advisor value : advisorRealmResults) {
                advisor.add(value.getFullName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_custom_item, advisor);
            dialogBinding.spAdvisor.setAdapter(arrayAdapter);

            dialogBinding.spAdvisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    advisorPosition=String.valueOf(advisorRealmResults.get(position).getAdvisorId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dialogBinding.spAdvisor.setVisibility(View.VISIBLE);
            dialogBinding.appointmentAdvisorTitle.setVisibility(View.VISIBLE);
        }
        else
            showError("No Available Advisor");

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
            showError("Can't Load Dealer");
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
            showError("Can't Load Services");
        }


    }


    @Override
    public void loadKms(boolean check)
    {
        pmsRealmResults = realm.where(Pms.class).findAll();

        if(!check && !pmsRealmResults.isEmpty() ) {




           List<String> pms = new ArrayList<>();
           for(Pms value : pmsRealmResults)
           {
              pms.add(value.getPmsMileage()+"km - "+value.getPmsMonth()+" months : "+value.getPmsName());
           }

           ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_custom_item, pms);
           dialogBinding.spPms.setAdapter(arrayAdapter);

           dialogBinding.spPms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 pmsPosition = String.valueOf(pmsRealmResults.get(position).getPmsId());
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });


           dialogBinding.spPms.setVisibility(View.VISIBLE);
           dialogBinding.appointmentPmsTitle.setVisibility(View.VISIBLE);
       }
       else
       {
           dialogBinding.spPms.setVisibility(View.GONE);
           dialogBinding.appointmentPmsTitle.setVisibility(View.GONE);

       }
    }


    @Override
    public void setAppointmentDate() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateBinding.etAppointDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

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
