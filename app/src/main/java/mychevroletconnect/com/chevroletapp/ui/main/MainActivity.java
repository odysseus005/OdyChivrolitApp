package mychevroletconnect.com.chevroletapp.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import mychevroletconnect.com.chevroletapp.databinding.ActivityMainBinding;
import mychevroletconnect.com.chevroletapp.model.data.User;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;



public class RacesListActivity
        extends MvpViewStateActivity<MainView, MainPresenter>
        implements SwipeRefreshLayout.OnRefreshListener , NavigationView.OnNavigationItemSelectedListener, MainView{

    private static final String TAG = RacesListActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private TextView txtName;
    private TextView txtEmail;
    private ImageView imgProfile;
    private Realm realm;
    private User user;
    private String searchText;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        searchText = "";
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        if (user == null) {
            Log.e(TAG, "No User found");
           // finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.appBarMain.setView(getMvpView());

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setTitle("Chevrolet App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);


        txtName = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.txt_name);
        txtEmail = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.txt_email);
        imgProfile = (ImageView) binding.navView.getHeaderView(0).findViewById(R.id.imageView);

        user = realm.where(User.class).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (user.isLoaded() && user.isValid())
                    updateUI();
            }
        });




        binding.navView.setNavigationItemSelectedListener(this);
        //binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.appBarMain.swipeRefreshLayout.setOnRefreshListener(this);

        binding.appBarMain.recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        binding.appBarMain.recyclerView.setItemAnimator(new DefaultItemAnimator());



        userRacesListAdapter = new UserRacesListAdapter(this, getMvpView());
        binding.appBarMain.recyclerView.setAdapter(userRacesListAdapter);




        binding.appBarMain.recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        binding.appBarMain.recyclerView2.setItemAnimator(new DefaultItemAnimator());

        upcomingRacesListAdapter = new UpcomingRacesListAdapter(this, getMvpView());
        binding.appBarMain.recyclerView2.setAdapter(upcomingRacesListAdapter);



        binding.appBarMain.recyclerView3.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        binding.appBarMain.recyclerView3.setItemAnimator(new DefaultItemAnimator());


        raceResultListAdapter = new RaceResultListAdapter(this, getMvpView());
        binding.appBarMain.recyclerView3.setAdapter(raceResultListAdapter);




    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        if (!searchText.isEmpty()) {
            search.setIconified(false);
            search.setQuery(searchText, true);
        }
        return true;
    }

    //MainActivity Methods start 09051964687

    private void updateUI() {
        txtName.setText(user.getFullName());
        txtEmail.setText(user.getEmailAddress());
        String imageURL = "";

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            imageURL = Endpoints.IMAGE_URL.replace(Endpoints.IMG_HOLDER, user.getImage());
        }

        Log.d("MainActivity", "imageUrl: " + imageURL);
        Glide.with(this)
                .load(imageURL)
                .transform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
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

        if (id == R.id.nav_company) {

        }  else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));

        }else if (id == R.id.nav_runner) {
            startActivity(new Intent(this, RunnerListActivity.class));

        }
        else if (id == R.id.nav_transactions) {

        }
        else if (id == R.id.nav_logout) {
            logOut(user);
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut(User user) {
        String token = user.getApiToken();
        if (token != null) {
            App.getInstance().getApiInterface().deleteGCMToken(Constants.BEARER+token, Constants.APPJSON)
                    .enqueue(new Callback<BasicResponse>() {
                        @Override
                        public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("token delete call", response.body().getMessage());
                            } else {
                                Log.e("token delete call", "server error");
                            }
                        }

                        @Override
                        public void onFailure(Call<BasicResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }

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
                Intent intent = new Intent(RacesListActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
                Toast.makeText(RacesListActivity.this, "Realm Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //MainAcitivity Methods End



  /*  private void prepareList() {
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
                onBackPressed();
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
    protected void onDestroy() {
        racesResultRealmResults.removeChangeListeners();
        reservationRealmResults.removeChangeListeners();
        upcomingRacesRealmResults.removeChangeListeners();


        realm.close();
        super.onDestroy();
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    public void onRefresh() {
        presenter.loadUpcomingRaces(user.getApiToken());
        presenter.loadRacesResult(user.getApiToken());
        presenter.loadUserRaces(user.getApiToken());
    }

    public void loadData()
    {
        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        reservationRealmResults = realm.where(Reservation.class).findAll();
        racesResultRealmResults = realm.where(RacesResult.class).findAll();
        upcomingRacesRealmResults = realm.where(UpcomingRaces.class).findAll();
        if (racesResultRealmResults.isLoaded() && racesResultRealmResults.isValid()) {
            getMvpView().setRacesResult();
        }else
        {
            presenter.loadRacesResult(user.getApiToken());
        }

        if (reservationRealmResults.isLoaded() && reservationRealmResults.isValid()) {
            getMvpView().setUserRaces();
        }else
        {
            presenter.loadUserRaces(user.getApiToken());
        }

        if (!(upcomingRacesRealmResults.isLoaded() && reservationRealmResults.isValid())) {
            getMvpView().setUpcomingRaces();
        }else
        {
            presenter.loadUpcomingRaces(user.getApiToken());
        }
    }

    @Override
    public void setRacesResult(){

        racesResultRealmResults = realm.where(RacesResult.class).findAllAsync();
        raceResultListAdapter.setRacesResult(realm.copyToRealmOrUpdate(racesResultRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING)));
        raceResultListAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUpcomingRaces() {


        upcomingRacesRealmResults = realm.where(UpcomingRaces.class).findAllAsync();
        upcomingRacesListAdapter.setUpcomingRaces(realm.copyFromRealm(upcomingRacesRealmResults.where()
                .findAll()));//Sorted("eventDateFrom", Sort.ASCENDING))););
        upcomingRacesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void intentUserRaces() {
        Log.d("TAG>>","Click");
        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("listIntent", "M");
        startActivity(intent);
    }

    @Override
    public void intentRacesResult(){

        Log.d("TAG>>","Click");
        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("listIntent", "R");
        startActivity(intent);
    }

    @Override
    public void intentUpcomingRaces() {


        Intent intent = new Intent(this, EventListActivity.class);
        intent.putExtra("listIntent", "U");
        startActivity(intent);
    }


    @Override
    public void setUserRaces() {
        reservationRealmResults = realm.where(Reservation.class).findAllAsync();

        userRacesListAdapter.setUserRaces(realm.copyFromRealm(reservationRealmResults.where()
                .findAll()));
        userRacesListAdapter.notifyDataSetChanged();
    }


    @Override
    public void stopRefresh() {
        binding.appBarMain.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showRacesResultDetails(RacesResult eventRacesResult) {

    }

    @Override
    public void showUpcomingRacesDetails(UpcomingRaces eventUpcomingRaces) {
        Intent intent = new Intent(this, UpcomingRaceDetailActivity.class);
        intent.putExtra(Constants.UPCOMING_ID, eventUpcomingRaces.getId());
        startActivity(intent);
    }

    @Override
    public void showUserRacesDetails(Reservation eventReservation) {

    }



    @NonNull
    @Override
    public ViewState<MainView> createViewState() {
        setRetainInstance(true);
        return new MainViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.appBarMain.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.appBarMain.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }
}
