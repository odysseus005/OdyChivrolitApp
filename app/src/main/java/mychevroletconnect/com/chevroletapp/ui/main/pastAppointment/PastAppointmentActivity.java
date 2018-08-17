package mychevroletconnect.com.chevroletapp.ui.main.pastAppointment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.databinding.ActivityAppointmentPastBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAppointmentDetailPastBinding;
import mychevroletconnect.com.chevroletapp.model.data.PastAppointment;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.util.FunctionUtils;


public class PastAppointmentActivity
        extends MvpViewStateFragment<PastAppointmentView, PastAppointmentPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, PastAppointmentView {

    private ProgressDialog progressDialog;
    private static final String TAG = PastAppointmentActivity.class.getSimpleName();
    private ActivityAppointmentPastBinding binding;
    private Realm realm;
    private User user;
    private RealmResults<PastAppointment> appointmentlmResults;
    private String searchText;
    public String id;
    private PastAppointmentAdapter appointmentListAdapter;
    private DialogAppointmentDetailPastBinding detailBinding;
    private Dialog dialogDetail;



    public PastAppointmentActivity(){

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }




    @NonNull
    @Override
    public ViewState<PastAppointmentView> createViewState() {
        setRetainInstance(true);
        return new PastAppointmentViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_appointment_past, container, false);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Appointment");

        presenter.onStart();


        appointmentListAdapter = new PastAppointmentAdapter(getActivity(), getMvpView());
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





        }


    @NonNull
    @Override
    public PastAppointmentPresenter createPresenter() {
        return new PastAppointmentPresenter();
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
//    private void prepareList() {
//        if (appointmentlmResults.isLoaded() && appointmentlmResults.isValid()) {
//            if (searchText.isEmpty()) {
//
//
//                appointmentlmResults = realm.where(Appointment.class).findAllAsync();
//                appointmentListAdapter.setAppointmentResult(realm.copyToRealmOrUpdate(appointmentlmResults.where()
//                        .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
//                appointmentListAdapter.notifyDataSetChanged();
//
//            } else {
//
//                appointmentlmResults = realm.where(Appointment.class).findAllAsync();
//                appointmentListAdapter.setAppointmentResult(realm.copyToRealmOrUpdate(appointmentlmResults.where()
//                        .contains("emailAddress",searchText, Case.INSENSITIVE)
//                        .or()
//                        .contains("firstName",searchText, Case.INSENSITIVE)
//                        .or()
//                        .contains("lastName",searchText, Case.INSENSITIVE)
//                        .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
//                appointmentListAdapter.notifyDataSetChanged();
//            }
//        }
//    }


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


//    public void loadData()
//    {
//        realm = Realm.getDefaultInstance();
//        User user = realm.where(User.class).findFirst();
//        appointmentlmResults = realm.where(PastAppointment.class).findAll();
//
//
//
//            if (appointmentlmResults.isLoaded() && appointmentlmResults.isValid()) {
//                getMvpView().setAppointmentList();
//
//            }else
//            {
//                presenter.loadAppointmentList(String.valueOf(user.getUserId()));
//
//            }
//
//
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void setAppointmentList(){





        appointmentlmResults = realm.where(PastAppointment.class).findAllAsync();
        appointmentListAdapter.setAppointmentResult(realm.copyToRealmOrUpdate(appointmentlmResults.where()
// \               .lessThan("dateMs",System.currentTimeMillis())
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        appointmentListAdapter.notifyDataSetChanged();



        if(appointmentListAdapter.getItemCount()==0)
        {

            binding.appointmentpasNoRecyclerview.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
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
        presenter.loadAppointmentList(String.valueOf(user.getUserId()));
    }






    @Override
    public void showAppointmentDetails2(PastAppointment appoint) {

        try {

            dialogDetail = new Dialog(getContext(), R.style.FullDialogTheme);
            dialogDetail.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            detailBinding = DataBindingUtil.inflate(
                    getLayoutInflater(),
                    R.layout.dialog_appointment_detail_past,
                    null,
                    false);


            Glide.with(getContext())
                    .load(Endpoints.URL_IMAGE+appoint.getAppointgaragerId()+appoint.getAppointgaragerName())
                    .centerCrop()
                    .error(R.drawable.placeholder_garage)
                    .into(detailBinding.appointDetailsImage);



            detailBinding.setAppointment(appoint);
            detailBinding.setView(getMvpView());
            detailBinding.appointmentDetailsStatus.setTextColor(appointmentListAdapter.getStatusColor(appoint.getAppointStatus()));

            String serviceFinal = "Service: \n";
            String[] items = appoint.getAppointServicesId().split(",");
            for (String item : items) {
                serviceFinal += presenter.getService(item).getServiceName() + "\n";
            }
            detailBinding.appointmentDetailsService.setText(FunctionUtils.removeLastChar(serviceFinal));

            if (Integer.parseInt(appoint.getAppointPMSId()) > 0) {
                detailBinding.appointmentDetailsPMS.setText("PMS Service: " + appoint.getAppointPMSMil() + "km - " + appoint.getAppointPMSMonth() + " months  " + appoint.getAppointPMSService());
                detailBinding.appointmentDetailsPMS.setVisibility(View.VISIBLE);
            }

            detailBinding.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDetail.dismiss();
                }
            });


            dialogDetail.setContentView(detailBinding.getRoot());
            dialogDetail.setCancelable(true);
            dialogDetail.show();
        }catch (Exception e)
        {
            presenter.loadAppointmentList(String.valueOf(user.getUserId()));
            showError("Can't Load Appointment Details");
        }

    }










}
