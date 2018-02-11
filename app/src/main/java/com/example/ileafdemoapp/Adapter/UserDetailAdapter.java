package com.example.ileafdemoapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ileafdemoapp.Network.User;
import com.example.ileafdemoapp.R;

import java.util.List;

/**
 * Created by Clint on 11/2/18.
 */

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder> {

    private List<User> users;
    private int recyclerview_item;


    public UserDetailAdapter(List<User> users, int recyclerview_item) {

        this.users = users;
        this.recyclerview_item = recyclerview_item;
    }

    @Override
    public UserDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(recyclerview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserDetailAdapter.ViewHolder holder, int position) {

        User item = users.get(position);
        holder.txt_user_details.setText(item.first_name);
        holder.txt_last_name.setText(item.last_name);
        holder.txt_age.setText(String.valueOf(item.age));
        holder.txt_location.setText(item.location);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_user_details, txt_last_name, txt_age, txt_location;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_user_details = (TextView) itemView.findViewById(R.id.txt_user_details);
            txt_last_name = (TextView) itemView.findViewById(R.id.txt_last_name);
            txt_age = (TextView) itemView.findViewById(R.id.txt_age);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
        }
    }
}
