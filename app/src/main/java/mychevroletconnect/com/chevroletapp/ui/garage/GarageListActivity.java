package mychevroletconnect.com.chevroletapp.ui.garage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
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
import android.widget.DatePicker;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityGarageBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAddGarageBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogEditGarageBinding;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.User;

import static java.security.AccessController.getContext;


public class GarageListActivity
        extends MvpViewStateActivity<GarageListView, GarageListPresenter>
        implements SwipeRefreshLayout.OnRefreshListener, GarageListView {

    private static final String TAG = GarageListActivity.class.getSimpleName();
    private ActivityGarageBinding binding;
    private Realm realm;
    private User user;
    private RealmResults<Garage> garageRealmResults;
    private GarageListAdapter garageListAdapter;
    private String searchText;
    public String id;
    DialogEditGarageBinding editDialogBinding;
    DialogAddGarageBinding addDialogBinding;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        searchText = "";

        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();


        binding = DataBindingUtil.setContentView(this, R.layout.activity_garage);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Garage");

        garageListAdapter = new GarageListAdapter(this, getMvpView());
        binding.recyclerView.setAdapter(garageListAdapter);





        //binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
         binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });






        binding.addGarage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addCar();
            }
        });


    }






    @NonNull
    @Override
    public GarageListPresenter createPresenter() {
        return new GarageListPresenter();
    }

    @NonNull
    @Override
    public ViewState<GarageListView> createViewState() {
        setRetainInstance(true);
        return new GarageViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);


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
        return super.onCreateOptionsMenu(menu);
    }

    /*private void prepareList() {
        if (eventRealmResults.isLoaded() && eventRealmResults.isValid()) {
            if (searchText.isEmpty()) {
                getMvpView().setData(realm.copyFromRealm(eventRealmResults));
            } else {
                getMvpView().setData(realm.copyToRealmOrUpdate(eventRealmResults.where()
                        .contains("eventName", searchText, Case.INSENSITIVE)
                        .or()
                        .contains("tags", searchText, Case.INSENSITIVE)
                        .findAll()));
            }
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              this.onBackPressed();
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
       // garageRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }


    @Override
    public void onRefresh() {

            presenter.loadGarageList(user.getUserId());


    }


    public void loadData()
    {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        garageRealmResults = realm.where(Garage.class).findAll();


           // if (garageRealmResults.isLoaded() && garageRealmResults.isValid()) {
           if(garageRealmResults.size()>0){
                getMvpView().setGarageList();


            }else
            {
                presenter.loadGarageList(user.getUserId());

            }


    }

    @Override
    public void setGarageList(){



        garageRealmResults = realm.where(Garage.class).findAllAsync();
        garageListAdapter.setEventResult(realm.copyToRealmOrUpdate(garageRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        garageListAdapter.notifyDataSetChanged();



        if(garageListAdapter.getItemCount()==0)
        {
            binding.garageNoRecyclerview.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }



    @Override
    public void stopRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void setDeleteGarageList(Garage event) {


        showError("SASASAS");

    }

    @Override
    public void setEditGarageList(Garage event) {


      Dialog dialog = new Dialog(this);
        editDialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_edit_garage,
                null,
                false);
        editDialogBinding.btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(GarageListActivity.this, editDialogBinding.btnChangeImage);
                popupMenu.inflate(R.menu.edit_product_image);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_select_picture:
                                selectPicture();
                                break;
                            case R.id.action_take_picture:
                                takePicture();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


    }



    @Override
    public void showGarageListDetails(Garage event) {


    }


    @Override
    public void onBirthdayClicked() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                addDialogBinding.etCarDop.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    @Override
    public void onBirthdayEdit() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editDialogBinding.etCarDop.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }


    public void addCar()
    {

      final Dialog dialog = new Dialog(this);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        addDialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_add_garage,
                null,
                false);


        addDialogBinding.setView(getMvpView());


        addDialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addDialogBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.registerCar(addDialogBinding.etCarModel.getText().toString(),
                        addDialogBinding.etCarChassis.getText().toString(),
                        addDialogBinding.etCarPlate.getText().toString(),
                        addDialogBinding.etCarYearModel.getText().toString(),
                        addDialogBinding.etCarDop.getText().toString(),
                        String.valueOf(user.getUserId()),
                        addDialogBinding.etCarName.getText().toString());
                //dialog.dismiss();
            }
        });
        dialog.setContentView(addDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();


    }




}
