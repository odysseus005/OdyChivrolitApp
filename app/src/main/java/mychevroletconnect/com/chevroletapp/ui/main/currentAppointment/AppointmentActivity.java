package mychevroletconnect.com.chevroletapp.ui.main.currentAppointment;

import android.Manifest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.databinding.ActivityAppointmentCurrentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAddAppointmentBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAppointmentDetailBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogChooseDateBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogChooseDealerBinding;
import mychevroletconnect.com.chevroletapp.model.data.Advisor;
import mychevroletconnect.com.chevroletapp.model.data.Appointment;
import mychevroletconnect.com.chevroletapp.model.data.Dealer;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.Pms;
import mychevroletconnect.com.chevroletapp.model.data.Schedule;
import mychevroletconnect.com.chevroletapp.model.data.Service;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.ui.register.RegisterActivity;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


import static android.app.Activity.RESULT_OK;


public class AppointmentActivity
        extends MvpViewStateFragment<AppointmentView, AppointmentPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, AppointmentView,DatePickerDialog.OnDateSetListener {


    private ProgressDialog progressDialog;
    private static final String TAG = AppointmentActivity.class.getSimpleName();
    private ActivityAppointmentCurrentBinding binding;
    private Realm realm;
    private User user;
    private RealmResults<Appointment> appointmentlmResults;
    private RealmResults<Garage> garageRealmResults;
    private RealmResults<Dealer> dealerRealmResults;
    private RealmResults<Service> servicesRealmResults;
    private RealmResults<Schedule> scheduleRealmResults;
    private List<Advisor> advisorRealmResults;
    private List<Pms> pmsRealmResults;
    private String searchText;
    public String id,appointid,gid;
    private GarageAdapter garageListAdapter;
    private ServiceAdapter serviceListAdapter;
    private DealerAdapter dealerListAdapter;
    private AppointmentAdapter appointmentListAdapter;
    private ScheduleAdapter scheduleListAdapter;
    private DialogAddAppointmentBinding dialogBinding;
    private DialogChooseDealerBinding dealerBinding;
    private DialogAppointmentDetailBinding detailBinding;
    private DialogChooseDateBinding dateBinding;
    private Dialog dialog,dialog2,dialog3,dialogDetail;
    private ArrayList<String> civil;
    private boolean reSchedchecker= false;

    private String selectedDealerId="",selectedadvisorPosition="0",selectedpmsPosition="",selectedScheduleId="",selectedDate="",selectedService="",selectedGarage="";

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
        presenter.loadServiceList(user.getUserId());
       // presenter.loadAppointmentList(String.valueOf(user.getUserId()));
        appointmentListAdapter = new AppointmentAdapter(getActivity(), getMvpView());
        scheduleListAdapter = new ScheduleAdapter(getActivity(), getMvpView());
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
             //   presenter.loadServiceList(user.getUserId());
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

        presenter.loadAppointmentList(String.valueOf(user.getUserId()));
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


//    public void loadData()
//    {
//        realm = Realm.getDefaultInstance();
//        User user = realm.where(User.class).findFirst();
//        appointmentlmResults = realm.where(Appointment.class).findAll();
//            if (appointmentlmResults.isLoaded() && appointmentlmResults.isValid()) {
//                getMvpView().setAppointmentList();
//            }else
//            {
//                presenter.loadAppointmentList(String.valueOf(user.getUserId()));
//            }
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setAppointmentList(){


        appointmentlmResults = realm.where(Appointment.class).findAll();
       appointmentListAdapter.setAppointmentResult(appointmentlmResults);//Sorted("eventDateFrom", Sort.ASCENDING)));

        appointmentListAdapter.notifyDataSetChanged();


        if(appointmentListAdapter.getItemCount()==0)
        {

            binding.appointmentcurrentNoRecyclerview.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }else
        {
            binding.appointmentcurrentNoRecyclerview.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
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
    public void appointmentExist(String message) {

        new AlertDialog.Builder(getContext())
                .setTitle("Schedule Appointment Error")
                .setMessage("You already have an appointment on the selected date. Reschedule your existing appointment.")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();



    }


    @Override
    public void showReturn(String message) {

        presenter.loadAppointmentList(String.valueOf(user.getUserId()));
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        selectedDealerId="";
        selectedadvisorPosition="0";selectedpmsPosition="";selectedScheduleId="";selectedDate="";selectedService="";selectedGarage="";
        scheduleListAdapter.reset();
        garageListAdapter.reset();
        serviceListAdapter.reset();


        dialog.dismiss();

    }






    @Override
    public void showAppointmentDetails(final Appointment appointment) {


        dialogDetail = new Dialog(getContext(),R.style.FullDialogTheme);

        dialogDetail.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        detailBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_appointment_detail,
                null,
                false);


        detailBinding.setView(getMvpView());
        detailBinding.setAppointment(appointment);


        Glide.with(getContext())
                .load(Endpoints.URL_IMAGE+appointment.getAppointgaragerId()+appointment.getAppointgaragerName()+".jpg")
                .centerCrop()
                .error(R.drawable.placeholder_garage)
                .into(detailBinding.appointDetailsImage);


        detailBinding.appointmentDetailsStatus.setTextColor(appointmentListAdapter.getStatusColor(appointment.getAppointStatus()));

        String serviceFinal = "Service: \n";
        String[] items = appointment.getAppointServicesId().split(",");
        for (String item : items) {
            serviceFinal += presenter.getService(item).getServiceName() + "\n";
        }
        detailBinding.appointmentDetailsService.setText(FunctionUtils.removeLastChar(serviceFinal));

        if (Integer.parseInt(appointment.getAppointPMSId()) > 0) {
            detailBinding.appointmentDetailsPMS.setText("PMS Service: " + appointment.getAppointPMSMil() + "km - " + appointment.getAppointPMSMonth() + " months  " + appointment.getAppointPMSService());
            detailBinding.appointmentDetailsPMS.setVisibility(View.VISIBLE);
        }

        if(appointment.getAppointStatus().equalsIgnoreCase("CANCELLED"))
        {
            detailBinding.cancel.setVisibility(View.GONE);
            detailBinding.resched.setVisibility(View.GONE);
        }

        detailBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you want cancel your appointment?")
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.cancelReservation(String.valueOf(appointment.getAppointId()));
                            }
                        })
                        .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })

                        .show();

            }
        });

        detailBinding.resched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSchedchecker = true;
                selectedDealerId = String.valueOf(appointment.getAppointdealerId());
                appointid = String.valueOf(appointment.getAppointId());
                gid = String.valueOf(appointment.getAppointgaragerId());
                chooseDateandSlot();

            }
        });

        detailBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDetail.dismiss(); reSchedchecker = false;
            }
        });

        dialogDetail.setContentView(detailBinding.getRoot());
        dialogDetail.setCancelable(true);
        dialogDetail.show();

    }


    @Override
    public void selectDealer(Dealer dealer) {

        dialogBinding.etDealer.setText(dealer.getDealerName());
        selectedDealerId = String.valueOf(dealer.getDealerId());
      //  presenter.loadAdvisorList(dealer.getDealerId());
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

                    selectedadvisorPosition=String.valueOf(advisorRealmResults.get(position).getAdvisorId());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dialogBinding.spAdvisor.setVisibility(View.VISIBLE);
            //dialogBinding.appointmentAdvisorTitle.setVisibility(View.VISIBLE); //Hide advisor
        }
        else
            showError("No Available Adviser");

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
    public void loadTimeslot()
    {
        scheduleRealmResults = realm.where(Schedule.class).findAll();
        scheduleListAdapter.setScheduleResult(realm.copyToRealmOrUpdate(scheduleRealmResults.where()
                .findAll()));
        scheduleListAdapter.notifyDataSetChanged();
        if(scheduleListAdapter.getItemCount()==0)
        {
            showError("No Available Schedule Yet");
        }
        else
        {
            dateBinding.chooseSlotTitle.setVisibility(View.VISIBLE);
            dateBinding.chooseSlotTitle2.setVisibility(View.VISIBLE);
            dateBinding.recyclerView.setVisibility(View.VISIBLE);
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
              pms.add(value.getPmsMileage()+"km - "+value.getPmsMonth()+" months  "+value.getPmsName());
           }

           ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_custom_item, pms);
           dialogBinding.spPms.setAdapter(arrayAdapter);

           dialogBinding.spPms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 selectedpmsPosition = String.valueOf(pmsRealmResults.get(position).getPmsId());
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


        Calendar sunday,saturday;
        List<Calendar> weekends = new ArrayList<>();
        int weeks = 50;

        for (int i = 0; i < (weeks * 7) ; i = i + 7) {
            sunday = Calendar.getInstance();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
//             saturday = Calendar.getInstance();
//             saturday.add(Calendar.DAY_OF_YEAR, (Calendar.SATURDAY - saturday.get(Calendar.DAY_OF_WEEK) + i));
//             weekends.add(saturday);
            weekends.add(sunday);
        }
        Calendar[] disabledDays = weekends.toArray(new Calendar[weekends.size()]);


        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                AppointmentActivity.this,
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
        );
        int daysallowable = 2;//get from database
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysallowable);
      //  Cal dateBefore30Days = cal.getTime();
        datePickerDialog.setMinDate(cal);
        datePickerDialog.setDisabledDays(disabledDays);
        datePickerDialog.show(getActivity().getFragmentManager(),"");



    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        dateBinding.etAppointDate.setText(dateFormatter.format(newDate.getTime()));

        dateBinding.chooseSlotTitle.setVisibility(View.INVISIBLE);
        dateBinding.chooseSlotTitle2.setVisibility(View.INVISIBLE);
        dateBinding.recyclerView.setVisibility(View.INVISIBLE);
        presenter.loadTimeslotList(user.getUserId(),selectedDealerId,dateFormatter.format(newDate.getTime()));


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


        dialogBinding.layoutAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!selectedDealerId.equals(""))
                chooseDateandSlot();
              else
                  showError("Please Select Dealer First");
            }
        });

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
                selectedDealerId="";
                selectedadvisorPosition="0";selectedpmsPosition="";selectedScheduleId="";selectedDate="";selectedService="";selectedGarage="";
                scheduleListAdapter.reset();
                garageListAdapter.reset();
                serviceListAdapter.reset();
            }
        });

        dialogBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               selectedService = serviceListAdapter.getSelectedService();
              selectedGarage =  garageListAdapter.getSelectedGarage();

                if(selectedDealerId.equals(""))
                {
                        showError("Invalid Dealer Field");
                }else if(selectedScheduleId.equals("")||selectedDate.equals(""))
                {
                    showError("Invalid Date Schedule Field");
                }
                else if(selectedService.equals(""))
                {
                    showError("Invalid Service Field");
                }
                else if(selectedGarage.equals(""))
                {
                    showError("Please Select Car");
                }
                else
                    presenter.reserveSched(String.valueOf(user.getUserId()),selectedGarage,selectedScheduleId,selectedDealerId,selectedadvisorPosition,selectedService,selectedpmsPosition,selectedDate,dialogBinding.etRemars.getText().toString());




            }
        });

        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();


    }




    public void chooseDelear()
    {


        dialog2 = new Dialog(getContext(),R.style.FullDialogTheme);

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
            public boolean onQueryTextChange(String query) {
                searchText = query;
                searchDealer();
                return true;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;

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


    public void chooseDateandSlot()
    {


        dialog3 = new Dialog(getContext());

        dialog3.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        dateBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_choose_date,
                null,
                false);


        dateBinding.setView(getMvpView());


        dateBinding.recyclerView.setAdapter(scheduleListAdapter);


        dateBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dateBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());





        dateBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });

        dateBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(scheduleListAdapter.getChoosenScheduleValue().equals("false")||scheduleListAdapter.getChoosenSchedule() == 0)) {

                    if (!reSchedchecker) {
                        dialogBinding.etDate.setText(dateBinding.etAppointDate.getText().toString());
                        dialogBinding.etTime.setText(FunctionUtils.hour24to12hour(scheduleListAdapter.getChoosenScheduleValue()));
                        selectedDate = dateBinding.etAppointDate.getText().toString();
                        selectedScheduleId = String.valueOf(scheduleListAdapter.getChoosenSchedule());
                        dialog3.dismiss();
                    } else
                        confirmResched(dateBinding.etAppointDate.getText().toString(), selectedScheduleId = String.valueOf(scheduleListAdapter.getChoosenSchedule()));
                }else
                    showError("Please Select Date and Slot");
            }
        });

        dialog3.setContentView(dateBinding.getRoot());
        dialog3.setCancelable(false);
        dialog3.show();
    }

    public void confirmResched(final String date, final String schedid)
    {

        new AlertDialog.Builder(getContext())
                .setTitle("Are you sure you want reschedule your appointment?")
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        presenter.reSchedReservation(appointid,schedid,date,gid);
                        dialog3.dismiss();
                    }
                })
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reSchedchecker = false;
                    }
                })

                .show();
    }



    @Override
    public void closeDialog(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        onRefresh();

        selectedDealerId="";
        selectedadvisorPosition="0";selectedpmsPosition="";selectedScheduleId="";selectedDate="";selectedService="";selectedGarage="";
        scheduleListAdapter.reset();
        garageListAdapter.reset();
        serviceListAdapter.reset();


        dialogDetail.dismiss();

    }




}
