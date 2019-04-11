package mychevroletconnect.com.chevroletapp.ui.garage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import io.realm.Realm;
import io.realm.RealmResults;
import mychevroletconnect.com.chevroletapp.R;
import mychevroletconnect.com.chevroletapp.databinding.ActivityGarageBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogAddGarageBinding;
import mychevroletconnect.com.chevroletapp.databinding.DialogEditGarageBinding;
import mychevroletconnect.com.chevroletapp.model.data.Garage;
import mychevroletconnect.com.chevroletapp.model.data.User;
import pl.aprilapps.easyphotopicker.EasyImage;

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
    String uploadCarImage = "";
    DialogEditGarageBinding editDialogBinding;
    DialogAddGarageBinding addDialogBinding;
    private ProgressDialog progressDialog;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 124;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 125;
    private static final int PERMISSION_CAMERA = 126;
    private Dialog dialog;
    private File compressedImageFile = null;

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


//        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchText = newText;
//                //prepareList();
//                return true;
//            }
//        });
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
        }else
        {
            binding.garageNoRecyclerview.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
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
    public void closeDialog(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        onRefresh();

        dialog.dismiss();

    }




    @Override
    public void setDeleteGarageList(final Garage garage) {


        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this car?")
                .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteCar(String.valueOf(garage.getGarageId()),String.valueOf(user.getUserId()));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();


    }

    @Override
    public void setEditGarageList(final Garage garage) {


      dialog = new Dialog(this);


      uploadCarImage = garage.getGarageId()+garage.getGarageName();

        editDialogBinding = DataBindingUtil.inflate(
                getLayoutInflater(),
                R.layout.dialog_edit_garage,
                null,
                false);

        editDialogBinding.setGarage(garage);

        editDialogBinding.btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(GarageListActivity.this, editDialogBinding.btnChangeImage);
                popupMenu.inflate(R.menu.edit_car_image);
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


        editDialogBinding.layoutCarDop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(GarageListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        editDialogBinding.etCarDop.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        editDialogBinding.editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editDialogBinding.editSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                presenter.editCar(  String.valueOf(garage.getGarageId()),editDialogBinding.etCarModel.getText().toString(),
                        editDialogBinding.etCarChassis.getText().toString(),
                        editDialogBinding.etCarPlate.getText().toString(),
                        editDialogBinding.etCarYearModel.getText().toString(),
                        editDialogBinding.etCarDop.getText().toString(),
                        editDialogBinding.etCarName.getText().toString());
                //dialog.dismiss();

            }
        });

        dialog.setContentView(editDialogBinding.getRoot());
        dialog.setCancelable(false);
        dialog.show();
    }



    private void takePicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return;
        }
        EasyImage.openCamera(this, 0);
    }

    private void selectPicture() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            return;
        }
        EasyImage.openGallery(this, 0);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




        super.onActivityResult(requestCode, resultCode, data);



        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
                showError(e.getLocalizedMessage());
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                // Log.d(TAG, imageFile.getAbsolutePath());
                uploadImage(imageFile);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(GarageListActivity.this);
                    if (photoFile != null) //noinspection ResultOfMethodCallIgnored
                        photoFile.delete();
                }
            }
        });
    }

    private void uploadImage(final File imageFile) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.design_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_user);

        Bitmap bitmap = null;
        Bitmap adjustedBitmap=null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile), null, options);
            compressedImageFile = new Compressor(this).compressToFile(imageFile);
        }  catch (Exception e) {
            showError("Error Image Compression");
            e.printStackTrace();
        }

       // Uri photoUri = Uri.fromFile( new File( imageFile.get));
        Uri photoUri = Uri.fromFile(imageFile);


        try {
            ExifInterface exif = new ExifInterface(photoUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}

          //  Bitmap.createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter);
             adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }



//        //   imageView.setImageURI(photoUri);
//        Glide.with(this)
//                .load(photoUri)
//                .error(R.drawable.ic_nav_appointment)
//                .into(imageView);


                imageView.setImageBitmap(adjustedBitmap);

        new AlertDialog.Builder(this)
                .setTitle("Upload Car Picture")
                .setView(view)
                .setPositiveButton("UPLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(compressedImageFile != null && !uploadCarImage.equalsIgnoreCase(""))
                            presenter.upload(uploadCarImage/*+".jpg"*/,compressedImageFile);
                        else if(!uploadCarImage.equalsIgnoreCase(""))
                            presenter.upload(uploadCarImage/*+".jpg"*/,imageFile);
                    }
                })
                .setNegativeButton("CANCEL", null)
                .setCancelable(false)
                .show();
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    EasyImage.openGallery(this, 0);
                } else { // Permission Denied
                    showError("Storage Read/Write Permission Denied");
                }
                break;
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    takePicture();
                } else { // Permission Denied
                    showError("Storage Read/Write Permission Denied");
                }
                break;
            case PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Permission Granted
                    takePicture();
                } else { // Permission Denied
                    showError("Camera Permission Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating...");
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
    public void startupLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopupLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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





    public void addCar()
    {

      dialog = new Dialog(this);

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
