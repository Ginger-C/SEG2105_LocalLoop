package com.project.localloop.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.localloop.R;
import com.project.localloop.database.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter <UserListAdapter.UserListViewHolder> {

    private List <User> dataList;

    public static class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView emailView;

        public UserListViewHolder(View itemView) {
            super(itemView);
            //nameView = itemView.findViewById(R.id.text_user_name);
            //emailView = itemView.findViewById(R.id.text_user_email);
        }
    }

    public UserListAdapter(List<User> list) {
        this.dataList = list;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_registered_user , parent, false);
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User data = dataList.get(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

