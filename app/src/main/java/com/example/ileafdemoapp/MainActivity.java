package com.example.ileafdemoapp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ileafdemoapp.Activity.LoginActivity;
import com.example.ileafdemoapp.Activity.UserDetailsActivity;
import com.example.ileafdemoapp.Fragments.SelectImageFromFragment;
import com.example.ileafdemoapp.Network.AppDatabase;
import com.example.ileafdemoapp.Network.User;
import com.example.ileafdemoapp.Utils.AppConst;
import com.example.ileafdemoapp.Utils.SharedPref;
import com.example.ileafdemoapp.Utils.Validation;
import com.example.ileafdemoapp.app.MyApplication;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ileafdemoapp.Utils.AppConst.CAMERA_PICTURE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.edttxt_places)
    EditText edttxt_places;
    @BindView(R.id.edttxt_dob)
    EditText edttxt_dob;
    @BindView(R.id.spinner_marital_status)
    Spinner spinner_marital_status;
    @BindView(R.id.radio_group_sex)
    RadioGroup radio_group_sex;
    @BindView(R.id.txt_submit)
    TextView txt_submit;
    @BindView(R.id.firstNameWrapper)
    TextInputLayout firstNameWrapper;
    @BindView(R.id.lastNameWrapper)
    TextInputLayout lastNameWrapper;
    @BindView(R.id.emailWrapper)
    TextInputLayout emailWrapper;
    @BindView(R.id.placesWrapper)
    TextInputLayout placesWrapper;
    @BindView(R.id.dobWrapper)
    TextInputLayout dobWrapper;
    @BindView(R.id.edttxt_first_name)
    EditText edttxt_first_name;
    @BindView(R.id.edttxt_last_name)
    EditText edttxt_last_name;
    @BindView(R.id.edttxt_email)
    EditText edttxt_email;
    @BindView(R.id.rel_loader)
    RelativeLayout rel_loader;


    private TextView txt_user_email;
    private CircleImageView circleImageView;
    //    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    private String TAG = "places";
    private String autocomplete_place;
    private int selected_item_id = 0;
    private int mYear, mMonth, mDay;
    private int user_selected_sex;
    private int age;
    private User user;
    private AppDatabase database;
    private int result_camera;
    private int result_ext_storage;
    private Uri mPhotoUri;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result_camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        result_ext_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        activity = this;
        database = AppDatabase.getDatabase(MainActivity.this);

        database.userDao().removeAllUsers();

        initViews();

        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView1.getHeaderView(0);
        txt_user_email = (TextView) v.findViewById(R.id.txt_user_email);
        circleImageView = (CircleImageView) v.findViewById(R.id.profile_image);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (result_camera != PackageManager.PERMISSION_GRANTED ||
                        result_ext_storage != PackageManager.PERMISSION_GRANTED) {

                    cameraPermission();

                } else {

                    imageEditClick();
                }


            }
        });


        setData();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void imageEditClick() {

        try {
            {
                final SelectImageFromFragment dialogFragment = new SelectImageFromFragment();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                FragmentManager fragmentManager = (this).getSupportFragmentManager();
                dialogFragment.show(fragmentManager, "Sample Fragment");

                dialogFragment.setListener(new SelectImageFromFragment.CallBack() {
                    @Override
                    public void onItemSelected(int selectedType) {
                        dialogFragment.dismiss();
                        imageEditItemSelected(selectedType);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imageEditItemSelected(int selectedType) {

        if (selectedType == CAMERA_PICTURE) {
          /*  cameraPermission();*/
            callCameraIntent();
        } else if (selectedType == AppConst.GALLERY_PICTURE) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, AppConst.GALLERY_PICTURE);
        }
    }

    private void callCameraIntent() {


        mPhotoUri = Validation.getTempUri(MainActivity.this);
        Log.e("mPhotUri", String.valueOf(mPhotoUri));

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_PICTURE);
        }
    }

    private void cameraPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {

            if (result_camera != PackageManager.PERMISSION_GRANTED || result_ext_storage != PackageManager.PERMISSION_GRANTED) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_permission_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                TextView txt_cancel = ButterKnife.findById(dialog, R.id.txt_not_now);
                TextView txt_continue = ButterKnife.findById(dialog, R.id.txt_continue);
                TextView txt_description = ButterKnife.findById(dialog, R.id.txt_description);
                TextView txt_placeholder_title = ButterKnife.findById(dialog, R.id.txt_placeholder_title);

                txt_placeholder_title.setText("CAMERA PERMISSION");
                txt_description.setText("We need to access your camera and gallery inorder to set the profile picture");


                txt_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                AppConst.PERMISSION_REQUEST_CODE_CAMERA);

                    }
                });

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });


            }

        } else {

            if (result_camera != PackageManager.PERMISSION_GRANTED || result_ext_storage != PackageManager.PERMISSION_GRANTED) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_permission_dialog);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


                TextView txt_cancel = ButterKnife.findById(dialog, R.id.txt_not_now);
                TextView txt_continue = ButterKnife.findById(dialog, R.id.txt_continue);
                TextView txt_description = ButterKnife.findById(dialog, R.id.txt_description);
                TextView txt_placeholder_title = ButterKnife.findById(dialog, R.id.txt_placeholder_title);

                txt_placeholder_title.setText("CAMERA PERMISSION");
                txt_description.setText("We need to access your camera and gallery inorder to set the profile picture");


                txt_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                AppConst.PERMISSION_REQUEST_CODE_CAMERA);

                    }
                });

                txt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });
            }

        }


    }

    private void initViews() {

        edttxt_places.setOnClickListener(this);
        edttxt_dob.setOnClickListener(this);
        txt_submit.setOnClickListener(this);
        spinner_marital_status.setOnItemSelectedListener(this);
        radio_group_sex.setOnCheckedChangeListener(this);

    }

    private void setData() {

        txt_user_email.setText(SharedPref.getRememberMeUserName(MyApplication.getInstance()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            exitApp();


        }

    }

    private void exitApp() {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_dets) {


            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {

            logout();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

//        MyApplication.getInstance().clearApplicationData();
        SharedPref.clear(MyApplication.getInstance());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.edttxt_places:
                callAutoCompleteAPI();
                break;
            case R.id.edttxt_dob:
                openDialogDatePicker();
                break;
            case R.id.txt_submit:
                if (validateFields()) {
                    rel_loader.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            rel_loader.setVisibility(View.GONE);
                            connectToDB();
                        }
                    }, 2000);

                }
                break;

        }
    }


    private void connectToDB() {

        List<User> users = database.userDao().getAllUser();
        if (users.size() == 0) {

            database.userDao().addUser(new User(1,
                    Validation.getString(edttxt_first_name),
                    Validation.getString(edttxt_last_name),
                    Validation.getString(edttxt_email),
                    Validation.getString(edttxt_dob),
                    selected_item_id, user_selected_sex,
                    Validation.getString(edttxt_places),
                    age));

            user = database.userDao().getAllUser().get(0);
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);

        } else {

            upDateData();
            Toast.makeText(getApplicationContext(),"User data updated", Toast.LENGTH_SHORT).show();
            database.userDao().updateUser(user);
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);

        }
    }

    private void upDateData() {

        user.first_name = Validation.getString(edttxt_first_name);
        user.last_name = Validation.getString(edttxt_last_name);
        user.email = Validation.getString(edttxt_email);
        user.date_of_birth = Validation.getString(edttxt_dob);
        user.marital_status = selected_item_id;
        user.sex = user_selected_sex;
        user.location = Validation.getString(edttxt_places);
        user.age = age;
    }

    private boolean validateFields() {

        if (!Validation.hasText(firstNameWrapper)) {
            return false;
        } else if (!Validation.hasText(lastNameWrapper)) {
            return false;
        } else if (!Validation.hasText(emailWrapper)) {
            return false;
        } else if (!Validation.hasText(dobWrapper)) {
            return false;
        } else if (user_selected_sex == 0) {
            Toast.makeText(getApplicationContext(), "please choose a sex", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Validation.hasText(placesWrapper)) {
            return false;
        }

        return true;

    }


    private void openDialogDatePicker() {

// Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        edttxt_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        age = getAge(year, monthOfYear, dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private int getAge(int year, int monthOfYear, int dayOfMonth) {

        Calendar today = Calendar.getInstance();


        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.DAY_OF_YEAR) < year) {
            age--;
        }

        return age;


    }

    private void callAutoCompleteAPI() {


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, AppConst.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(TAG, "Place: " + place.getName());
                autocomplete_place = place.getName().toString();
                edttxt_places.setText(autocomplete_place);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }*/

        try {

            if (resultCode != RESULT_OK) return;

            switch (requestCode) {
                case AppConst.GALLERY_PICTURE:
                    if (data == null) return;
                    Uri selectedImage = data.getData();
                    Log.e("gallery image", String.valueOf(selectedImage));
                    setProfilePicture(String.valueOf(selectedImage));
                    break;
                case CAMERA_PICTURE:
                    Log.e("camera image", String.valueOf(mPhotoUri));
                    setProfilePicture(String.valueOf(mPhotoUri));
                    break;

                case AppConst.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.e(TAG, "Place: " + place.getName());
                    autocomplete_place = place.getName().toString();
                    edttxt_places.setText(autocomplete_place);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setProfilePicture(String mediaPath) {


        try {


            Picasso.with(activity)
                    .load(mediaPath)
                    .placeholder(R.drawable.default_user_image)
                    .error(R.drawable.default_user_image)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(circleImageView);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selected_item_id = (int) spinner_marital_status.getSelectedItemId();
        Log.e("selected item", String.valueOf(selected_item_id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.radio_btn_male) {

            user_selected_sex = 1;
            Log.e("selected sex", String.valueOf(user_selected_sex));

        } else if (checkedId == R.id.radio_btn_female) {

            user_selected_sex = 2;
            Log.e("selected sex", String.valueOf(user_selected_sex));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == AppConst.PERMISSION_REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                imageEditClick();
                circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageEditClick();
                    }
                });
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
