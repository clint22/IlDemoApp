package com.example.ileafdemoapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ileafdemoapp.Adapter.UserDetailAdapter;
import com.example.ileafdemoapp.MainActivity;
import com.example.ileafdemoapp.Network.AppDatabase;
import com.example.ileafdemoapp.Network.User;
import com.example.ileafdemoapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lin_no_user_details)
    LinearLayout lin_no_user_details;
    @BindView(R.id.txt_enter_user_dets)
    TextView txt_enter_user_dets;
    @BindView(R.id.user_details_recycler_view)
    RecyclerView user_details_recycler_view;

    private User user;
    private AppDatabase database;
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        setToolbar();
        initViews();
        checkDbConnection();


    }

    private void checkDbConnection() {

        database = AppDatabase.getDatabase(getApplicationContext());
        users = database.userDao().getAllUser();
        if (!users.isEmpty()) {

            user = database.userDao().getAllUser().get(0);
            lin_no_user_details.setVisibility(View.GONE);
            setAdapter();
        }


    }

    private void setAdapter() {


        user_details_recycler_view.setVisibility(View.VISIBLE);
        lin_no_user_details.setVisibility(View.GONE);

        user_details_recycler_view.setHasFixedSize(true);
        user_details_recycler_view.setAdapter(new UserDetailAdapter(users, R.layout.recyclerview_item));
        user_details_recycler_view.setLayoutManager(new LinearLayoutManager(this));


    }

    private void initViews() {

        txt_enter_user_dets.setOnClickListener(this);
    }

    private void setToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.txt_enter_user_dets:
                Intent intent = new Intent(UserDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
