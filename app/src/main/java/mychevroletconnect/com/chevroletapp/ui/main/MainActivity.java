package mychevroletconnect.com.chevroletapp.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.app.Endpoints;
import mychevroletconnect.com.chevroletapp.databinding.ActivityMainBinding;
import mychevroletconnect.com.chevroletapp.model.data.User;
import mychevroletconnect.com.chevroletapp.ui.login.LoginActivity;
import mychevroletconnect.com.chevroletapp.ui.profile.ProfileActivity;
import mychevroletconnect.com.chevroletapp.util.CircleTransform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Realm realm;
    private User user;
    private ActivityMainBinding binding;
    private TextView txtName;
    private TextView txtEmail;
    private ImageView imgProfile;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.appBarMain.toolbar);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Chevrolet App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        Menu nav_Menu =  binding.navView.getMenu();
        nav_Menu.findItem(R.id.nav_login).setVisible(false);

        MainTabAdapter mAdapter = new MainTabAdapter(getSupportFragmentManager());
        binding.appBarMain.viewPager.setAdapter(mAdapter);
        binding.appBarMain.tabs.setupWithViewPager(binding.appBarMain.viewPager, true);

        //binding.appBarMain.tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        txtName = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.txt_name);
        txtEmail = (TextView) binding.navView.getHeaderView(0).findViewById(R.id.txt_email);
        imgProfile = (ImageView) binding.navView.getHeaderView(0).findViewById(R.id.imageView);




        if(user != null)
                    updateUI();

    }


    private void updateUI() {

            txtName.setText(user.getFullName());
        txtEmail.setText(user.getEmail());
        String imageURL = "";

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            imageURL = Endpoints.URL_IMAGE + (user.getImage());
        }

        Log.d(">>>>>>>>>>", "imageUrl: " + imageURL);
        Glide.with(this)
                .load(imageURL)
                .transform(new CircleTransform(this))
                .error(R.drawable.profile_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
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
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(this, LoginActivity.class));
        }else if (id == R.id.nav_dealer) {

        }else if (id == R.id.nav_appointment) {
           // startActivity(new Intent(this, MainActivity.class));
        }
        else if (id == R.id.nav_promo) {

        } else if (id == R.id.nav_showroom) {

        } else if (id == R.id.nav_parts) {

        } else if (id == R.id.nav_testdrive) {

        } else if (id == R.id.nav_roadside) {

        }else if (id == R.id.nav_pms) {

        }else if (id == R.id.nav_warranty) {

        }else if (id == R.id.nav_care) {

        }
        else if (id == R.id.nav_logout) {
            logOut(user);
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut(User user) {
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                realm.close();
                Toast.makeText(MainActivity.this, "Realm Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
