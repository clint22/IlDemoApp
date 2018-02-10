package com.example.ileafdemoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ileafdemoapp.Activity.LoginActivity;
import com.example.ileafdemoapp.Activity.UserDetailsActivity;
import com.example.ileafdemoapp.Network.AppDatabase;
import com.example.ileafdemoapp.Network.User;
import com.example.ileafdemoapp.Utils.SharedPref;
import com.example.ileafdemoapp.Utils.Validation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    private NavigationView navigationView;
    private TextView txt_user_email;
    ;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG = "places";
    private String autocomplete_place;
    private int selected_item_id = 0;
    private int mYear, mMonth, mDay;
    private int user_selected_sex;
    private int age;
    private User user;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database = AppDatabase.getDatabase(getApplicationContext());
        // cleanup for testing some initial data
        database.userDao().removeAllUsers();

        initViews();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        txt_user_email = (TextView) v.findViewById(R.id.txt_user_email);

        setData();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initViews() {

        edttxt_places.setOnClickListener(this);
        edttxt_dob.setOnClickListener(this);
        txt_submit.setOnClickListener(this);
        spinner_marital_status.setOnItemSelectedListener(this);
        radio_group_sex.setOnCheckedChangeListener(this);

    }

    private void setData() {

        txt_user_email.setText(SharedPref.getRememberMeUserName(MainActivity.this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

      /*  if (id == R.id.nav_home) {
            // Handle the camera action
        } else*/

        if (id == R.id.nav_user_dets) {


            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_logout) {

            logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {

        SharedPref.clear(MainActivity.this);
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
                    connectToDB();
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
                    selected_item_id,user_selected_sex,
                    Validation.getString(edttxt_places),
                    age));

            user = database.userDao().getAllUser().get(0);
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            startActivity(intent);

        }
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
            Toast.makeText(getApplicationContext(),"please choose a sex",Toast.LENGTH_SHORT).show();
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

                        age = getAge(year,monthOfYear,dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private int getAge(int year, int monthOfYear, int dayOfMonth) {

        Calendar today = Calendar.getInstance();


        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.DAY_OF_YEAR) < year){
            age--;
        }

        return age;



    }

    private void callAutoCompleteAPI() {


        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
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
}
